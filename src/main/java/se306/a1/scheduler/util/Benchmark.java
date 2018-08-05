package se306.a1.scheduler.util;

import java.time.Duration;

/**
 * Helper class used for benchmarking purposes.
 *
 * @author Abhinav Behal
 */
public class Benchmark {

    /**
     * Runs the given function a specified number of times and returns the average elapsed time.
     *
     * @param func       the function to run
     * @param timesToRun the number of times to run the function
     * @return the average elapsed time
     */
    public static Duration time(Runnable func, int timesToRun) {
        Duration total = Duration.ofNanos(0);
        if (timesToRun <= 0) {
            return total;
        }
        for (int i = 0; i < timesToRun; ++i) {
            long start = System.nanoTime();
            func.run();
            total = total.plusNanos(System.nanoTime() - start);
        }
        return total.dividedBy(timesToRun);
    }
}
