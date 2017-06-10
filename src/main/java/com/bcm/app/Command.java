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
        System.out.println("Start run command"); //debug
        StringBuffer output = new StringBuffer();
        Process process;
        try {
            System.out.println("Before process exec"); //debug
            process = Runtime.getRuntime().exec(command);
            System.out.println("After process exec"); //debug
            process.waitFor();
            System.out.println("Process finished"); //debug
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            System.out.println("Ready to read all output from prcoess"); //debug
            while((line = reader.readLine()) != null){
                output.append(line + "\n");
            }
            System.out.println("Finish read all output from prcoess"); //debug
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(output.toString()); //debug
        return output.toString();
    }

}
