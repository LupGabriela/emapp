package View.GUI;

import Controller.Controller;
import Model.PrgState.PrgState;
import Model.Stmt.IStmt;
import Repository.IRepository;
import Repository.Repository;
import Utils.Collections.MyDictionary;
import Utils.Collections.MyHeap;
import Utils.Collections.MyList;
import Utils.Collections.MyStack;
import Utils.Exceptions.MyException;
import View.Examples;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.List;

public class SelectProgramWindow {
    private final Stage stage;
    private final ListView<ExampleItem> programsList = new ListView<>();

    // pastel color
    private static final String PASTEL_PINK = "#3b1c5a";

    public SelectProgramWindow(Stage stage) {
        this.stage = stage;
        stage.setTitle("Select Program");

        programsList.setItems(FXCollections.observableArrayList(buildExamples()));
        programsList.setStyle("-fx-control-inner-background: " + PASTEL_PINK + ";");

        Button runBtn = new Button("Open");
        runBtn.setStyle("""
                -fx-background-color: #f8bbd0;
                -fx-font-weight: bold;
        """);

        runBtn.setOnAction(e -> openSelected());

        programsList.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                openSelected();
            }
        });

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setCenter(programsList);
        root.setStyle("-fx-background-color: " + PASTEL_PINK + ";");

        HBox bottom = new HBox(10, runBtn);
        bottom.setPadding(new Insets(10, 0, 0, 0));
        root.setBottom(bottom);

        Scene scene = new Scene(root, 900, 500);
        stage.setScene(scene);
    }

    public void show() {
        stage.show();
    }

    private List<ExampleItem> buildExamples() {
        return List.of(
                new ExampleItem(1, Examples.example1(), "ex1.txt"),
                new ExampleItem(2, Examples.example2(), "ex2.txt"),
                new ExampleItem(3, Examples.example3(), "ex3.txt"),
                new ExampleItem(4, Examples.example4(), "ex4.txt"),
                new ExampleItem(5, Examples.example5(), "ex5.txt"),
                new ExampleItem(6, Examples.example6(), "ex6.txt"),
                new ExampleItem(7, Examples.example7(), "ex7.txt"),
                new ExampleItem(8, Examples.example8(), "ex8.txt"),
                new ExampleItem(9, Examples.example9(), "ex9.txt"),
                new ExampleItem(10, Examples.example10(), "ex10.txt"),
                new ExampleItem(11, Examples.example11(), "ex11.txt"),
                new ExampleItem(12, Examples.example12(), "ex12.txt"),
                new ExampleItem(13, Examples.example13(), "ex13.txt")
        );
    }

    private void openSelected() {
        ExampleItem item = programsList.getSelectionModel().getSelectedItem();
        if (item == null) {
            new Alert(Alert.AlertType.WARNING, "Select a program first.").showAndWait();
            return;
        }

        IStmt program = item.getProgram();

        try {
            program.typecheck(new MyDictionary<>());
        } catch (MyException ex) {
            new Alert(Alert.AlertType.ERROR, "Typecheck failed:\n" + ex.getMessage()).showAndWait();
            return;
        }

        PrgState initial = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                new MyDictionary<>(),
                new MyHeap<>(),
                program
        );

        IRepository repo = new Repository(initial, item.getLogFile());
        Controller ctrl = new Controller(repo);

        MainWindow mainWindow = new MainWindow(ctrl);
        mainWindow.show();
    }
}
