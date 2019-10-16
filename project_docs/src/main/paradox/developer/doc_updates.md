## How to edit documentation

We use Paradox to automatically build the documentation.

[Paradox documentation](https://developer.lightbend.com/docs/paradox/current/overview.html)

The source files for the documentation is stored under tap/documentation.

There are 3 folders in the /documentation directory which include the 3 sections that you see on the left.

```
documentation/developer
documentation/overview
documentation/user
```

You will notice that within each folder is an `index.md` file which will tell paradox the child pages of that directory.

To set the title of the page at the top of the file include a line like so `#### This is the title`

The rest of the documentation can be written with simple markdown [Github Style](https://help.github.com/articles/basic-writing-and-formatting-syntax/)

If you wish to include or change any documentation only edit the files in the documentation folder. The `docs` folder is where paradox will build the final documentation to and should not be edited locally.

To build your documentation run the following command inside the sbt shell

```
updateDocs
```  

If you are making a pull request please complete these steps to ensure there are no conflicts.

* Delete all files inside the /docs directory.
* Run `updateDocs` inside the sbt shell
* inside the docs directory delete the following files
    * client-opt.js
        * client-opt.js.map
    * client-opt-bundle.js
        * client-opt-bundle.js.map
    * client-opt-library.js
        * client-opt-library.js.map
    * client-opt-loader.js
   
Now you are able to make a pull request. For more information on contributing see @ref:[Contributing](contributing.md)


**Document Last Updated on 21/11/2018**
