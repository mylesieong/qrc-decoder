package com.bcm.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Convertor {

    public final static String COMMAND = "java -jar pdfbox-app-2.0.5.jar PDFToImage ";

    public static void convertPDFToImage( String filename){
        executeCommand(COMMAND + filename);
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

}
