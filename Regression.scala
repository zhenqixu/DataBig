import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.{SQLContext}

import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.util.MLUtils;
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;
import org.apache.spark.mllib.classification.{LogisticRegressionModel, LogisticRegressionWithLBFGS}
import org.apache.spark.mllib.feature.PCA
import org.apache.spark.mllib.feature.Normalizer
import org.apache.spark.mllib.util.MLUtils

import org.apache.spark.rdd.RDD

val sqlCtx = new SQLContext(sc)
import sqlCtx._
val data_1 = sqlCtx.jsonFile("DataBig/data.json")
data_1.show
val size = data_1.columns.size
val RDD = data_1.rdd.map(_.mkString(" ")).map(line=>line.split(" ").filter(str=>str.exists(_.isLetter)^true).map(_.toDouble)).filter(s=>s.length==size)
//RDD.map(_.mkString(" ")).collect().foreach(println)

val data = RDD.map(s=>LabeledPoint(s(4), Vectors.dense(s(0),s(1),s(2),s(3))))
data.take(3).foreach(println)

val normalizer1 = new Normalizer()
val data1 = data.map(x => LabeledPoint(x.label, normalizer1.transform(x.features)))
data1.take(3).foreach(println)


val pca = new PCA(2).fit(data1.map(_.features))
val projected = data1.map(p => p.copy(features = pca.transform(p.features)))
projected.take(5).foreach(println)

// Split data into training (80%) and test (20%).
val splits = projected.randomSplit(Array(0.8, 0.2), seed = 11L)
val training = splits(0).cache()
val test = splits(1)

// Run training algorithm to build the model
val model = new LogisticRegressionWithLBFGS().setNumClasses(10).run(training)

// Compute raw scores on the test set.
val predictionAndLabels = test.map { case LabeledPoint(label, features) =>
  val prediction = model.predict(features)
  (prediction, label)
}

// Get evaluation metrics.
val metrics = new MulticlassMetrics(predictionAndLabels)
val accuracy = metrics.accuracy
println(s"Accuracy = $accuracy")