package ditributed_algs.homework_1.prefixSum;

import distributed_algs.homework_1.prefix_sum.PrefixSum;
import distributed_algs.homework_2.merge_sort.ParallelMergeSort;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ParallelMergeTest {



    @Test
    public void test0() {
        int[] arr = ParallelMergeSort.generateTestData((int)Math.pow(2, 20));
        arr = new ParallelMergeSort().parallelSort(arr);
        PrefixSum.printArray(arr);

        Assert.assertTrue(ParallelMergeSort.isArraySortedAsc(arr), "Array is not sorted in ascending order");
    }
}
