name := """Social Crawler"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  javaJpa,
  "org.hibernate" % "hibernate-entitymanager" % "5.1.0.Final",
  "com.restfb" % "restfb" % "1.22.0", 
  "mysql" % "mysql-connector-java" % "5.1.38",
  cache,
  javaWs
)


fork in run := true