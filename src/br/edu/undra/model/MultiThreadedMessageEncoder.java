/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.undra.model;

import br.edu.undra.model.runnables.MessageEncoderRunnable;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author alexandre
 */
public class MultiThreadedMessageEncoder {

    private final OutputStreamWriter report;
    private int maxTokensPerThread = 2500;

    /**
     *
     * @param report
     */
    public MultiThreadedMessageEncoder(OutputStreamWriter report) {
        this.report = report;
    }

    
    public void encode(InputStream is, OutputStreamWriter out) {

        List<String> tokens = new ArrayList<>();

        try (Scanner scanner = new Scanner(is)) {

            tokens.add(scanner.nextLine());

            while (scanner.hasNextLine()) {

                tokens.add(scanner.nextLine());

                if (shouldEncodeTokensInNewThread(tokens)) {
                    encodeTokensInNewThread(out, tokens);
                    tokens.clear();
                }
            }

            if (tokens.isEmpty()) {
            } else {
                encodeTokensInNewThread(out, tokens);
            }
        }
    }

    public Task[] splitIntoTasks(InputStream is, OutputStreamWriter out, int tokensPerTask) {

        Task[] tasks = new Task[1000];
        int taskNum = 0;
        List<String> tokens = new ArrayList<>();
        setTokensPerThread(tokensPerTask);

        try (Scanner scanner = new Scanner(is)) {

            tokens.add(scanner.nextLine());
            
            while (scanner.hasNextLine()) {

                tokens.add(scanner.nextLine());
                
                if (shouldEncodeTokensInNewThread(tokens)) {
                    if (taskNum > tasks.length - 1) {
                        tasks = Arrays.copyOf(tasks, 2 * taskNum);
                    }
                    tasks[taskNum++] = encodeTokensInNewThread(out, tokens);
                    tokens.clear();
                }
            }//fim while
            if (tokens.isEmpty()) {
            } else {
                if (taskNum > tasks.length - 1) {
                    tasks = Arrays.copyOf(tasks, taskNum + 1);
                }
                tasks[taskNum] = encodeTokensInNewThread(out, tokens);
            }
        }

        tasks = Arrays.copyOf(tasks, tasks.length - getNullCount(tasks));//removes nulls from tasks array

        return tasks;
    }

    private int getNullCount(Task[] tasks) {

        int nullCount = 0;

        for (Task task : tasks) {
            if (task == null) {
                nullCount++;
            }
        }
        return nullCount;
    }

    private Task encodeTokensInNewThread(OutputStreamWriter out, List<String> tokens) {

        MessageEncoder encoder = new MessageEncoder();
        encoder.setOut(out);
        return new Task(new MessageEncoderRunnable(encoder, new ArrayList<>(tokens), report));

    }

    private boolean shouldEncodeTokensInNewThread(List<String> tokens) {
        return tokens.size() % getMaxTokensPerThread() == 0;
    }

    public void setTokensPerThread(int maxTokensPerThread) {
        this.maxTokensPerThread = maxTokensPerThread;
    }

    public int getMaxTokensPerThread() {
        return maxTokensPerThread;
    }

}
