package com.bcm.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Decoder {

    public final static String COMMAND = "lib/pingLM ";

    public static String decodeImageFile(String filename){
        String result = "";
        String commandOutput = executeCommand(COMMAND + filename);
        String[] outputTokens = tokenizeOutput(commandOutput);
        for (int i = 0; i < outputTokens.length ; i++){
            result += outputTokens[i] + "\t";
        }
        return result; 
    }

    private static String executeCommand(String command){
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

    private static String[] tokenizeOutput(String output){
        System.out.println("Debug message Myles:");
        System.out.println(output);
        if (output == null 
                || output.compareTo("") == 0
                || output.contains("error") ){
            return new String[0];
        }

        String[] outputTokenized = output.split(";");
        ArrayList<String> helperList = new ArrayList<String>();
        for (int i = 0 ; i < 6 ; i++){
            if (outputTokenized[i] != null){
                helperList.add(outputTokenized[i]);
            }else{
                helperList.add("");
            }
        }
        String[] result = new String[6];
        return helperList.toArray(result);
    }
    
}
