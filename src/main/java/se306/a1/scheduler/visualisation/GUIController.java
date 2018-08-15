package se306.a1.scheduler.visualisation;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import se306.a1.scheduler.Main;
import se306.a1.scheduler.algorithm.AStarByteScheduler;
import se306.a1.scheduler.data.schedule.Schedule;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class GUIController implements Initializable {

    @FXML
    private Label timeLabel;

    @FXML
    private Label bestLabel;

    @FXML
    private Label threadsLabel;

    @FXML
    private Label statesLabel;

    @FXML
    private Label queueLabel;

    @FXML
    private Label nodesLabel;

    @FXML
    private Label edgesLabel;


    public GUIController() {}
    public GUIController(GUILauncher launcher) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // start running the algorithm
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                Main.run();
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public void updateStats(Map<String, String> stats) {
        for (String stat : stats.keySet()) {
            switch (stat) {
                case "NODES": nodesLabel.setText(stats.get(stat));
                    break;
                case "EDGES": edgesLabel.setText(stats.get(stat));
                    break;
                case "THREADS": threadsLabel.setText(stats.get(stat));
                    break;
                case "QUEUE LENGTH": queueLabel.setText(stats.get(stat));
                    break;
                case "STATES SEEN": statesLabel.setText(stats.get(stat));
                    break;
                case "RUN TIME": timeLabel.setText(stats.get(stat));
                    break;

            }
        }
    }
}
