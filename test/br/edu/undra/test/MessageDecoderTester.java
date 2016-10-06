package br.edu.undra.test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import br.edu.undra.app.util.Messages;
import br.edu.undra.model.MessageDecoder;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexandre
 */
public class MessageDecoderTester {

    private final MessageDecoder decoder;

    public MessageDecoderTester(MessageDecoder decoder) {
        this.decoder = decoder;
    }

    public boolean run(Map<String, String> givenExpectedValuesMap) {

        boolean passed;
        boolean passedAll;
        passedAll = true;

        for (String givenValue : givenExpectedValuesMap.keySet()) {
            passed = decoder.decode(givenValue).contains(givenExpectedValuesMap.get(givenValue));
            if(passed){}else Logger.getLogger(MessageDecoderTester.class.toString()).log(Level.SEVERE, "FAILED : input={0} : decoderOutput={1} : expectedDecoderOutput={2}", new Object[]{givenValue, decoder.decode(givenValue), givenExpectedValuesMap.get(givenValue)});
            passedAll = passedAll & passed;
        }

        return passedAll;

    }

    public boolean testUpperToLower(String anUpperToLowerCase, MessageDecoder decoder, String result) {

        boolean passed;

        passed = decoder.decode(anUpperToLowerCase).contains(result);

        if (passed) {
            Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.INFO, "PASSED");
        } else {
            Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.SEVERE, "FAILED");
        }

        return passed;
    }

    public boolean testUpperToPunctuation(String anUpperToPunctuationCase, MessageDecoder decoder, String result) {

        boolean passed;

        passed = decoder.decode(anUpperToPunctuationCase).contains(result);

        if (passed) {
            Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.INFO, "PASSED");
        } else {
            Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.SEVERE, "FAILED");
        }

        return passed;
    }

    public boolean testLowerToPunctuation(String anLowerToPunctuationCase, MessageDecoder decoder, String result) {

        boolean passed;

        passed = decoder.decode(anLowerToPunctuationCase).contains(result);

        if (passed) {
            Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.INFO, "PASSED");
        } else {
            Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.SEVERE, "FAILED");
        }

        return passed;
    }

    public boolean testLowerToUpper(String anLowerToUpperCase, MessageDecoder decoder, String result) {

        boolean passed;

        passed = decoder.decode(anLowerToUpperCase).contains(result);

        if (passed) {
            Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.INFO, "PASSED");
        } else {
            Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.SEVERE, "FAILED");
        }

        return passed;
    }

    public boolean testPunctuationToUpper(String anPunctuationToUpperCase, MessageDecoder decoder, String result) {

        boolean passed;

        passed = decoder.decode(anPunctuationToUpperCase).contains(result);

        if (passed) {
            Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.INFO, "PASSED");
        } else {
            Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.SEVERE, "FAILED");
        }

        return passed;
    }

    public boolean testMixed(String anMixed, MessageDecoder decoder, String result) {

        boolean passed;

        passed = decoder.decode(anMixed).contains(result);

        if (passed) {
            Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.INFO, "PASSED");
        } else {
            Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.SEVERE, "FAILED");
        }

        return passed;
    }

    public static void main(String[] args) throws InterruptedException, IOException {

        Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.INFO,Messages.RUNNING_TESTS);
        
        Map<String, String> tests = new HashMap<>();
        tests.put("1,0,2", "Ab");
        tests.put("1,0,0,1,1,1,1,", "A!!!!");
        tests.put("1,0,1,2,0,1,1,1,1,", "Aab!!!!");
        tests.put("1,0,0,2,3,", "aBC");
        tests.put("1,0,0,2,0,3,0,0,4,", "aBcD");
        tests.put("1,1,1,0,1,", "!!!A");
        tests.put("1,5,5,0,1,", "!  A");
        tests.put("1,0,0,5,0,1,", "A A");
        tests.put("1,0,0,5,0,1,0,0,3,1,3,0,0,1,", "A A,!,a");
        
        MessageDecoderTester tester = new MessageDecoderTester(new MessageDecoder());
        
        boolean passed = tester.run(tests);
        
        String hasPassed;
        if (passed) {
            hasPassed = "[OK] : TODOS TESTES PASSARAM.";
        } else {
            hasPassed = "[FALHOU] : UM OU MAIS TESTES NAO PASSOU.";
        }
        
        Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.INFO, "{0}  {1}", new Object[]{Messages.FINISHED_RUNNING_TESTS, hasPassed+"\n"});

    }
}
