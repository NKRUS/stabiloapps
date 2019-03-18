package ru.kit.stabiloapps.dynamicTest;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ru.kit.SoundManagerSingleton;

import java.io.IOException;

/**
 * Created by mikha on 23.01.2017.
 */
public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Button btn = new Button();
        btn.setText("Стабила");
        btn.setOnAction(event -> {
            Stage s =  new DynamicTestStage("", SoundManagerSingleton.getInstance());
            s.show();
        });
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root,300,250));
        primaryStage.show();
    }
}
