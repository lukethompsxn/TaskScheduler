package se306.a1.scheduler.visualisation;

import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class GUIController implements Initializable {

    @FXML
    public Label timeLabel;

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

    @FXML
    private Pane processorPane;

    @FXML
    private Pane graphPane;


    public GUIController() {}
    public GUIController(GUILauncher launcher) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void updateView(Map<String, String> stats, JComponent graph, JComponent processor) {
        SwingNode processorNode = new SwingNode();
        SwingNode graphNode = new SwingNode();
        processorNode.setContent(processor);
        graphNode.setContent(graph);
        processorPane.getChildren().removeAll();
        processorPane.getChildren().add(processorNode);
        graphPane.getChildren().removeAll();
        graphPane.getChildren().add(processorNode);
        updateStats(stats);
    }

    private void updateStats(Map<String, String> stats) {
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
