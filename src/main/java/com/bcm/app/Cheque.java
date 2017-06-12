package com.bcm.app;

public class Cheque {

    private static void debug(String m){
        System.out.print("[DEBUG]");
        System.out.println(m);
    }

    /**
     * Static util that parse string from AMCM API to Cheque object 
     * @param String stdout from amcm api 
     * @return Cheque feedback the string into cheque object, if blob is error, then return null
     */
    public static Cheque parse(String blob){

        try{
        String subs = blob.substring(0,50);
        debug("In cheque string: " + subs);

        byte[] foo;
        // read: - write: utf8
        foo = blob.getBytes();
        debug("In cheque parsing getBytes(): " + foo);
        debug("In cheque parsing Build UTF8 string from getBytes(): " + new String(foo, "UTF8").substring(0,50) );

        // read: utf8 write: utf8
        foo = blob.getBytes("UTF8");
        debug("In cheque parsing getBytes(UTF8): " + foo);
        debug("In cheque parsing Build UTF8 string from getBytes(UTF8): " + new String(foo, "UTF8").substring(0,50));
        
        // read: cp937 write: utf8
        foo = blob.getBytes("Cp937");
        debug("In cheque parsing getBytes(Cp937): " + foo);
        debug("In cheque parsing Build UTF8 string from getBytes(Cp937): " + new String(foo, "UTF8").substring(0,50));

        // read: - write: -
        foo = blob.getBytes();
        debug("In cheque parsing getBytes(): " + foo);
        debug("In cheque parsing Build string from getBytes(): " + new String(foo).substring(0,50));

        // read: utf8 write: -
        foo = blob.getBytes("UTF8");
        debug("In cheque parsing getBytes(UTF8): " + foo);
        debug("In cheque parsing Build string from getBytes(UTF8): " + new String(foo).substring(0,50));

        // read: cp937 write: -
        foo = blob.getBytes("Cp937");
        debug("In cheque parsing getBytes(Cp937): " + foo);
        debug("In cheque parsing Build string from getBytes(Cp937): " + new String(foo).substring(0,50));

        // read: - write: Cp937
        foo = blob.getBytes();
        debug("In cheque parsing getBytes(): " + foo);
        debug("In cheque parsing Build Cp937 string from getBytes(): " + new String(foo, "Cp937").substring(0,50));

        // read: utf8 write: Cp937
        foo = blob.getBytes("UTF8");
        debug("In cheque parsing getBytes(UTF8): " + foo);
        debug("In cheque parsing Build Cp937 string from getBytes(UTF8): " + new String(foo, "Cp937").substring(0,50));

        // read: cp937 write: Cp937
        foo = blob.getBytes("Cp937");
        debug("In cheque parsing getBytes(Cp937): " + foo);
        debug("In cheque parsing Build Cp937 string from getBytes(Cp937): " + new String(foo, "Cp937").substring(0,50));

        }catch (Exception e){
            e.printStackTrace();
        }

        if (blob == null 
                || blob.compareTo("") == 0
                || blob.contains("error") ){
            return null;
        }

        String encodedBlob = blob; 
        try{
            encodedBlob = new String( blob.getBytes("UTF8") );
        }catch(Exception e){
            e.printStackTrace();
        }
        Cheque cheque = new Cheque();
        String[] blobTokenized = encodedBlob.split(";");

        for (int i = 0 ; i < 6 ; i++){  //Tailor made for blob result from AMCM API
            if ( i == 0) cheque.setBank(blobTokenized[i] != null ? Integer.parseInt(blobTokenized[i]) : 0 );
            if ( i == 1) cheque.setType(blobTokenized[i] != null ? blobTokenized[i] : "" );
            if ( i == 2) cheque.setCcy(blobTokenized[i] != null ? blobTokenized[i] : "" );
            if ( i == 3) cheque.setHolder(blobTokenized[i] != null ? blobTokenized[i] : "" );
            if ( i == 4) cheque.setId(blobTokenized[i] != null ? blobTokenized[i] : "" );
            if ( i == 5) cheque.setAccount(blobTokenized[i] != null ? blobTokenized[i] : "" );
        }
        
        return cheque;
    }

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

    public int getAmount(){
        return this.amount;
    }

    public String getEnvelope(){
        return this.envelope;
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
        result.append(this.bank);
        result.append("\"");
        result.append(",");
        result.append("\"");
        result.append(this.type);
        result.append("\"");
        result.append(",");
        result.append("\"");
        result.append(this.ccy);
        result.append("\"");
        result.append(",");
        result.append("\"");
        result.append(this.id);
        result.append("\"");
        result.append(",");
        result.append("\"");
        result.append(this.amount);
        result.append("\"");
        result.append(",");
        result.append("\"");
        result.append(this.holder);
        result.append("\"");
        result.append(",");
        result.append("\"");
        result.append(this.account);
        result.append("\"");
        result.append(",");
        result.append("\"");
        result.append(this.envelope);
        result.append("\"");

        debug("Before write to csv: " + result.toString());

        return result.toString();

    }

}
