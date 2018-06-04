package ditributed_algs.course_project_test;

import distributed_algs.course_project.ParallelMST.BoruvkaParallel;
import distributed_algs.course_project.ParallelMST.BoruvkaSeq;
import distributed_algs.course_project.ParallelMST.Edge;
import distributed_algs.homework_4.closest_pair.ClosestPair;
import distributed_algs.homework_4.closest_pair.Point;
import lombok.SneakyThrows;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BoruvkaMST {

    private static final int NUMBER_OF_VERTICIES_BIG = 10000;
    private static final int NUMBER_OF_VERTICIES_MEDIUM = 250;

    @Test
    public void testSequentialSmall() {

        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1 , 1));
        edges.add(new Edge(1, 2 , 2));
        edges.add(new Edge(0, 2 , 3));
        edges.add(new Edge(2, 3 , 5));
        edges.add(new Edge(3, 0 , 4));


        BoruvkaSeq boruvka = new BoruvkaSeq();
        Set<Edge> boruvkaGraph = boruvka.createBoruvkaGraph(edges, 4);
        int totalWeight = 0;

        for (Edge edge : boruvkaGraph) {
            totalWeight += edge.weight;
            System.out.println(edge);
        }

        Assert.assertEquals(totalWeight, 7);
        Assert.assertEquals(boruvkaGraph.size(), 3);
    }

    @Test
    public void testParallelSmall() {

        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(1, 2 , 1));
        edges.add(new Edge(2, 3 , 2));
        edges.add(new Edge(1, 3 , 3));
        edges.add(new Edge(3, 4 , 5));
        edges.add(new Edge(4, 1 , 4));


        BoruvkaParallel boruvka = new BoruvkaParallel();
        Set<Edge> boruvkaGraph = boruvka.computeBoruvkaGraph(edges, 4);
        int totalWeight = 0;

        for (Edge edge : boruvkaGraph) {
            totalWeight += edge.weight;
            System.out.println(edge);
        }

        Assert.assertEquals(totalWeight, 7);
        Assert.assertEquals(boruvkaGraph.size(), 3);
    }

    @Test
    public void testSequentialMedium() {

        List<Edge> edges = readTestDataFromFile("data/mediumEWG.txt");

        BoruvkaSeq boruvka = new BoruvkaSeq();
        Set<Edge> boruvkaGraph = boruvka.createBoruvkaGraph(edges, NUMBER_OF_VERTICIES_MEDIUM);
        double totalWeight = 0;

        for (Edge edge : boruvkaGraph) {
            totalWeight += edge.weight;
            System.out.println(edge);
        }

        DecimalFormat df = new DecimalFormat("#.#####");
        totalWeight = Double.valueOf(df.format(totalWeight));

        Assert.assertEquals(totalWeight, 10.46351);
        Assert.assertEquals(boruvkaGraph.size(), 249);
    }

    @Test
    public void testParallelMedium() {

        List<Edge> edges = readTestDataFromFile("data/mediumEWG.txt");


        BoruvkaParallel boruvka = new BoruvkaParallel();
        Set<Edge> boruvkaGraph = boruvka.computeBoruvkaGraph(edges, NUMBER_OF_VERTICIES_MEDIUM);
        double totalWeight = 0;

        for (Edge edge : boruvkaGraph) {
            totalWeight += edge.weight;
            System.out.println(edge);
        }

        DecimalFormat df = new DecimalFormat("#.#####");
        totalWeight = Double.valueOf(df.format(totalWeight));

        Assert.assertEquals(totalWeight, 10.46351);
        Assert.assertEquals(boruvkaGraph.size(), 249);
    }

    @Test
    public void testSequentialBig() {

        List<Edge> edges = readTestDataFromFile("data/10000EWG.txt");

        BoruvkaSeq boruvka = new BoruvkaSeq();
        Set<Edge> boruvkaGraph = boruvka.createBoruvkaGraph(edges, NUMBER_OF_VERTICIES_BIG);
        double totalWeight = 0;

        for (Edge edge : boruvkaGraph) {
            totalWeight += edge.weight;
            System.out.println(edge);
        }

        DecimalFormat df = new DecimalFormat("#.#####");
        totalWeight = Double.valueOf(df.format(totalWeight));

        Assert.assertEquals(totalWeight, 65.24072);
        Assert.assertEquals(boruvkaGraph.size(), 9999);
    }

    @Test
    public void testParallelBig() {

        List<Edge> edges = readTestDataFromFile("data/10000EWG.txt");


        BoruvkaParallel boruvka = new BoruvkaParallel();
        Set<Edge> boruvkaGraph = boruvka.computeBoruvkaGraph(edges, NUMBER_OF_VERTICIES_BIG);
        double totalWeight = 0;

        for (Edge edge : boruvkaGraph) {
            totalWeight += edge.weight;
            System.out.println(edge);
        }

        DecimalFormat df = new DecimalFormat("#.#####");
        totalWeight = Double.valueOf(df.format(totalWeight));

        Assert.assertEquals(totalWeight, 65.24072);
        Assert.assertEquals(boruvkaGraph.size(), 9999);
    }

    @Test
    public void perfTest() {

        List<Edge> edges = readTestDataFromFile("data/10000EWG.txt");

        // JVM warm up
        BoruvkaSeq boruvkaWarm = new BoruvkaSeq();
        boruvkaWarm.createBoruvkaGraph(new ArrayList<>(edges), NUMBER_OF_VERTICIES_BIG);

        // sequential run
        long startTime = System.currentTimeMillis();
        BoruvkaSeq boruvkaSeq = new BoruvkaSeq();
        boruvkaSeq.createBoruvkaGraph(new ArrayList<>(edges), NUMBER_OF_VERTICIES_BIG);
        long endTime = System.currentTimeMillis();
        long sequentialTime = endTime - startTime;
        System.out.println("Run time for sequential: " + sequentialTime);

        // parallel run
        startTime = System.currentTimeMillis();
        BoruvkaSeq boruvkaPar = new BoruvkaSeq();
        boruvkaPar.createBoruvkaGraph(new ArrayList<>(edges), NUMBER_OF_VERTICIES_BIG);
        endTime = System.currentTimeMillis();
        long parallelTime = endTime - startTime;
        System.out.println("Run time for parallel: " + parallelTime);
        System.out.println("DIFFERENCE " + ((double)sequentialTime/parallelTime));
    }


    @SneakyThrows
    private List<Edge> readTestDataFromFile(String pathToFile) {
        File file = new File(pathToFile);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        ArrayList<Edge> edges = new ArrayList<>();
        while ((line = bufferedReader.readLine()) != null) {
            String[] split = line.split(" ");
            Edge edge = new Edge(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Double.parseDouble(split[2]));
            edges.add(edge);
        }
        fileReader.close();
        return edges;
    }
}
