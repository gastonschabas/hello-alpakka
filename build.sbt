ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val AkkaVersion = "2.6.18"
lazy val AlpakkaKafka = "3.0.0"
lazy val AkkaHttpVersion = "10.2.9"
lazy val ScalaLogging = "3.9.4"
lazy val TypeSafeConfig = "1.4.2"
lazy val LogbackClassic = "1.2.10"
lazy val ScalaTest = "3.2.11"
lazy val TestcontainersScala = "0.40.2"

lazy val root = (project in file("."))
  .settings(
    name := "hello-alpakka",
    libraryDependencies ++= Seq(
      "com.typesafe.akka"          %% "akka-stream"       % AkkaVersion,
      "com.typesafe.akka"          %% "akka-stream-kafka" % AlpakkaKafka,
      "com.typesafe.akka"          %% "akka-http"         % AkkaHttpVersion,
      "com.typesafe.scala-logging" %% "scala-logging"     % ScalaLogging,
      "com.typesafe"                % "config"            % TypeSafeConfig,
      "ch.qos.logback"              % "logback-classic"   % LogbackClassic
    )
  )
