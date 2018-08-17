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
import java.util.Random;

public class ProcessorWindow {
    private ByteStateManager manager;
    private ByteState state;
    private Map<Processor, Map<Node, Integer>> scheduleTimes;
    private Random rand = new Random();

    private Color color;

    private final int PROCESSORS;
    private final int WIDTH;
    private final int HEIGHT;
    private Bounds bounds;

    ProcessorWindow(ByteStateManager manager, ByteState state, Color color, int width, int height) {
        this.manager = manager;
        this.state = state;
        scheduleTimes = new HashMap<>();

        this.color = color;

        PROCESSORS = manager.getProcessors().size();
        WIDTH = width / PROCESSORS;
        HEIGHT = height;

        buildVisual();

    }

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


    public void draw(GraphicsContext gc) {
        int yOffset = 30;

        //Dynamically change scale if too high, default scale = 5
        int scale = 5;
        if (state.getLength() * scale > HEIGHT) {
            scale = HEIGHT / state.getLength();
        }

        //Graphics2D g2d = (Graphics2D) gc;
        //FontMetrics fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(gc.getFont());

        //Clears panel
//        g.clearRect(0, 0, getWidth(), getHeight());
        gc.clearRect(0, 0, 1080, 720);

        for (Processor processor : manager.getProcessors()) {
            int numProc = Integer.parseInt(processor.getName());
            //x-shift based on which processor it is
            int xPos = numProc * WIDTH;

            String text = "Processor " + numProc;
            //Rectangle2D rect = new Rectangle2D(1,1,)
            reportSize(text, gc.getFont());

            gc.strokeLine(xPos, yOffset, (numProc + 1) * WIDTH, yOffset);
            gc.strokeText(text, xPos + WIDTH / 2 - (int) (bounds.getWidth() / 2), yOffset / 2);
        }

        //Draw each processor
        for (Processor processor : scheduleTimes.keySet()) {

            int numProc = Integer.parseInt(processor.getName());
            //x-shift based on which processor it is
            int xPos = numProc * WIDTH;

            String text = "Processor " + numProc;
            //Rectangle2D rect = new Rectangle2D(1,1,)
            reportSize(text, gc.getFont());

            //Processor name/number & line separator
//            g.drawLine(xPos, yOffset, (numProc + 1) * WIDTH, yOffset);
//            g.drawString(text, xPos + WIDTH / 2 - (int) (rect.getWidth() / 2), yOffset / 2);
//            gc.strokeLine(xPos, yOffset, (numProc + 1) * WIDTH, yOffset);
//            gc.strokeText(text, xPos + WIDTH / 2 - (int) (bounds.getWidth() / 2), yOffset / 2);


            //Draws each scheduled task
            for (Map.Entry<Node, Integer> entry : scheduleTimes.get(processor).entrySet()) {
                Node node = entry.getKey();
                int startTime = entry.getValue();

//                g.setColor(getRandomColor(manager.indexOf(node)));
//                g.fillRect(xPos,
//                        yOffset + startTime * scale,
//                        WIDTH,
//                        node.getCost() * scale);
                gc.setFill(getRandomColor(manager.indexOf(node), color));
                gc.fillRect(xPos,
                        yOffset + startTime * scale,
                        WIDTH,
                        node.getCost() * scale);

//                g.setColor(Color.black);
//                rect = fm.getStringBounds(node.getLabel(), g2d);
                gc.setFill(Color.BLACK);

//                g.drawString(node.getLabel(),
//                        xPos + WIDTH / 2 - (int) (rect.getWidth() / 2),
//                        yOffset + startTime * scale + node.getCost() * scale / 2 + (int) rect.getHeight() / 2);
                gc.strokeText(node.getLabel(),
                        xPos + WIDTH / 2 - (int) (bounds.getWidth() / 2),
                        yOffset + startTime * scale + node.getCost() * scale / 2 + (int) bounds.getHeight() / 2);
            }
        }
    }

    private static Color getRandomColor(int val, Color color) {
        //Minimum of % 8 / 12 for largest color diff
        double r = color.getRed() * (1 - val % 8 / 16f);
        double g = color.getGreen() * (1 - val % 8 / 16f);
        double b = color.getBlue() * (1 - val % 8 / 16f);

        return Color.color(r, g, b);
    }

    public void reportSize(String s, Font myFont) {
        Text text = new Text(s);
        text.setFont(myFont);
        Bounds tb = text.getBoundsInLocal();
        Rectangle stencil = new Rectangle(
                tb.getMinX(), tb.getMinY(), tb.getWidth(), tb.getHeight()
        );

        Shape intersection = Shape.intersect(text, stencil);

        Bounds ib = intersection.getBoundsInLocal();
        bounds = ib;
    }
}
