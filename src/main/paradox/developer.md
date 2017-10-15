### Developer docs

#### Project Structure

Although TAP is a Play project, the project structure is that of a Maven/sbt project
and does not follow the Play default project structure.

The build.sbt file indicates that we are using an alternative project structure to Play.

        disablePlugins(PlayLayoutPlugin)
        PlayKeys.playMonitoredFiles ++= (sourceDirectories in (Compile, TwirlKeys.compileTemplates)).value

For more details, see the "Default SBT Layout" section in the "Anatomy of a Play Application"
chapter in the Play 2.6 Documentation:

        [play-project-anatomy](https://playframework.com/documentation/2.6.x/Anatomy)


#### Running of the Command Line

Refer to [user-documentaton](./user.md)

#### Setting the TAP project in Intellij IDEA

An IDE provides a more convenient and efficient way of doing development.
The Intellij IDEA is preferred because of its Scala support, and relative ease
of use.

1. Check that the Scala and SBT plugins are installed.
   Install them if they are missing.

         File-Settings-Plugins

2. Import the project into Intellij IDEA

         File - New - Project from Existing sources

         Navigate to the build.sbt file in the tap directory where you cloned the project source code.


3. Ensure the JVM has enough memory to work with (-Xmx2048M -Xss1M -Xmx512M)

         File - Settings - Other Settings - SBT - VM Parameters

4. Add the library dependencies

         File - Project Structure - Modules

         Point to and add the jars you imported into the lib directory:

         cc.factorie.app.nlp.all-models-1.0.0.jar
         nlpfactorie_2.12-0.1.jar

5. Run Tap

        Run - Run Tap
