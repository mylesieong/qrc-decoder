package com.bcm.app; 

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main{

    public static void main( String[] args ){
        SwingUtilities.invokeLater(new Runnable(){
            @Override 
            public void run(){
                JFrame gui = new GUI();
                gui.setVisible(true);
            }
        });
    }

}
