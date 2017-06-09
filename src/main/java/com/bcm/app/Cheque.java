package com.bcm.app;

public class Cheque {

    /**
     * Static util that parse string from AMCM API to Cheque object 
     * @param String stdout from amcm api 
     * @return Cheque feedback the string into cheque object
     */
    public static Cheque parse(String blob){
        Cheque cheque = new Cheque();
        return cheque;
    }

    /**
     * String util that export object to csv according to certain sequence
     * @return String a valid csv line
     */
    public static String toCsv(){
        String csvLine = "";
        return csvLine;
    }

    /* Properties */
    private int bank;   /* e.g. 113 */
    private String type;    /* e.g. CHQ */
    private String ccy;     /* e.g. HKD */
    private String id;     /* e.g. 2000106 */
    private String holder;     /* e.g. CHAN DA MAN */
    private String account;     /* e.g. 43212654334 */

    public void setBack(int bank){
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

    public int getBank(){
        return this.bank;
    }

    public String getType(){
        return this.type;
    }

    public String getCcy(){
        return this.ccy;
    }

    public String getId(){
        return this.id;
    }

    public String getHolder(){
        return this.holder;
    }

    public String getAccount(){
        return this.account;
    }

}
