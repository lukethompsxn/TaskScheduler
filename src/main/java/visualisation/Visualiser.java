package visualisation;

import se306.a1.scheduler.data.schedule.ByteState;

/**
 * This class is used to manage the visualisation interaction with the algorithm
 */
public class Visualiser {
    ByteState currentState;

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
        drawTree();
        drawProcessorMap();
    }

    /**
     * This method is called whenever the current schedule is updated to handle
     * the redrawing of the tree visualisation.
     */
    private void drawTree() {

    }

    /**
     * This method is called whenever the current schedule is update to handle
     * the redrawing of the processor map visualisation.
     */
    private void drawProcessorMap() {

    }
}
