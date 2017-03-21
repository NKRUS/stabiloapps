package ru.kit.stabiloapps.dynamicTest;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ru.kit.SoundManagerSingleton;

/**
 * Created by mikha on 23.01.2017.
 */
public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = new Pane();
        primaryStage.setScene(new Scene(root));
        Stage stage = new DynamicTestStage("", SoundManagerSingleton.getInstance());
        stage.show();
    }
}
