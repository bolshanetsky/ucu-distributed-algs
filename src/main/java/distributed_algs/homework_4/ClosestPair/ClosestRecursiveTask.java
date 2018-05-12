package distributed_algs.homework_4.ClosestPair;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

import static distributed_algs.homework_4.ClosestPair.ClosestPair.boundaryMerge;
import static distributed_algs.homework_4.ClosestPair.ClosestPair.closest;
import static distributed_algs.homework_4.ClosestPair.ClosestPair.merge;

public class ClosestRecursiveTask extends RecursiveTask<Double> {

    private static final int THRESHOLD = 10000;
    private final Point[] pointsByX;
    private final Point[] pointsByY;
    private final int start;
    private final int end;

    public ClosestRecursiveTask(Point[] pointsByX, Point[] pointsByY, int start, int end) {
        this.pointsByX = pointsByX;
        this.pointsByY = pointsByY;
        this.start = start;
        this.end = end;

    }

    @Override
    protected Double compute() {
        if (end - start <= 1) return Double.POSITIVE_INFINITY;

        int midIndex = start + (end - start) / 2;
        Point median = pointsByX[midIndex];
        double delta;

        // if problem size drops below threshold, we process sequential.
        if (end - start < THRESHOLD) {
             return closest(pointsByX, pointsByY, start, end);
        } else {
            List<RecursiveTask> tasks = Arrays
                    .asList(new ClosestRecursiveTask(pointsByX, pointsByY, start, midIndex), new ClosestRecursiveTask(pointsByX, pointsByY, midIndex + 1, end));

            delta = ForkJoinTask.invokeAll(tasks).stream().mapToDouble(ForkJoinTask<Double>::join).min().getAsDouble();

            merge(pointsByY, start, midIndex, end);
            return boundaryMerge(pointsByY, median, delta, start, end);
        }
    }
}
