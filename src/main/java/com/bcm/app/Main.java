package com.bcm.app; 

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.util.Properties;

import java.io.FileReader;

/**
 * Entrance of qrc-decoder application. 
 * Note that since the path to property file is relative path, 
 * please make sure property file's presence in the folder 
 * at which jvm run.
 *
 * @author Myles Ieong (BI77PGM)
 * @date 2017-08-15
 */
public class Main{

    private static String propertyFilePath = "config.ini";

    public static void main( String[] args ){
        SwingUtilities.invokeLater(new Runnable(){
            @Override 
            public void run(){
                Properties prop = new Properties();
                try{
                    prop.load(new FileReader(propertyFilePath));
                }catch (Exception e){
                    e.printStackTrace();
                }
                JFrame gui = new GUI(prop);
                gui.setVisible(true);
            }
        });
    }

}
