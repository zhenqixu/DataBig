import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.rdd.{PairRDDFunctions,RDD}
import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.{SQLContext}

import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.util.MLUtils;
import org.apache.spark.mllib.classification.SVMWithSGD;
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;

val sqlCtx = new SQLContext(sc)
import sqlCtx._
val data_1 = sqlCtx.jsonFile("data.json")
data_1.show
val RDD = data_1.rdd.map(_.mkString(" ")).map(line=>line.split(" ").filter(str=>(str!="null" && str!="Infinity")).map(_.toDouble))
val data = RDD.map(s=>LabeledPoint(s(4), Vectors.dense(s(0),s(1),s(2),s(3))))
data.take(3).foreach(println)

val splits = data.randomSplit(Array(0.6, 0.4), seed = 11L);
val training = splits(0).cache();

val test = splits(1);
val numIterations = 100;
val model = SVMWithSGD.train(training, numIterations);
val predictionAndLabels = test.map { case LabeledPoint(label, features) =>
val prediction = model.predict(features)
(prediction, label)
};
val metrics = new MulticlassMetrics(predictionAndLabels);
val accuracy = metrics.accuracy;
println(s"Accuracy = $accuracy");