#### Building TAP

(1) Move to the directory where you placed the tap product. Assuming it is in the home directory, you should see a build.sbt file containing instruction on how the build is done, as well as this README.md file:

```bash
cd ~/tap
```

        
(2) Create a ```LocalSbtSettings.scala``` file in the ```project``` directory. This will hold your specific settings for your build:

```scala
import sbt._

object LocalSbtSettings {

  val githubBaseUrl = "https://github.com/uts-cic/tap" //Change this to your fork
  val scaladocApiBaseUrl = "https://uts-cic.github.io/tap" //Change this to your fork
  
  val dockerRepoURI = "the.url.to.publish.docker.images.to"
}
```

(3) Compile the product.

The first compile will be very slow as dependencies have to be fetched and placed
in the ```~/.ivy2 directory```.

```bash
sbt compile
```

The following compile warnings seem to be safe to ignore, but ensure the
last compile ends with [SUCCESS] :

```sbtshell
  com.typesafe.akka:akka-stream_2.12:2.5.6 is selected over {2.5.4, 2.5.3, 2.4.19}
  com.typesafe.akka:akka-actor_2.12:2.5.6 is selected over {2.5.4, 2.4.19}
  com.google.guava:guava:22.0 is selected over 19.0
  ...srcc/main/scala/handlers/GraphQlHandler.scala:42: match may not be exhaustive.
  ...
  there were 8 feature warnings; re-run with -feature for details
  two warnings found^[[0m
  Total time: 11 s, completed Oct 7, 2017 12:39:37 PM^[[0m
```


