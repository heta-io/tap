## Project Structure

Although TAP is a Play project, the project structure is that of a Maven/sbt project
and does not follow the Play default project structure.

The build.sbt file indicates that we are using an alternative project structure to Play.

        disablePlugins(PlayLayoutPlugin)
        PlayKeys.playMonitoredFiles ++= (sourceDirectories in (Compile, TwirlKeys.compileTemplates)).value

For more details, see the "Default SBT Layout" section in the "Anatomy of a Play Application"
chapter in the Play 2.6 Documentation:

        [play-project-anatomy](https://playframework.com/documentation/2.6.x/Anatomy)



