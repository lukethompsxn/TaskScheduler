package se306.a1.scheduler.visualisation.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import se306.a1.scheduler.visualisation.Visualiser;

import java.io.IOException;

/**
 * This class used to launch the JavaFX GUI and set the controller class.
 */
public class GUILauncher extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Task Scheduler");
        primaryStage.setResizable(false);
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Visualiser.fxml"));

            GUIController controller = new GUIController(this);
            loader.setController(controller);
            Visualiser.setController(controller);

            Pane rootPane = loader.load();

            Scene scene = new Scene(rootPane, 1080, 720);
            primaryStage.setScene(scene);

            primaryStage.setOnCloseRequest(event -> System.exit(0));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
