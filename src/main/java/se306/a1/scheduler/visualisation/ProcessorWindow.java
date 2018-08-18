package se306.a1.scheduler.visualisation;

import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import se306.a1.scheduler.data.graph.Node;
import se306.a1.scheduler.data.schedule.ByteState;
import se306.a1.scheduler.data.schedule.Processor;
import se306.a1.scheduler.manager.ByteStateManager;

import java.util.HashMap;
import java.util.Map;

/**
 * This class allows for the Processors and their corresponding tasks to be displayed to the GUI.
 */
public class ProcessorWindow {
    private ByteStateManager manager;
    private ByteState state;
    private Map<Processor, Map<Node, Integer>> scheduleTimes;

    private Color color;

    private final int PROCESSORS;
    private final int WIDTH;
    private final int HEIGHT;
    private Bounds bounds;

    public ProcessorWindow(ByteStateManager manager, ByteState state, Color color, int width, int height) {
        this.manager = manager;
        this.state = state;
        scheduleTimes = new HashMap<>();

        this.color = color;

        PROCESSORS = manager.getProcessors().size();
        WIDTH = width / PROCESSORS;
        HEIGHT = height;

        buildVisual();

    }

    /**
     * This method builds up the HashMap scheduledTimes in order to have the required processor and task information
     * available to be able to more easily display them.
     */
    private void buildVisual() {
        Map<Node, Integer> startTimes = state.getStartTimes();
        Map<Node, Processor> processors = state.getProcessors();

        for (Node n : processors.keySet()) {
            Processor p = processors.get(n);
            if (!scheduleTimes.containsKey(p))
                scheduleTimes.put(processors.get(n), new HashMap<>());

            scheduleTimes.get(p).put(n, startTimes.get(n));
        }
    }

    /**
     * This method takes a GraphicContext object and uses this in order to print the processors aspect
     * of the GUI. This is done by taking the number of processors and their assigned tasks and dynamically
     * calculating their positions to be displayed.
     * @param gc a GraphicContext object used to paint processors tab in GUI
     */
    public void draw(GraphicsContext gc) {
        int yOffset = 30;

        //Dynamically change scale if too high, default scale = 5
        int scale = 5;
        if (state.getLength() * scale > HEIGHT) {
            scale = HEIGHT / state.getLength();
        }

        //Clears panel
        gc.clearRect(0, 0, 1080, 720);

        // Draws each processor
        for (Processor processor : manager.getProcessors()) {
            int numProc = Integer.parseInt(processor.getName());
            //x-shift based on which processor it is
            int xPos = numProc * WIDTH;

            String text;

            // If more 11 processors shortens the processor name
            if (manager.getProcessors().size() > 11) {
                text = "Pro " + numProc;
                getBound(text, gc.getFont());
            } else {
                text = "Processor " + numProc;
                getBound(text, gc.getFont());
            }

            gc.strokeLine(xPos, yOffset, (numProc + 1) * WIDTH, yOffset);
            gc.strokeText(text, xPos + WIDTH / 2 - (int) (bounds.getWidth() / 2), yOffset / 2 + 4);
        }

        // Draws each scheduled task to its corresponding processor
        for (Processor processor : scheduleTimes.keySet()) {

            int numProc = Integer.parseInt(processor.getName());

            //x-shift based on which processor it is
            int xPos = numProc * WIDTH;

            for (Map.Entry<Node, Integer> entry : scheduleTimes.get(processor).entrySet()) {
                Node node = entry.getKey();
                int startTime = entry.getValue();

                // Select a color for the task to be drawn
                gc.setFill(getRandomColor(manager.indexOf(node), color));

                // Draws task
                gc.fillRect(xPos,
                        yOffset + startTime * scale,
                        WIDTH,
                        node.getCost() * scale);

                gc.setFill(Color.BLACK);

                int xPosProcessorName;
                if (manager.getProcessors().size() > 11) {
                    xPosProcessorName = xPos + WIDTH / 2 - (int) (bounds.getWidth() / 2) + 9;
                } else {
                    xPosProcessorName = xPos + WIDTH / 2 - (int) (bounds.getWidth() / 2) + 26;
                }

                // Draws task name
                gc.strokeText(node.getLabel(),
                        xPosProcessorName,
                        yOffset + startTime * scale + node.getCost() * scale / 2 + (int) bounds.getHeight() / 2);
            }
        }
    }

    /**
     * This method
     * @param val the index value of a node
     * @param color the base color
     * @return the color to display the current task
     */
    private static Color getRandomColor(int val, Color color) {
        //Minimum of % 8 / 12 for largest color diff
        double r = color.getRed() * (1 - val % 8 / 16f);
        double g = color.getGreen() * (1 - val % 8 / 16f);
        double b = color.getBlue() * (1 - val % 8 / 16f);

        return Color.color(r, g, b);
    }

    /**
     * This method calculates the bounding area of a line of text, i.e if the string was in a box it would locate the
     * positions of its four corners.
     * @param string the line of text which its bounds are required
     * @param font the font of the input text
     */
    private void getBound(String string, Font font) {
        Text text = new Text(string);
        text.setFont(font);
        Bounds tb = text.getBoundsInLocal();
        Rectangle stencil = new Rectangle(tb.getMinX(), tb.getMinY(), tb.getWidth(), tb.getHeight());

        Shape intersection = Shape.intersect(text, stencil);

        bounds = intersection.getBoundsInLocal();
    }
}
