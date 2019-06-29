name := "kafka-streams-join-example"

version := "0.1"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.apache.kafka" %% "kafka" % "0.11.0.0",
  "com.typesafe" % "config" % "1.3.1",
  "log4j" % "log4j" % "1.2.17",
  "org.apache.avro" % "avro-maven-plugin" % "1.8.2",
  "org.apache.avro" % "avro" % "1.8.2",
  "org.apache.kafka" % "kafka-streams" % "0.11.0.0"
)

