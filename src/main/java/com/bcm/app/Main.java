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
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JDialog;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.List;
import java.util.ArrayList;
import java.util.Properties;

public class Main extends JFrame implements ActionListener{

    /*
     * Screen component
     */
    private JLabel mTitle;
    private JLabel mSubtitle;
    private JTextField mFileTextField;
    private JButton mChooseButton;
    private JButton mValidateButton;
    private JButton mExportButton;
    private JTextArea mTextArea;
    private JScrollPane mScrollPane;
    private JFileChooser mFileChooser;
    private JLabel mAmountLabel;
    private JLabel mQuantityLabel;
    private JLabel mUnmatchLabel;
    private JLabel mAmount;
    private JLabel mQuantity;
    private JLabel mUnmatch;

    /*
     * Properties
     */
    private String mBankName;
    private String mAppName;
    private String mBranchName;
    private String mInputPath;
    private String mOutputPath;
    private String mOutputName;
    private String mTempPath;

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
            FileInputStream input = new FileInputStream("qrc.properties");
            prop.load(input);
            mBankName = prop.getProperty("BANK_NAME");
            mAppName = prop.getProperty("APP_NAME");
            mBranchName = prop.getProperty("BRANCH_NAME");
            mInputPath = prop.getProperty("INPUT_FOLDER");
            mOutputPath = prop.getProperty("OUTPUT_FOLDER");
            mOutputName = prop.getProperty("OUTPUT_NAME");
            mTempPath = prop.getProperty("TEMP_FOLDER");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initScreen(){
        this.setBounds(100, 100, 700, 410);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(null);
        
        mTitle = new JLabel(mBankName);
        mTitle.setBounds(15, 15, 700, 20);
        this.getContentPane().add(mTitle);

        mSubtitle = new JLabel(mAppName);
        mSubtitle.setBounds(15, 40, 700, 20);
        this.getContentPane().add(mSubtitle);

        mFileTextField = new JTextField("");
        mFileTextField.setBounds(15, 65, 390, 20);
        this.getContentPane().add(mFileTextField);
        
        mChooseButton = new JButton("Choose");
        mChooseButton.setBounds(420, 65, 80, 20);
        mChooseButton.addActionListener(this);
        this.getContentPane().add(mChooseButton);
        
        mValidateButton = new JButton("Validate");
        mValidateButton.setBounds(510, 65, 80, 20);
        mValidateButton.addActionListener(this);
        this.getContentPane().add(mValidateButton);
        
        mExportButton = new JButton("Export");
        mExportButton.setBounds(600, 65, 80, 20);
        mExportButton.addActionListener(this);
        this.getContentPane().add(mExportButton);

        mTextArea = new JTextArea("");
        mScrollPane = new JScrollPane(mTextArea);
        mScrollPane.setBounds(15, 100, 665, 220);
        this.getContentPane().add(mScrollPane);
        
        mAmountLabel = new JLabel("Total Amount(HKD/MOP):");
        mAmountLabel.setBounds(15, 320, 150, 20);
        this.getContentPane().add(mAmountLabel);

        mAmount = new JLabel("{amount}");
        mAmount.setBounds(160, 320, 150, 20);
        this.getContentPane().add(mAmount);

        mQuantityLabel = new JLabel("Total Qty(HKD/MOP):");
        mQuantityLabel.setBounds(15, 340, 150, 20);
        this.getContentPane().add(mQuantityLabel);

        mQuantity = new JLabel("{qty}");
        mQuantity.setBounds(160, 340, 150, 20);
        this.getContentPane().add(mQuantity);

        mUnmatchLabel = new JLabel("Unmatchable:");
        mUnmatchLabel.setBounds(15, 360, 150, 20);
        this.getContentPane().add(mUnmatchLabel);

        mUnmatch = new JLabel("{um}");
        mUnmatch.setBounds(160, 360, 150, 20);
        this.getContentPane().add(mUnmatch);

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

            copyPDFs(targetFolder, tempPathName);

            convertPDFs(tempPathName);

            // Decode all generated imaged Files 
            File tempPath = new File(tempPathName);
            List<Cheque> cheques = new ArrayList<Cheque>();
            File[] fileList = tempPath.listFiles(new FileFilter(){
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

            // Output statistic 
            int lost = 0;
            int amountHKD = 0;
            int amountMOP = 0;
            int qtyHKD = 0;
            int qtyMOP = 0;

            for ( Cheque c : cheques ){
                echo(c.toString());
                if (c.isEmpty()){
                    lost += 1;
                }else{
                    if (c.getCcy().compareTo("HKD")==0){
                        amountHKD += c.getAmount();
                        qtyHKD += 1;
                    }
                    if (c.getCcy().compareTo("MOP")==0){
                        amountMOP += c.getAmount();
                        qtyMOP += 1;
                    }
                }
            }

            this.mAmount.setText(Integer.toString(amountHKD) + " / " + Integer.toString(amountMOP));
            this.mQuantity.setText(Integer.toString(qtyHKD) + " / " + Integer.toString(qtyMOP));
            this.mUnmatch.setText(Integer.toString(lost));

            // Delete all temp files and folder
            delete(tempPath);
           
        }else if (e.getSource() == this.mExportButton){
            echo("Start export...");
            String targetFolder = this.mFileTextField.getText();
            String tempPathName = "temp";

            copyPDFs(targetFolder, tempPathName);

            convertPDFs(tempPathName);

            // Decode all generated imaged Files 
            File tempPath = new File(tempPathName);
            List<Cheque> cheques = new ArrayList<Cheque>();
            File[] fileList = tempPath.listFiles(new FileFilter(){
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

            // Loops chq list to output csv string
            String csvContent = "";
            String csvHeader = "\"Bank\",\"Type\",\"Currency\",\"Cheque No\",\"Amount\",\"Account Name\",\"Account Number\",\"Envelope Number\"";
            csvContent = csvContent + csvHeader + "\n";
            for ( Cheque c : cheques ){
                if (!c.isEmpty()){
                    csvContent = csvContent + c.toCsv() + "\n";
                }
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

        String pdfName = file.substring(file.lastIndexOf(File.separator)); 
        String tempPDFName = path + File.separator + pdfName;

        try{
            /* Build a folder next to target pdf and make its copy into folder*/
            File tempPath = new File(path);
            File originPDF = new File(file);
            tempPath.mkdir();
            Files.copy(new FileInputStream(originPDF), Paths.get(tempPDFName), REPLACE_EXISTING);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    /*
     * helper function: convert pdfs under given folder to jpg at same location
     */
    private void convertPDFs(String path){
        Command.runCommand("lib/convert -density 240 -quality 80 -trim " + path + "/*.pdf temp/temp.jpg"); //wildcard eats all pdfs
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
