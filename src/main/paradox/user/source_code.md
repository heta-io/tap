#### Getting a local copy of TAP

1. Clone the github repository to your local repository copy on your PC, or
download the zip file if you just want to build the project, and have no intention
to submit a contribution.

To clone the project, position to the directory where you want the local repository placed
(Your home directory is fine) and issue the following command.
This will create a copy of the repository in the tap directory.

        git clone https://github.com/uts-cic/tap.git

2. The following jar files should also be placed in the lib directory:
   (Note that the models jar is relatively large at around 750M so it might take
   some time to download depending on your internet speed).


        [tap-models-jar](https://s3-ap-southeast-2.amazonaws.com/dev-tap-cic-uts/cc.factorie.app.nlp.all-models-1.0.0.jar)
        [tap-factorie-jar](https://s3-ap-southeast-2.amazonaws.com/dev-tap-cic-uts/nlpfactorie_2.12-0.1.jar)

    Copy the files to the lib directory:

        cd ~/tap
        mkdir -p lib
        cd lib
        wget https://s3-ap-southeast-2.amazonaws.com/dev-tap-cic-uts/cc.factorie.app.nlp.all-models-1.0.0.jar
        wget https://s3-ap-southeast-2.amazonaws.com/dev-tap-cic-uts/nlpfactorie_2.12-0.1.jar


