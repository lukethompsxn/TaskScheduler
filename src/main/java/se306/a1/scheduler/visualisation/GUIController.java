package se306.a1.scheduler.visualisation;

import com.jfoenix.controls.JFXTabPane;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import se306.a1.scheduler.Main;
import se306.a1.scheduler.data.schedule.ByteState;
import se306.a1.scheduler.manager.ByteStateManager;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class GUIController implements Initializable {
    private GraphicsContext gc;

    @FXML
    public Label timeLabel;

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

    @FXML
    private JFXTabPane tabPane;

    // Constructors
    GUIController() { }
    GUIController(GUILauncher launcher) { }

    /**
     * This method is automatically called when the GUI is launched, and creates a thread for the main algorithm to run
     * behind the scenes.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

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


    /**
     * This method sets up some initial parameters for the GUI, specifically the processor and graph displays
     * @param graphWindow graph to be added to one of the GUI's tabs
     */
    public void setup(GraphWindow graphWindow) {
        graphPane.getChildren().add(graphWindow.getViewPanel());
        Canvas cs = new Canvas(868, 666); //
        this.gc = cs.getGraphicsContext2D();
        processorPane.getChildren().add(cs);
    }


    /**
     * This method help coordinate updates to GUI based on changes from the algorithm.
     */
    public void update(GraphWindow graphWindow, ByteState currentState, ByteStateManager manager, ByteState state,
                       Color color, int width, int height, HashMap<String, String> stats) {

        new ProcessorWindow(manager,state,color,width,height).draw(gc);
        updateStats(stats);
        graphWindow.drawHighlighting(currentState);
    }

    /**
     * This method updates all of the GUI statistics
     * @param stats Map of the algorithms current statistics
     */
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
