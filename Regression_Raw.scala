import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.{SQLContext}
import org.apache.spark.sql.functions.mean

import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.util.MLUtils;
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;
import org.apache.spark.mllib.classification.{LogisticRegressionModel, LogisticRegressionWithLBFGS}
import org.apache.spark.mllib.feature.PCA
import org.apache.spark.mllib.feature.Normalizer
import org.apache.spark.mllib.feature.{StandardScaler, StandardScalerModel}


import org.apache.spark.rdd.RDD

//Create Dataframe
val sqlCtx = new SQLContext(sc)
import sqlCtx._
val dataFrame0 = sqlCtx.jsonFile("DataBig/data0.json")
val dataFrame1 = sqlCtx.jsonFile("DataBig/data1.json")
val dataFrame2 = sqlCtx.jsonFile("DataBig/data2.json")

val dataFrame = dataFrame0.union(dataFrame1).union(dataFrame2)

//1. Convert Dataframe to RDD, and convert the entry to double type
val size = dataFrame.columns.size
  //1.1 Convert all the entries to string type.
val dataRDDString = dataFrame.rdd.map(_.mkString(" "))
  //1.2 Filte non-numeric entries and convert the rest to Double type.
val dataRDDDouble = dataRDDString.map(line=>line.split(" ").filter(str=>str.exists(_.isLetter)^true).map(_.toDouble))
  //1.3 Filte rows with invalid size.
val datafiltered = dataRDDDouble.filter(s=>s.length==size)
//RDD.map(_.mkString(" ")).collect().foreach(println)

//2. Create label for dataset
val dataLabeledPoint = datafiltered.map(s=>LabeledPoint(s(4), Vectors.dense(s(0),s(1),s(2),s(3))))
dataLabeledPoint.take(3).foreach(println)

//3. Split data into training (80%) and test (20%).
val splits = dataLabeledPoint.randomSplit(Array(0.8, 0.2), seed = 11L)
val training = splits(0).cache()
val test = splits(1)

//4. Run training algorithm to build the model
val model = new LogisticRegressionWithLBFGS().setNumClasses(2).run(training)

//5. Compute raw scores on the test set.
val predictionAndLabels = test.map { case LabeledPoint(label, features) =>
  val prediction = model.predict(features)
  (prediction, label)
}

//10. Get evaluation metrics.
val metrics = new MulticlassMetrics(predictionAndLabels)
val accuracy = metrics.accuracy
println(s"Accuracy = $accuracy")
