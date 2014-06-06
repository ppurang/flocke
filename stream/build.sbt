name := "flocke-stream"

organization := Common.organization

scalaVersion := Common.scalaVersion

resolvers ++= Common.resolvers

scalacOptions ++= Common.scalacOptions

cancelable := Common.cancelable

libraryDependencies ++= Seq (
  Common.stream, Common.scalacheck, Common.scalatest
)

initialCommands := """
                   | println("stream...")
                   | import org.purang.net.flocke.Flocke
                   | import org.purang.net.flocke.stream.FlockeStream._
                   | import scalaz._, Scalaz._
                   |
                   | implicit val f = Flocke.hex("54:42:49:97:a7:56")
                   |""".stripMargin
