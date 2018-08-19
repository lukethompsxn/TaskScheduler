package se306.a1.scheduler.visualisation.view;

import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
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
 * This class allows for the Processors and their corresponding tasks to be
 * displayed to the GUI.
 */
public class ProcessorWindow extends Canvas {
    private ByteStateManager manager;
    private ByteState state;
    private Map<Processor, Map<Node, Integer>> scheduleTimes;

    private Color color;
    private static final String COLOR = "40ABB4";
    private static final String FILL_COLOR = "FCFCFC";
    private static final String STROKE_COLOR = "303038";
    private static final String FADED_WHITE = "FFFFFFAA";

    private final int PROCESSORS;
    private final int PROCWIDTH;
    private Bounds bounds;

    public ProcessorWindow(ByteStateManager manager, ByteState state, int width, int height) {
        this.manager = manager;
        this.state = state;
        scheduleTimes = new HashMap<>();

        this.color = Color.web(COLOR);

        PROCESSORS = manager.getProcessors().size();
        PROCWIDTH = Math.min(width / PROCESSORS, 175);

        setWidth(width);
        setHeight(height);

        buildVisual();
    }

    /**
     * This method builds up the HashMap scheduledTimes in order to have the required processor and task information
     * available to be able to more easily display them.
     */
    private void buildVisual() {
        Map<Node, Integer> startTimes = state.getStartTimes(manager);
        Map<Node, Processor> processors = state.getProcessors(manager);

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
     *
     * @param gc a GraphicContext object used to paint processors tab in GUI
     */
    public void draw(GraphicsContext gc) {
        int xOffset = 0;
        int yOffset = 30;

        if (PROCWIDTH * PROCESSORS < getWidth()) {
            xOffset = (int) (getWidth() / 2 - PROCWIDTH * PROCESSORS / 2);
        }

        int[] scales = {5, 10, 20, 40};
        double scale = 1;
        for (int scaleVal : scales) {
            if (state.getLength() * scaleVal > getHeight()) {
                scale = getHeight() / state.getLength();
                break;
            } else {
                scale = scaleVal;
            }
        }

        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        for (Processor processor : manager.getProcessors()) {
            int numProc = Integer.parseInt(processor.getName());
            int xPos = numProc * PROCWIDTH + xOffset;

            String text;

            if (manager.getProcessors().size() > 11) {
                text = "P" + numProc;
                getBound(text, gc.getFont());
            } else {
                text = "Processor " + numProc;
                getBound(text, gc.getFont());
            }

            gc.setFill(Color.web(FILL_COLOR));
            gc.setStroke(Color.web(STROKE_COLOR));
            gc.strokeLine(xPos, yOffset, (numProc + 1) * PROCWIDTH, yOffset);
            gc.fillText(text,
                    xPos + PROCWIDTH / 2 - (int) (bounds.getWidth() / 2),
                    yOffset / 2 + (int) (bounds.getHeight() / 2));
        }

        for (Processor processor : scheduleTimes.keySet()) {
            int numProc = Integer.parseInt(processor.getName());
            int xPos = numProc * PROCWIDTH + xOffset;

            for (Map.Entry<Node, Integer> entry : scheduleTimes.get(processor).entrySet()) {
                Node node = entry.getKey();
                int startTime = entry.getValue();

                gc.setFill(getColor(manager.indexOf(node), color));
                gc.fillRect(xPos,
                        yOffset + startTime * scale,
                        PROCWIDTH,
                        node.getCost() * scale);

                gc.setStroke(Color.web(FADED_WHITE));
                gc.strokeRect(xPos,
                        yOffset + startTime * scale,
                        PROCWIDTH,
                        node.getCost() * scale);

                getBound(node.getLabel(), gc.getFont());
                int xPosProcessorName = xPos + PROCWIDTH / 2 - (int) (bounds.getWidth() / 2);

                gc.strokeText(node.getLabel(),
                        xPosProcessorName,
                        yOffset + startTime * scale + node.getCost() * scale / 2 + bounds.getHeight() / 2);

                gc.setFill(Color.BLACK);
            }
        }
    }

    /**
     * This method gets a new color based on the one that is passed in.
     *
     * @param val   the index value of a node
     * @param color the base color
     * @return the color to display the current task
     */
    private static Color getColor(int val, Color color) {
        //Minimum of % 8 / 12 for largest color diff
        double r = color.getRed() * (1 - val % 8 / 16f);
        double g = color.getGreen() * (1 - val % 8 / 16f);
        double b = color.getBlue() * (1 - val % 8 / 16f);

        return Color.color(r, g, b);
    }

    /**
     * This method calculates the bounding area of a line of text, i.e if the string was in a box it would locate the
     * positions of its four corners.
     *
     * @param string the line of text which its bounds are required
     * @param font   the font of the input text
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
