#### Running TAP

1. Move to the directory where you placed the tap product. Assuming it is
in the home directory:

        cd ~/tap

2. Ensure the JVM has enough memory to run the application.

        export _JAVA_OPTIONS="-Xmx2048M -Xss1M"

3. You can either run TAP using either of the following methods:

    - Using sbt:

        sbt run

    - Using play:
        play

        If you get an exception in the SBtParser as in the following lines :

            java.lang.UnsupportedOperationException: Position.start on class scala.reflect.internal.util.OffsetPosition
            at scala.reflect.internal.util.Position.start(Position.scala:114)
            at sbt.internals.parser.SbtParser.sbt$internals$parser$SbtParser$$convertStatement$1(SbtParser.scala:148)

        This means Play is picking up an old SBT Parser that cannot deal with parsing the latest layout of the SBT files
        as explained in this exchange: [play-parse-bug](https://github.com/sbt/sbt/issues/1739)

        In order to avoid this error, and run the application with play, you should update play to use the 2.10.4
        Parser which is the minimum level parser that fixes the error.
        This update will take a long time to run, but is a one off.

            SBT_SCALA_VERSION=2.10.4 play update

        You then have to run play always specifying the SBT Scala version as described in the next step.

    - Using play, but determining the SBT Scala version

          This is necessary if you want to run play and need to avoid the SbtParser exception.

              SBT_SCALA_VERSION=2.10.4 play run

    After you run the TAP Server, you should see the following lines, indicating that the server is
    running and listening to requests :

        Picked up _JAVA_OPTIONS: -Xms512M -Xmx2048M -Xss1M
        [info] Loading global plugins from ~/.sbt/0.13/plugins
        [info] Loading project definition from ~/tap/project
        [info] Set current project to tap (in build file:~/tap/)
        --- (Running the application, auto-reloading is enabled) ---
        [info] p.c.s.AkkaHttpServer - Listening for HTTP on /0:0:0:0:0:0:0:0:9000

    You can now connect to the server by typing the following in your browser:

        http://localhost:9000/

    The product should start and you can use it.
    You should see the following in your browser.

        TAP Server with GraphQL
        Use the graphiql IDE here

    To end the server, close your browser session then type return in your console
    where the server is running.
    The following message should appear on the console.

        [info] p.c.s.AkkaHttpServer - Stopping server...
