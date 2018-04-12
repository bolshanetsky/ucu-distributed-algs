package distributed_algs.homework_2.merge_sort;

import distributed_algs.homework_1.prefix_sum.PrefixSum;
import org.testng.Assert;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;

/**
 * Implementation of Parallel Merger Sort using Java ForkJoin framework.
 * This framework specially designed to parallel recursive tasks.
 */
public class ParallelMergeSort {

    public static final int NUMBER_OF_PROCESSORS = 2;

    public int[] parallelSort(int[] array) {

        ForkJoinPool pool = new ForkJoinPool(NUMBER_OF_PROCESSORS);
        pool.invoke(new MergeAction(array, 0, array.length));

        return array;
    }

    // RUN ME TEST
    public static void main(String[] args) {
        int[] arr = ParallelMergeSort.generateTestData((int)Math.pow(2, 20));
        arr = new ParallelMergeSort().parallelSort(arr);
        PrefixSum.printArray(arr);

        Assert.assertTrue(ParallelMergeSort.isArraySortedAsc(arr), "Array is not sorted in ascending order");
    }

    public static int[] generateTestData(int arrayLength) {
        int[] arr = new int[arrayLength];

        for (int i = 0; i < arrayLength; i++) {
            arr[i] = new Random().nextInt(arrayLength);
        }

        return arr;
    }

    public static boolean isArraySortedAsc(int[] arr) {
        int prev = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < prev) {
                return false;
            }
        }

        return true;
    }
}
