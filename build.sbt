name := "flink-learning-example"
scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.apache.flink" %% "flink-scala" % "1.2.0",
  "org.apache.flink" %% "flink-streaming-scala" % "1.2.0",
  "org.apache.flink" %% "flink-clients" % "1.2.0"
)
