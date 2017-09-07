package com.bcm.app;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import java.util.List;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Class Cheques is the utility class that manipulate Cheque objects.
 * It provides method that parse Cheque objects from given pdf file,
 * method that export injects Cheque objects into csv file, and method
 * that analyse given Cheque objects.
 *
 */
public class Cheques {

    /**
     * Iterate injected Cheque List and return the quantity of certain 
     * type of cheque in the list.
     *
     * @param List<Cheque> a list of cheque objects 
     * @param int states the type we want to calculate 
     *      (CHEQUE_MOP/CHEQUE_HKD/CHEQUE_UNMATCH)
     * @return quantity of cheque objects in given type
     */
    public static int getQuantity(List<Cheque> roster, int type){

        int result = 0;

        if (type == Cheque.CHEQUE_MOP){

            for ( Cheque c : roster ){
                if (!c.isEmpty() && c.getCcy().compareTo("MOP")==0 ){
                    result ++;
                }
            }

        }else if (type == Cheque.CHEQUE_HKD){

            for ( Cheque c : roster ){
                if (!c.isEmpty() && c.getCcy().compareTo("HKD")==0 ){
                    result ++;
                }
            }
            
        }else if (type == Cheque.CHEQUE_UNMATCH){

            for ( Cheque c : roster ){
                if (c.isEmpty()){
                    result ++;
                }
            }

        }

        return result;
    }

    /**
     * Export a list of cheque objects into a CSV format file.
     * (note that a dummy header will be inserted)
     *
     * @param List<Cheque> cheque objects to be export
     * @param String csv file name
     * @return void
     */
    public static void export(List<Cheque> roster, String file){

        String csvContent = "";
        String csvHeader = "\"1\",\"H\",\"H\",\"H\",\"1\",\"H\",\"H\",\"H\"";
        csvContent = csvContent + csvHeader + "\n";

        // Prepare the content
        for ( Cheque c : roster ){
            if (!c.isEmpty()){
                csvContent = csvContent + Cheques.toCsv(c) + "\n";
            }
        }

        // Output the file
        try{
            File export = new File(file);
            FileOutputStream fos = new FileOutputStream(export);

            if (!export.exists()){
                export.createNewFile();
            }

            fos.write(csvContent.getBytes("UTF8"));
            fos.flush();
            fos.close();

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    /**
     * Read PDF(s) from target folder and parse cheque objects from it/them.
     * It first clears the temp folder, then copy the PDF(s) to temp folder and
     * use ImageMagick/convert to split the PDF(s) into image files. Lastly,
     * it uses AMCM API(pingLM.exe) to parse all image files into Cheque objects. 
     *
     * @param String path name of target folder that contains PDF(s)
     * @param String temp folder path that is used to put the images converted
     *   from the PDF(s) temporary. Temp folder will not be cleared until the
     *   next invokion of parse method.
     * @return List<Cheque> A list of cheque objects
     */
    public static List<Cheque> parse(String target, String temp) throws Exception {

        delete(new File(temp));

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
            Cheque newCheque = Cheques.parseCheque(Command.runCommand("lib/pingLM " +  f.getAbsolutePath())); 
            cheques.add(newCheque); //for unmatchable cheque, will pass in as EMPTY_CHEQUE
        }

        return cheques;
    }

    /** 
     * private helper function: copy pdf(s) under target folder to temp folder 
     */
    private static void copyPDFs(String from, String to) throws Exception {

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

    /** 
     * Private helper function: copy target file to a specific path
     */
    private static void copyPDF(String file, String path) throws Exception {

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
            byte[] c = new byte[1024];
            int r = 0;
            while ( (r = bis.read(c)) != -1 ){
                bos.write(c);
                bos.flush();
            }

        }catch (Exception ex){

            ex.printStackTrace();
            throw ex;

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

    /**
     * Private helper function: convert pdfs under given folder to jpg and save
     * the image file to the same folder. 
     */
    private static void convertPDFs(String path){

        File target = new File(path);

        File[] pdfList = target.listFiles(new FileFilter(){
            @Override 
            public boolean accept(File f){
                String type =f.getName().substring(f.getName().lastIndexOf(".") + 1);
                return type.compareToIgnoreCase("pdf") == 0;
            }
        });

        for (File f : pdfList){
            String tempPrefix = f.getAbsolutePath().substring( 0, f.getAbsolutePath().lastIndexOf(".") );
            Command.runCommand("lib/gswin32 -sDEVICE=jpeg -r240 -o " + tempPrefix + "%03d.jpg " + f.getAbsolutePath() + " -q -dNOPROMPT -dBATCH"); 
        }
    }

    /**
     * Private helper function: delete file/directory (if it is a folder and it
     * is not empty, delete both the folder and its contents) 
     */
    private static void delete(File file){
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

    /**
     * Static utililty that parse string from AMCM QR-Code Parsing API 
     * into Cheque object.
     * @param String passed from amcm qrcode parsing api. 
     *  e.g. '123;CHQ;MOP;Min Tech Company;001204932;00001924;'
     * @return Cheque parsed from blob string, if blob is empty/error, 
     *  then return an empty cheque.
     */
    public static Cheque parseCheque(String blob){
        
        Cheque cheque = Cheque.getEmptyCheque();

        // Validate not null or empty or nonreadable
        if (blob == null 
                || blob.compareTo(Cheque.EMPTY_STRING) == 0
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
     * Static Utility that export this object to csv according to AMCM-format
     *
     * @return String a AMCM-format csv line
     *   format: 'Bank Type Ccy ChqNum Amount Holder Account EnvelopeNum'
     * e.g.'"123","CHQ","MOP","00001924","500000","Min Tech Company","001204932",""'
     */
    public static String toCsv(Cheque chq){

        StringBuilder result = new StringBuilder();

        result.append("\"");
        result.append(chq.getBank());
        result.append("\"");
        result.append(",");
        result.append("\"");
        result.append(chq.getType());
        result.append("\"");
        result.append(",");
        result.append("\"");
        result.append(chq.getCcy());
        result.append("\"");
        result.append(",");
        result.append("\"");
        result.append(chq.getId());
        result.append("\"");
        result.append(",");
        result.append("\"");
        result.append(chq.getAmount());
        result.append("\"");
        result.append(",");
        result.append("\"");
        result.append(chq.getHolder());
        result.append("\"");
        result.append(",");
        result.append("\"");
        result.append(chq.getAccount());
        result.append("\"");
        result.append(",");
        result.append("\"");
        result.append(chq.getEnvelope());
        result.append("\"");

        return result.toString();

    }

}
