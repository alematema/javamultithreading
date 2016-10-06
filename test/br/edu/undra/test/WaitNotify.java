package br.edu.undra.test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexandre
 */
public class WaitNotify {

    public static void main(String[] args) throws InterruptedException {
        
        WaitNotify wn = new WaitNotify();
        
        System.out.println(Thread.currentThread().getName() + " WAITING TASK IN ANOTHER THREAD");
        
        wn.doTask(wn);
        
        synchronized(wn){
             wn.wait();
        }
       
        System.out.println(Thread.currentThread().getName() + " NOTIFIED : RESTART RUNNING ...");
        
    }

    public void doTask(WaitNotify wf) {

        new Thread( new MyRunnable(wf) ).start();

    }

    class MyRunnable implements Runnable {
    
        WaitNotify wn;
        public MyRunnable(WaitNotify wf) {
            this.wn = wf;
        }
        
        @Override
        public void run() {
            
            int sleep = 3000;
              
            System.out.print(Thread.currentThread().getName() + " RUNNING TASK - " + sleep/1_000F +" secs");
            
            try {
                Thread.currentThread().sleep(sleep);
            } catch (InterruptedException ex) {
                Logger.getLogger(WaitNotify.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println(" - DONE");
            
            synchronized(this.wn){
                this.wn.notify();
            }
        }

    }

}
