# TaskScheduler: Team 3 - Vaporware

![Vaporware](https://github.com/lukethompsxn/TaskScheduler/blob/master/res/misc/Vaporware.png "Vaporware")

### Github Username & UPIs

| Github Username | UPI       |
| --------------- | --------- |
| lukethompsxn    | `ltho948` |
| AbhinavBehal    | `abeh957` |
| Joelz1          | `jcla776` |
| Z-Qi            | `zqia153` |
| RodgerRG        | `rgu663`  |


### About TaskScheduler
TaskScheduler is a java command line application which takes an input graph and a number of processors. It then generates a schedule for executing these tasks which is the optimal scheduling allocation and order. The algorithm is implemented using an AStar algorithm using various pruning methods.

### How To Build TaskScheduler
- If you have Gradle installed, run `gradle jar` in the project root directory from command line.
- If you want to use the wrapper, run `./gradlew jar` in the project root directory from command line.

### How To Run TaskScheduler
There are two compulsary input arguments, a) input graph, b) number of processors. There are also optional arguments, please see TaskScheduler Interface below.

1) Navigate to the folder containing scheduler.jar
2) Run the command **java -jar scheduler.jar \<input-graph\> \<num processors\>**

If using the optional parameter `-v` then a graphic window will open. In this window, you can view a statistical overview on the right hand pane. A Processor Allocation and Graph Tree Hierarchy are provided in the tab pane. Simply click on the different tab to move between then.
- In the **Processor Allocation** each processor which has been specified in the arguments will appear. Each task which is allocated to that processor will be displayed under the corresponding processor in the scheduled time order (including blank space). The number on each box corresponds to the number of the task it represents.
- In the **Graph Tree Hierarchy** you can view the tree hierarchy resulting from the task dependencies. The layout is structured such that on each tier (excluding entry nodes) it sits below its lowest parent. When a node is **currently** scheduled, it will appear in blue with the corresponding edge. If a node was **previously** scheduled, but is not in the current schedule, then the node and the corresponding edge will appear in pink. If a node is **yet to be seen** then it will appear in grey.


### TaskScheduler Interface
```
java -jar scheduler.jar INPUT.dot P [OPTION]
INPUT. dot a task graph with integer weights in dot format
P number of processors to schedule the INPUT graph on
Optional :
-p N use N cores for execution in parallel (default is sequential)
-v visualise the search
-o OUPUT output file is named OUTPUT (default is INPUT-output.dot)
```

### Project Information
The project wiki home page can be found [here](https://github.com/lukethompsxn/TaskScheduler/wiki). Direct links to wiki pages: [Planning](https://github.com/lukethompsxn/TaskScheduler/wiki/Planning), [Project Structure](https://github.com/lukethompsxn/TaskScheduler/wiki/Project-Structure), [Basic Scheduler](https://github.com/lukethompsxn/TaskScheduler/wiki/Basic-Scheduler), [A* Implementation](https://github.com/lukethompsxn/TaskScheduler/wiki/A*-Implementation), [Parallelisation](https://github.com/lukethompsxn/TaskScheduler/wiki/Parallelisation), [Visualisation](https://github.com/lukethompsxn/TaskScheduler/wiki/Visualisation), [Libraries & Plugins](https://github.com/lukethompsxn/TaskScheduler/wiki/Libraries-&-Plugins), [Meetings & Minutes](https://github.com/lukethompsxn/TaskScheduler/wiki/Meetings-&-Minutes).

A history of issues is located [here](https://github.com/lukethompsxn/TaskScheduler/issues?utf8=%E2%9C%93&q=).


### Acknowledgements
- **Graph Parser**: [PayPal Graph Parser](https://github.com/paypal/digraph-parser)
- **Command Line Arguments Parsing**: [Apache Commons CLI](https://commons.apache.org/proper/commons-cli/)
- **Unit Testing**: [JUnit](https://junit.org/)
- **Logger**: [log4j](https://logging.apache.org/log4j/)
- **Graphing**: [GraphStream](http://graphstream-project.org/)
- **Visualisation Components**: [JFoenix](http://www.jfoenix.com/)
