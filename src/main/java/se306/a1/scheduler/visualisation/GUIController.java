package se306.a1.scheduler.visualisation;

import javafx.concurrent.Task;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import se306.a1.scheduler.Main;
import se306.a1.scheduler.data.schedule.ByteState;
import se306.a1.scheduler.manager.ByteStateManager;

import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class GUIController implements Initializable {
    private GraphicsContext gc;

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
    private AnchorPane processorPane;

    @FXML
    private AnchorPane graphPane;

    public GUIController() {}
    public GUIController(GUILauncher launcher) { }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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


    public void setup(GraphWindow graphWindow) {
        graphPane.getChildren().add(graphWindow.getViewPanel());
        Canvas cs = new Canvas(1080, 720); // TODO adjust these to what they should be @joel
        this.gc = cs.getGraphicsContext2D();
        processorPane.getChildren().add(cs);
    }


    public void update(GraphWindow graphWindow, ByteState currentState, ByteStateManager manager, ByteState state, Color color, int width, int height, HashMap<String, String> stats) {
        new ProcessorWindow(manager,state,color,width,height).draw(gc);
        updateStats(stats);
        graphWindow.drawHighlighting(currentState);
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
