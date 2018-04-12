package distributed_algs.homework_3.MapReduce;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class IntMapper implements Callable<Map<Integer, Integer>> {

    private final List<Integer> ints;

    public IntMapper(List<Integer> list) {
        this.ints = list;
    }

    @Override
    public Map<Integer, Integer> call() {
        HashMap<Integer, Integer> map = new HashMap<>();
        ints.forEach(integer -> map.put(integer, map.getOrDefault(integer, 0) + 1));
        return map;
    }
}
