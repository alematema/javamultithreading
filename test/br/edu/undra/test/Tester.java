package br.edu.undra.test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexandre
 */
public final class Tester {
    
   
    public static void main(String[] args)  {// Nao estou usando JUnit propositalmente
        
        try {
            
            MessageEncoderTester.main(args);
            MessageDecoderTester.main(args);
            
        } catch (InterruptedException | IOException ex) {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}