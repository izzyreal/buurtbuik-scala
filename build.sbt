scalaVersion := "2.12.7" // Also supports 2.11.x

val http4sVersion = "0.21.0-M4"

// Only necessary for SNAPSHOT releases
resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion
)

scalacOptions ++= Seq("-Ypartial-unification")