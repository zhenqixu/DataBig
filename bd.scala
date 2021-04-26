import org.apache.spark.mllib.classification.SVMWithSGD;
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics;
import org.apache.spark.mllib.util.MLUtils;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;


val data = MLUtils.loadLibSVMFile(sc, "data.txt");
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