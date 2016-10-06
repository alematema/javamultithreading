package br.edu.undra.test;

import br.edu.undra.app.util.CodecUtilities;
import br.edu.undra.model.MessageEncoder;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

public class MessageEncoderTester {

    private MessageEncoder encoder;

    public MessageEncoderTester() {
    }

    public MessageEncoderTester(MessageEncoder encoder) {
        this.encoder = encoder;
    }

    public boolean test(Map<String, String> givenExpectedMap) {

        boolean passed = true;

        for (String givenValue : givenExpectedMap.keySet()) {
            passed = passed & encoder.encode(givenValue).equals(givenExpectedMap.get(givenValue));
        }

        return passed;

    }

    public boolean testeUpperToLower(String anUpperToLowerCase, MessageEncoder encoder, String result) {

        boolean passed;
        passed = result.equals(encoder.encode(anUpperToLowerCase));

        if (passed) {
            Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.INFO, "PASSED :: " + result + " :: " + " was encoded by " + anUpperToLowerCase);

        } else {
            Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.SEVERE, "FAILED");
        }

        return passed;
    }

    public boolean testeUpperToPunctuation(String anUpperToPunctuationCase, MessageEncoder encoder, String result) {

        boolean passed;

        passed = result.equals(encoder.encode(anUpperToPunctuationCase));

        if (passed) {
            Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.INFO, "PASSED :: " + result + " :: " + " was encoded by " + anUpperToPunctuationCase);
        } else {
            Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.SEVERE, "FAILED");
        }

        return passed;
    }

    public boolean testeLowerToPunctuation(String anLowerToPunctuationCase, MessageEncoder encoder, String result) {

        boolean passed;

        passed = result.equals(encoder.encode(anLowerToPunctuationCase));

        if (passed) {
            Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.INFO, "PASSED :: " + result + " :: " + " was encoded by " + anLowerToPunctuationCase);
        } else {
            Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.SEVERE, "FAILED");
        }

        return passed;
    }

    public boolean testeLowerToUpper(String anLowerToUpperCase, MessageEncoder encoder, String result) {

        boolean passed;

        passed = result.equals(encoder.encode(anLowerToUpperCase));

        if (passed) {
            Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.INFO, "PASSED :: " + result + " :: " + " was encoded by " + anLowerToUpperCase);
        } else {
            Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.SEVERE, "FAILED");
        }

        return passed;
    }

    public boolean testePunctuationToUpper(String anPunctuationToUpperCase, MessageEncoder encoder, String result) {

        boolean passed;

        passed = result.equals(encoder.encode(anPunctuationToUpperCase));

        if (passed) {
            Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.INFO, "PASSED");
        } else {
            Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.SEVERE, "FAILED");
        }

        return passed;
    }

    public boolean testePunctuationToLower(String anPunctuationToLowerCase, MessageEncoder encoder, String result) {

        boolean passed;

        passed = result.equals(encoder.encode(anPunctuationToLowerCase));

        if (passed) {
            Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.INFO, "PASSED :: " + result + " :: " + " was encoded by " + anPunctuationToLowerCase);
        } else {
            Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.SEVERE, "FAILED");
        }

        return passed;
    }

    public boolean testeMixed(String anMixed, MessageEncoder encoder, String result) {

        boolean passed;

        passed = result.equals(encoder.encode(anMixed));

        if (passed) {
            Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.INFO, "PASSED :: " + result + " :: " + " was encoded by " + anMixed);
        } else {
            Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.SEVERE, "FAILED");
        }

        return passed;
    }

    public static void main(String[] args) throws FileNotFoundException, InterruptedException, IOException {

        Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.INFO,CodecUtilities.MESSAGES.RUNNING_TESTS );

        MessageEncoder encoder = new MessageEncoder();
        OutputStreamWriter out = new FileWriter("textEncodedWEasy.txt");
        encoder.setOut(out);
        MessageEncoderTester tester = new MessageEncoderTester();

        boolean passed;

        passed = tester.testeUpperToLower("Abcde", encoder, "1,0,2,3,4,5,");
        passed = passed & tester.testeUpperToPunctuation("A!!!!", encoder, "1,0,0,1,1,1,1,");
        passed = passed & tester.testeLowerToPunctuation("Aab!!!!", encoder, "1,0,1,2,0,1,1,1,1,");
        passed = passed & tester.testeLowerToUpper("aBC", encoder, "1,0,0,2,3,");
        passed = passed & tester.testeLowerToUpper("aBcD", encoder, "1,0,0,2,0,3,0,0,4,");
        passed = passed & tester.testePunctuationToUpper("!!!A", encoder, "1,1,1,0,1,");
        passed = passed & tester.testePunctuationToUpper("!  A", encoder, "1,5,5,0,1,");
        passed = passed & tester.testePunctuationToUpper("!  A?", encoder, "1,5,5,0,1,0,0,2,");
        passed = passed & tester.testePunctuationToLower("!  ab", encoder, "1,5,5,0,0,1,2,");
        passed = passed & tester.testeMixed("A A", encoder, "1,0,0,5,0,1,");
        passed = passed & tester.testeMixed("A A,!,a", encoder, "1,0,0,5,0,1,0,0,3,1,3,0,0,1,");

        String hasPassed;
        if (passed) {
            hasPassed = "[OK] : TODOS TESTES PASSARAM.";
        } else {
            hasPassed = "[FALHOU] : UM OU MAIS TESTES NAO PASSOU.";
        }

        Logger.getLogger(MessageEncoderTester.class.toString()).log(Level.INFO, "{0}  {1}", new Object[]{CodecUtilities.MESSAGES.FINISHED_RUNNING_TESTS, hasPassed+"\n"});

    }

}
//CONSTRUÍDO COM SUCESSO (tempo total: 5 minutos 51 segundos) # 1.015.500 linhas !!! # 1 mil Threads (1000)
//CONSTRUÍDO COM SUCESSO (tempo total: 5 minutos 54 segundos) # 1.015.500 linhas !!! # 850 Threads(1500)
//CONSTRUÍDO COM SUCESSO (tempo total: 6 minutos 11 segundos) # 1.015.500 linhas !!! # 8 mil Threads()
//CONSTRUÇÃO      PARADA (tempo total: 1 h 1min 56 segundos ) COM SLICING # 1.015.500 linhas !!! # 500 Threads()
//CONSTRUÍDO COM SUCESSO (tempo total: 6 minutos 46 segundos) SEM SLICING # 1.015.500 linhas !!! # 500 Threads()
