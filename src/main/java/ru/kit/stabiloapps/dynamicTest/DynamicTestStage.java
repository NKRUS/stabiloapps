package ru.kit.stabiloapps.dynamicTest;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kit.stabiloapps.dynamicTest.controller.DynamicTestController;

import java.io.IOException;
import java.util.List;

/**
 * Created by mikha on 23.01.2017.
 */
public class DynamicTestStage extends Stage {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicTestStage.class);
    private DynamicTestController controller;
    private String path;

    public DynamicTestStage(String path) {
        try {
            this.path = path;
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ru/kit/stabiloapps/fxml/test_ronberga.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
            controller.setStage(this);
            this.setTitle("Динамическая проба - стабилометрия");
            this.setMaximized(true);

            this.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPath() {
        return path;
    }

    public void close(boolean isCompleteTest) {

        if (!isCompleteTest) {
            // =========== ALERT DIALOG ==============
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Выход");
            alert.setHeaderText("Вы действительно хотите выйти?");
            alert.setContentText("Данные не были сохранены. При выходе данные будут потеряны.");
            // =======================================

            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                cancelTasks();
                LOG.info("CLOSE STAGE, NOT SAVE");
                this.close();
            }
        } else {
            cancelTasks();
            LOG.info("CLOSE STAGE AND SAVE");
            this.close();
        }
    }

    private void cancelTasks() {
        List<Task> tasks = controller.getTasks();
        for (Task task : tasks) {
            task.isCancelled();
        }
    }
}
