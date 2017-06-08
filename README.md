# QR Code Cheque Decoder

## Prequisition

* Compile the C# code with command `csc /out:parseQRC.exe pingLM.cs /r:FreeImageNET.dll`

* Java Runtime is installed

* Install Ghostscript

## Compile C# Code with C# Compiler *csc*

### Compiles to executables

1. Build {source-code}.cs file
1. run cmd: `csc /out:pgm.exe csdemo.cs`
1. run executable: `./pgm`

### Compiles to DLL library

1. Build {source-code}.cs file
1. run cmd: `csc /target:library csdemo.cs`

### External Dependency Injection

- Dependency in *using xxx*: `csc /out:pgm.exe pingLM.cs /r:FreeImageNET.dll`
- Dependency in `[DllImport...]`, because it is a **runtime dll binding**, so only need to ensure that dll is available in the same directory that pgm.exe is invoked (in this case the LM_Decoder.dll).

## Compile Java Code

`mvn clean package`

## Run the Program

`java -cp {name_w_path}.jar com.bcm.app.Main`

# Working Log

> 2017-05-09

Source pdfbox to be the alternatives of imageMagick, the benefics is no installation for pdfbox (use it as the command line tool)

> 2017-05-10

Build the convert logic but found that the density of some testing is too low that cuase program collapse. And yet the upload logic is not applied.

> 2017-05-25

Add the csv exporting button and tested

# Issues Lot

## How to refactor the project to be testable 

Principle:
* SOLID, KISS, DRY
* No design pattern is needed

Concrete plan:
* use springboot to ensure the di principle
* separate the screen, the user's knowledge (where is the file), the vender's knowledge (parseQRC.exe and convert.exe), action. 
* the Action wrap the knowledge performing, and it should be testable. 

## How to use the convert.exe and pingLM.exe (for deployment)

The basic idea is to copy the exe to system's $TMP directory and invoke it after copy. For java code part, please reference below:
* https://www.mkyong.com/java/java-read-a-file-from-resources-folder/
* https://stackoverflow.com/questions/600146/run-exe-which-is-packaged-inside-jar

Note that the convert.exe(ImageMagick) is proved to be runable at standalone and it only needs the delegates.xml and magic.xml(not necessary) which should be present either in the same folder of convert.exe or in the same folder of where the command is invoke. 

The program will call `lib/convert.exe` and `lib/pingLM.exe` from the java from a batch. Make sure these exes are available.
