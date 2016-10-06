/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.undra.app;

import java.io.IOException;

/**
 *
 * @author alexandre
 */
public class EncoderDecoderMvcDriver {

    /**
     *
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {

        long initTime = System.currentTimeMillis();
        
        MultiThreadedEncoderDecoderMVC app = new MultiThreadedEncoderDecoderMVC();

        app.start();
              
        while (app.isWorking()){};//do nothing while threads are running 

        app.shuttDown(initTime);

    }

}
