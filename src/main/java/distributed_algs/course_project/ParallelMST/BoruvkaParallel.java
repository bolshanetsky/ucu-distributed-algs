package distributed_algs.course_project.ParallelMST;

import lombok.SneakyThrows;

import java.util.*;
import java.util.concurrent.*;

public class BoruvkaParallel {

    private static final int NUMBER_OF_CORES = 2;
    public Set<Edge> mst = ConcurrentHashMap.newKeySet();

    /**
     * Return set of edges forming MST using Boruvka's algorithm
     * @param edges
     * @param verticesCount
     * @return set of edges representing MST of the original graph.
     */
    @SneakyThrows
    public Set<Edge> computeBoruvkaGraph(List<Edge> edges, int verticesCount) {
        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_CORES);
        Queue<Component> components = initializeComponents(edges, verticesCount);
        ArrayList<Future> listOfFutures = new ArrayList<>();
        while (!components.isEmpty()) {
            listOfFutures.add(executor.submit(new BoruvkaWorker(components, mst)));
        }

        // join all threads
        for (Future future : listOfFutures) {
            future.get();
        }

        return mst;
    }

    private Queue<Component> initializeComponents(List<Edge> edges, int verticesCount) {
        Component[] components = new Component[verticesCount];
        for (int i = 0; i < components.length ; i++) {
            components[i] = new Component(i + 1);
        }

        for (Edge edge : edges) {
            components[edge.from].addEdge(edge);
            components[edge.to].addEdge(edge);
            edge.setFromComponent(components[edge.from]);
            edge.setToComponent(components[edge.to]);
        }

        return new ConcurrentLinkedQueue<>(Arrays.asList(components));
    }
}
