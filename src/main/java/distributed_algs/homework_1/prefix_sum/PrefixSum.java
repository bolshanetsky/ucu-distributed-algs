package distributed_algs.homework_1.prefix_sum;

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PrefixSum {
    public static int MAX_CORES = 2;
    public static int[] outputArray;

    @SneakyThrows
    public static int[] prefixSum(int[] inputArray) {
        int length = inputArray.length;
        outputArray = Arrays.copyOf(inputArray, length + 1);
        ExecutorService executor = Executors.newFixedThreadPool(MAX_CORES);

        int depth = (int) (Math.log((double) length) / Math.log(2.0));

        for (int level = 1; level < depth + 1; level++) {
            int processingField = 1 << level;
            int coreNumber = length / processingField;
            if (coreNumber > MAX_CORES) {
                coreNumber = MAX_CORES;
                processingField = length / coreNumber;
            }

            runSumTasksParallel(executor, level, processingField, coreNumber, Summator.class);
        }

        int cumSum = outputArray[length - 1];
        outputArray[length - 1] = 0;

        for (int level = depth; level > 0; level--) {
            int processingField = 1 << level;
            int coreNumber = length / processingField;
            if (coreNumber > MAX_CORES) {
                coreNumber = MAX_CORES;
                processingField = length / coreNumber;
            }

            runSumTasksParallel(executor, level, processingField, coreNumber, DownSummator.class);
        }
        outputArray[length] = cumSum;

        return outputArray;
    }

    @SneakyThrows
    private static void runSumTasksParallel(ExecutorService executor, int level, int processingField, int coreNumber,
                                            Class<? extends Summator> task) {
        List<Future> list = new ArrayList<>();
        for (int core = 0; core < coreNumber; core++) {
            Callable sum = task.getConstructor(Integer.class, Integer.class, Integer.class).newInstance(level, core,
                    processingField);
            list.add(executor.submit(sum));
        }

        for (Future fut : list) {
            fut.get();
        }
    }

    public static void printArray(int[] array) {
        StringBuilder strb = new StringBuilder();
        strb.append("[");
        for (int i : array) {
            strb.append(" " + i);
        }
        strb.append("]");

        System.out.println(strb.toString());
    }
}
