## Quick Start
---

@@toc { depth=5 }
### Get started with Docker

#### Docker for Windows 10
@@@ note { title=Notice }
Docker for Windows requires:

Windows 10 64bit: Pro, Enterprise or Education (1607 Anniversary Update, Build 14393 or later).

Virtualization is enabled in BIOS. Typically, virtualization is enabled by default. This is different from having Hyper-V enabled. For more detail see [Virtualization must be enabled](https://docs.docker.com/docker-for-windows/troubleshoot/#virtualization-must-be-enabled) in Troubleshooting.

CPU SLAT-capable feature.

At least 4GB of RAM.
@@@

**If you do not have windows 10 Pro, Enterprise or Education, Please use [Docker Toolbox](#docker-toolbox).**

This is the easiest way to get started with docker and TAP. Windows 10 has an app called Docker For Windows which will handle everything automatically for you.
See [How To install Docker For Windows](https://docs.docker.com/docker-for-windows/install/)

1. Ensure you have docker for windows installed and it is running.
2. First, quickly create a virtual switch for your virtual machine (VM) to use, so they can use your network adapter.
   
    * Launch Hyper-V Manager
    * Click Virtual Switch Manager in the right-hand menu
    * Click Create Virtual Switch of type External
    * Give it the name "myswitch", and check the box to share your host machine’s active network adapter
    
    ![switch creation](https://i.imgur.com/zgt6jYd.png)
  
3. Now create a VM we can use to run TAP on. (You may need to run this in admin, press windows key + x > Command Prompt (Admin))

        docker-machine create -d hyperv --hyperv-memory 2048  --hyperv-virtual-switch "myswitch" myvm1
        
    Notice we are passing in some custom variables

    * We are giving it 2gb of ram 
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
    * lastly we include the docker image name in this case is "andrewresearch/tap:3.2.2" (3.2.2 is the version number, which may change at a later date. be sure to use the latest available)

7. That's it! if all went well, The docker should mention `factorie-nlp-api - Completed in xms` and you should be able to access your TAP instance by navigating to your Docker IP in a browser! (in my case 10.1.1.190:9000)

    ![docker demo](https://i.imgur.com/FG7F0EM.png)
    
    **If there are any issues with this documentation, or you wish to suggest changes, [open an issue](https://github.com/heta-io/tap/issues).**
   
        



#### Docker ToolBox 

Ensure you have installed Docker Toolbox

[Install Docker Toolbox for Windows](https://docs.docker.com/toolbox/toolbox_install_windows/)

[Install Docker Toolbox for Mac](https://docs.docker.com/toolbox/toolbox_install_mac/)

**When installing Docker Toolbox, Ensure you install VirtualBox and Git, To ensure the program works correctly.**

Once you have Docker Toolbox running and have run the Quick Setup Icon it created you should have this screen.
![docker ready](https://i.imgur.com/UK8jLl3.png)


1. Firstly let's remove the default docker machine, as we need to create one with 2gb memory.

        docker-machine rm default
        
    ![remove machine](https://i.imgur.com/9cbCH3f.png)
    
2. Great, Let's create a new one and pass in some custom parameters

        docker-machine create -d virtualbox --virtualbox-memory=2048 myvm1
        
    This created a new machine called "myvm1" and gives it 2gb of ram instead of the default 1gb.
    
3. Great! let's get our docker machine IP.

        docker-machine ls
    
    ![dm ls](https://i.imgur.com/X4NdtNS.png)   
    
    My ip in this case would be 192.168.99.100 
    
4. Now we want to control our docker machine
        
        docker-machine env myvm1
        
    this will return a command we can run to set our machine as the active environment.       
    
    ![docker env](https://i.imgur.com/AaN1ntr.png)
    
    The last line is what we want, copy and paste it to set this machine as active.
    
            eval $("C:\Program Files\Docker Toolbox\docker-machine.exe" env myvm1)   
            
5. Now we can start our Tap Image.
        
        docker run -e JAVA_OPTS="-Xms512M -Xmx6000M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=256M" -e TAP_HOSTS="192.168.99.100:9000" -e TAP_SECRET="test" -p 9000:9000 andrewresearch/tap:3.2.2
    
    There are a few parameters here:
       
    * JAVA_OPTS is passing in our custom java environment variables which allow us to increase the amount of memory the application can use.
    * TAP_HOSTS is telling our application which IP is authorised to use our application, In this case you need to pass in your machine IP you wrote down earlier. In my case it is 192.168.99.100:9000
    * TAP_SECRET is a secret variable we can pass through to our application to ensure we are allowed to run the image. in this case just include "test"
    * -p tells the docker machine which port to use, So we are mapping our Port 9000 to the machines port 9000
    * lastly we include the docker image name in this case is "andrewresearch/tap:3.2.2" (3.2.2 is the version number, which may change at a later date. be sure to use the latest available)
        
    Once that runs it will automatically download everything you require and then let you know it is running with `[info] factorie-nlp-api - Completed in xxxxx ms`
    
6. That's it! TAP is now running in a docker machine, You should be able to use TAP in the browser by navigating to you machine IP with the port 9000

    **In my case i would navigate to 192.168.99.100:9000**
    ![browser](https://i.imgur.com/KLQIn6n.png) 
    
    **If there are any issues with this documentation, or you wish to suggest changes, [open an issue](https://github.com/heta-io/tap/issues).**   


#### Docker CE for Linux

Ensure you have installed Docker CE

They have instructions for several versions of linux, Ensure you follow the instructions for your particular version of linux.

[Installing Docker CE on Linux](https://docs.docker.com/install/linux/docker-ce/ubuntu/)

Ensure you have installed Docker Machine

[Install docker machine on linux](https://docs.docker.com/machine/install-machine/#install-machine-directly)

Ensure you have installed Oracle VirtualBox for linux

[VirtualBox Instructions](https://www.virtualbox.org/wiki/Downloads)

1. Create a Virtual Machine to run our docker container.

        docker-machine create -d virtualbox --virtualbox-memory=2048 myvm1
        
2. run `docker-machine ls` to get the IP of our new machine.

    ![linux dm ls](https://i.imgur.com/02pce0v.png)
    
    In my case the IP is 192.168.99.100. Let's write that down for later.
    
3. We need to set our new docker machine as our active environment.

        docker-machine env myvm1
    
    ![linux dm env](https://i.imgur.com/WOB0679.png)
    
    This will return a command we can run which will set our environment to the active docker machine.
    Copy and paste the last line into your terminal.
    
        eval $(docker-machine env myvm1)
    Now any docker commands we run will run on the docker machine.
    
4. Run our TAP image.
        
        docker run -e JAVA_OPTS="-Xms512M -Xmx6000M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=256M" -e TAP_HOSTS="192.168.99.100:9000" -e TAP_SECRET="test" -p 9000:9000 andrewresearch/tap:3.2.2
        
    There are a few parameters here:
           
    * JAVA_OPTS is passing in our custom java environment variables which allow us to increase the amount of memory the application can use.
    * TAP_HOSTS is telling our application which IP is authorised to use our application, In this case you need to pass in your machine IP you wrote down earlier. In my case it is 192.168.99.100:9000
    * TAP_SECRET is a secret variable we can pass through to our application to ensure we are allowed to run the image. in this case just include "test"
    * -p tells the docker machine which port to use, So we are mapping our Port 9000 to the machines port 9000
    * lastly we include the docker image name in this case is "andrewresearch/tap:3.2.2" (3.2.2 is the version number, which may change at a later date. be sure to use the latest available)
            
    Great once that completes you should see the message `[info] factorie-nlp-api - Completed in xxxxx ms`

5. That's it! you should be good to go. You can now access the TAP instance by navigating to your machine ip in your browser with the port 9000
    
    **In my case that would 192.168.99.100:9000**
    
    ![linux browser](https://i.imgur.com/RUzxqch.png)
    
    **If there are any issues with this documentation, or you wish to suggest changes, [open an issue](https://github.com/heta-io/tap/issues).**
    
    

#### Run Docker on Digital Ocean
Docker Droplet - Coming Soon

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

