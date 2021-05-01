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
import org.apache.spark.mllib.feature.{StandardScaler, StandardScalerModel}


import org.apache.spark.rdd.RDD

//Create Dataframe
val sqlCtx = new SQLContext(sc)
import sqlCtx._
val dataFrame = sqlCtx.jsonFile("DataBig/data.json")
dataFrame.show

//Convert Dataframe to RDD, and flite invalid entry.
val size = dataFrame.columns.size
val dataRDD = dataFrame.rdd.map(_.mkString(" ")).map(line=>line.split(" ").filter(str=>str.exists(_.isLetter)^true).map(_.toDouble)).filter(s=>s.length==size)
//RDD.map(_.mkString(" ")).collect().foreach(println)

//Create label for dataset
val dataLabeledPoint = dataRDD.map(s=>LabeledPoint(s(4), Vectors.dense(s(0),s(1),s(2),s(3))))
dataLabeledPoint.take(3).foreach(println)

//Scaler
val scaler = new StandardScaler().fit(dataLabeledPoint.map(x => x.features))
val dataScaled = dataLabeledPoint.map(x => LabeledPoint(x.label, scaler.transform(x.features)))

//1st. Normalize the Dataset
val normalizer = new Normalizer()
val dataNormalized = dataLabeledPoint.map(x => LabeledPoint(x.label, normalizer.transform(x.features)))
dataNormalized.take(3).foreach(println)

//Use PCA to process Dataset
val pca = new PCA(3).fit(dataNormalized.map(_.features))
val projected = dataNormalized.map(p => p.copy(features = pca.transform(p.features)))
projected.take(5).foreach(println)

// Split data into training (80%) and test (20%).
val splits = dataLabeledPoint.randomSplit(Array(0.8, 0.2), seed = 11L)
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