package ditributed_algs.homework_1.prefixSum;

import distributed_algs.homework_1.prefix_sum.PrefixSum;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PrefixSumTest {

    @Test
    public void test0() {
        int[] ints = {1, 1, 1, 1, 1, 1, 1, 1};
        ints = PrefixSum.prefixSum(ints);

        int sum = 0;
        for (int i : ints) sum += i;
        Assert.assertEquals(sum, 36);
    }

    @Test
    public void test1() {
        int[] ints = {1, 1, 1, 1, 1, 1, 1, 1};
        ints = PrefixSum.prefixSum(ints);
        PrefixSum.MAX_CORES = 4;

        int sum = 0;
        for (int i : ints) sum += i;
        Assert.assertEquals(sum, 36);
    }

    @Test
    public void test2() {
        int[] ints = {1, 1, 1, 1, 1, 1, 1, 1};
        ints = PrefixSum.prefixSum(ints);

        PrefixSum.MAX_CORES = 1;
        int sum = 0;
        for (int i : ints) sum += i;
        Assert.assertEquals(sum, 36);
    }

    @Test
    public void test3() {
        int[] ints = {1, 1, 1, 1, 1, 1, 1, 1};
        ints = PrefixSum.prefixSum(ints);

        PrefixSum.MAX_CORES = 8;
        int sum = 0;
        for (int i : ints) sum += i;
        Assert.assertEquals(sum, 36);
    }
}
