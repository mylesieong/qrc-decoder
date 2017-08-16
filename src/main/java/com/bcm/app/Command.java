package com.bcm.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.io.File;

/**
 * An utility class that runs dos/bash command and return the standard 
 * output (stdout) in String form.
 *
 */
public class Command{

    /**
     * Utility method: run command injected.
     *
     * @param String command. 
     *  e.g."dir", or in this context, "pingLM {image.name}"
     * @return String stdout of the command
     */
    public static String runCommand(String command){

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
