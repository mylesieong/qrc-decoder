package com.bcm.app;

import java.io.File;
import java.io.FileOutputStream;

public class Cheque {

    private final static String EMPTY_STRING = "";

    /* Properties */
    private int bank;   /* e.g. 113 */
    private String type;    /* e.g. CHQ */
    private String ccy;     /* e.g. HKD */
    private String id;     /* e.g. 2000106 */
    private String holder;     /* e.g. CHAN DA MAN */
    private String account;     /* e.g. 43212654334 */
    private int amount;
    private String envelope;

    public void setBank(int bank){
        this.bank = bank;
    }

    public void setType(String type){
        this.type = type;
    }

    public void setCcy(String ccy){
        this.ccy = ccy;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setHolder(String holder){
        this.holder = holder;
    }

    public void setAccount(String account){
        this.account = account;
    }

    public void setAmount(int amount){
        this.amount = amount;
    }

    public void setEnvelope(String envelope){
        this.envelope = envelope;
    }

    public int getBank(){
        return this.bank;
    }

    public String getType(){
        return this.type != null ? this.type : EMPTY_STRING ;
    }

    public String getCcy(){
        return this.ccy != null ? this.ccy : EMPTY_STRING ;
    }

    public String getId(){
        return this.id != null ? this.id : EMPTY_STRING ;
    }

    public String getHolder(){
        return this.holder != null ? this.holder : EMPTY_STRING ;
    }

    public String getAccount(){
        return this.account != null ? this.account : EMPTY_STRING ;
    }

    public int getAmount(){
        return this.amount;
    }

    public String getEnvelope(){
        return this.envelope != null ? this.envelope : EMPTY_STRING ;
    }

    /**
     * Static util that parse string from AMCM API to Cheque object 
     * @param String stdout from amcm api 
     * @return Cheque feedback the string into cheque object, if blob is error, then return null
     */
    public static Cheque parse(String blob){
        
        if (blob == null 
                || blob.compareTo(EMPTY_STRING) == 0
                || blob.contains("error") ){
            return null;
        }

        Cheque cheque = new Cheque();
        String[] blobTokenized = blob.split(";");

        //Tailor made for blob result from AMCM API
        cheque.setBank(blobTokenized[0] != null ? Integer.parseInt(blobTokenized[0]) : 0 );
        cheque.setType(blobTokenized[1]);
        cheque.setCcy(blobTokenized[2]);
        cheque.setHolder(blobTokenized[3]);
        cheque.setAccount(blobTokenized[4]);
        cheque.setId(blobTokenized[5]);
        
        return cheque;
    }
    
    /**
     * String util that export object to csv according to certain sequence
     * @return String a valid csv line
     */
    public String toCsv(){

        StringBuilder result = new StringBuilder();

        /* The seq should be:
         * Bank: Type: Ccy: ChqId: Amount: Holder: Account: Envelope
         */
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

    @Override
    public String toString(){

        StringBuilder result = new StringBuilder();

        result.append(this.getBank());
        result.append(":");
        result.append(this.getType());
        result.append(":");
        result.append(this.getCcy());
        result.append(":");
        result.append(this.getId());
        result.append(":");
        result.append(this.getAmount());
        result.append(":");
        result.append(this.getHolder());
        result.append(":");
        result.append(this.getAccount());
        result.append(":");
        result.append(this.getEnvelope());

        return result.toString();

    }
}
