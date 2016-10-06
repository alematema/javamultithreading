/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.undra.view;

import br.edu.undra.app.util.CodecUtilities;
import br.edu.undra.app.util.Duration;
import br.edu.undra.model.Task;
import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author alexandre
 */
public class EncoderDecoderMVCFrame extends SimpleFrame {

    private final JPanel encoderDecoderPanel;
    private long titleMessageSwap;
    //private long nonParallelDuration = 0;
    //private long parallelDuration = 0;
    private boolean isAboutToEnd = false;
    private boolean hasAlreadyLogged = false;

    private final List<String> players;

    public EncoderDecoderMVCFrame(JPanel panel) {
        this.encoderDecoderPanel = panel;
        players = new ArrayList<>();
    }

    public void initShowAndPreapare() throws IOException, InterruptedException {

        CodecUtilities.runGC();
        CodecUtilities.logAppStarting();

        setSize(740, 0);
        //showIt("ENCODER/DECODER : preparing ...  Wait!!! "  );
        showIt(CodecUtilities.PREPARING_TITLE);
        setLocation(10, 19);
        SwingUtilities.updateComponentTreeUI(this);

        this.encoderDecoderPanel.setOpaque(true);
        this.add(new JScrollPane(this.encoderDecoderPanel));

        CodecUtilities.logAppPreparingGui();

        ((EncoderDecoderPanel) this.encoderDecoderPanel).prepareGraphicInformation(this);

        synchronized (this) {
            this.wait();
        }

        CodecUtilities.runGC();
        CodecUtilities.logAppRunning0();

        ((EncoderDecoderPanel) this.encoderDecoderPanel).run(this);

        synchronized (this) {
            this.wait();
        }

        CodecUtilities.runGC();
        CodecUtilities.logAppRunning1();

        //this.setTitle("ENCODER/DECODER : processing #" + getEncoderDecoderPanel().getThreadsCount() + " THREADS : Encoding to file /" + getEncoderDecoderPanel().getOutputFileName());
        this.setTitle("ENCODER/DECODER : processando #" + getEncoderDecoderPanel().getThreadsCount() + " THREADS : salvando em /" + getEncoderDecoderPanel().getOutputFileName());

    }

    public EncoderDecoderPanel getEncoderDecoderPanel() {
        return (EncoderDecoderPanel) encoderDecoderPanel;
    }

    public boolean hasAnyRunningTask() {

        boolean hasAnyRunningTask = false;
        int howManyThreadsAreAlive = 0;

        for (Task task : getEncoderDecoderPanel().getTasks()) {
            if (task != null) {
                boolean isThreadAlive = task.getThread().isAlive();
                hasAnyRunningTask = hasAnyRunningTask || isThreadAlive;
                if (isThreadAlive) {
                    howManyThreadsAreAlive++;
                }
            }
        }

        if (hasAnyRunningTask) {
            updateRemainingWorking(howManyThreadsAreAlive);
        }

        return hasAnyRunningTask;
    }

    private void updateNONPARALLELWorkingDuration(int howManyThreadsAreAlive) {

        Duration nonParallelDuration = CodecUtilities.getTaskDuration((long) (((float) (getEncoderDecoderPanel().getTokenProcessingDuration() / 1000F)) * getEncoderDecoderPanel().getTasks().length * getEncoderDecoderPanel().getTokensPerTask()));

        ++howManyThreadsAreAlive;
        ++howManyThreadsAreAlive;
        if (howManyThreadsAreAlive < getEncoderDecoderPanel().getThreadsCount()) {
            howManyThreadsAreAlive -= 2;
            updateWorkingDuration(howManyThreadsAreAlive);
        } else {
            //this.setTitle("ENCODER/DECODER : would take " + nonParallelDuration + " for a NON-PARALLEL processing version "  );
            this.setTitle("ENCODER/DECODER : " + nonParallelDuration + " para uma versão NÃO-PARALELA executar ");
        }
    }

    private void updateWorkingDuration(int howManyThreadsAreAlive) {

        Duration taskDuration = CodecUtilities.getTaskDuration(getEncoderDecoderPanel().getRemainingTime());

        //this.setTitle("ENCODER/DECODER : processing #" + howManyThreadsAreAlive + " THREADS : Encoding to file /" + getEncoderDecoderPanel().getOutputFileName() + "  :  (" + taskDuration + ")");
        this.setTitle("ENCODER/DECODER : processando #" + howManyThreadsAreAlive + " THREADS : termina em  :  (" + taskDuration + ")");
    }

    private void updateRemainingWorking(int howManyThreadsAreAlive) {

        if (howManyThreadsAreAlive < 5) {
            isAboutToEnd = true;
            CodecUtilities.runGC();
        }

        titleMessageSwap++;

        if (titleMessageSwap < 1500) {
            updateWorkingDuration(howManyThreadsAreAlive);
        } else if (titleMessageSwap < 3000) {
            updateNONPARALLELWorkingDuration(howManyThreadsAreAlive);
        } else {
            titleMessageSwap = 0;
        }
    }

    public boolean isAboutToEnd() {
        return isAboutToEnd;
    }

    public void logIsAboutToEnd() {
        if (hasAlreadyLogged) {
        } else {
            CodecUtilities.logAppIsAboutToEnd();
            hasAlreadyLogged = true;
        }
    }

    public void blink(long milis, Component component) throws InterruptedException {

        component.setVisible(false);
        Thread.sleep(milis);
        component.setVisible(true);

    }

    public void close(long initTime) throws InterruptedException, IOException {

        CodecUtilities.logAppShuttingDown();
        this.setSize((int) (this.getSize().getWidth() - 50), (int) this.getSize().getHeight());
        CodecUtilities.runGC();

        blink(50, this);

        getEncoderDecoderPanel().close();//fecha todos streams e etc

        Runtime.getRuntime().gc();
        CodecUtilities.logAppHasClosedResources();
        CodecUtilities.logAppByeBye(initTime);

        //setTitle("ENCODER/DECODER has completed running #" + getEncoderDecoderPanel().getThreadsCount() + " threads");
        setTitle("ENCODER/DECODER : COMPLETO : processadas #" + getEncoderDecoderPanel().getThreadsCount() + " threads");
        Thread.sleep(3000);
        blink(100, getEncoderDecoderPanel());
        //setTitle("ENCODER/DECODER : closing in 2 secs");
        setTitle("ENCODER/DECODER : FECHANDO : em 2 segundos");
        Thread.sleep(1900);
        blink(20, getEncoderDecoderPanel());
        //setTitle("ENCODER/DECODER : closing in 1 sec");
        setTitle("ENCODER/DECODER : FECHANDO : em 1 segundo");
        Thread.sleep(1400);
        setTitle("ENCODER/DECODER : BYE");
        blink(200, this);
        Thread.sleep(80);
        blink(80, this);
        Thread.sleep(30);

        System.exit(0);

    }

    public boolean addPlayer(String player) {
        boolean added = false;
        if (players.contains(player)) {
        } else {
            added = players.add(player);
        }
        return added;
    }

    public boolean removePlayer(String player) {
        return players.remove(player);
    }

    public void reduceSize(int width) {
        if (players.isEmpty()) {
            this.setSize((int) this.getSize().getWidth() - width, (int) this.getSize().getHeight());
        }
    }

    public void expand(int width) {
        if (players.size() == 1) {
            this.setSize(width + (int) this.getSize().getWidth(), (int) this.getSize().getHeight());
        }
    }
}
