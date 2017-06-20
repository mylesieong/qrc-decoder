package com.bcm.app;

import static java.nio.file.StandardCopyOption.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.io.File;
import java.io.FileFilter;
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
            this.mFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        }
        return this.mFileChooser;

    }
        
    public void initialize() {

        this.setBounds(100, 100, 700, 360);
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
        mValidateButton.setBounds(510, 15, 80, 20);
        mValidateButton.addActionListener(this);
        this.getContentPane().add(mValidateButton);
        
        mExportButton = new JButton("Export");
        mExportButton.setBounds(600, 15, 80, 20);
        mExportButton.addActionListener(this);
        this.getContentPane().add(mExportButton);

        mTextArea = new JTextArea("");
        mScrollPane = new JScrollPane(mTextArea);
        mScrollPane.setBounds(15, 50, 665, 270);
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
            String targetFolder = this.mFileTextField.getText();
            String tempPathName = "temp";

            // Copy the target pdf under a selected folder to project home/temp
            File target = new File(targetFolder);
            File[] pdfList = target.listFiles(new FileFilter(){
                @Override 
                public boolean accept(File f){
                    String filetype = f.getName().substring(f.lastIndexOf("."));
                    boolean result = filetype.compareToIgnoreCase("pdf");
                    return result;
                }
            });
            for (File f : pdfList){
                copyTargetToTemp(f.getAbsolutePath(), tempPathName);
            }

            // Run command line tool to convert pdf to image 
            Command command = new Command();
            command.setCommand("lib/convert -density 240 -quality 80 -trim temp/*.pdf temp/temp.jpg"); //Proven eat all pdfs
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
            String targetFolder = this.mFileTextField.getText();
            String tempPathName = "temp";

            // Copy the target pdf under a selected folder to project home/temp
            File target = new File(targetFolder);
            File[] pdfList = target.listFiles(new FileFilter(){
                @Override 
                public boolean accept(File f){
                    String filetype = f.getName().substring(f.lastIndexOf("."));
                    boolean result = filetype.compareToIgnoreCase("pdf");
                    return result;
                }
            });
            for (File f : pdfList){
                copyTargetToTemp(f.getAbsolutePath(), tempPathName);
            }

            // Run command line tool to convert pdf to image 
            Command command = new Command();
            command.setCommand("lib/convert -density 240 -quality 80 -trim temp/*.pdf temp/temp.jpg"); //Proven eat all pdfs
            command.runCommand();

            // Decode all generated imaged Files 
            File tempPath = new File(tempPathName);
            List<Cheque> cheques = new ArrayList<Cheque>();
            File[] fileList = tempPath.listFiles();
            for (int i = 0; i < fileList.length ; i++){
                if (fileList[i].getAbsolutePath().endsWith(".jpg")){
                    command.setCommand("lib/pingLM " +  fileList[i].getAbsolutePath());
                    Cheque newCheque = Cheque.parse(command.runCommand()); 
                    if (newCheque!= null) cheques.add(newCheque);
                }
            }

            // Loops chq list to output csv string
            String csvContent = "";
            String csvHeader = "\"Bank\",\"Type\",\"Currency\",\"Cheque No\",\"Amount\",\"Account Name\",\"Account Number\",\"Envelope Number\"";
            csvContent = csvContent + csvHeader + "\n";
            for (int i = 0; i < cheques.size() ; i++){
                csvContent = csvContent + cheques.get(i).toCsv() + "\n";
            }

            // Output the file
            String exportName = targetFolder + File.separator + "output.csv"; 
            try{
                File export = new File(exportName);
                FileOutputStream fos = new FileOutputStream(export);

                if (!export.exists()){
                    export.createNewFile();
                    echo("Create file:" + exportName);
                }

                fos.write(csvContent.getBytes("UTF8"));
                fos.flush();
                fos.close();

            }catch (Exception ex){
                 ex.printStackTrace();
            }

            // Delete all temp files and folder
            delete(tempPath);

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

        String pdfName = filename.substring(filename.lastIndexOf(File.separator)); 
        String tempPDFName = pathName + File.separator + pdfName;

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
