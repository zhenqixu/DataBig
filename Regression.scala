import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.{SQLContext}

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
val dataFrame = sqlCtx.jsonFile("DataBig/data.json")

//1. Remove the redundant data
val distinctDataFrame = dataFrame.distinct()

distinctDataFrame.count()
//2. Remove null rows.
val validDataFrame = distinctDataFrame.na.drop("any")
validDataFrame.count()

//3. Convert Dataframe to RDD, and convert the entry to double type
val size = validDataFrame.columns.size
  //3.1 Convert all the entries to string type.
val dataRDDString = validDataFrame.rdd.map(_.mkString(" ")).cache()
  //3.2 Filte non-numeric entries and convert the rest to Double type.
val dataRDDDouble = dataRDDString.map(line=>line.split(" ").filter(str=>str.exists(_.isLetter)^true).map(_.toDouble))
  //3.3 Filte rows with invalid size.
val datafiltered = dataRDDDouble.filter(s=>s.length==size)
//RDD.map(_.mkString(" ")).collect().foreach(println)

//4. Create label for dataset
val dataLabeledPoint = datafiltered.map(s=>LabeledPoint(s(4), Vectors.dense(s(0),s(1),s(2),s(3))))
dataLabeledPoint.take(3).foreach(println)

//5. Normalize the Dataset
val normalizer = new Normalizer()
val dataNormalized = dataLabeledPoint.map(x => LabeledPoint(x.label, normalizer.transform(x.features))).cache()
//dataNormalized.take(3).foreach(println)

//6. Use PCA to process Dataset
val pca = new PCA(3).fit(dataNormalized.map(_.features))
val projected = dataNormalized.map(p => p.copy(features = pca.transform(p.features)))
projected.take(5).foreach(println)

//7. Split data into training (80%) and test (20%).
val splits = projected.randomSplit(Array(0.8, 0.2), seed = 11L)
val training = splits(0).cache()
val test = splits(1)

//8. Run training algorithm to build the model
val model = new LogisticRegressionWithLBFGS().setNumClasses(10).run(training)

//9. Compute raw scores on the test set.
val predictionAndLabels = test.map { case LabeledPoint(label, features) =>
  val prediction = model.predict(features)
  (prediction, label)
}

//10. Get evaluation metrics.
val metrics = new MulticlassMetrics(predictionAndLabels)
val accuracy = metrics.accuracy
println(s"Accuracy = $accuracy")
