package se306.a1.scheduler.algorithm;

public interface Scheduler {

    /**
     * Initialises and executes the algorithm.
     * This method will setup config parameters, manage parsing, then executes
     * the corresponding algorithm implementation as parameters supplied.
     *
     * @param input filepath of the input .dot file
     * @param numProcessors number of processors to be run on
     * @param numCores  number of cores to be run on (optional)
     * @param hasVisualisation  flag for specifying visualisation (optional)
     * @param output filepath of output .dot file (optional)
     */
    void init(String input,
              int numProcessors,
              Integer numCores,
              Boolean hasVisualisation,
              String output
    );
}
