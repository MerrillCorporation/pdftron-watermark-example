# PDFTron Watermark Demo

## Setup steps
* Put PDFNet.jar in the "lib" dir
* Put the native library file in the "lib" dir
* Build and run the app with "gradlew bootRun"
* Use the app at http://localhost:7102 and use the two download links


### Notes:
The app must be started with the command line arg: "-Djava.library.path=<path to your lib dir>", example: -Djava.library.path="C:\code\repos\watermarkdemo\lib"

This should be done automatically when using "gradlew bootRun"


### Cloud Foundry Files
We deploy our apps in Cloud Foundry. manifest.yml is used to configure the application deploy.

The copy of the linux native library in /src/main/resources/META-INF is for the cloud foundry deployment as well