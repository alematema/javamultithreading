/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.undra.model.runnables;

import br.edu.undra.app.util.CodecUtilities;
import br.edu.undra.model.Task;
import br.edu.undra.view.EncoderDecoderMVCFrame;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexandre
 */
public class RandomLineStarterRunnable implements Runnable {

    private final EncoderDecoderMVCFrame mVCFrame;
    private final Task[] tasks;

    public RandomLineStarterRunnable(EncoderDecoderMVCFrame mVCFrame, Task[] tasks) {
        this.mVCFrame = mVCFrame;
        this.tasks = tasks;
    }

    @Override
    public void run() {

        List<Integer> startedThreadIndexList = new ArrayList<>();
        
        tasks[0].start();
        startedThreadIndexList.add(0);
        
        mVCFrame.setSize(710, (int) mVCFrame.getSize().getHeight());
        //mVCFrame.setTitle("ENCODER/DECODER : STARTING : " + tasks[0].getThread().getName());
        mVCFrame.setTitle(CodecUtilities.getStartingTaskTitle(tasks[0].getThread().getName()));

        int sleepRatio = 1;

        if (tasks.length < 100) {
            sleepRatio = 1;
        } else if (tasks.length < 1000) {
            sleepRatio = 10;
        } else if (tasks.length < 10000) {
            sleepRatio = 100;
        } else if (tasks.length < 100000) {
            sleepRatio = 1000;
        }

        Random random = new Random();
        int index;

        for (int i = tasks.length - 1; i > 0; --i) {

            index = getANonStartedThreadIndex(startedThreadIndexList, random);

            tasks[index].start();

            startedThreadIndexList.add(index);

            try {
                if (i % (tasks.length / 100) == 0) {
                    mVCFrame.setTitle(CodecUtilities.getStartingTaskTitle(startedThreadIndexList.size(), tasks.length));
                }
            } catch (Exception e) {
                mVCFrame.setTitle(CodecUtilities.getStartingTaskTitle(startedThreadIndexList.size(), tasks.length));
            }

            try {
                tasks[index].getThread().sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(RandomLineStarterRunnable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        synchronized (mVCFrame) {
            mVCFrame.notify();
        }

    }

    private int getANonStartedThreadIndex(List<Integer> startedThreadIndexList, Random random) {

        int index = random.nextInt(tasks.length);

        while (startedThreadIndexList.contains(index)) {
            index = random.nextInt(tasks.length);
        }

        return index;

    }

}
