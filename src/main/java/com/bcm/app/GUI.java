package com.bcm.app; 

import java.io.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Properties;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import net.miginfocom.swing.MigLayout;

/**
 * A Graphic User Interface for the QR code recognizing.
 *
 */
public class GUI extends JFrame implements ActionListener{

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
    private JLabel mUnreadableLabel;
    private JLabel mQuantity;
    private JLabel mUnreadable;

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

    /*
     * Data members
     */
    private List<Cheque> mCheques;

    /**
     * Class initializer method.
     * Called when class GUI object creation. 
     *
     * @param java.util.Properties should be injected by external code
     */
    public GUI(Properties prop) {
        loadProperties(prop);
        initScreen();
    }

    /**
     * Class initializer method: Properties initializer.
     * Called when class GUI object creation. 
     *
     * @param java.util.Properties 
     * @return void
     */
    private void loadProperties(Properties prop){
        mBankName = prop.getProperty("BANK_NAME");
        mAppName = prop.getProperty("APP_NAME");
        mBranchName = prop.getProperty("BRANCH_NAME");
        mBranchCode = prop.getProperty("BRANCH_CODE");
        mInputPath = prop.getProperty("INPUT_FOLDER");
        mOutputPath = prop.getProperty("OUTPUT_FOLDER");
        mOutputName = prop.getProperty("OUTPUT_NAME");
        mTempPath = prop.getProperty("TEMP_FOLDER");
    }

    /**
     * Class initializer method: Screen initializer.
     * Called when class GUI object creation. 
     *
     * @param void
     * @return void
     */
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
        mImage.setIcon(new ImageIcon("img\\bcm_logo_s.jpg"));
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
        
        mQuantityLabel = new JLabel("Qty (MOP/HKD):");
        this.getContentPane().add(mQuantityLabel, "flowx,cell 0 4,grow");
        
        mQuantity = new JLabel("- / -");
        this.getContentPane().add(mQuantity, "cell 1 4,grow");
        
        mUnreadableLabel = new JLabel("Unreadable:");
        this.getContentPane().add(mUnreadableLabel, "cell 0 5,grow");
        
        mUnreadable = new JLabel("-");
        this.getContentPane().add(mUnreadable, "cell 1 5,grow");

    }
    
    /** 
     * In this method, the major action logic is specify. 
     * If there are future changes on operation steps or 
     * adding new steps, only need to modify code in this
     * method.
     *
     * @param ActionEvent passed by swing framework 
     * @return void
     */
    @Override
    public void actionPerformed(ActionEvent e){

        if (e.getSource() == mOpenButton){
            
            JFileChooser jfc = this.getFileChooser();
            int returnValue = jfc.showOpenDialog(null);

            if ( returnValue == JFileChooser.APPROVE_OPTION){
                mTextField.setText(jfc.getSelectedFile().getAbsolutePath());
            }

        }else if (e.getSource() == mLoadButton){

            echo("Start loading...");

            try{
                // No matter whether loaded before, re-load
                mCheques = Cheques.parse(mTextField.getText(), mTempPath);
                int qtyMOP = Cheques.getQuantity(mCheques, Cheque.CHEQUE_MOP);
                int qtyHKD = Cheques.getQuantity(mCheques, Cheque.CHEQUE_HKD);
                int qtyUnreadable = Cheques.getQuantity(mCheques, Cheque.CHEQUE_UNMATCH);
    
                mQuantity.setText(qtyMOP + " / " + qtyHKD);
                mUnreadable.setText(Integer.toString(qtyUnreadable));
    
                for (Cheque c : mCheques){
                    echo(c.toString());
                }
             
                echo("Loading finished");
    
                JOptionPane.showMessageDialog(this, "Loading finished.");

            }catch(Exception ex){

                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Load interrupted. Please check path existence and file integrity.");

            }
           
        }else if (e.getSource() == this.mExportButton){

            echo("Start exporting...");

            try{
                // Check if loaded before, dont load again and export directly
                if (mCheques == null || mCheques.size() == 0){
                    mCheques = Cheques.parse(mTextField.getText(), mTempPath);
                }
    
                int qtyMOP = Cheques.getQuantity(mCheques, Cheque.CHEQUE_MOP);
                int qtyHKD = Cheques.getQuantity(mCheques, Cheque.CHEQUE_HKD);
                int qtyUnreadable = Cheques.getQuantity(mCheques, Cheque.CHEQUE_UNMATCH);
    
                mQuantity.setText(qtyMOP + " / " + qtyHKD);
                mUnreadable.setText(Integer.toString(qtyUnreadable));
    
                for (Cheque c : mCheques){
                    echo(c.toString());
                }
    
                Cheques.export(mCheques, mOutputPath + File.separator + mOutputName);
             
                echo("Exporting finished");
    
                JOptionPane.showMessageDialog(this, "Export finished.");
    
            }catch(Exception ex){

                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Load/Export interrupted. Please check path existence and file integrity.");

            }

        }
    }

    /**
     * Helper function: get global singleton JFileChooser
     *
     * @return  JFileChooser 
     */
    private JFileChooser getFileChooser(){

        if ( this.mFileChooser == null){
            this.mFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            this.mFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        }
        return this.mFileChooser;

    }
        
    /**
     * Helper function: echo to textArea on GUI
     *
     * @param   message the String to present onto TextArea
     * @return  void
     */
    private void echo(String message){
        this.mTextArea.setText(this.mTextArea.getText() + "\n" + message);
    }

}

