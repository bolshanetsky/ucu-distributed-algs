package distributed_algs.course_project.ParallelMST;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Component {

    private int nodeId;
    public Lock lock = new ReentrantLock();

    // edges pointing to other components.
    public PriorityQueue<Edge> edges = new PriorityQueue<>(Comparator.comparingDouble(edge -> edge.weight));
    public boolean isMerged;

    public Component(int nodeId) {
        this.nodeId = nodeId;
    }

    public void addEdge(Edge edge) {
        if (edge.getFromComponent() == this && edge.getToComponent() == this) {
            return;
        } else {
            edges.add(edge);
        }
    }

    public void merge(Component compToMerge) {
        ArrayList<Edge> allEdges = new ArrayList<>(this.edges);
        allEdges.addAll(compToMerge.getEdges());
        this.edges.clear();

        for (Edge edge : allEdges) {
            if (edge.getToComponent() == compToMerge) {
                edge.setToComponent(this);
            }
            if (edge.getFromComponent() == compToMerge) {
                edge.setFromComponent(this);
            }

            addEdge(edge);
        }

//        internalEdges.addAll(compToMerge.internalEdges);

        // clean up
        compToMerge.edges.clear();
//        compToMerge.internalEdges.clear();
    }
    public PriorityQueue<Edge> getEdges() {
        return edges;
    }

    public Edge getMinEdge() {
        return edges.peek();
    }
}
