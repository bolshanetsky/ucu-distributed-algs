package distributed_algs.homework_3.MapReduce;

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

public class IntegerMapReduce {

    /**
     * Count integers using parallel Map Reduce approach.
     * @param inputData - array of integers to count
     * @param coresNumber - number of cpu cores, used to create thread pool
     * @param numbersRange - range of numbers to count e.g. from 0 to 9.
     * @return
     */
    @SneakyThrows
    public Map<Integer, Integer> countNumbers(ArrayList<Integer> inputData, int coresNumber, int numbersRange) {
        ConcurrentHashMap<Integer, Integer> resultMap = new ConcurrentHashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(coresNumber);

        // throw exception if input size cannot be divide on cores count.
        if (inputData.size() % coresNumber != 0) throw new IllegalArgumentException("Number of cores doesn't correspond to input size");

        // Execute Map, each mapper maps it's own range of data, split from original input.
        int processingField = inputData.size() / coresNumber;
        List<Future<Map<Integer, Integer>>> list = new ArrayList<>();
        for (int core = 0; core < coresNumber; core++) {
            IntMapper map = new IntMapper(inputData.subList(core * processingField, (core + 1) * processingField));
            list.add(executor.submit(map));
        }

        // gather map results
        List<Map<Integer, Integer>> resultList = new CopyOnWriteArrayList<>();
        for (Future<Map<Integer, Integer>> future : list) {
            Map<Integer, Integer> map = future.get();
            resultList.add(map);
        }

        // execute Reduce, each reducer reduces only numbers in certain range.
        int processingRange = (int) Math.ceil((double)numbersRange / coresNumber);
        for (int core = 0; core < coresNumber; core++) {
            IntReducer map = new IntReducer(resultList, resultMap, core * processingRange, processingRange);
            list.add(executor.submit(map));
        }

        // wait for reduce to finish.
        for (Future future : list) {
            future.get();
        }

        return resultMap;
    }

    // RUN ME
    public static void main(String[] args) {

        // Test data
        ArrayList<Integer> integers = generateListOfIntegersPowerOfTwo(16);

        // Count number using MapReduce
        Map<Integer, Integer> result = new IntegerMapReduce().countNumbers(integers, 2, 10);
        printMap(result);

        // Verify calculations
        int sum = result.values().stream().mapToInt(Integer::intValue).sum();
        System.out.println("Control: Input size = " + integers.size() + " -> Output size = " + sum);
        if (sum != integers.size()) throw new RuntimeException();
    }

    private static int getRandomIntegerInRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    private static ArrayList<Integer> generateListOfIntegersPowerOfTwo(int powerOfTwo) {
        int length = (int) Math.pow(2, powerOfTwo);
        ArrayList<Integer> result = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            result.add(getRandomIntegerInRange(0, 9));
        }

        return result;
    }

    private static void printMap(Map<Integer, Integer> map) {
        map.entrySet().forEach(entry -> {
            System.out.println("Number " + entry.getKey() + " -> count: " + entry.getValue());
        });
    }
}
