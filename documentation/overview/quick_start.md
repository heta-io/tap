## Quick Start

- [Get started with Docker](#get-started-with-docker)
    - [Docker for Windows (Windows 10/ Hyper-V)](#docker-for-windows-windows-10-hyper-v)
    - [Docker ToolBox (Mac, Linux, Windows 7 and 8)](#docker-toolbox-mac-linux-windows-7-and-8)
- [Get started locally without Docker](#get-started-locally-without-docker)





### Get started with Docker

#### Docker for Windows (Windows 10/ Hyper-V)
@@@ note { title=Notice }
Docker for Windows requires:

Windows 10 64bit: Pro, Enterprise or Education (1607 Anniversary Update, Build 14393 or later).

Virtualization is enabled in BIOS. Typically, virtualization is enabled by default. This is different from having Hyper-V enabled. For more detail see [Virtualization must be enabled](https://docs.docker.com/docker-for-windows/troubleshoot/#virtualization-must-be-enabled) in Troubleshooting.

CPU SLAT-capable feature.

At least 4GB of RAM.
@@@

This is the easiest way to get started with docker and TAP. Windows 10 has an app called Docker For Windows which will handle everything automatically for you.
See [How To install Docker For Windows](https://docs.docker.com/docker-for-windows/install/)

1. Ensure you have docker for windows installed and it is running.
2. First, quickly create a virtual switch for your virtual machines (VMs) to share, so they can connect to each other.
   
    * Launch Hyper-V Manager
    * Click Virtual Switch Manager in the right-hand menu
    * Click Create Virtual Switch of type External
    * Give it the name "myswitch", and check the box to share your host machine’s active network adapter
    
    ![switch creation](https://i.imgur.com/zgt6jYd.png)
  
3. Now create a VM we can use to run TAP on. (You may need to run this in admin, press windows key + x > Command Prompt (Admin))

        docker-machine create -d hyperv --hyperv-memory 4096 --hyperv-cpu-count 2 --hyperv-virtual-switch "myswitch" myvm1
        
    Notice we are passing in some custom variables

    * We are giving it 4gb of ram
    * We are allocating 2 cpus
    * We are choosing the switch we just created "myswitch"
    * We are naming it "myvm1"

4.  Once it is created, verify that it is running and write down it's IP. (here mine is 10.1.1.190)
        
        docker-machine ls
        
    ![docker machine ip](https://i.imgur.com/OSIfVV4.png)
        
5. To control your newly created docker machine there is a simple command you can run

        docker-machine env myvm1
        
    ![docker env](https://i.imgur.com/0Xumdot.png)
        
    This will return a command you can run in order to automatically send your docker commands to your vm. (the last line minus the REM)

    **Yours may be different than mine, be sure to run whatever command your terminal returns.**

    run that command
        
        @FOR /f "tokens=*" %i IN ('docker-machine env myvm1') DO @%i
        
    Now any docker commands we run will run on the machine we created! Now let's get TAP running.

6. run docker run and pass in a number of parameters

        docker run -e JAVA_OPTS="-Xms512M -Xmx6000M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=256M" -e TAP_HOSTS="10.1.1.190:9000" -e TAP_SECRET="test" -p 9000:9000 andrewresearch/tap:3.2.2
        
    There are a few parameters here:
   
    * JAVA_OPTS is passing in our custom java environment variables which allow us to increase the amount of memory the application can use.
    * TAP_HOSTS is telling our application which IP is authorised to use our application, In this case you need to pass in your machine IP you wrote down earlier. In my case it is 10.1.1.190:9000
    * TAP_SECRET is a secret variable we can pass through to our application to ensure we are allowed to run the image. in this case just include "test"
    * -p tells the docker machine which port to use, So we are mapping our Port 9000 to the machines port 9000
    * lastly we include the docker image name in this case is "andrewresearch/tap:3.2.2" (3.2.2 is the version number, which may change at a later date. be sure to use the lastest availible)

7. That's it! if all went well, The docker should mention `factorie-nlp-api - Completed in xms` and you should be able to access your TAP instance by navigating to your Docker IP in a browser! (in my case 10.1.1.190:9000)

    ![docker demo](https://i.imgur.com/Hb1FBSB.png)
    If there are any issues with this documentation, or you wish to suggest changes, [open an issue](https://github.com/heta-io/tap/issues).
   
        



#### Docker ToolBox (Mac, Linux, Windows 7 and 8)
this is how u run docker on win 7 etc docker toolbox

#### Run Docker on AWS
this is how u run docker on aws

### Get started locally without Docker

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

