import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {

        Graph graph = new Graph();
        graph.setVisible(true);
        graph.setLocationRelativeTo(null);
        /*RBTree myTree = new RBTree();
        runInsertBenchmark(myTree, 100000);*/

    }

    private static void runInsertBenchmark(RBTree tree, int numNodesToInsert) {
        long startTime = System.nanoTime();

        // Benchmark pentru inserare
        for (int i = 0; i < numNodesToInsert; ++i) {
            tree.insert(i);
        }

        long endTime = System.nanoTime();
        long insertDuration = endTime - startTime;

        System.out.println("Timp pentru inserare a " + numNodesToInsert + " noduri: "
                + TimeUnit.NANOSECONDS.toMillis(insertDuration) + " milisecunde.");
    }
}