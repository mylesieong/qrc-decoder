package com.bcm.app; 

import static java.nio.file.StandardCopyOption.*;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JDialog;
import javax.swing.JButton;
import java.awt.Font;
import net.miginfocom.swing.MigLayout;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.List;
import java.util.ArrayList;
import java.util.Properties;

public class Main extends JFrame implements ActionListener{

    /*
     * Screen component
     */
    private JLabel mBankLabel;
    private JLabel mAppLabel;
    private JLabel mImage;
    private JTextField mTextField;
    private JButton mOpenButton;
    private JButton mLoadButton;
    private JButton mExportButton;
    private JTextArea mTextArea;
    private JScrollPane mScrollPane;
    private JFileChooser mFileChooser;
    private JLabel mQuantityLabel;
    private JLabel mUnmatchLabel;
    private JLabel mQuantity;
    private JLabel mUnmatch;

    /*
     * Properties
     */
    private String mBankName;
    private String mAppName;
    private String mBranchName;
    private String mBranchCode;
    private String mInputPath;
    private String mOutputPath;
    private String mOutputName;
    private String mTempPath;
    
    private List<Cheque> mCheques;

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
        
    private void initialize() {
        loadProperties();
        initScreen();
    }

    private void loadProperties(){
        Properties prop = new Properties();
        try {
            FileInputStream input = new FileInputStream("config.ini");
            prop.load(input);
            mBankName = prop.getProperty("BANK_NAME");
            mAppName = prop.getProperty("APP_NAME");
            mBranchName = prop.getProperty("BRANCH_NAME");
            mBranchCode = prop.getProperty("BRANCH_CODE");
            mInputPath = prop.getProperty("INPUT_FOLDER");
            mOutputPath = prop.getProperty("OUTPUT_FOLDER");
            mOutputName = prop.getProperty("OUTPUT_NAME");
            mTempPath = prop.getProperty("TEMP_FOLDER");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initScreen(){

        this.setResizable(false);
        this.setBounds(100, 100, 700, 385);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(
                new MigLayout("", "[110px:110px:110px][110px:110px:110px][110px:110px:110px][110px:110px:110px][110px:110px:110px][110px:110px:110px]", "[25px:25px:25px][25px:25px:25px][25px:25px:25px][200px:200px:200px][25px:25px:25px][25px:25px:25px]")
        );
        
        mBankLabel = new JLabel(mBankName);
        mBankLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        this.getContentPane().add(mBankLabel, "cell 0 0 4 1,grow");
        
        mImage = new JLabel("");
        mImage.setIcon(new ImageIcon("C:\\cygwin\\home\\bi77\\qrc-decoder\\img\\bcm_logo_s.jpg"));
        this.getContentPane().add(mImage, "cell 4 0 2 2,alignx right,growy");
        
        mAppLabel = new JLabel(mAppName);
        mAppLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        this.getContentPane().add(mAppLabel, "cell 0 1 4 1,grow");
        
        mTextField = new JTextField(mInputPath);
        mTextField.setColumns(50);
        this.getContentPane().add(mTextField, "cell 0 2 3 1,grow");
        
        mOpenButton = new JButton("Open Folder");
        mOpenButton.addActionListener(this);
        this.getContentPane().add(mOpenButton, "cell 3 2,grow");
        
        mLoadButton = new JButton("Load");
        mLoadButton.addActionListener(this);
        this.getContentPane().add(mLoadButton, "cell 4 2,grow");
        
        mExportButton = new JButton("Export");
        mExportButton.addActionListener(this);
        this.getContentPane().add(mExportButton, "cell 5 2,grow");
        
        mTextArea = new JTextArea();
        mScrollPane = new JScrollPane(mTextArea);
        this.getContentPane().add(mScrollPane, "cell 0 3 6 1,grow");
        
        mQuantityLabel = new JLabel("Qty (HKD/MOP):");
        this.getContentPane().add(mQuantityLabel, "flowx,cell 0 4,grow");
        
        mQuantity = new JLabel("{qty}");
        this.getContentPane().add(mQuantity, "cell 1 4,grow");
        
        mUnmatchLabel = new JLabel("Unmatchable:");
        this.getContentPane().add(mUnmatchLabel, "cell 0 5,grow");
        
        mUnmatch = new JLabel("{int}");
        this.getContentPane().add(mUnmatch, "cell 1 5,grow");

    }
    
    /* 
     * In this method, the major action logic is specify. 
     * If there are future changes on operation steps or 
     * adding new steps, only need to modify code in this
     * method.
     */
    @Override
    public void actionPerformed(ActionEvent e){

        if (e.getSource() == this.mOpenButton){
            
            JFileChooser jfc = this.getFileChooser();
            int returnValue = jfc.showOpenDialog(null);

            if ( returnValue == JFileChooser.APPROVE_OPTION){
                this.mTextField.setText(jfc.getSelectedFile().getAbsolutePath());
            }

        }else if (e.getSource() == this.mLoadButton){

            echo("Start validation...");

            this.mCheques = this.parse(this.mTextField.getText(), this.mTempPath);

            // Output statistic 
            int lost = 0;
            int qtyHKD = 0;
            int qtyMOP = 0;

            for ( Cheque c : mCheques ){
                echo(c.toString());
                if (c.isEmpty()){
                    lost += 1;
                }else{
                    if (c.getCcy().compareTo("HKD")==0){
                        qtyHKD += 1;
                    }
                    if (c.getCcy().compareTo("MOP")==0){
                        qtyMOP += 1;
                    }
                }
            }

            this.mQuantity.setText(Integer.toString(qtyHKD) + " / " + Integer.toString(qtyMOP));
            this.mUnmatch.setText(Integer.toString(lost));

            // Delete all temp files and folder
            delete(new File(this.mTempPath));
           
        }else if (e.getSource() == this.mExportButton){

            echo("Start export...");

            if (this.mCheques == null){
                mCheques = this.parse(this.mTextField.getText(), this.mTempPath);
            }

            // Loops chq list to output csv string
            String csvContent = "";
            String csvHeader = "\"1\",\"H\",\"H\",\"H\",\"H\",\"H\",\"H\",\"H\"";
            csvContent = csvContent + csvHeader + "\n";
            for ( Cheque c : mCheques ){
                if (!c.isEmpty()){
                    csvContent = csvContent + c.toCsv() + "\n";
                }
            }

            // Output the file
            String exportName = this.mOutputPath + File.separator + this.mOutputName; 
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
            delete(new File(this.mTempPath));

        }
    }

    /*
     * helper function: read pdf from target folder and parse cheque objs from it
     * @parameter: String path name of target folder
     * @parameter: String path name of temp folder
     * @return: An array of cheques
     */
    private List<Cheque> parse(String target, String temp){

        copyPDFs(target, temp);
        convertPDFs(temp);

        // Decode all generated imaged Files 
        File tempFolder = new File(temp);
        List<Cheque> cheques = new ArrayList<Cheque>();
        File[] fileList = tempFolder.listFiles(new FileFilter(){
            @Override 
            public boolean accept(File f){
                String type =f.getName().substring(f.getName().lastIndexOf(".") + 1);
                return type.compareToIgnoreCase("jpg") == 0;
            }
        });

        for ( File f : fileList ){
            Cheque newCheque = Cheque.parse(Command.runCommand("lib/pingLM " +  f.getAbsolutePath())); 
            cheques.add(newCheque); //for unmatchable cheque, will pass in as EMPTY_CHEQUE
        }

        return cheques;
    }

    /* 
     * helper function: copy pdfs under target folder to temp folder 
     */
    private void copyPDFs(String from, String to){

        File target = new File(from);

        File[] pdfList = target.listFiles(new FileFilter(){
            @Override 
            public boolean accept(File f){
                String type =f.getName().substring(f.getName().lastIndexOf(".") + 1);
                return type.compareToIgnoreCase("pdf") == 0;
            }
        });

        for (File f : pdfList){
            copyPDF(f.getAbsolutePath(), to);
        }
    } 

    /* 
     * helper function: copy target file to a specific path
     */
    private void copyPDF(String file, String path){

        String nameOfPDF = file.substring(file.lastIndexOf(File.separator)); 
        String fullnameOfTempPDF = path + File.separator + nameOfPDF;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try{
            //Create path directory if not exist
            File pathFile = new File(path);
            if (!pathFile.exists()){
                pathFile.mkdir();
            }

            //Delete duplicate file if exists
            File tempPDF = new File(fullnameOfTempPDF);
            if (tempPDF.exists()){
                delete(tempPDF);
            }
            tempPDF.createNewFile();

            //Build input stream and output stream
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            fos = new FileOutputStream(fullnameOfTempPDF);
            bos = new BufferedOutputStream(fos);

            //Read from input and write to output
            int c;
            while ( (c = bis.read()) != -1){
                bos.write(c);
                bos.flush();
            } 

        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            try {
                if (fis != null) fis.close();
                if (fos != null) fos.close();
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /*
     * helper function: convert pdfs under given folder to jpg at same location
     */
    private void convertPDFs(String path){

        File target = new File(path);

        File[] pdfList = target.listFiles(new FileFilter(){
            @Override 
            public boolean accept(File f){
                String type =f.getName().substring(f.getName().lastIndexOf(".") + 1);
                return type.compareToIgnoreCase("pdf") == 0;
            }
        });

        for (File f : pdfList){
            Command.runCommand("lib/gswin32 -sDEVICE=jpeg -r240 -o " + path + "/temp%03d.jpg " + f.getAbsolutePath() + " -q -dNOPROMPT -dBATCH"); 
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

}
