package se306.a1.scheduler.visualisation;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import se306.a1.scheduler.data.graph.Graph;
import se306.a1.scheduler.data.schedule.ByteState;
import se306.a1.scheduler.manager.ByteStateManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

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

    private JFrame frame;
    private final int WIDTH;
    private final int HEIGHT = 720;

    private Random rand = new Random();
    private Color color = getRandomColor();

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

        WIDTH = 1080; //Math.max(200, manager.getProcessors().size() * 50);

        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(WIDTH + 15, HEIGHT);

        ExecutorService executor = Executors.newScheduledThreadPool(2);
        executor.submit(new Runnable() {
            @Override
            public void run() {
                Application.launch(GUILauncher.class);
            }
        });
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

        graphWindow.drawHighlighting(currentState);
        drawComponents();
    }

    /**
     * This method is called whenever the current schedule is updated to handle
     * the redrawing of the processor map visualisation.
     */
    private void drawComponents() {
//        final JFXPanel fxPanel = new JFXPanel();
//        JPanel p = new JPanel();
//        JPanel p2 = new JPanel();
//        p.setLayout(new GridLayout(2,0));
//        p2.setLayout(new GridLayout(0,2));
//
//        p.add();
//        p.add(graphWindow.drawHighlighting(currentState));
//        p2.add(p);
//        p2.add(new StatisticsWindow(manager, graph));
//
//        frame.add(p2);
//        frame.setVisible(true);

//       controller.updateView(prepareStats(), graphWindow.drawHighlighting(currentState), new ProcessorWindow(manager, currentState, color, WIDTH, HEIGHT));
        controller.timeLabel.setText("hello");
        System.out.println("gg");
//        try {
//            Parent root = FXMLLoader.load(getClass().getResource("se306/a1/scheduler/visualisation/Visualiser.fxml"));
//            Scene scene = new Scene(root, 1080, 720);
//            processorNode.setContent(new ProcessorWindow(manager, currentState, color, WIDTH, HEIGHT));
//            graphNode.setContent(graphWindow.drawHighlighting(currentState));
//            fxPanel.setScene(scene);
//
//            frame.add(fxPanel);
//            frame.setVisible(true);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }

    /**
     * This method returns a random colour where its RGB values are all between
     * 0.5 and 1, i.e. 'lighter/brighter' colours.
     *
     * @return Color object with randomly initialised RGB values
     */
    private Color getRandomColor() {
        float r = rand.nextFloat() / 2f + 0.5f;
        float g = rand.nextFloat() / 2f + 0.5f;
        float b = rand.nextFloat() / 2f + 0.5f;
        return new Color(r, g, b);
    }

    private HashMap<String, String> prepareStats() {
        stats.put("NODES", graph.getAllNodes().size() + "");
        stats.put("EDGES", graph.getEdgeCount() + "");
        stats.put("PROCESSORS", manager.getProcessors().size() + "");
        stats.put("THREADS", manager.getNumCores() + "");
        stats.put("QUEUE LENGTH", manager.getQueueLength() + "");
        stats.put("STATES SEEN", manager.getNumStatesSeen() + "");
        stats.put("RUN TIME", 0 + "");
        return stats;
    }

    public static void setController(GUIController ctrlr) {
        controller = ctrlr;
    }
}
