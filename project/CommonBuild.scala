import sbt._
import Keys._

object Common {
  val organization = "org.purang.net"

  val version = "0.1.0"

  val scalaVersion = "2.10.4"

  val resolvers = Seq(
    "Scalaz Bintray Repo" at "http://dl.binray.com/scalaz/releases"
  )

  val scalacOptions = Seq("-unchecked", 
 	"-optimize", 
        "-feature", 
	"-language:_", 
        "-deprecation", "-Xfatal-warnings", "-Xlint",  "-encoding",  "UTF-8", "-target:jvm-1.7", "-Ywarn-adapted-args", "-Ywarn-value-discard", "-Xlog-reflective-calls", "-Yinline-warnings",   "-Yclosure-elim",
  "-Yinline", "-Xverify", "-Ywarn-all")

  val cancelable = true

  object Versions {
    val scalaz = "7.0.6"
    
  }

  //libs 
  val http4sCore  = "org.http4s" %% "http4s-core" % "0.2.0-SNAPSHOT"
  val http4sDsl   = "org.http4s" %% "http4s-dsl" % "0.2.0-SNAPSHOT"
  val http4sBlaze = "org.http4s" %% "http4s-blaze" % "0.2.0-SNAPSHOT"
  val argonaut    = "io.argonaut" %% "argonaut" % "6.0.4"
  val stream      = "org.scalaz.stream" %% "scalaz-stream" % "0.4.1"
  //test libs
  val expecty     = "org.expecty" % "expecty" % "0.10" % "test"
  val scalacheck  = "org.scalacheck" %% "scalacheck" % "1.11.4" % "test"
  val specs2      = "org.specs2" %% "specs2" % "2.3.12" % "test"
  val scalatest   = "org.scalatest" %% "scalatest" % "2.1.7" % "test"


 val initialCommands ="""
                     | println("initial commands ...") 
                     |""".stripMargin

 val fork = true

}

object ShellPrompt {
  object devnull extends ProcessLogger {
    def info (s: => String) {}
    def error (s: => String) { }
    def buffer[T] (f: => T): T = f
  }

  val oops = "-?-"

  def currBranch = ("git branch" lines_! devnull headOption) getOrElse oops stripPrefix "* "

  def currModifications = ("git status -sb" lines_! devnull tail).foldLeft("")(
      (a,n) =>n.trim.split(" ").headOption.map(m => if(a.contains(m)) a else a + m) getOrElse oops
    )

  def buildShellPrompt(ver: String) = {
    (state: State) => {
      val currProject = Project.extract (state).currentProject.id
      "%s : %s : %s : %s> ".format (
        currProject, currBranch, ver, currModifications
      )
    }
  }
}
