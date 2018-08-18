package se306.a1.scheduler.visualisation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class GUILauncher extends Application {

    private Stage primaryStage;
    private Pane rootPane;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Task Scheduler");
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Visualiser.fxml"));

            GUIController controller = new GUIController(this);
            loader.setController(controller);
            Visualiser.setController(controller);

            rootPane = loader.load();

            Scene scene = new Scene(rootPane, 1080, 720);
            primaryStage.setScene(scene);

            primaryStage.setOnCloseRequest(event -> System.exit(0));
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}