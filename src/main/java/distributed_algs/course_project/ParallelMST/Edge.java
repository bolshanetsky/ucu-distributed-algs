package distributed_algs.course_project.ParallelMST;

import lombok.Data;

@Data
public class Edge {

    public int from;
    public int to;
    public double weight;
    private Component fromComponent;
    private Component toComponent;

    public Edge(int from, int to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public void setFromComponent(Component fromComponent) {
        this.fromComponent = fromComponent;
    }

    public void setToComponent(Component toComponent) {
        this.toComponent = toComponent;
    }

    public Component getConnectedComponent(Component otherComponent) {
        if (fromComponent == otherComponent) return toComponent;
        else if (toComponent == otherComponent) return fromComponent;
        throw new RuntimeException("This edge is not connected to component" + otherComponent);
    }

    @Override
    public String toString() {
        return String.format("%d -> %d : %s", from , to, weight);
    }
}