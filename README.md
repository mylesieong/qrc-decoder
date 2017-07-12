# QR Code Cheque Decoder

# Install from package

* Java Runtime is installed
* Ghostscript is installed
* Copy the git repo to PC and double click the link of batch file (note that the pingLM.exe might be platform-dependent which might need a re-compilation)

# Build from source

1. Compile C# Code with C# Compiler *csc*: `csc /out:pingLM.exe pingLM.cs /r:FreeImageNET.dll` and put the pingLM.exe and 3 dll to lib/ folder
1. Compile Java Code: `mvn clean package`
1. Run the Program by click the shortcut link of the batch file.

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

## How to use the convert.exe and pingLM.exe (for deployment) [SOLVED]
The basic idea is to copy the exe to system's $TMP directory and invoke it after copy. For java code part, please reference below:
* https://www.mkyong.com/java/java-read-a-file-from-resources-folder/
* https://stackoverflow.com/questions/600146/run-exe-which-is-packaged-inside-jar

Note that the convert.exe(ImageMagick) is proved to be runable at standalone and it only needs the delegates.xml and magic.xml(not necessary) which should be present either in the same folder of convert.exe or in the same folder of where the command is invoke. 

The program will call `lib/convert.exe` and `lib/pingLM.exe` from the java from a batch. Make sure these exes are available.

## Chinese name issue - C# side [SOLVED]
We cannot present chinese from pingLM.exe but on the GUI decode Example exe provided from AMCM, chinese is enabled. I studied and found that because pingLM.exe output to concole (using Console.WriteLine method in C#) so that we lose the UTF8 knowledge at this action. We solve it by adding Console.encoding = Encoding.UTF8 and problem solved.

## Java code process.waitFor() hangs [SOLVED]
After we change the pingLM.exe enabling UTF8, we have new problem that process.waitFor() method hangs. The solution is posted [here](https://stackoverflow.com/questions/5483830/process-waitfor-never-returns) and we will study. The git fix branch is fix_chinese. 

## Chinese name issue - Java side [SOLVED]
In command class, when I inject the utf8 knowledge into inputStreamReader from the command line output, the return string is good engouh to show 100% accurate chinese on JTextArea. But for the Cheque parsing method, it still goes "???". I then tried the getbyte in UTF8 manner from the blob (string injected from command output to cheque parse input) and it can show 余?傑. 
I solve it with collaborating the fact that string represent different char so that when we write a java string to file, be aware of the encoding method we chose in order not to lose the resolution (like this time, our mistake is we write a chinese to fileoutputstream with getBytes(ASCII)).

## Detect multiple pdf [SOLVE]
The application is required to read multiple pdf in a folder. This is poc by using imageMagick convert exe by passing in wildcard \*.pdf and it still can merge the output image to seqence.

## Faster pdf 2 img convertion with ghostscript
Use this command: `./gswin32 -sDEVICE=jpeg -r240 -o ./temp%03d.jpg tobeconvert.PDF -q -dNOPROMPT -dBATCH`
