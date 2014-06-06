name := "flocke-core"

organization := Common.organization

scalaVersion := Common.scalaVersion

resolvers ++= Common.resolvers

scalacOptions ++= Common.scalacOptions

cancelable := Common.cancelable

libraryDependencies ++= Seq (
  Common.scalacheck, Common.scalatest
)

initialCommands := """
                   | println("core...")
                   | import org.purang.net.flocke.MacAddress._
                   | import org.purang.net.flocke.Flocke, Flocke._
                   |
                   |""".stripMargin
