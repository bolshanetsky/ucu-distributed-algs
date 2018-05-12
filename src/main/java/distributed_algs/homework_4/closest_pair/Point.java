package distributed_algs.homework_4.closest_pair;

public class Point implements Comparable<Point> {

    private final Double x;
    private final Double y;

    public Point(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public double distanceTo(Point that) {
        double deltaX = this.getX() - that.getX();
        double deltaY = this.getY() - that.getY();
        return Math.sqrt(deltaX*deltaX + deltaY*deltaY);
    }

    @Override
    public int compareTo(Point that) {
        return Double.compare(this.getY(), that.getY());
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "{" + "x=" + x + ", y=" + y + '}';
    }
}
