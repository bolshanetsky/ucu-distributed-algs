package homework_4.spark

import org.apache.spark.graphx.{GraphLoader, PartitionStrategy, VertexId}
import org.apache.spark.{SparkConf, SparkContext}

object CountTriangleApp {

  def main(args: Array[String]): Unit = {

    if (args.length != 1) throw new RuntimeException("Illegal Arguments, you should pass file path as a single parameter")

    val filePath = args(0)

    val conf = new SparkConf().setAppName("TriangleCounter").setMaster("local[*]")
    val sc = new SparkContext(conf);

    val graph = GraphLoader.edgeListFile(sc, filePath, true).partitionBy(PartitionStrategy.RandomVertexCut)

    val triCounts = graph.triangleCount().vertices

    triCounts.collect().foreach(println)
  }
}