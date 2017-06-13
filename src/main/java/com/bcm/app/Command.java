package com.bcm.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.io.File;

public class Command{

    private String command;

    public void setCommand(String command){
        this.command = command;
    }

    public String getCommand(){
        return this.command;
    }

    public String runCommand(){
        return runCommand(this.command);
    }

    public String runCommand(String command){

        StringBuffer output = new StringBuffer();

        try {

            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(command); //prcoess starts to run from here

            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF8");
            BufferedReader reader = new BufferedReader(isr);

            String line = null;
            while( (line = reader.readLine()) != null){
                output.append(line);
                output.append("\n");
            }

            int exitValue = process.waitFor();
            output.append("Finish. Process Exit Value:");
            output.append(exitValue);

        }catch (Exception e){
            e.printStackTrace();
        }

        return output.toString();
    }

}
