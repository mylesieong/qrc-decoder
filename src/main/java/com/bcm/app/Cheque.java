package com.bcm.app;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Cheque Object Abstraction. Class Cheque provides cheque properties 
 * getters and setters, blank cheque template, equals and isEmpty  
 * checkers and csv format convert method.
 *
 */
public class Cheque {

    /* 
     * Properties template
     */
    private final static int EMPTY_INT = 0;
    private final static String EMPTY_STRING = "";
    private final static Cheque EMPTY_CHEQUE = new Cheque();

    /* 
     * Properties 
     */
    private int bank;   /* e.g. 113 */
    private String type;    /* e.g. CHQ */
    private String ccy;     /* e.g. HKD */
    private String id;     /* e.g. 17000238 */
    private String holder;     /* e.g. CHAN DA MAN */
    private String account;     /* e.g. 43212654334 */
    private int amount;
    private String envelope;

    /**
     * ***PRIVATE***
     * Constructor: Create object set with empty properties.
     * The constructor is private and only be invoked by 
     * Cheque:getEmptyCheque() method.
     *
     */
    private Cheque(){
        super();
        this.setBank(EMPTY_INT);
        this.setType(EMPTY_STRING);
        this.setCcy(EMPTY_STRING);
        this.setId(EMPTY_STRING);
        this.setHolder(EMPTY_STRING);
        this.setAccount(EMPTY_STRING);
        this.setAmount(EMPTY_INT);
        this.setEnvelope(EMPTY_STRING);
    }

    /**
     * Setter
     *
     * @param int e.g. 103
     * @return void
     */
    public void setBank(int bank){
        this.bank = bank;
    }

    /**
     * Setter
     *
     * @param String e.g. 'CHQ'
     * @return void
     */
    public void setType(String type){
        this.type = type;
    }

    /**
     * Setter
     *
     * @param String e.g. 'HKD'
     * @return void
     */
    public void setCcy(String ccy){
        this.ccy = ccy;
    }

    /**
     * Setter
     *
     * @param String the cheque number. e.g. '17000238'
     * @return void
     */
    public void setId(String id){
        this.id = id;
    }

    /**
     * Setter
     *
     * @param String cheque owner's name. e.g. 'CHAN DA MAN'
     * @return void
     */
    public void setHolder(String holder){
        this.holder = holder;
    }

    /**
     * Setter
     *
     * @param String cheque owner's other bank A/C. e.g. '43212654334'
     * @return void
     */
    public void setAccount(String account){
        this.account = account;
    }

    /**
     * Setter
     *
     * @param int cheque ammount (not being used yet at v1.0) 
     * @return void
     */
    public void setAmount(int amount){
        this.amount = amount;
    }

    /**
     * Setter
     *
     * @param int envelope number for abnormal cheque (not being used yet at v1.0) 
     * @return void
     */
    public void setEnvelope(String envelope){
        this.envelope = envelope;
    }

    /**
     * Getter
     */
    public int getBank(){
        return this.bank;
    }

    /**
     * Getter
     */
    public String getType(){
        return this.type != null ? this.type : EMPTY_STRING ;
    }

    /**
     * Getter
     */
    public String getCcy(){
        return this.ccy != null ? this.ccy : EMPTY_STRING ;
    }

    /**
     * Getter
     */
    public String getId(){
        return this.id != null ? this.id : EMPTY_STRING ;
    }

    /**
     * Getter
     */
    public String getHolder(){
        return this.holder != null ? this.holder : EMPTY_STRING ;
    }

    /**
     * Getter
     */
    public String getAccount(){
        return this.account != null ? this.account : EMPTY_STRING ;
    }

    /**
     * Getter
     */
    public int getAmount(){
        return this.amount;
    }

    /**
     * Getter
     */
    public String getEnvelope(){
        return this.envelope != null ? this.envelope : EMPTY_STRING ;
    }

    /**
     * Static utililty that parse string from AMCM QR-Code Parsing API 
     * into Cheque object.
     * @param String passed from amcm qrcode parsing api. 
     *  e.g. '123;CHQ;MOP;Min Tech Company;001204932;00001924;'
     * @return Cheque parsed from blob string, if blob is empty/error, 
     *  then return an empty cheque.
     */
    public static Cheque parse(String blob){
        
        Cheque cheque = Cheque.getEmptyCheque();

        // Validate not null or empty or nonreadable
        if (blob == null 
                || blob.compareTo(EMPTY_STRING) == 0
                || blob.contains("error") ){
            return cheque;  //empty cheque
        }

        try{
            //Tailor made for blob result from AMCM API
            String[] blobTokenized = blob.split(";");
            cheque.setBank(blobTokenized[0] != null ? Integer.parseInt(blobTokenized[0]) : 0 );
            cheque.setType(blobTokenized[1]);
            cheque.setCcy(blobTokenized[2]);
            cheque.setHolder(blobTokenized[3]);
            cheque.setAccount(blobTokenized[4]);
            cheque.setId(blobTokenized[5]);
        }catch (Exception e){
            System.out.println("There is mal-format found during parsing");
        }

        return cheque;

    }
    
    /**
     * Factory method: get Empty Cheque
     *
     * @return Cheque set with empty properties
     */
    public static Cheque getEmptyCheque(){
        return new Cheque();    
    }

    /**
     * Checker method: whether this Cheque is empty 
     *
     * @return boolean 
     */
    public boolean isEmpty(){
        return this.equals(EMPTY_CHEQUE);
    }

    /**
     * Checker method: whether properties of this Cheque equals 
     * another given cheque's properties. 
     *
     * @param Object that will be checked and converted into an Cheque
     *   object before properties comparision
     * @return boolean 
     */
    @Override
    public boolean equals(Object obj){
        if ( !(obj instanceof Cheque) ){
            return false;
        }

        Cheque opp = (Cheque)obj;

        return this.getBank() == opp.getBank() &&
               this.getType().compareTo(opp.getType()) == 0 && 
               this.getCcy().compareTo(opp.getCcy()) == 0 && 
               this.getId().compareTo(opp.getId()) == 0 && 
               this.getHolder().compareTo(opp.getHolder()) == 0 && 
               this.getAccount().compareTo(opp.getAccount()) == 0 && 
               this.getAmount() == opp.getAmount() &&
               this.getEnvelope().compareTo(opp.getEnvelope()) == 0 ; 
    }

    /**
     * Utility that export this object to csv according to AMCM-format
     *
     * @return String a AMCM-format csv line
     *   format: 'Bank Type Ccy ChqNum Amount Holder Account EnvelopeNum'
     * e.g.'"123","CHQ","MOP","00001924","500000","Min Tech Company","001204932",""'
     */
    public String toCsv(){

        StringBuilder result = new StringBuilder();

        result.append("\"");
        result.append(this.getBank());
        result.append("\"");
        result.append(",");
        result.append("\"");
        result.append(this.getType());
        result.append("\"");
        result.append(",");
        result.append("\"");
        result.append(this.getCcy());
        result.append("\"");
        result.append(",");
        result.append("\"");
        result.append(this.getId());
        result.append("\"");
        result.append(",");
        result.append("\"");
        result.append(this.getAmount());
        result.append("\"");
        result.append(",");
        result.append("\"");
        result.append(this.getHolder());
        result.append("\"");
        result.append(",");
        result.append("\"");
        result.append(this.getAccount());
        result.append("\"");
        result.append(",");
        result.append("\"");
        result.append(this.getEnvelope());
        result.append("\"");

        return result.toString();

    }


    /** 
     * Utility for debug use
     */
    @Override
    public String toString(){

        StringBuilder result = new StringBuilder();

        result.append(this.getBank());
        result.append("\t");
        result.append(this.getType());
        result.append("\t");
        result.append(this.getCcy());
        result.append("\t");
        result.append(this.getId());
        result.append("\t");
        result.append(this.getAmount());
        result.append("\t");
        result.append(this.getHolder());
        result.append("\t");
        result.append(this.getAccount());
        result.append("\t");
        result.append(this.getEnvelope());

        return result.toString();

    }
}
