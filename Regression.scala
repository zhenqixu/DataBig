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

var dataFrame = dataFrame0.union(dataFrame1).union(dataFrame2)
dataFrame.count()
//1. Remove the redundant data
dataFrame = dataFrame.distinct()
//distinctDataFrame.count()

//2. Replace null values with AVG of that column
dataFrame = dataFrame.na.fill(dataFrame.columns.zip(dataFrame.select(dataFrame.columns.map(mean(_)): _*).first.toSeq).toMap)
dataFrame.show(1000)
//validDataFrame.count()

//3. Convert Dataframe to RDD, and convert the entry to double type
val size = dataFrame.columns.size
  //3.1 Convert all the entries to string type.
val dataRDDString = dataFrame.rdd.map(_.mkString(" "))
  //3.2 Filte non-numeric entries and convert the rest to Double type.
val dataRDDDouble = dataRDDString.map(line=>line.split(" ").filter(str=>str.exists(_.isLetter)^true).map(_.toDouble))
  //3.3 Filte rows with invalid size.
val datafiltered = dataRDDDouble.filter(s=>s.length==size)
//RDD.map(_.mkString(" ")).collect().foreach(println)

//4. Create label for dataset
val dataLabeledPoint = datafiltered.map(s=>LabeledPoint(s(4), Vectors.dense(s(0),s(1),s(2),s(3))))
dataLabeledPoint.take(3).foreach(println)

val scaler = new StandardScaler().fit(dataLabeledPoint.map(x => x.features))
val dataScaler = dataLabeledPoint.map(x => LabeledPoint(x.label, scaler.transform(x.features)))

//5. Normalize the Dataset
val normalizer = new Normalizer()
val dataNormalized = dataScaler.map(x => LabeledPoint(x.label, normalizer.transform(x.features))).cache()
//dataNormalized.take(3).foreach(println)

//ElementwiseProduct
import org.apache.spark.mllib.feature.ElementwiseProduct
val transformingVector = Vectors.dense(1.0, 1.0, 1.0, 0.25)
val transformer = new ElementwiseProduct(transformingVector)
val transformedData = dataNormalized.map(x => LabeledPoint(x.label, transformer.transform(x.features)))


//7. Split data into training (80%) and test (20%).
val splits = transformedData.randomSplit(Array(0.8, 0.2), seed = 11L)
val training = splits(0).cache()
val test = splits(1)

//8. Run training algorithm to build the model
val model = new LogisticRegressionWithLBFGS().setNumClasses(2).run(training)

//9. Compute raw scores on the test set.
val predictionAndLabels = test.map { case LabeledPoint(label, features) =>
  val prediction = model.predict(features)
  (prediction, label)
}

//10. Get evaluation metrics.
val metrics = new MulticlassMetrics(predictionAndLabels)
val accuracy = metrics.accuracy
println(s"Accuracy = $accuracy")
