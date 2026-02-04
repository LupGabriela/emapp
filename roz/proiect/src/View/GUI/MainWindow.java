package View.GUI;

import Controller.Controller;
import Model.PrgState.PrgState;
import Model.Stmt.IStmt;
import Model.Values.StringValue;
import Model.Values.Value;
import Utils.Exceptions.MyException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainWindow {
    private final Controller controller;
    private final Stage stage = new Stage();

    // pastel color
    private static final String PASTEL_PINK = "#3b1c5a";

    // (a)
    private final TextField prgCountField = new TextField();

    // (b)
    private final TableView<HeapEntry> heapTable = new TableView<>();

    // (c)
    private final ListView<String> outList = new ListView<>();

    // (d)
    private final ListView<String> fileTableList = new ListView<>();

    // (e)
    private final ListView<Integer> prgIdList = new ListView<>();

    // (f)
    private final TableView<SymEntry> symTable = new TableView<>();

    // (g)
    private final ListView<String> exeStackList = new ListView<>();

    // (h)
    private final Button oneStepBtn = new Button("Run one step");

    public MainWindow(Controller controller) {
        this.controller = controller;

        stage.setTitle("Interpreter - Program State");
        prgCountField.setEditable(false);

        configureTables();
        layoutUI();
        applyPastelStyle();
        wireEvents();

        refreshAll();
    }

    public void show() {
        stage.show();
    }

    private void configureTables() {
        TableColumn<HeapEntry, Integer> addrCol = new TableColumn<>("Address");
        addrCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        addrCol.setPrefWidth(120);

        TableColumn<HeapEntry, String> valCol = new TableColumn<>("Value");
        valCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        valCol.setPrefWidth(260);

        heapTable.getColumns().addAll(addrCol, valCol);

        TableColumn<SymEntry, String> nameCol = new TableColumn<>("Variable");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(160);

        TableColumn<SymEntry, String> symValCol = new TableColumn<>("Value");
        symValCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        symValCol.setPrefWidth(220);

        symTable.getColumns().addAll(nameCol, symValCol);
    }

    private void layoutUI() {
        VBox left = new VBox(8,
                new Label("PrgStates count"),
                prgCountField,
                new Label("PrgState IDs"),
                prgIdList,
                oneStepBtn
        );
        left.setPadding(new Insets(10));
        left.setPrefWidth(220);

        VBox center = new VBox(10,
                new Label("Heap"),
                heapTable,
                new Label("SymTable (selected PrgState)"),
                symTable
        );
        center.setPadding(new Insets(10));

        VBox right = new VBox(10,
                new Label("Out"),
                outList,
                new Label("FileTable"),
                fileTableList,
                new Label("ExeStack (top first)"),
                exeStackList
        );
        right.setPadding(new Insets(10));
        right.setPrefWidth(320);

        BorderPane root = new BorderPane();
        root.setLeft(left);
        root.setCenter(center);
        root.setRight(right);

        root.setStyle("-fx-background-color: " + PASTEL_PINK + ";");

        Scene scene = new Scene(root, 1200, 750);
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> controller.shutdownExecutor());
    }

    private void applyPastelStyle() {
        String controlBg = "-fx-control-inner-background: " + PASTEL_PINK + ";";

        // TextField
        prgCountField.setStyle(controlBg);

        // ListViews
        outList.setStyle(controlBg);
        fileTableList.setStyle(controlBg);
        exeStackList.setStyle(controlBg);
        prgIdList.setStyle(controlBg);

        // TableViews
        heapTable.setStyle(controlBg);
        symTable.setStyle(controlBg);

        // Button (slightly darker)
        oneStepBtn.setStyle("""
                -fx-background-color: #f8bbd0;
                -fx-font-weight: bold;
        """);
    }

    private void wireEvents() {
        prgIdList.getSelectionModel().selectedItemProperty().addListener((obs, oldId, newId) -> {
            if (newId != null) refreshSelectedProgramState(newId);
        });

        oneStepBtn.setOnAction(e -> {
            try {
                List<PrgState> active = controller.removeCompletedPrg(controller.getPrgList());
                if (active.isEmpty()) {
                    refreshAll();
                    return;
                }

                controller.conservativeGarbageCollector(active);
                controller.oneStepForAllPrg(active);

                refreshAll();
            } catch (MyException ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
                refreshAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                new Alert(Alert.AlertType.ERROR, "Interrupted: " + ex.getMessage()).showAndWait();
            }
        });
    }

    private void refreshAll() {
        List<PrgState> prgs = controller.getPrgList();

        prgCountField.setText(String.valueOf(prgs.size()));

        ObservableList<Integer> ids = FXCollections.observableArrayList(
                prgs.stream().map(PrgState::getID).collect(Collectors.toList())
        );
        prgIdList.setItems(ids);

        Integer selected = prgIdList.getSelectionModel().getSelectedItem();
        if (selected == null && !ids.isEmpty()) {
            prgIdList.getSelectionModel().select(0);
            selected = prgIdList.getSelectionModel().getSelectedItem();
        }

        if (selected != null) {
            refreshSelectedProgramState(selected);
        } else {
            heapTable.setItems(FXCollections.observableArrayList());
            outList.setItems(FXCollections.observableArrayList());
            fileTableList.setItems(FXCollections.observableArrayList());
            symTable.setItems(FXCollections.observableArrayList());
            exeStackList.setItems(FXCollections.observableArrayList());
        }

        oneStepBtn.setDisable(controller.removeCompletedPrg(prgs).isEmpty());
    }

    private void refreshSelectedProgramState(int prgId) {
        PrgState prg = controller.getPrgList().stream()
                .filter(p -> p.getID() == prgId)
                .findFirst()
                .orElse(null);
        if (prg == null) return;

        // Heap
        List<HeapEntry> heapEntries = new ArrayList<>();
        for (Integer addr : prg.getHeapTable().getKeys()) {
            Value val = prg.getHeapTable().lookUp(addr);
            heapEntries.add(new HeapEntry(addr, val.toString()));
        }
        heapTable.setItems(FXCollections.observableArrayList(heapEntries));

        // Out
        outList.setItems(FXCollections.observableArrayList(
                prg.getOut().getList().stream().map(Object::toString).toList()
        ));

        // FileTable
        fileTableList.setItems(FXCollections.observableArrayList(
                prg.getFileTable().getKeys().stream().map(StringValue::toString).toList()
        ));

        // SymTable
        List<SymEntry> symEntries = new ArrayList<>();
        for (String name : prg.getSymTable().getKeys()) {
            Value val = prg.getSymTable().lookUp(name);
            symEntries.add(new SymEntry(name, val.toString()));
        }
        symTable.setItems(FXCollections.observableArrayList(symEntries));

        // ExeStack (top first)
        exeStackList.setItems(FXCollections.observableArrayList(
                prg.getStk().toListSReversed().stream().map(IStmt::toString).toList()
        ));
    }
}
