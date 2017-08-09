package com.bcm.app;

import java.io.File;
import java.io.FileOutputStream;

public class Cheque {

    private final static String EMPTY_STRING = "";
    private final static Cheque EMPTY_CHEQUE = new Cheque();

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
     */
    public static Cheque getEmptyCheque(){
        return new Cheque();    
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

    /**
     * Return whether Cheque is empty 
     * @return boolean 
     */
    public boolean isEmpty(){
        return this.equals(EMPTY_CHEQUE);
    }

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
     * Constructor: Cheque()
     */
    private Cheque(){
        super();
        this.setBank(0);
        this.setType(EMPTY_STRING);
        this.setCcy(EMPTY_STRING);
        this.setId(EMPTY_STRING);
        this.setHolder(EMPTY_STRING);
        this.setAccount(EMPTY_STRING);
        this.setAmount(0);
        this.setEnvelope(EMPTY_STRING);
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
