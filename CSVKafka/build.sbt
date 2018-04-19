name := "CSVKafka"

version := "0.1"

scalaVersion := "2.12.5"

libraryDependencies ++= Seq(
  "org.apache.kafka" %% "kafka" % "1.0.1"
)

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"

libraryDependencies += "org.scalatest" % "scalatest_2.12" % "3.0.5" % "test"