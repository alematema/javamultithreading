/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.undra.test;

import br.edu.undra.model.Mode;

/**
 *
 * @author alexandre
 */
public class ModeTester {

    public Mode testNext(Mode current, Mode next) {

        System.out.print("CURRENT : " + current);
        System.out.println(" --> NEXT : " + next);

        current.forward();

        if (Mode.current.equals(next)) {
            System.out.println(Mode.current + " TEST PASSED");
        } else {
            System.err.println(Mode.current + " TEST FAILED");
        };

        System.out.println("------------------------ >>> CURRENT : " + current);

        return next;

    }

    private Mode testPrevious(Mode current, Mode previous) {
        
        
        
        System.out.print("CURRENT : " + current);
        System.out.println(" --> PREVIOUS : " + previous);

        current.rewind();

        if (Mode.current.equals(previous)) {
            System.out.println(Mode.current + " TEST PASSED");
        } else {
            System.err.println(Mode.current + " TEST FAILED");
        };

        System.out.println("------------------------ >>> CURRENT : " + current);

        return previous;
    }

    public static void main(String[] args) {

        ModeTester tester = new ModeTester();
        Mode current = Mode.UPPERCASE;
        Mode next = Mode.LOWERCASE;

        tester.testNext(current, next);
        current = next;
        next = Mode.PUNCTUATION;
        tester.testNext(current, next);
        current = next;
        next = Mode.UPPERCASE;
        tester.testNext(current, next);
        current = next;
        Mode previous = Mode.PUNCTUATION;
        tester.testPrevious(current, previous);

//        for(int i=0;i<Mode.values().length;i++){
//            next =  tester.testNext(current, next);
//        }
    }

}
