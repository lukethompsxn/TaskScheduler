package se306.a1.scheduler.visualisation;

import se306.a1.scheduler.data.graph.Graph;
import se306.a1.scheduler.data.schedule.ByteState;
import se306.a1.scheduler.manager.ByteStateManager;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * This class is used to manage the visualisation interaction with the algorithm
 */
public class Visualiser {
    private ByteStateManager manager;
    private ByteState currentState;
    private Graph graph;

    private GraphWindow graphWindow;

    private JFrame frame;
    private final int WIDTH;
    private final int HEIGHT = 500;

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

        WIDTH = Math.max(200, manager.getProcessors().size() * 50);

        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(WIDTH + 15, HEIGHT);
        frame.setVisible(true);
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
        frame.getContentPane().removeAll(); //TODO panel still needs clearing
        frame.add(new ProcessorWindow(manager, currentState, color, WIDTH, HEIGHT));
        frame.add(new StatisticsWindow(manager, graph));
      //  frame.add(graphWindow.drawHighlighting(currentState));
        frame.setVisible(true);
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
}
