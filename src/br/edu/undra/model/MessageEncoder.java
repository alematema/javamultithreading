package br.edu.undra.model;

import br.edu.undra.model.exceptions.LowerCaseEncoderException;
import br.edu.undra.model.exceptions.PunctuationEncoderException;
import br.edu.undra.model.exceptions.UpperCaseDecoderException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MessageEncoder {

    private final int longTaskDurationSimulationTime = 150;// simula quanto tempo em milis cada linha/task numa thread durará.
    private int longTaskDuration = 999;
    private OutputStreamWriter out;
    private final Map<Character, Integer> upperCaseEncoderMap;
    private final Map<Character, Integer> lowerCaseEncoderMap;
    private final Map<Character, Integer> punctuationEncoderMap;
    
    public MessageEncoder() {

        this.longTaskDurationSimulationTime = 150;// simula quanto tempo em milis cada linha/task numa thread durará.

        
        upperCaseEncoderMap = new HashMap<>();
        lowerCaseEncoderMap = new HashMap<>();
        punctuationEncoderMap = new HashMap<>();

        for (char i = 'A'; i <= 'Z'; i++) {// popula o mapa com maiusculas A ...
            // Z
            upperCaseEncoderMap.put(i, (i - 'A' + 1));
        }

        for (char i = 'a'; i <= 'z'; i++) {// popula o mapa com minusculas a ...
            // z
            lowerCaseEncoderMap.put(i, (i - 'a' + 1));
        }
        // popula o mapa com sinais de pontuacao
        punctuationEncoderMap.put('!', 1);
        punctuationEncoderMap.put('?', 2);
        punctuationEncoderMap.put(',', 3);
        punctuationEncoderMap.put('.', 4);
        punctuationEncoderMap.put(' ', 5);
        punctuationEncoderMap.put(';', 6);
        punctuationEncoderMap.put('"', 7);
        punctuationEncoderMap.put('\'', 8);

        // this.current.forward(this.current.forward(current));
    }

    public void encode(InputStream is, OutputStreamWriter out) {

        List<String> tokens = new ArrayList<>();
        try (Scanner scanner = new Scanner(is)) {
            tokens.add(scanner.nextLine());
            while (scanner.hasNextLine()) {
                tokens.add(scanner.nextLine());
            }
        }

        setOut(out);

        int line = 0;
        long init = System.currentTimeMillis();

        for (String message : tokens) {
            String encondeMessage = this.encode(message);
            line++;
            long tookUntilNow = System.currentTimeMillis() - init;
            String remains = Long.toString(getRemainingTime(tokens, line, tookUntilNow) / 1000);
            tookUntilNow /= 1000;
            //System.out.println(tookUntilNow + " secs [remains " + remains + "secs] : " + "[" + (100 * ((float) line / tokens.size())) + "%] : Ln" + line + " of " + tokens.size() + " : " + Thread.currentThread().getName() + " encoding " + message + " to " + encondeMessage);
            System.out.println(tookUntilNow + " secs [faltam " + remains + "secs] : " + "[" + (100 * ((float) line / tokens.size())) + "%] : Ln" + line + " of " + tokens.size() + " : " + Thread.currentThread().getName() + " codificando " + message + " para " + encondeMessage);
        }
    }

    private long getRemainingTime(List<String> tokens, int line, long tookUntilNow) {
        long totalTimeForComplete = tokens.size() * tookUntilNow / line;
        return totalTimeForComplete - tookUntilNow;
    }

    public String readMessage(InputStream is) throws IOException {

        StringBuilder sb = new StringBuilder();
        int input = is.read();// reads the byte stream, with an enter at its

        while (input != 10) {// while not End Of Line,eg, an ENTER char at the
            // // end of the line
            sb.append(((char) input));// interprets input bytes as chars
            input = is.read();// moves to forward byte in the stream
        }

        return sb.toString();
        //return new Scanner(is).nextLine();//linha equivalente ao codigo acima
    }

    // 6:20
    public String encode(String message) {

        String[] tokens = message.split("");
        StringBuilder encodedMessage = encode(tokens);//isso aqui eh muito rápido, e demanda 

        try {// Simula uma tarefa longa. Duracao da tarefa definida por longTaskDurationSimulationTime, em milisegundos
            Thread.sleep(longTaskDurationSimulationTime);
        } catch (InterruptedException ex) {
            Logger.getLogger(MessageEncoder.class.getName()).log(Level.SEVERE, null, ex);
        }// fim simula

//        try {// Sincroniza escrita no OutputStream
//            synchronized (getOut()) {
//                getOut().write(encodedMessage.toString() + "\n");
//                getOut().flush();
//            }
//
//        } catch (IOException ex) {
//            Logger.getLogger(MessageEncoder.class.getName()).log(Level.SEVERE, null, ex);
//        }// fim sincroniza

        return encodedMessage.toString();// retorna string codificada 
    }

    private StringBuilder encode(String[] tokens) {

        Mode.current = Mode.get(tokens[0], upperCaseEncoderMap, lowerCaseEncoderMap, punctuationEncoderMap);
        StringBuilder encodedMessage = new StringBuilder();
        int navigator = 0;

        while (navigator < tokens.length) {
            char token = tokens[navigator].toCharArray()[0];
            try {
                encodedMessage.append(this.encode(tokens[navigator], Mode.current));
                encodedMessage.append(",");
                navigator++;
            } catch (UpperCaseDecoderException e) {
                appendChangedModeChar(encodedMessage);
                if (mustChangeMode(token, punctuationEncoderMap)) {
                    appendChangedModeChar(encodedMessage);
                }
            } catch (LowerCaseEncoderException e) {
                appendChangedModeChar(encodedMessage);
                if (mustChangeMode(token, upperCaseEncoderMap)) {
                    appendChangedModeChar(encodedMessage);
                }
            } catch (PunctuationEncoderException e) {
                appendChangedModeChar(encodedMessage);
                if (mustChangeMode(token, lowerCaseEncoderMap)) {
                    appendChangedModeChar(encodedMessage);
                }
            }
        }

        return encodedMessage;
    }

    private boolean mustChangeMode(char token, Map<Character, Integer> encoderMap) {
        return encoderMap.get(token) != null;
    }

    private void appendChangedModeChar(StringBuilder encodedMessage) {
        encodedMessage.append("0");
        encodedMessage.append(",");
        Mode.forward();
    }

    private int encode(String toEncode, Mode mode)
            throws UpperCaseDecoderException, LowerCaseEncoderException, PunctuationEncoderException {

        int encodedInt = 0;

        char token = toEncode.toCharArray()[0];

        if (mode.equals(Mode.UPPERCASE)) {
            try {
                encodedInt = upperCaseEncoderMap.get(token);
            } catch (Exception e) {
                throw new UpperCaseDecoderException();
            }
        }

        if (mode.equals(Mode.LOWERCASE)) {
            try {
                encodedInt = lowerCaseEncoderMap.get(token);
            } catch (Exception e) {
                throw new LowerCaseEncoderException();
            }
        }

        if (mode.equals(Mode.PUNCTUATION)) {
            try {
                encodedInt = punctuationEncoderMap.get(token);
            } catch (Exception e) {
                throw new PunctuationEncoderException();
            }
        }

        return encodedInt;
    }

    public boolean isLongTankRunning() {
        return this.longTaskDurationSimulationTime >= longTaskDuration;
    }

    public void setOut(OutputStreamWriter out) {
        this.out = out;
    }

    public OutputStreamWriter getOut() {
        return out;
    }

    public int getLongTaskDurationSimulationTime() {
        return longTaskDurationSimulationTime;
    }

    public void setLongTaskDuration(int longTaskDuration) {
        this.longTaskDuration = longTaskDuration;
    }

    public int getLongTaskDuration() {
        return longTaskDuration;
    }
    public static void main(String[] args) {
        MessageEncoder encoder = new MessageEncoder();
        System.out.println(encoder.encode("ulia temhjkhjkhklhlgjkljgl "
                + "dfgd hjkhjk"
                + "UIUIUIUIU!!!!!mkklkjhkj MM bananas na cesta"));
    }
}
