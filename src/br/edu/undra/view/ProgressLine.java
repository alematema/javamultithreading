/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.undra.view;

import br.edu.undra.app.util.CodecUtilities;
import br.edu.undra.model.Task;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author alexandre
 */
public class ProgressLine extends JPanel implements ActionListener {

    private final int id;
    private Task task;
    private JProgressBar progressBar;
    private JTextField progressText;
    private JRadioButton play;
    private Color progressBarDefaultBackGroundColor;
    private final  EncoderDecoderMVCFrame mVCFrame;
    private int width;
    
    private long remainingTime;
    

    public ProgressLine(int id,  EncoderDecoderMVCFrame mVCFrame) {
        this.id = id;
        this.mVCFrame = mVCFrame;
        init();
    }



    private void init() {
        
        progressBar = new JProgressBar();
        progressBarDefaultBackGroundColor = progressBar.getBackground();
        progressBar.setMaximum(CodecUtilities.PROGRESS_BAR_MAXIMUM_VALUE);
        progressBar.putClientProperty("JComponent.sizeVariant", "large");
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        if (this.id % 2 == 1) {
            progressBar.setForeground(CodecUtilities.ODD_FOREGROUND_COLOR);
        } else {
            progressBar.setForeground(CodecUtilities.EVEN_FOREGROUND_COLOR);
        }

        //System.out.println("bar = " + progressBar.getSize().getWidth());
        width+=progressBar.getWidth();
        
        progressText = new JTextField();
        progressText.setForeground(Color.GRAY);
        //progress.setText("Thread-" + id + " sleeping... Wainting for starting...");
        progressText.setText(CodecUtilities.getSleepingTaskTitle(id));
        progressText.setEditable(false);

        //System.out.println("text  = " + progressText.getSize().getWidth());
        
        width+=progressText.getWidth();
        
        play = new JRadioButton(new ImageIcon("images/pause.16x16.png"));
        play.setVisible(false);
        play.setSelected(true);
        play.setEnabled(false);
        play.putClientProperty("JComponent.sizeVariant", "mini");
        play.setForeground(Color.BLUE);
        
        play.setName(Long.toString(Calendar.getInstance().getTime().getTime()));

        play.addActionListener(this);

        width+=play.getWidth();
        
        setLayout(new GridLayout(1, 3));
        

        add(progressBar, BorderLayout.WEST);
        add(progressText, BorderLayout.CENTER);
        add(play, BorderLayout.EAST);
        
    }

    public boolean hasCompletedTask() {
        return this.getProgressBar().getValue() == this.getProgressBar().getMaximum();
    }

    public int getCompletedPercentage() {
        return this.getProgressBar().getValue();
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }

    public JTextField getProgressText() {
        return progressText;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public JRadioButton getPlay() {
        return play;
    }

    public int getId() {
        return id;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(play)) {

            if (isPlaying()) {
                
                mVCFrame.removePlayer(play.getName());
                mVCFrame.reduceSize(CodecUtilities.EXPAND_REDUCE_WINDOW_PIXELS);

                play.setSelectedIcon(new ImageIcon("images/pause.16x16.png"));
                play.setForeground(Color.BLUE);

                progressBar.setBackground(progressBarDefaultBackGroundColor);
                
                if (this.id % 2 == 1) {
                    progressBar.setForeground(CodecUtilities.ODD_FOREGROUND_COLOR);
                } else {
                    progressBar.setForeground(CodecUtilities.EVEN_FOREGROUND_COLOR);
                }

                progressText.setForeground(Color.BLUE);
                progressText.setText("Syncronizing ... ");

                System.out.println("PLAYING");

            } else {
                
                mVCFrame.addPlayer(play.getName());
                mVCFrame.expand(CodecUtilities.EXPAND_REDUCE_WINDOW_PIXELS);
                
                play.setIcon(new ImageIcon("images/play.16x16.png"));

                progressBar.setBackground(CodecUtilities.PAUSED_TASK_COLOR);
                progressBar.setForeground(Color.WHITE);

                progressText.setForeground(Color.BLUE);
                progressText.setText(CodecUtilities.PAUSED_TASK_MESSAGE);

                System.out.println("PAUSED");
            }
        }
    }

    boolean isLeastAdvancedTask(int leastAdvancedTask) {
        return getCompletedPercentage() < leastAdvancedTask;
    }
    
    
    public int getWidthh(){
        return width;
    }

    public boolean isPlaying(){
        return this.play.isSelected();
    }
    
    public void setText(String text){
        this.progressText.setText(text);
    }
    public void setValue(int value){
        this.progressBar.setValue(value);
    }
}
