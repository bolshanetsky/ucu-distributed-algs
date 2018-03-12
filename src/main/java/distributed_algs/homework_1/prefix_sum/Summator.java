package distributed_algs.homework_1.prefix_sum;

import java.util.concurrent.Callable;

public class Summator implements Callable<Void> {

    protected final int level;
    protected final int threadNumber;
    protected final int processingField;
    protected static int[] array;

    public Summator(Integer level, Integer core, Integer processingField) {
        this.level = level;
        this.threadNumber = core;
        this.processingField = processingField;
        this.array = PrefixSum.outputArray;
    }

    public Void call() {
        System.out.println(String.format("Run level:%s, summator:%s, pocessingField: %s", level, threadNumber,
                processingField));
        int lBound = processingField * threadNumber;
        int rBound = processingField * (threadNumber + 1);

        int from = lBound - 1 + (1 << level);
        int step = 1 << level;

        for (int sumIndex = from; sumIndex < rBound; sumIndex += step) {
            array[sumIndex] = array[sumIndex] + array[sumIndex - (1 << level - 1)];
        }

        PrefixSum.printArray(array);

        return null;
    }
}
