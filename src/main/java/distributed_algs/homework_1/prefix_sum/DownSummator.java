package distributed_algs.homework_1.prefix_sum;

import lombok.SneakyThrows;

public class DownSummator extends Summator {

    public DownSummator(Integer level, Integer core, Integer processingField) {
        super(level, core, processingField);
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
