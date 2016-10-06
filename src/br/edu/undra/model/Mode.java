/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.undra.model;

import java.util.Map;

/**
 *
 * @author alexandre
 */
public enum Mode {

    UPPERCASE, LOWERCASE, PUNCTUATION;

    public static Mode current = Mode.UPPERCASE;

    static public Mode forward() {

        Mode next = current;
        //System.out.print("Going from " + current);

        switch (current) {

            case UPPERCASE:
                next = Mode.LOWERCASE;
                break;
            case LOWERCASE:
                next = Mode.PUNCTUATION;
                break;
            case PUNCTUATION:
                next = Mode.UPPERCASE;
                break;
        }

        //System.out.println(" to " + forward);
        current = next;

        return next;

    }

    static public Mode rewind() {

        Mode preview = current;
        //System.out.print("Going from " + current);

        switch (current) {

            case UPPERCASE:
                preview = Mode.PUNCTUATION;
                break;
            case LOWERCASE:
                preview = Mode.UPPERCASE;
                break;
            case PUNCTUATION:
                preview = Mode.LOWERCASE;
                break;
        }

        //System.out.println(" to " + forward);
        current = preview;

        return preview;

    }

    static Mode getEncoded(String token, Map<Integer, Character> upper, Map<Integer, Character> lower, Map<Integer, Character> punctuation) {

        Mode mode_ = Mode.UPPERCASE;

        int tokenKey = Integer.parseInt(token);

        if (upper.containsKey(tokenKey)) {
            mode_ = Mode.UPPERCASE;
        }
        if (lower.containsKey(tokenKey)) {
            mode_ = Mode.LOWERCASE;
        }
        if (punctuation.containsKey(tokenKey)) {
            mode_ = Mode.PUNCTUATION;
        }

        return mode_;

    }

    @Override
    public String toString() {

        String mode_ = "";

        switch (this) {

            case UPPERCASE:
                mode_ = "UPPERCASE MODE";
                break;
            case LOWERCASE:
                mode_ = "LOWERCASE MODE";
                break;
            case PUNCTUATION:
                mode_ = "PUNCTUATION MODE";
                break;
        }

        return mode_;
    }

    ;

	static public Mode get(String s, Map<Character, Integer> upper, Map<Character, Integer> lower,
            Map<Character, Integer> punctuation) {

        Mode mode_ = Mode.UPPERCASE;

        char token = s.toCharArray()[0];

        if (upper.containsKey(token)) {
            mode_ = Mode.UPPERCASE;
        }

        if (lower.containsKey(token)) {
            mode_ = Mode.LOWERCASE;
        }
        if (punctuation.containsKey(token)) {
            mode_ = Mode.PUNCTUATION;
        }

        return mode_;
    }

    public static void main(String args[]) {

        Mode.current = Mode.UPPERCASE;
        System.out.print("CURRENT : " + Mode.current);
        System.out.println(" --> NEXT : " + Mode.forward());
        Mode.rewind();
        Mode.forward();
        if (Mode.current.equals(Mode.LOWERCASE)) {
            System.out.println(Mode.current + " TEST PASSED");
        } else {
            System.err.println(Mode.current + " TEST FAILED");
        };
        System.out.print("CURRENT : " + Mode.current);
        System.out.println(" --> NEXT : " + Mode.forward());
        Mode.forward();
        Mode.rewind();
  
        if (Mode.current.equals(Mode.PUNCTUATION)) {
            System.out.println(Mode.current + " TEST PASSED");
        } else {
            System.err.println(Mode.current + " TEST FAILED");
        };
        System.out.print("CURRENT : " + Mode.current);
        System.out.println(" --> NEXT : " + Mode.forward());
        Mode.forward();
        Mode.rewind();
    
        if (Mode.current.equals(Mode.UPPERCASE)) {
            System.out.println(Mode.current + " TEST PASSED");
        } else {
            System.err.println(Mode.current + " TEST FAILED");
        };
    }

}
