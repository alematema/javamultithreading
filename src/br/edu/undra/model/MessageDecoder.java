package br.edu.undra.model;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MessageDecoder {

    private final Mode mode = Mode.UPPERCASE;

    private final Map<Integer, Character> upperCaseDecoderMap;
    private final Map<Integer, Character> lowerCaseDecoderMap;
    private final Map<Integer, Character> punctuationDecoderMap;
    private int upperCaseModule = 0;
    private int punctuationCaseModule = 0;

    public MessageDecoder() {

        upperCaseDecoderMap = new HashMap<>();
        lowerCaseDecoderMap = new HashMap<>();
        punctuationDecoderMap = new HashMap<>();

        for (char i = 'A'; i <= 'Z'; i++) {// popula o mapa com maiusculas A ... Z
            upperCaseDecoderMap.put((i - 'A' + 1), i);
        }

        for (char i = 'a'; i <= 'z'; i++) {// popula o mapa com minusculas a ... z
            lowerCaseDecoderMap.put((i - 'a' + 1), i);
        }

        // popula o mapa com sinais de pontuacao
        punctuationDecoderMap.put(1, '!');
        punctuationDecoderMap.put(2, '?');
        punctuationDecoderMap.put(3, ',');
        punctuationDecoderMap.put(4, '.');
        punctuationDecoderMap.put(5, ' ');
        punctuationDecoderMap.put(6, ';');
        punctuationDecoderMap.put(7, '"');
        punctuationDecoderMap.put(8, '\'');

        upperCaseModule = upperCaseDecoderMap.size();
        punctuationCaseModule = punctuationDecoderMap.size();

        // this.rollMode(this.rollMode(this.rollMode(Mode.UPPERCASE)));
    }

    public String readMessage(InputStream is) throws IOException {

        StringBuilder sb = new StringBuilder();
        int input = is.read();//reads the byte stream, with an enter at its end

        while (input != 10) {// while not End Of Line,eg, an ENTER char at the end of the line
            sb.append(((char) input));//interprets input bytes as chars
            input = is.read();//moves to forward byte in the stream
        }

        return sb.toString();

        //return new Scanner(is).nextLine();//linha equivalente ao codigo acima
    }

    public List<String> decode(String encodedMessage) {

        List<String> decodeds = new ArrayList<>();

        for (Mode m : Mode.values()) {

            StringBuilder decodedMessage = new StringBuilder();
            String[] tokens = encodedMessage.split(",");

            Mode.current = m;

            for (String token : tokens) {

                int code = this.parseInteger(token);

                try {
                    decodedMessage.append(this.decode(code, Mode.current));
                } catch (Exception e) {
                    Mode.forward();
                }
            }

            decodeds.add(decodedMessage.toString());
        }

        return decodeds;
    }

    private int parseInteger(String chars) {
        return Integer.parseInt(chars);
    }

    private Character decode(int number, Mode mode) throws Exception {

        try {

            char decoded = ' ';

            if (mode.equals(Mode.PUNCTUATION)) {
                number = number % punctuationCaseModule;
                decoded = this.punctuationDecoderMap.get(number);
            }

            number = number % upperCaseModule;
            if (mode.equals(Mode.UPPERCASE)) {
                decoded = this.upperCaseDecoderMap.get(number);
            }
            if (mode.equals(Mode.LOWERCASE)) {
                decoded = this.lowerCaseDecoderMap.get(number);
            }

            return decoded;

        } catch (Exception e) {
            throw new Exception(e);
        }

    }

    public static void main(String[] args) throws IOException {

        MessageDecoder decoder = new MessageDecoder();
        String message = decoder.readMessage(System.in);
        System.out.println(message);
        System.out.println(decoder.decode(message));
        // 18,12312,171,763,98423,1208,216,11,500,18,241,0,32,20620,27,10
        // 18,12312,171,763,98423,1208,216,11,500,18,241,0,32,20620,27,10,27,18,12312,171,763,98423,1208,216,11,500,18,241,0,32,20620,27,10,27,18,12312,171,763,98423,1208,216,11,500,18,241,0,32,20620,27,10,27,18,12312,171,763,98423,1208,216,11,500,18,241,0,32,20620,27,10
//        try ( // 36,24624,342,1526,196846,2416,432,1000,36
//            OutputStreamWriter out = new FileWriter("decoded.txt")) {
//            Scanner sc = new Scanner(new FileInputStream("encodedText.txt"));
//            String encoded = sc.nextLine();
//            List<String> decodeds = decoder.decode(encoded);
//            for (String decoded : decodeds) {
//                
//                out.write(decoded);
//                out.flush();
//                
//                
//            }
//
//            while (sc.hasNextLine()) {
//                //System.out.println("br.edu.undra.model.MessageDecoder.main()" + encoded);
//                encoded = sc.nextLine();
//                decoded = decoder.decode(encoded);
//                out.write(decoded + "\n");
//                out.flush();
//            }
//        }

//        OutputStreamWriter[] outs = new OutputStreamWriter[3];
//        OutputStreamWriter out = new FileWriter("decoded0.txt");
//        outs[0] = out;
//        out = new FileWriter("decoded1.txt");
//        outs[1] = out;
//        out = new FileWriter("decoded2.txt");
//        outs[2] = out;
//
//        Scanner sc = new Scanner(new FileInputStream("encodedText.txt"));
//        String encoded = sc.nextLine();
//        List<String> decodeds = decoder.decode(encoded);
//        for (int i = 0; i < 3; i++) {
//            outs[i].write(decodeds.get(i) + "\n");
//            outs[i].flush();
//        }
//
//        while (sc.hasNextLine()) {
//            encoded = sc.nextLine();
//            decodeds = decoder.decode(encoded);
//            for (int i = 0; i < 3; i++) {
//                outs[i].write(decodeds.get(i) + "\n");
//                outs[i].flush();
//            }
//        }
//        System.out.println("FIM " + "br.edu.undra.model.MessageDecoder.main()");
    }
}
