# QR Code Cheque Decoder

## Prequisition

* Compile the C# code with command `csc /out:parseQRC.exe pingLM.cs /r:FreeImageNET.dll`

* Add the path to _parseQRC.exe_ to System Path

* Java Runtime is installed

## Compile C# Code with C# Compiler *csc*

### Compiles to executables
1. Build *.cs file
1. run cmd: `csc /out:pgm.exe csdemo.cs`
1. run executable: `./pgm`

### Compiles to DLL library
1. Build *.cs file
1. run cmd: `csc /target:library csdemo.cs`

### External Dependency Injection
- Dependency in *using xxx*: `csc /out:pgm.exe pingLM.cs /r:FreeImageNET.dll`
- Dependency in `[DllImport...]`, because it is a **runtime dll binding**, so only need to ensure that dll is available in the same directory that pgm.exe is invoked (in this case the LM_Decoder.dll).

## Compile Java Code



## Run the Program

Invoke program with `java -cp {name_w_path}.jar com.bcm.app.App {name_w_path}.{image_format}`
