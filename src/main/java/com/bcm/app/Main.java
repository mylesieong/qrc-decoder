package com.bcm.app;

import static java.nio.file.StandardCopyOption.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JDialog;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.List;
import java.util.ArrayList;

public class Main extends JFrame implements ActionListener{

    private JTextField mFileTextField;
    private JButton mChooseButton;
    private JButton mValidateButton;
    private JButton mExportButton;
    private JButton mUploadButton;
    private JTextArea mTextArea;
    private JScrollPane mScrollPane;
    private JFileChooser mFileChooser;

    public static void main( String[] args ){

        Main screen = new Main();
        screen.initialize();
        screen.setVisible(true);

    }

    public JFileChooser getFileChooser(){
        if ( this.mFileChooser == null){
            this.mFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        }
        return this.mFileChooser;
    }
        
    public void initialize() {

        this.setBounds(100, 100, 720, 360);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(null);
        
        mFileTextField = new JTextField("");
        mFileTextField.setBounds(15, 15, 390, 20);
        this.getContentPane().add(mFileTextField);
        
        mChooseButton = new JButton("Choose");
        mChooseButton.setBounds(420, 15, 80, 20);
        mChooseButton.addActionListener(this);
        this.getContentPane().add(mChooseButton);
        
        mValidateButton = new JButton("Validate");
        mValidateButton.setBounds(515, 15, 30, 20);
        mValidateButton.addActionListener(this);
        this.getContentPane().add(mValidateButton);
        
        mExportButton = new JButton("Export");
        mExportButton.setBounds(545, 15, 30, 20);
        mExportButton.addActionListener(this);
        this.getContentPane().add(mExportButton);

        mUploadButton = new JButton("Upload");
        mUploadButton.setBounds(605, 15, 80, 20);
        mUploadButton.addActionListener(this);
        this.getContentPane().add(mUploadButton);
        
        mTextArea = new JTextArea("");
        mScrollPane = new JScrollPane(mTextArea);
        mScrollPane.setBounds(15, 50, 670, 250);
        this.getContentPane().add(mScrollPane);
        
        this.setResizable(false);

    }
    
    /* 
     * In this method, the major action logic is specify. 
     * If there are future changes on operation steps or 
     * adding new steps, only need to modify code in this
     * method.
     */
    @Override
    public void actionPerformed(ActionEvent e){

        if (e.getSource() == this.mChooseButton){
            
            JFileChooser jfc = this.getFileChooser();
            int returnValue = jfc.showOpenDialog(null);

            if ( returnValue == JFileChooser.APPROVE_OPTION){
                this.mFileTextField.setText(jfc.getSelectedFile().getAbsolutePath());
            }

        }else if (e.getSource() == this.mValidateButton){

            echo("Start validation...");
            String originPDFName = this.mFileTextField.getText();
            String tempPathName = "temp";

            // Copy the target pdf to project home/temp
            copyTargetToTemp(originPDFName, tempPathName);

            // Run command line tool to convert pdf to image 
            Command command = new Command();
            command.setCommand("lib/convert -density 240 -quality 80 -trim temp/temp.pdf temp/temp.jpg");
            command.runCommand();

            // Decode all generated imaged Files 
            File tempPath = new File(tempPathName);
            File[] fileList = tempPath.listFiles();
            for (int i = 0; i < fileList.length ; i++){
                if (fileList[i].getAbsolutePath().endsWith(".jpg")){
                    command.setCommand("lib/pingLM " +  fileList[i].getAbsolutePath());
                    echo(command.runCommand());
                }
            }

            // Delete all temp files and folder
            delete(tempPath);
           
        }else if (e.getSource() == this.mExportButton){
            echo("Start export...");
            String originPDFName = this.mFileTextField.getText();
            String tempPathName = "temp";

            // Copy the target pdf to project home/temp
            copyTargetToTemp(originPDFName, tempPathName);

            // Run command line tool to convert pdf to image 
            Command command = new Command();
            command.setCommand("lib/convert -density 240 -quality 80 -trim temp/temp.pdf temp/temp.jpg");
            command.runCommand();

            // Decode all generated imaged Files 
            File tempPath = new File(tempPathName);
            List<Cheque> cheques = new ArrayList<Cheque>();
            File[] fileList = tempPath.listFiles();
            for (int i = 0; i < fileList.length ; i++){
                if (fileList[i].getAbsolutePath().endsWith(".jpg")){
                    command.setCommand("lib/pingLM " +  fileList[i].getAbsolutePath());
                    cheques.add(Cheque.parse(command.runCommand()));
                }
            }

            // Loops chq list to output csv string
            String csvContent = "";
            for (int i = 0; i < cheques.size() ; i++){
                csvContent = csvContent + cheques.get(i).toCsv() + "\n";
            }

            // Output the file
            String exportName = originPDFName.substring(0, originPDFName.lastIndexOf(".")) + ".csv"; 
            try{
                File export = new File(exportName);
                FileOutputStream fos = new FileOutputStream(export);

                if (!export.exists()){
                    export.createNewFile();
                    echo("Create file:" + exportName);
                }

                fos.write(csvContent.getBytes());
                fos.flush();
                fos.close();

            }catch (Exception ex){
                 ex.printStackTrace();
            }

            // Delete all temp files and folder
            delete(tempPath);

        }else if (e.getSource() == this.mUploadButton){

            /* TODO */

        }

    }

    /*
     * helper function: delete file/directory (allow not empty)
     */
    private void delete(File file){
        try {
            if (file.isDirectory()){
                //if file is a folder and empty
                if (file.list().length == 0){
                    file.delete();
                    echo("delete " + file.getName());
                }else{
                    //if file is a folder but not empty, delete sub files
                    //and remove the empty folder afterward
                    String files[] = file.list();
                    for (String temp: files){
                        File fileToBeDelete = new File(file, temp);
                        delete(fileToBeDelete);
                    }
                    if (file.list().length == 0){
                        file.delete();
                    }
                    echo("delete " + file.getName());
                }

            }else{
                file.delete();
                echo("delete " + file.getName());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
     * helper function: echo to textArea
     */
    private void echo(String message){
        this.mTextArea.setText(this.mTextArea.getText() + "\n" + message);
    }


    /* 
     * helper function: copy target file to a specific path
     */
    private void copyTargetToTemp(String filename, String pathName){

        String tempPDFName = pathName + File.separator + "temp.pdf";

        try{
            /* Build a folder next to target pdf and make its copy into folder*/
            File tempPath = new File(pathName);
            File originPDF = new File(filename);
            tempPath.mkdir();
            Files.copy(new FileInputStream(originPDF), Paths.get(tempPDFName), REPLACE_EXISTING);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

}
