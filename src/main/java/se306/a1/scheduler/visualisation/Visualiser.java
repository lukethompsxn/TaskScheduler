package se306.a1.scheduler.visualisation;

import javafx.application.Platform;
import se306.a1.scheduler.data.graph.Graph;
import se306.a1.scheduler.data.schedule.ByteState;
import se306.a1.scheduler.manager.ByteStateManager;
import se306.a1.scheduler.visualisation.controller.GUIController;
import se306.a1.scheduler.visualisation.view.GraphWindow;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

/**
 * This class is used to manage the visualisation interaction with the algorithm.
 */
public class Visualiser {
    private ByteStateManager manager;
    private ByteState currentState;
    private Graph graph;

    private GraphWindow graphWindow;
    private static GUIController controller;

    private final int WIDTH = 875;
    private final int HEIGHT = 637;

    private Instant startTime;

    /**
     * This is the constructor for Visualiser.
     * It takes graph as a parameter as this is required for base in the
     * visualisation and it is not updated throughout the scheduling process.
     *
     * @param graph graph object for the given directed graph
     */
    public Visualiser(ByteStateManager manager, se306.a1.scheduler.data.graph.Graph graph) {
        this.manager = manager;
        this.graph = graph;
        this.graphWindow = new GraphWindow(graph, manager);

        startTime = Instant.now();

        Platform.runLater(() -> controller.setup(graphWindow));
    }

    /**
     * This method is used to update the current schedule which is being
     * displayed.
     * This is called from StateManager every X seconds with the current latest
     * schedule at that point in time.
     *
     * @param state the latest explored state from the top of the queue
     */
    public void updateCurrentState(ByteState state) {
        this.currentState = state;
        Platform.runLater(() ->
                controller.update(graphWindow, currentState, manager, currentState, WIDTH, HEIGHT, prepareStats()));
    }

    /**
     * This method is used to prepare the stats HashMap based on information from
     * the graph and the manager.
     *
     * @return HashMap representing the stats at the current state
     */
    private HashMap<String, String> prepareStats() {
        HashMap<String, String> stats = new HashMap<>();
        NumberFormat formatter = NumberFormat.getInstance();
        stats.put("NODES", graph.getAllNodes().size() + "");
        stats.put("EDGES", graph.getEdgeCount() + "");
        stats.put("PROCESSORS", manager.getProcessors().size() + "");
        stats.put("THREADS", manager.getNumCores() + "");
        stats.put("QUEUE LENGTH", formatter.format(manager.getQueueLength()));
        stats.put("STATES SEEN", formatter.format(manager.getNumStatesSeen()));
        Date time = new Date(Duration.between(startTime, Instant.now()).toMillis());
        stats.put("RUN TIME", (new SimpleDateFormat("mm:ss:SS").format(time)));
        return stats;
    }

    /**
     * This method is used to set the controller in this Visualiser from the
     * GUILauncher.
     *
     * @param controller the controller for the visualisation
     */
    public static void setController(GUIController controller) {
        Visualiser.controller = controller;
    }
}
