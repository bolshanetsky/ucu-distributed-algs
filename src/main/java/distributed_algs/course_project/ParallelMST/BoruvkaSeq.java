package distributed_algs.course_project.ParallelMST;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BoruvkaSeq {

    public Set<Edge> mst = new HashSet<>();

    /**
     * Return set of edges forming MST using Boruvka's algorithm
     * @param edges
     * @param verticesCount
     * @return set of edges representing MST of the original graph.
     */
    public Set<Edge> createBoruvkaGraph(List<Edge> edges, int verticesCount) {

        Queue<Component> components = initializeComponents(edges, verticesCount);
        Component component;

        while (!components.isEmpty()) {

            component = components.poll();

            if (!component.lock.tryLock()) {
                continue; // another thread is working on this component
            }

            if (component.isMerged) {
                component.lock.unlock();
                continue; // node is already merged to MST
            }

            // get minimal weighted edge
            Edge edge = component.getMinEdge();
            if (edge == null) {
                break; // all edges are merged to the tree
            }

            Component secondComponent = edge.getConnectedComponent(component);

            if(!secondComponent.lock.tryLock()) {
                // release current component, if nearest counterpart is locked
                component.lock.unlock();
                components.add(component);
                continue;
            }

            if (secondComponent.isMerged) {
                // release both nodes if nearest counterpart is merged already.
                secondComponent.lock.unlock();
                component.lock.unlock();
                components.add(component);
                continue;
            }

            secondComponent.isMerged = true;

            component.merge(secondComponent);
            components.add(component);
            mst.add(edge);

            component.lock.unlock();
            secondComponent.lock.unlock();
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
