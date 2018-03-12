package distributed_algs.homework_1.prefix_sum;

import lombok.SneakyThrows;

import java.util.concurrent.Callable;

public class DownSummator implements Callable<Void> {

    private final int level;
    private final int threadNumber;
    private final int processingField;
    private static int[] array;

    public DownSummator(int level, int core, int processingField) {
        this.level = level;
        this.threadNumber = core;
        this.processingField = processingField;
        this.array = PrefixSum.outputArray;
    }

    @SneakyThrows
    public Void call() {
        System.out.println(String.format("Run level:%s, d_summator:%s, pocessingField: %s", level, threadNumber,
                processingField));
        int lBound = processingField * threadNumber;
        int rBound = processingField * (threadNumber + 1);

        for (int sumIndex = lBound + (1 << level) - 1; sumIndex < rBound; sumIndex += 1 << level) {
            int val = array[sumIndex];
            array[sumIndex] = array[sumIndex] + array[sumIndex - (1 << level - 1)];
            array[sumIndex - (1 << level - 1)] = val;
        }

        PrefixSum.printArray(array);

        return null;
    }
}
