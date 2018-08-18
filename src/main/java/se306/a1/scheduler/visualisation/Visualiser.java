package se306.a1.scheduler.visualisation;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import se306.a1.scheduler.data.graph.Graph;
import se306.a1.scheduler.data.schedule.ByteState;
import se306.a1.scheduler.manager.ByteStateManager;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Random;

/**
 * This class is used to manage the visualisation interaction with the algorithm
 */
public class Visualiser {
    private ByteStateManager manager;
    private ByteState currentState;
    private Graph graph;
    private HashMap<String, String> stats;

    private GraphWindow graphWindow;
    private static GUIController controller;

    private final int WIDTH = 875;
    private final int HEIGHT = 637;

    private Random rand = new Random();
    private Color color = getRandomColor();

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
        this.graphWindow = new GraphWindow(graph);

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
        Platform.runLater(() -> {
            controller.update(graphWindow, currentState, manager, currentState, color, WIDTH, HEIGHT, prepareStats());
        });
    }

    /**
     * This method returns a random colour where its RGB values are all between
     * 0.5 and 1, i.e. 'lighter/brighter' colours.
     *
     * @return Color object with randomly initialised RGB values
     */
    private Color getRandomColor() {
        double r = rand.nextFloat() / 2f + 0.5f;
        double g = rand.nextFloat() / 2f + 0.5f;
        double b = rand.nextFloat() / 2f + 0.5f;
        return new Color(r, g, b, 1);
    }

    private HashMap<String, String> prepareStats() {
        stats = new HashMap<>();
        stats.put("NODES", graph.getAllNodes().size() + "");
        stats.put("EDGES", graph.getEdgeCount() + "");
        stats.put("PROCESSORS", manager.getProcessors().size() + "");
        stats.put("THREADS", manager.getNumCores() + "");
        stats.put("QUEUE LENGTH", manager.getQueueLength() + "");
        stats.put("STATES SEEN", manager.getNumStatesSeen() + "");
        stats.put("RUN TIME", Duration.between(startTime, Instant.now()).toString().replaceAll("PT", "").replaceAll("S","") + "s");
        return stats;
    }

    public static void setController(GUIController ctrlr) {
        controller = ctrlr;
    }
}
