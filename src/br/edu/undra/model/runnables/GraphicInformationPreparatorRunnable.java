/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.undra.model.runnables;

import br.edu.undra.app.util.CodecUtilities;
import br.edu.undra.model.Task;
import br.edu.undra.view.EncoderDecoderMVCFrame;
import br.edu.undra.view.ProgressLine;
import java.awt.BorderLayout;
import javax.swing.JPanel;

/**
 *
 * @author alexandre
 */
public class GraphicInformationPreparatorRunnable implements Runnable {

    private final EncoderDecoderMVCFrame mVCFrame;
    private final Task[] tasks;
    private final JPanel panel;
    private final ProgressLine[] progressLines;

    public GraphicInformationPreparatorRunnable(EncoderDecoderMVCFrame mVCFrame, Task[] tasks, JPanel panel, ProgressLine[] progressLines) {
        this.mVCFrame = mVCFrame;
        this.tasks = tasks;
        this.panel = panel;
        this.progressLines = progressLines;
    }

    @Override
    public void run() {
        
        for (int index = 0; index < tasks.length; index++) {
            
            mVCFrame.setSize(660, (int) Math.min(680, (index/(float)tasks.length)*680));
            //if(index%15==0)mVCFrame.setTitle("ENCODER/DECODER : BUILDING GUI : " + String.format("%.2f", Math.min(99,((float) index / (float) tasks.length) * 500)) + " %");
            if(index%25==0) mVCFrame.setTitle(CodecUtilities.getGraphicInfoTitle(index/(float)tasks.length*100));

            if (tasks[index] == null) {}
            else {
                // do some MVC bindings...
                ProgressLine progressLine = new ProgressLine(index,  mVCFrame);//The actual View MVC component
                tasks[index].getRunnable().doViewBinding(progressLine);
                progressLine.setTask(tasks[index]);//binds the progress line view to its model

                panel.add(progressLine, BorderLayout.CENTER);//adds to the main panel this progress line
                progressLines[index] = progressLine;

            }
        }
        
        synchronized(mVCFrame){
            mVCFrame.notify();
        }
        
        mVCFrame.setSize(660, 680);

    }

}
