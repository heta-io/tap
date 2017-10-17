#### Building TAP

1. Move to the directory where you placed the tap product. Assuming it is
in the home directory, you should see a build.sbt file containing instruction
on how the build is done, as well as this README.md file:

        cd ~/tap

2. Compile the product.

     The first compile will be very slow as dependencies have to be fetched and placed
     in the ~/.ivy2 directory.

         sbt compile

     The following compile warnings seem to be safe to ignore, but ensure the
     last compile ends with [SUCCESS] :

        com.typesafe.akka:akka-stream_2.12:2.5.6 is selected over {2.5.4, 2.5.3, 2.4.19}
        com.typesafe.akka:akka-actor_2.12:2.5.6 is selected over {2.5.4, 2.4.19}
        com.google.guava:guava:22.0 is selected over 19.0
        ...srcc/main/scala/handlers/GraphQlHandler.scala:42: match may not be exhaustive.
        ...
        there were 8 feature warnings; re-run with -feature for details
        two warnings found^[[0m
        Total time: 11 s, completed Oct 7, 2017 12:39:37 PM^[[0m


      If you get the following compile errors, it means the jars that TAP relies are not
      being accessed from the lib directory.
      Ensure the missing jars are installed, as explained in the
      "Getting a local copy of TAP" section.

        import cc.factorie.app.nlp._
        ~/tap/src/main/scala/au.edu.utscic.tap/nlp/factorie/Annotator.scala:28: not found: value DocumentAnnotatorPipeline


