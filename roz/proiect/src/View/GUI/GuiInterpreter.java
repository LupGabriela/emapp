package View.GUI;

import javafx.application.Application;
import javafx.stage.Stage;

public class GuiInterpreter extends Application {
    @Override
    public void start(Stage primaryStage) {
        SelectProgramWindow window = new SelectProgramWindow(primaryStage);
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

