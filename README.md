# TaskScheduler: Team 3 - Vaporware

### Github Username & UPIs

| Github Username | UPI       |
| --------------- | --------- |
| lukethompsxn    | `ltho948` |
| AbhinavBehal    | `abeh957` |
| Joelz1          | `jcla776` |
| Z-Qi            | `zqia153` |
| RodgerRG        | `rgu663`  |


### About TaskScheduler
TaskScheduler is a java command line application which takes an input graph and a number of processors. It then generates a schedule for executing these tasks which ~~is~~ (will be) the optimal scheduling allocation and order. Currently, it is implemented using a greedly algorithm which selects the next task to be scheduled based on the 'cheapest' node at the that point in time.


### How To Run TaskScheduler
There are two compulsary input arguments, a) input graph, b) number of processors. There are also optional arguments, please see TaskScheduler Interface below.

1) Navigate to the folder containing scheduler.jar
2) Run the command **java -jar scheduler.jar \<input-graph\> \<num processors\>**


### TaskScheduler Interface
- java -jar scheduler.jar INPUT.dot P [OPTION]
- INPUT. dot a task graph with integer weights in dot format
- P number of processors to schedule the INPUT graph on
- Optional :
- -p N use N cores for execution in parallel (default is sequential)
- -v visualise the search
- -o OUPUT output file is named OUTPUT (default is INPUT-output.dot)


### Acknowledgements
- **Graph Parser**: [PayPal Graph Parser](https://github.com/paypal/digraph-parser)
- **Command Line Arguments Parsing**: [Apache Commons CLI](https://commons.apache.org/proper/commons-cli/)
- **Unit Testing**: [JUnit](https://junit.org/)
- **Logger**: [log4j](https://logging.apache.org/log4j/)
