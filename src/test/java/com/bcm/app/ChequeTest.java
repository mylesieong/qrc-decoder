package com.bcm.app;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 */
public class ChequeTest extends TestCase{

    private Cheque tester;
    private Cheque testerShort;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ChequeTest( String testName ){
        super( testName );

        //Init the tester Cheque object
        tester = Cheque.getEmptyCheque();
        tester.setBank(103);
        tester.setType("CHQ");
        tester.setCcy("MOP");
        tester.setHolder("Mr Chan");
        tester.setAccount("ACC00102");
        tester.setId("ID990002");

        //Init the testerShort Cheque object
        testerShort = Cheque.getEmptyCheque();
        testerShort.setBank(103);
        testerShort.setType("CHQ");
        testerShort.setHolder("Mr Chan");

    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite( ChequeTest.class );
    }

    /**
     * 
     */
    public void testParseNormalCase(){
        String blob = "103;CHQ;MOP;Mr Chan;ACC00102;ID990002";
        Cheque testee = Cheques.parseCheque(blob);
        assertTrue(testee.equals(tester));
    }

    /**
     * 
     */
    public void testParseNormalCaseExtraColon(){
        String blob = "103;CHQ;MOP;Mr Chan;ACC00102;ID990002;";
        Cheque testee = Cheques.parseCheque(blob);
        assertTrue(testee.equals(tester));
    }

    /**
     * 
     */
    public void testParseMalDataType(){
        String blob = "BoC;CHQ;MOP;Mr Chan;ACC00102;ID990002";
        Cheque testee = Cheques.parseCheque(blob);
        assertTrue(testee.isEmpty());
    }

    /**
     * 
     */
    public void testParseExtraParts(){
        String blob = "103;CHQ;MOP;Mr Chan;ACC00102;ID990002;ABCD;EEEE;TT;END";
        Cheque testee = Cheques.parseCheque(blob);
        assertTrue(testee.equals(tester));
    }

    /**
     * 
     */
    public void testParseLackOfParts5Colons(){
        String blob = "103;CHQ;;Mr Chan;;";
        Cheque testee = Cheques.parseCheque(blob);
        assertTrue(testee.equals(testerShort));
    }

    /**
     * 
     */
    public void testParseLackOfParts2Colons(){
        String blob = "103;CHQ;;Mr Chan";
        Cheque testee = Cheques.parseCheque(blob);
        assertTrue(testee.equals(testerShort));
    }

    /**
     * 
     */
    public void testParseNullInput(){
        Cheque testee = Cheques.parseCheque(null);
        assertTrue(testee.isEmpty());
    }

    /**
     * 
     */
    public void testEqualsSame(){
        Cheque testee = Cheque.getEmptyCheque();
        testee.setBank(tester.getBank());
        testee.setType(tester.getType());
        testee.setCcy(tester.getCcy());
        testee.setHolder(tester.getHolder());
        testee.setAccount(tester.getAccount());
        testee.setId(tester.getId());
        assertTrue(testee.equals(tester));
    }

    /**
     * 
     */
    public void testEqualsDifferentInNumber(){
        Cheque testee = Cheque.getEmptyCheque();
        testee.setBank(tester.getBank() + 1);
        testee.setType(tester.getType());
        testee.setCcy(tester.getCcy());
        testee.setHolder(tester.getHolder());
        testee.setAccount(tester.getAccount());
        testee.setId(tester.getId());
        assertTrue(!testee.equals(tester));
    }

    /**
     * 
     */
    public void testEqualsDifferentInString(){
        Cheque testee = Cheque.getEmptyCheque();
        testee.setBank(tester.getBank());
        testee.setType(tester.getType());
        testee.setCcy(tester.getCcy());
        testee.setHolder("HolderStub");
        testee.setAccount(tester.getAccount());
        testee.setId(tester.getId());
        assertTrue(!testee.equals(tester));
    }

}

