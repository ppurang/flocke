lazy val root = (project in file(".")).settings(publish := {}, publishLocal := {}).
  aggregate(core, stream, server)

lazy val core = project

lazy val stream = project.dependsOn(core)

lazy val server = project.dependsOn(stream)

shellPrompt in ThisBuild := ShellPrompt.buildShellPrompt(Common.version)

