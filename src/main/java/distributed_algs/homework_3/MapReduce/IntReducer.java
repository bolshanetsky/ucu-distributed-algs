package distributed_algs.homework_3.MapReduce;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class IntReducer implements Callable<Map<Integer, Integer>> {

    private final List<Map<Integer, Integer>> listOfMap;
    private final int rangeLength;
    private final int rangeStart;
    private final Map<Integer, Integer> finalMap;

    public IntReducer(List<Map<Integer, Integer>> listOfMappedResult, Map<Integer, Integer> finalMap, int rangeStart,
                      int rangeLength) {
        this.listOfMap = listOfMappedResult;
        this.rangeStart = rangeStart;
        this.rangeLength = rangeLength;
        this.finalMap = finalMap;
    }

    @Override
    public Map<Integer, Integer> call() {
        HashMap<Integer, Integer> resultMap = new HashMap<>();

        listOfMap.forEach(map -> {
            for (int i = rangeStart ; i < rangeStart + rangeLength; i++) {
                if (map.containsKey(i)) {
                    finalMap.put(i, finalMap.getOrDefault(i, 0) + map.get(i));
                }
            }
        });

        return resultMap;
    }
}
