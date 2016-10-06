/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.undra.view;

import br.edu.undra.model.MultiThreadedMessageEncoder;
import br.edu.undra.model.runnables.RandomLineStarterRunnable;
import br.edu.undra.model.Task;
import br.edu.undra.model.runnables.GraphicInformationPreparatorRunnable;
import java.awt.GridLayout;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import javax.swing.JPanel;

/**
 *
 * @author alexandre
 */
public class EncoderDecoderPanel extends JPanel {

    private MultiThreadedMessageEncoder encoder;//model

    private EncoderDecoderMVCFrame decoderMVCFrame;
    private OutputStreamWriter out;
    private InputStream is;
    private OutputStreamWriter report;
    private int threadsCount;
    private int tokensPerTask = 500;
    private int tokenProcessingDuration;

    private final String outputFileName = "encodedText.txt";
    private final String inputFileName = "meioMilhao.txt";
    private final String reportFileName = "report1.txt";

    private Task[] tasks;
    private ProgressLine[] progressLines;

    public void prepareGraphicInformation(EncoderDecoderMVCFrame mVCFrame) throws IOException {

        mVCFrame.setSize(740, 0);

        out = new FileWriter(outputFileName);
        is = new FileInputStream(inputFileName);
        report = new FileWriter(reportFileName);

        encoder = new MultiThreadedMessageEncoder(report);
        tasks = encoder.splitIntoTasks(is, out, getTokensPerTask());

        threadsCount = tasks.length;
        progressLines = new ProgressLine[tasks.length];

        JPanel panel = new JPanel(new GridLayout(tasks.length, 1));

        add(panel);

        tokenProcessingDuration = tasks[0].getRunnable().getEncoder().getLongTaskDurationSimulationTime();
        if(tokenProcessingDuration==0)tokenProcessingDuration=1;//em mili segundos

        new Thread(new GraphicInformationPreparatorRunnable(mVCFrame, tasks, panel, progressLines)).start();
    }

    public void run(EncoderDecoderMVCFrame mVCFrame) throws InterruptedException {
        new Thread(new RandomLineStarterRunnable(mVCFrame, tasks)).start();
    }

    public FileInputStream getIs() {
        return (FileInputStream) is;
    }

    public FileWriter getOut() {
        return (FileWriter) out;
    }

    public FileWriter getReport() {
        return (FileWriter) report;
    }

    public int getThreadsCount() {
        return threadsCount;
    }

    public String getInputFileName() {
        return inputFileName;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public String getReportFileName() {
        return reportFileName;
    }

    public Task[] getTasks() {
        return tasks;
    }

    public ProgressLine[] getProgressLines() {
        return progressLines;
    }

    public long getRemainingTime() {
        return getLeastNonCompletedTask().getRemainingTime();
    }

    public void setTokensPerTask(int tokensPerTask) {
        this.tokensPerTask = tokensPerTask;
    }

    public int getTokensPerTask() {
        return tokensPerTask;
    }

    public int getTokenProcessingDuration() {
        return tokenProcessingDuration;
    }

    private ProgressLine getLeastNonCompletedTask() {

        ProgressLine progressLine = progressLines[0];

        int leastAdvancedTask = Integer.MAX_VALUE;
        for (ProgressLine line : progressLines) {
            if (!line.hasCompletedTask()) {
                if (line.isLeastAdvancedTask(leastAdvancedTask)) {
                    leastAdvancedTask = line.getCompletedPercentage();
                    progressLine = line;
                }
            }
        }

        return progressLine;
    }

    public void close() throws IOException {
        
        getOut().close();
        getIs().close();
        getReport().close();
        
    }

}
