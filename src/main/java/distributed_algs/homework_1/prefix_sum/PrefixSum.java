package distributed_algs.homework_1.prefix_sum;

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

            List<Future> list = new ArrayList<Future>();
            for (int core = 0; core < coreNumber; core++) {
                Summator sum = new Summator(level, core, processingField);
                list.add(executor.submit(sum));
            }

            for (Future fut : list) {
                fut.get();
            }
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

            List<Future> list = new ArrayList<Future>();
            for (int core = 0; core < coreNumber; core++) {
                DownSummator dsum = new DownSummator(level, core, processingField);
                list.add(executor.submit(dsum));
            }

            for (Future dsum : list) {
                dsum.get();
            }
        }
        outputArray[length] = cumSum;

        return outputArray;
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
