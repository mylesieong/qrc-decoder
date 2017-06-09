package com.bcm.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.File;

public class Command{

    private String command;
    // COMMAND = "lib/pingLM ";
    // COMMAND = "lib/convert -density 240 -quality 80 -trim ";

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
