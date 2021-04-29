import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.rdd.{PairRDDFunctions,RDD}
import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.{SQLContext}


val sqlCtx = new SQLContext(sc)
import sqlCtx._
val data_1 = sqlCtx.jsonFile("hdfs:///user/xz2998/DataBig/data.json")
data_1.show

val DataRDD = data_1.select("account1","account2","account3").rdd.map(_.mkString(",")).map(s => Vectors.dense(s.split(",").map(_.toDouble))).cache()
val numClusters = 3
val numIterations = 1000
val clusters = KMeans.train(DataRDD, numClusters, numIterations)

val WSSSE = clusters.computeCost(DataRDD)
println("Within Set Sum of Squared Errors = " + WSSSE)

var tt_data = clusters.predict(DataRDD)
var label = data_1.select("label").rdd
var res = tt_data.zip(label)
res.collect().foreach{println}

// Load and parse the data
/*val data = sc.textFile("hdfs:///user/xz2998/DataBig/data.csv")
val parsedData = data.map(s => Vectors.dense(s.split(' ').map(_.toDouble))).cache()

// Cluster the data into two classes using KMeans
val numClusters = 3
val numIterations = 100
val clusters = KMeans.train(parsedData, numClusters, numIterations)

// Evaluate clustering by computing Within Set Sum of Squared Errors
val WSSSE = clusters.computeCost(parsedData)
println("Within Set Sum of Squared Errors = " + WSSSE)

val result = clusters.predict(parsedData)
result.take(10).foreach(println)
*/


