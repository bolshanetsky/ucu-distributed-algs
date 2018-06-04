package distributed_algs.course_project.ParallelMST;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;

public class BoruvkaWorker implements Callable<Void> {

    private final Queue<Component> components;
    private final Set<Edge> mst;

    public BoruvkaWorker(Queue<Component> components, Set<Edge> mst) {
        this.components = components;
        this.mst = mst;
    }

    @Override
    public Void call() {
        Component component = components.poll();

        if (component == null || !component.lock.tryLock()) {
            return null;
        }

        if (component.isMerged) {
            component.lock.unlock();
            return null; // node is already merged to MST
        }

        // get minimal weighted edge
        Edge edge = component.getMinEdge();
        if (edge == null) {
            return null; // all components are merged to the tree
        }

        Component secondComponent = edge.getConnectedComponent(component);

        if(!secondComponent.lock.tryLock()) {
            // release current component, if nearest counterpart is locked
            component.lock.unlock();
            components.add(component);
            return null;
        }

        if (secondComponent.isMerged) {
            // release both nodes if nearest counterpart is merged already.
            secondComponent.lock.unlock();
            component.lock.unlock();
            components.add(component);
            return null;
        }

        secondComponent.isMerged = true;

        component.merge(secondComponent);
        components.add(component);
        mst.add(edge);

        component.lock.unlock();
        secondComponent.lock.unlock();

        return null;
    }
}
