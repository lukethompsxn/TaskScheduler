package se306.a1.scheduler.data;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The purpose of this class is to store the scheduling information for each
 * node, i.e. the startTime of the task and the processor which the task is
 * allocated to.
 * @author Luke Thompson, Rodger Gu
 */
public class Schedule {
    private ArrayList<Processor> processors;
    
    public Schedule() {
    	processors = new ArrayList<>();
    }
    
    public void addProcessor() {
    	processors.add(new Processor());
    }
    /**
     * This method create a new node (task) to pair (startTime & processor) entry
     * in the map.
     * @param node the task which the startTime & processor are for
     * @param startTime the start time of the node (task)
     * @param processor the processor which the node (task) is carried out on
     */
    public void addNodeData(Node node, int startTime, int processor) {
        processors.get(processor).process(node, startTime);
    }
    
    /**
     * This method returns the processors, which contain a list of times and the corresponding
     * nodes that they relate to. As such, this is all the information required to create the
     * final schedule.
     * @return a collection of all the processors which have been used to compute the tasks.
     *  */
    public Collection<Processor> getProcessors() {
    	return processors;
    }
    
    /**
     * This method returns the given time that a processor is currently at. The largest time value
     * of all the processors is the max time that the schedule will take to compute.
     * @param processor the processor number to check.
     * @return the time value of that processor. */
    public int getTime(int processor) {
    	return processors.get(processor).getTime();
    }
}
