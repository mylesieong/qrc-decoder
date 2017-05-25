package com.bcm.app;

import static java.nio.file.StandardCopyOption.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.io.File;
import java.io.FileInputStream;

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

            String fileChooserResult = this.mFileTextField.getText();
            String tempPathName = fileChooserResult.substring(0, fileChooserResult.lastIndexOf(File.separator)) + File.separator + "temp"; 
            String tempPDFName = tempPathName + File.separator + "temp.pdf";

          try{

            this.mTextArea.setText("Start validation...");

            /* Build a folder next to target pdf and make its copy into folder*/
            File targetPDF = new File(fileChooserResult);
            File tempPath = new File(tempPathName);

            tempPath.mkdir();
            this.mTextArea.setText(this.mTextArea.getText() + "\n"
                    + "Make temp directory:" + tempPathName );

            Files.copy(new FileInputStream(targetPDF), Paths.get(tempPDFName), REPLACE_EXISTING);
            this.mTextArea.setText(this.mTextArea.getText() + "\n"
                    + "Create temp pdf copy:" + tempPDFName );

            /* Run command line tool to convert pdf to image */
            Convertor.convertPDFToImage(tempPDFName);

            /* Decode all generated imaged Files */
            File[] fileList = tempPath.listFiles();
            for (int i = 0; i < fileList.length ; i++){
                if (fileList[i].getAbsolutePath().endsWith(".jpg")){
                    String decodeText = Decoder.decodeImageFile(fileList[i].getAbsolutePath());
                    this.mTextArea.setText(this.mTextArea.getText() + "\n"
                         + decodeText );
                }
            }

          }catch (Exception ex){

              ex.printStackTrace();

          }finally{

              /* Remove the temp folder */
              delete(new File(tempPathName));
              
          }

        }else if (e.getSource() == this.mExportButton){

            

            
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
                }

            }else{
                file.delete();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
