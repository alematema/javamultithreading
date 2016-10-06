/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.undra.app;

import br.edu.undra.view.EncoderDecoderMVCFrame;
import br.edu.undra.view.EncoderDecoderPanel;
import java.io.IOException;

/**
 *
 * @author alexandre
 */
public class MultiThreadedEncoderDecoderMVC {

    EncoderDecoderMVCFrame frame;

    public MultiThreadedEncoderDecoderMVC() {
        frame = new EncoderDecoderMVCFrame(new EncoderDecoderPanel());
    }

    public void start() throws IOException, InterruptedException {
        frame.initShowAndPreapare();
    }

    public boolean isWorking() throws InterruptedException {
        
        boolean isWorking = frame.hasAnyRunningTask();

        Thread.sleep(5);//melhorar aqui, usando wait/notify ou futures
        
        if (isAboutToEnd()) {
            logIsAboutToEnd();
        }
        
        return isWorking;
    }

    public boolean isAboutToEnd() {
        return frame.isAboutToEnd();
    }

    public void logIsAboutToEnd() {
        frame.logIsAboutToEnd();
    }

    public void shuttDown(long initTime) throws InterruptedException, IOException {
        frame.close(initTime);
    }

}
