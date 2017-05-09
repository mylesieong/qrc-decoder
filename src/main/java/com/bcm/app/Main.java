package com.bcm.app;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JDialog;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Main extends JFrame implements ActionListener{

    private JTextField mFileTextField;
    private JButton mChooseButton;
    private JButton mValidateButton;
    private JButton mUploadButton;
    private JTextArea mTextArea;

    public static void main( String[] args ){
        Main screen = new Main();
        screen.initialize();
        screen.setVisible(true);
    }

    /*
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int returnValue = jfc.showOpenDialog(null);
        if ( returnValue == JFileChooser.APPROVE_OPTION){
            File selectedFile = jfc.getSelectedFile();
            System.out.println(selectedFile.getAbsolutePath());
        }
    */
        
    public void initialize() {

        this.setBounds(100, 100, 720, 360);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(null);
        
        mFileTextField = new JTextField("Test Field");
        mFileTextField.setBounds(15, 15, 390, 20);
        this.getContentPane().add(mFileTextField);
        
        mChooseButton = new JButton("Choose");
        mChooseButton.setBounds(420, 15, 80, 20);
        mChooseButton.addActionListener(this);
        this.getContentPane().add(mChooseButton);
        
        mValidateButton = new JButton("Validate");
        mValidateButton.setBounds(515, 15, 80, 20);
        mValidateButton.addActionListener(this);
        this.getContentPane().add(mValidateButton);
        
        mUploadButton = new JButton("Upload");
        mUploadButton.setBounds(605, 15, 80, 20);
        mUploadButton.addActionListener(this);
        this.getContentPane().add(mUploadButton);
        
        mTextArea = new JTextArea("Text Area");
        mTextArea.setBounds(15, 50, 670, 250);
        this.getContentPane().add(mTextArea);

    }
    
    @Override
    public void actionPerformed(ActionEvent e){

        /*
        if (e.getSource() == this.mStartButton){
        }else if (e.getSource() == this.mStopButton){
        }else if (e.getSource() == this.mExitButton){
        }else if (e.getSource() == this.mLoadConfigButton){
        }else{
            System.out.println("Callback to MainFrame");
        }
        */
    }
}
