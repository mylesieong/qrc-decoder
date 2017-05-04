package com.bcm.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class App {
    public final static String COMMAND = "parseQRC ";
    public static void main( String[] args ){
	if (args.length != 1){
	    return ;
	}
	String imageFile = args[0];
        App app = new App();
	String output = app.executeCommand(COMMAND + imageFile);
	System.out.println(output);
    }
    private String executeCommand(String command){
        StringBuffer output = new StringBuffer();
	Process process;
	try {
	    process = Runtime.getRuntime().exec(command);
	    process.waitFor();
	    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    String line = "";
	    while((line = reader.readLine()) != null){
                output.append(line + "\n");
	    }
	}catch (Exception e){
            e.printStackTrace();
	}
	return output.toString();
    }
}
