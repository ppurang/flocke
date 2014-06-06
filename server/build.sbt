name := "flocke-server"

version := Common.version

organization := Common.organization

scalaVersion := Common.scalaVersion

resolvers ++= Common.resolvers

//scalacOptions ++= Common.scalacOptions

libraryDependencies ++= Seq(
  Common.http4sCore, Common.http4sDsl, Common.http4sBlaze, Common.argonaut,
  Common.scalatest
  )

Revolver.settings

seq(com.typesafe.sbt.SbtStartScript.startScriptForClassesSettings: _*)

cancelable := true

fork := true

logBuffered := false

//seq(bintrayPublishSettings:_*)

licenses += ("BSD", url("http://www.tldrlegal.com/license/bsd-3-clause-license-%28revised%29"))
