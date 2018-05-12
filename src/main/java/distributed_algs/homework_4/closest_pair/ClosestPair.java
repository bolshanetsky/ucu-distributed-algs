package distributed_algs.homework_4.closest_pair;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ForkJoinPool;

public class ClosestPair {

    public static Point closest1, closest2;
    public static double closestDistance = Double.POSITIVE_INFINITY;

    public static final int NUMBER_OF_PROCESSORS = 2;

    public ClosestPair(Point[] points, boolean runParallel) {
        if (points == null) throw new IllegalArgumentException("Input is invalid");
        closest1 = closest2 = null;
        closestDistance = Double.POSITIVE_INFINITY;

        ForkJoinPool pool = new ForkJoinPool(NUMBER_OF_PROCESSORS);
        
        // sort by x
        Arrays.sort(points, Comparator.comparingDouble(Point::getX));

        // make copy of array to use it for y-based sorting
        Point[] pointsByY = Arrays.copyOf(points, points.length);

        if (runParallel) {
            // parallel version
            pool.invoke(new ClosestRecursiveTask(points, pointsByY, 0, points.length - 1));
        }
        else {
            // sequential version
            closest(points, pointsByY, 0, points.length -1);
        }

        // print out result
        System.out.println("Closest points are : " + closest1 + "and " + closest2 + " with distance = " +
                closestDistance);
    }

    // sequential recursive call
    public static double closest(Point[] pointsByX, Point[] pointsByY, int start, int end) {
        if (end - start <= 1) return Double.POSITIVE_INFINITY;

        int midIndex = start + (end - start) / 2;
        Point median = pointsByX[midIndex];

        double delta1 = closest(pointsByX, pointsByY, start, midIndex);
        double delta2 = closest(pointsByX, pointsByY, midIndex + 1, end);
        double delta = Math.min(delta1, delta2);

        merge(pointsByY, start, midIndex, end);

        return boundaryMerge(pointsByY, median, delta, start, end);
    }

    public static double boundaryMerge(Point[] pointsByY, Point median, double delta, int start, int end) {

        Point[] m_closePoints = new Point[pointsByY.length];

        // sequence of points closer than delta, sorted by y-coordinate
        int m = 0;
        for (int i = start; i <= end; i++) {
            if (Math.abs(pointsByY[i].getX() - median.getX()) < delta)
                m_closePoints[m++] = pointsByY[i];
        }

        // compare each point to its neighbors with y-coordinate closer than delta
        for (int i = 0; i < m; i++) {
            // a geometric argument shows that this loop iterates at most 7 times
            for (int j = i + 1; (j < m) && (m_closePoints[j].getY() - m_closePoints[i].getY() < delta); j++) {
                double distance = m_closePoints[i].distanceTo(m_closePoints[j]);
                if (distance < delta) {
                    delta = distance;
                    if (distance < closestDistance) {
                        System.out.println("Old distance: " + closestDistance + " new distance = " + distance);
                        closestDistance = delta;
                        closest1 = m_closePoints[i];
                        closest2 = m_closePoints[j];
                    }
                }
            }
        }
        return delta;
    }


    public static void merge(Point[] pointsByY, int start, int middle, int end) {
        Point[] localArray = new Point[end - start];

        int leftIndex = start;
        int rightIndex = middle;
        int currentIndex = 0;

        while (leftIndex < middle && rightIndex < end) {
            Point left = pointsByY[leftIndex];
            Point right = pointsByY[rightIndex];

            if (left.compareTo(right) <= 0) {
                localArray[currentIndex] = pointsByY[leftIndex];
                leftIndex++;
            } else {
                localArray[currentIndex] = pointsByY[rightIndex];
                rightIndex++;
            }
            currentIndex++;
        }

        if (leftIndex < middle) {
            System.arraycopy(pointsByY, leftIndex, localArray, currentIndex, middle - leftIndex);
        } else if (rightIndex < end) {
            System.arraycopy(pointsByY, rightIndex, localArray, currentIndex, end - rightIndex);
        }

        System.arraycopy(localArray, 0, pointsByY, start, localArray.length);
    }

    // Print array for debugging purposes.
    public static void printArray(Object[] array) {
        StringBuilder strb = new StringBuilder();
        strb.append("[");
        for (Object i : array) {
            strb.append(" " + i);
        }
        strb.append("]");

        System.out.println(strb.toString());
    }
}
