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
            String decodeText = decodePDF(originPDFName);
            echo(decodeText);

        }else if (e.getSource() == this.mExportButton){

            echo("Start export...");
            String originPDFName = this.mFileTextField.getText();
            String decodeText = decodePDF(originPDFName);
            String exportName = originPDFName.substring(0, originPDFName.lastIndexOf(".")) + ".csv"; 
            
            try{

                File export = new File(exportName);
                FileOutputStream fos = new FileOutputStream(export);

                if (!export.exists()){
                    export.createNewFile();
                    echo("Create file:" + exportName);
                }

                //fos.write(decodeText.getBytes());
                fos.write(toCSV(decodeText, "\t").getBytes());
                fos.flush();
                fos.close();

            }catch (Exception ex){

                 ex.printStackTrace();

            }

        }else if (e.getSource() == this.mUploadButton){

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
     * decode pdf function break pdf to images and decode images 
     * to text. Return the text.
     * NOTE: result text use \n to separate lines and \t to separate tokens
     */
    private String decodePDF(String filename){
        String tempPathName = filename.substring(0, filename.lastIndexOf(File.separator)) + File.separator + "temp"; 
        String tempPDFName = tempPathName + File.separator + "temp.pdf";
        StringBuilder result = new StringBuilder();

        try{

            /* Build a folder next to target pdf and make its copy into folder*/
            File tempPath = new File(tempPathName);
            File originPDF = new File(filename);

            tempPath.mkdir();
            echo("Make temp directory:" + tempPathName);

            Files.copy(new FileInputStream(originPDF), Paths.get(tempPDFName), REPLACE_EXISTING);
            echo("Create temp pdf copy:" + tempPDFName);

            /* Run command line tool to convert pdf to image */
            Convertor.convertPDFToImage(tempPDFName);

            /* Decode all generated imaged Files */
            File[] fileList = tempPath.listFiles();
            for (int i = 0; i < fileList.length ; i++){
                if (fileList[i].getAbsolutePath().endsWith(".jpg")){

                    String c = Decoder.decodeImageFile(fileList[i].getAbsolutePath());
                    if (c.compareTo("") != 0){
                        result.append(c);
                        result.append("\n");
                    }

                }
            }

        }catch (Exception ex){

            ex.printStackTrace();

        }finally{

            /* Remove the temp folder */
            delete(new File(tempPathName));
         
        }

        return result.toString();

    }

    /*
     * parse string line to csv format 
     * Input string and deliminator
     * output string
     */
    private String toCSV(String content, String deliminator){

        if (content == null) return "";

        String[] lines = content.split("\n");
        StringBuilder result = new StringBuilder();

        for (String line : lines){

            //result.append(line.replaceAll(deliminator, "\""));
            String[] tokens = line.split(deliminator);
            for (int i = 0; i < tokens.length ; i++ ){
                result.append("\"");
                result.append(tokens[i]);
                result.append("\"");
                if ( i < tokens.length - 1 ){
                    result.append(",");
                }
            }
            
            result.append("\n");

        }

        return result.toString();

    }

}
