## IDE Set-up

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
