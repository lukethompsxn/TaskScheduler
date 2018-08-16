package se306.a1.scheduler.visualisation;

import javafx.concurrent.Task;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import se306.a1.scheduler.Main;
import se306.a1.scheduler.data.schedule.ByteState;
import se306.a1.scheduler.manager.ByteStateManager;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class GUIController implements Initializable {
    private static Visualiser visualiser;

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

    @FXML
    private SwingNode graphNode;

    @FXML
    private SwingNode processorNode;

    public GUIController() {}
    public GUIController(GUILauncher launcher) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("test");
        // start running the algorithm
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                Main.runVisualise();
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
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

    public void update(GraphWindow graphWindow, ByteState currentState, ByteStateManager manager, ByteState state, Color color, int width, int height, HashMap<String, String> stats) {
        updateStats(stats);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                processorNode.setContent(new ProcessorWindow(manager, currentState, color, width, height));
                graphNode.setContent(graphWindow.drawHighlighting(currentState));
            }
        });
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

    public static void setVisualiser(Visualiser v) {
        visualiser = v;
    }
}
