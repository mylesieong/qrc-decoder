package com.bcm.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class App {

    public final static String COMMAND = "parseQRC ";

    public static void main( String[] args ){
        if (args.length != 1){
            return ;
        }
        String imageFile = args[0];
        String output = executeCommand(COMMAND + imageFile);
        String[] outputTokens = tokenizeOutput(output);
        for (int i = 0; i < outputTokens.length ; i++){
            System.out.print(outputTokens[i] + "\t");
        }
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
