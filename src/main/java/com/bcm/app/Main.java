package com.bcm.app; 

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.util.Properties;

import java.io.FileReader;

public class Main{

    public static void main( String[] args ){
        SwingUtilities.invokeLater(new Runnable(){
            @Override 
            public void run(){
                Properties prop = new Properties();
                try{
                    prop.load(new FileReader("config.ini"));
                }catch (Exception e){
                    e.printStackTrace();
                }
                JFrame gui = new GUI(prop);
                gui.setVisible(true);
            }
        });
    }

}
