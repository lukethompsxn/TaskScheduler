package se306.a1.scheduler.data.schedule;

import se306.a1.scheduler.data.graph.Node;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface State {
    Map<Node, Integer>  getStartTimes();
    Set<Node> getUnscheduledTasks();
    Set<Node> getScheduledTasks();
    List<Processor> getProcessors();
}
