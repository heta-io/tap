## How to edit documentation

We use Paradox to automatically build the documentation.

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

To build your documentation run the following command.

```
sbt paradox; sbt copyDocs
```  

This will generate html files in the docs directory, and any modified files will be 
part of you pull request.
