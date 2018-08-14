package se306.a1.scheduler.visualisation;

import se306.a1.scheduler.data.graph.Graph;
import se306.a1.scheduler.manager.ByteStateManager;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class StatisticsWindow extends JPanel {
    private ByteStateManager manager;
    private Map<String, String> stats;
    private Instant timeStart;
    private String[] colors;

    private static final int WIDTH = 150;
    private static final int HEIGHT = 100;

    public StatisticsWindow(ByteStateManager manager, Graph graph) {
        this.manager = manager;
        this.stats = new HashMap<>();
        this.timeStart = Instant.now();

        stats.put("NODES", graph.getAllNodes().size() + "");
        stats.put("EDGES", graph.getEdgeCount() + "");
        stats.put("PROCESSORS", manager.getProcessors().size() + "");
        stats.put("THREADS", manager.getNumCores() + "");

        colors = new String[7];
        colors[0] = "#2E294E";
        colors[1] = "#984447";
        colors[2] = "#774C60";
        colors[3] = "#476C9B";
        colors[4] = "#297373";
        colors[5] = "#617073";
        colors[6] = "#581908";

        buildComponent();
    }

    /**
     * This method is used to build the dynamic components of the stats display.
     * It is called every time a new state is pushed to visualiser
     */
    private void buildComponent() {
        stats.put("QUEUE LENGTH", manager.getQueueLength() + "");
        stats.put("STATES SEEN", manager.getNumStatesSeen() + "");
        stats.put("RUN TIME", Duration.between(timeStart, Instant.now()).getSeconds() + "");
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        FontMetrics fm = g2d.getFontMetrics();

        g.clearRect(0,0, getWidth(), getHeight());

        int i = 0;
        for (String item : stats.keySet()) {
            int xPos = (i * WIDTH) + (i * 10);
            g.setColor(Color.decode(colors[i]));
            g.fillRoundRect(xPos, 0, WIDTH, HEIGHT, 5, 5);

            g.setFont(new Font("TimesNewRoman", Font.ITALIC, 15));
            g.setColor(Color.LIGHT_GRAY);
            g.drawString(item, xPos + 5, 15);

//            g.setFont(new Font("TimesNewRoman", Font.BOLD, 20));
//            FontMetrics fontMetrics =
//            g.setColor(Color.WHITE);
            Rectangle2D rect = fm.getStringBounds(stats.get(item), g2d);
            g.drawString(stats.get(item), xPos + (WIDTH/2) - (int) (rect.getWidth()/2), HEIGHT/2);
            i++;
        }
    }
}
