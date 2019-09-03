scalaVersion := "2.12.7" // Also supports 2.11.x

val http4sVersion = "0.21.0-M4"
val doobieVersion = "0.7.0"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.tpolecat" %% "doobie-core" % doobieVersion,
  "org.tpolecat" %% "doobie-postgres" % doobieVersion,
  "org.slf4j" % "slf4j-simple" % "2.0.0-alpha0",
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "io.circe" %% "circe-generic" % "0.12.0-RC4",
  "io.circe" %% "circe-literal" % "0.12.0-RC4"
)

scalacOptions ++= Seq("-Ypartial-unification")