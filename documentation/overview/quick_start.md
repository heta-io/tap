## Quick Start

- [Get started with Docker](#get-started-with-docker)
- [Get started locally](#get-started-locally)





### Get started with Docker
Getting Started with Docker - Coming soon

### Get started locally

Clone the project source code with git:

```bash
git clone https://github.com/heta-io/tap
```

Ensure you have the [latest sbt](http://www.scala-sbt.org) installed

Add a ```LocalSbtSettings.scala``` file in the ```project``` directory. This will hold your specific settings for your build:
   
```scala
import sbt._

object LocalSbtSettings {
 val githubBaseUrl = "https://github.com/uts-cic/tap" //Change this to your fork
 val scaladocApiBaseUrl = "https://uts-cic.github.io/tap" //Change this to your fork
 val dockerRepoURI = "the.url.to.publish.docker.images.to"
}
```

4. Run TAP with enough memory to process larger models:

  ```bash
  sbt -J-Xmx4096M run
  ```

5. Connect to ```http://localhost:9000``` with your web browser

