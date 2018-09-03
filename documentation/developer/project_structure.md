## Project Structure

Although TAP is a Play project, the project structure is that of a Maven/sbt project
and does not follow the Play default project structure.

The build.sbt file indicates that we are using an alternative project structure to Play.

        disablePlugins(PlayLayoutPlugin)
        PlayKeys.playMonitoredFiles ++= (sourceDirectories in (Compile, TwirlKeys.compileTemplates)).value
        
This project is laid out in an MVC structure (Model, View and Controller).

There are 2 main components to this project, 
* The front end (Client)
* The back end (Server)

The following is a run down of the most relevant files in the project.

* app - Contains the controllers, models and views.
    * controllers - Contains the controllers of the project
        * handlers
    * models
        * graphql
    * views
        * GraphqlPage - The GraphQl Interface page which allows you to test out the graphql queries in a playground.
        * HomePage - The home page which contains links to the docs, The source code and the Graphql Interfact.
* documentation - Contains all the documentation source code that can be automatically generated see [Documentation Updates](doc_updates.md)
* tap_client
    * src
* tap_server
    * src
        
        

For more details, see the "Default SBT Layout" section in the "Anatomy of a Play Application"
chapter in the Play 2.6 Documentation:

        [play-project-anatomy](https://playframework.com/documentation/2.6.x/Anatomy)



