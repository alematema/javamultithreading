/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.undra.model;

import br.edu.undra.model.runnables.MessageEncoderRunnable;

/**
 *
 * @author alexandre
 */
public class Task {

    private final Thread thread;
    private final MessageEncoderRunnable runnable;

    public Task(MessageEncoderRunnable runnable) {
        this.runnable = runnable;
        this.thread = new Thread(runnable);
    }

    public Thread getThread() {
        return thread;
    }

    public MessageEncoderRunnable getRunnable() {
        return runnable;
    }

    public void start(){
        this.thread.start();
    }
}
