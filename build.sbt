scalaVersion := "2.12.7" // Also supports 2.11.x

val http4sVersion = "0.21.0-M4"
val doobieVersion = "0.7.0"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.tpolecat" %% "doobie-core"      % doobieVersion,
  "org.tpolecat" %% "doobie-postgres"  % doobieVersion
)

scalacOptions ++= Seq("-Ypartial-unification")