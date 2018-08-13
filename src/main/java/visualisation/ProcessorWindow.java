package visualisation;

import se306.a1.scheduler.data.graph.Node;
import se306.a1.scheduler.data.schedule.ByteState;
import se306.a1.scheduler.data.schedule.Processor;
import se306.a1.scheduler.manager.ByteStateManager;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ProcessorWindow extends JPanel {
    private ByteStateManager manager;
    private ByteState state;
    private Map<Processor, Map<Node, Integer>> scheduleTimes;
    private Random rand = new Random();

    private final int PROCESSORS;
    private final int WIDTH;
    private final int HEIGHT;

    ProcessorWindow(ByteStateManager manager, ByteState state, int width, int height) {
        this.manager = manager;
        this.state = state;
        scheduleTimes = new HashMap<>();

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

    @Override
    public void paintComponent(Graphics g) {
        int yOffset = 30;

        //Dynamically change scale if too high, default scale = 5
        int scale = 5;
        if (state.getLength() * scale > HEIGHT) {
            scale = HEIGHT / state.getLength();
        }

        Graphics2D g2d = (Graphics2D) g;
        FontMetrics fm = g2d.getFontMetrics();

        //Clears panel
        g.clearRect(0, 0, getWidth(), getHeight());

        //Draw each processor
        for (Processor processor : scheduleTimes.keySet()) {

            int numProc = Integer.parseInt(processor.getName());
            //x-shift based on which processor it is
            int xPos = numProc * WIDTH;

            String text = "proc " + numProc;
            Rectangle2D rect = fm.getStringBounds(text, g2d);

            //Processor name/number & line separator
            g.drawLine(xPos, yOffset, (numProc + 1) * WIDTH, yOffset);
            g.drawString(text, xPos + WIDTH / 2 - (int) (rect.getWidth() / 2), yOffset / 2);

            //Draws each scheduled task
            for (Map.Entry<Node, Integer> entry : scheduleTimes.get(processor).entrySet()) {
                Node node = entry.getKey();
                int startTime = entry.getValue();

                g.setColor(getRandomColor());
                g.fillRect(xPos,
                        yOffset + startTime * scale,
                        WIDTH,
                        node.getCost() * scale);

                g.setColor(Color.black);
                rect = fm.getStringBounds(node.getLabel(), g2d);

                g.drawString(node.getLabel(),
                        xPos + WIDTH / 2 - (int) (rect.getWidth() / 2),
                        yOffset + startTime * scale + node.getCost() * scale / 2 + (int) rect.getHeight() / 2);
            }
        }
    }

    //TODO implement this to save on remaking processor panels
//    @Override
//    public void repaint() {
//        paintComponent(getGraphics());
//    }

    private Color getRandomColor() {
        double r = rand.nextFloat() / 2f + 0.5;
        double g = rand.nextFloat() / 2f + 0.5;
        double b = rand.nextFloat() / 2f + 0.5;
        return new Color((float) r, (float) g, (float) b);
    }
}
