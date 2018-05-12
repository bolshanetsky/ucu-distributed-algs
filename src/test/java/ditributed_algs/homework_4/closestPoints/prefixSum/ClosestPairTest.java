package ditributed_algs.homework_4.closestPoints.prefixSum;

import distributed_algs.homework_4.ClosestPair.ClosestPair;
import distributed_algs.homework_4.ClosestPair.Point;
import org.testng.annotations.Test;

import java.util.Random;

public class ClosestPairTest {

    @Test
    public void test1() {
        Point[] testArray = new Point[] {
            new Point(1.0, 1.0),
            new Point(100.0, 2.0),
            new Point(3.0, 2.0),
            new Point(4.0, 8.0),
            new Point(10.0, 20.0),
            new Point(-30.0, -41.0),
            new Point(-30.0, -43.0),
            new Point(-36.0, -1.0),
            new Point(-130.0, 425.0),
        };

        new ClosestPair(testArray, true);
    }

    @Test
    public void test2() {
        Point[] testArray = generateTestArray(100000, -1000000, +1000000);

        new ClosestPair(testArray, true);
    }

    @Test
    public void perfTest() {
        Point[] testArray = generateTestArray(100000, -1000000, +1000000);

        // JVM warm up
        new ClosestPair(testArray, false);

        // sequential run
        long startTime = System.currentTimeMillis();
        new ClosestPair(testArray, false);
        long endTime = System.currentTimeMillis();
        long sequentialTime = endTime - startTime;
        System.out.println("Run time for sequential: " + sequentialTime);

        // parallel run
        startTime = System.currentTimeMillis();
        new ClosestPair(testArray, true);
         endTime = System.currentTimeMillis();
        long parallelTime = endTime - startTime;
        System.out.println("Run time for parallel: " + parallelTime);
        System.out.println("DIFFERENCE " + ((double)sequentialTime/parallelTime));
    }


    private Point[] generateTestArray(int arraySize, int rangeMin, int rangeMax) {
        Point[] arr = new Point[arraySize];
        Random rnd = new Random();

        for (int i = 0; i < arraySize; i++) {
            double randomValueX = rangeMin + (rangeMax - rangeMin) * rnd.nextDouble();
            double randomValueY = rangeMin + (rangeMax - rangeMin) * rnd.nextDouble();
            arr[i] = new Point(randomValueX, randomValueY);
        }

        return arr;
    }
 }
