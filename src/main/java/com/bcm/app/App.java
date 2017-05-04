package com.bcm.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class App {
    public static void main( String[] args ){
        App app = new App();
	String command = "ping -n 3 www.google.com";
	String output = app.executeCommand(command);
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
