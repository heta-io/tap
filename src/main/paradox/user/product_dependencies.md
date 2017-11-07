#### Product Dependencies

The following products are required to build and run TAP, and to potentially
help you contribute to the TAP project.
They are basically the tools required to run an SBT play application.
Other dependencies are also specified in build.sbt and will be automatically
downloaded during the first build.

Many of the tools/products will be downloaded during the build as build.sbt dependencies
or indirectly by other products.
Perhaps you want to **start with cloning and building TAP, then refer to the list in
this section in response to errors to see what you might be missing**.

1. Git

    You will need the Git source control command line tools and/or a Git gui
    interface if you want to clone and contribute to the project, otherwise you
    can just download a zip of the source code.

    The git tools are available either through your software installation tool such as Synaptic or
    from the [Git website](https://git-scm.com).

2. sbt

    sbt will build the project and download much of the needed software for us, but if it is not
    on our system already, we need to download it first.
    Even if sbt is already installed, we may need to use a different level of sbt to do the build.

    Once you downloaded the code, position in the ~/tap directory, and issue the following command :

        sbt about

    in the ~/tap directory.
    You should get the following output :

        [info] This is sbt 0.13.16
        [info] The current project is {file:~/tap/}tap 3.0.4or
        [info] The current project is built against Scala 2.12.3

    If the sbt version is different from than 0.13.16, wait until the first compile is done, and issue
    the check again as the sbt build should download the correct version for you.
    If not, download the the 0.13.16 version that is used in the project from the [sbt website](http://www.scala-sbt.org/)

3. Scala

    The Scala compiler version used to compile the code is 2.12.3.
    This should match the run-time Scala version as Scala run-time is not
    backward compatible.
    This should be taken care of by the build.sbt file on the first compile.

    You can issue the following command to check your Scala version:

        scala -version

    This is the expected output:

        Scala code runner version 2.12.3 -- Copyright 2002-2017, LAMP/EPFL and Lightbend, Inc.:

4. Java SE Development Kit (JDK) and Java run-time (JRE)

    Check that you have an up to date version of the JRE.
    It should be at least 1.8 (The build is tested using 1.8), otherwise you have to update
    the Java run time on your machine.

    Issue:

        java -version

    The JDK is also needed to compile java files in the project, and should also be at the 1.8
    level. Issue :

        javac -version

    If the JDK or JRE are missing or at an older level, the 1.8 level can be downloaded
    from [Oracle](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).

    Installing the JDK should take care of installing the JRE also.

    You should provide enough memory for the JDK and JRE to build and run this project. While TAP will run with standard memory settings, certain NLP processes which require the loading of large models will require additional memory.
    
    The following values are suggested :

       -Xmx4096M -Xss1M

5. The Play Framework

    This can be downloaded from the [Play Framework website](https://playframework.com).

    Press on the Download 2.6.6 button and follow the appropriate instructions for your
    operating system.


