/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.undra.app.util;

import br.edu.undra.view.EncoderDecoderMVCFrame;
import br.edu.undra.view.EncoderDecoderPanel;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexandre
 */
public class CodecUtilities {
    
    static public int EXPAND_REDUCE_WINDOW_PIXELS = 60;

    static public String SPLITTER = ":";
    static public Messages MESSAGES;
    /*PROGRESS BAR SETTINGS*/
    static public int PROGRESS_BAR_MAXIMUM_VALUE = 100;
    static public Color ODD_FOREGROUND_COLOR = Color.ORANGE;
    static public Color EVEN_FOREGROUND_COLOR = Color.BLUE;
    static public Color COMPLETED_TASK_COLOR = Color.GREEN;
    static public Color PAUSED_TASK_COLOR = Color.lightGray;
    /*END PROGRESS BAR SETTINGS*/
    
    public static String PAUSED_TASK_MESSAGE = "Tarefa está PAUSADA.  Pressione \" > \" para continuar ... ";
    public static String PREPARING_TITLE = "ENCODER/DECODER : preparando ...  Aguarde!!! ";

    private static final String APP_STARTING = "\n\n......................................APP STARTING......";
    private static final String APP_RUNNING_0 = "\n\n......................................APP STARTING THREADS...";
    private static final String APP_RUNNING_1 = "\n\n......................................APP PROCESSING THREADS...";
    private static final String APP_SHUTTINGDOWN = "\n\n......................................APP SHUTTING DOWN...\n";
    private static final String APP_PREPARING_GUI = "\n\n......................................APP PREPARING GUI...\n";
    private static final String APP_IS_ABOUT_TO_END = "\n\n......................................APP IS ABOUT TO END...\n";
    private static final String APP_CLOSING_RESOURCES = "\n\n......................................APP HAS CLOSED RESOURCES...";
    private static final String APP_BYE_BYE = "\n\n......................................APP SAYS BYE-BYE...";

    static public String getGraphicInfoTitle(float percentage){
        return "ENCODER/DECODER : GERANDO INFORMAÇÃO GRÁFICA : " + String.format("%.2f", percentage) + "%";
    }
    
    static public String getStartingTaskTitle(String name){
        return "ENCODER/DECODER : INICIANDO : " + name;
    }
    
    static public String getStartingTaskTitle(int fraction, int total){
        return "ENCODER/DECODER : INICIANDO THREADS : " + "iniciadas " + fraction + " de " + total + " threads ";
    }
    
    static public String getSleepingTaskTitle(int taskId){
        return "Tarefa-" + taskId + " sleeping... Esperando para iniciar ...";
    }
    
    static public String getLongTaskRunningTitle(String taskName){
        return "TAREFA-"+taskName + " : PROCESSANDO : TAREFA LONGA : AGUARDE... ";
    }
    
    static public String getRunningTaskMessage(String taskId,long tookUntilNow, String remains, int lineId, int linesTotal){
        return "TAREFA-"+taskId + " : [" + tookUntilNow + "secs / " + remains + "secs] : [" + lineId + " / " +linesTotal + " lines]";
    }
    
    static public void logAppStarting() {
        Logger.getLogger(EncoderDecoderMVCFrame.class.toString()).log(Level.INFO, "{0} {1}={2}", new Object[]{CodecUtilities.APP_STARTING, "freeMemory", Runtime.getRuntime().freeMemory()+"\n\n"});
    }

    static public void logAppPreparingGui() {
        Logger.getLogger(EncoderDecoderMVCFrame.class.toString()).log(Level.INFO, "{0} ", new Object[]{CodecUtilities.APP_PREPARING_GUI});
    }

    static public void logAppRunning0() {
        Logger.getLogger(EncoderDecoderMVCFrame.class.toString()).log(Level.INFO, "{0} {1}={2}", new Object[]{CodecUtilities.APP_RUNNING_0, "freeMemory", Runtime.getRuntime().freeMemory()+"\n\n"});
    }
    
    static public void logAppRunning1() {
        Logger.getLogger(EncoderDecoderMVCFrame.class.toString()).log(Level.INFO, "{0} {1}={2}", new Object[]{CodecUtilities.APP_RUNNING_1, "freeMemory", Runtime.getRuntime().freeMemory()+"\n\n"});
    }

    static public void logAppIsAboutToEnd() {
        Logger.getLogger(EncoderDecoderMVCFrame.class.toString()).log(Level.INFO, "{0} ", new Object[]{CodecUtilities.APP_IS_ABOUT_TO_END});
    }

    static public void logAppHasClosedResources() {
        Logger.getLogger(EncoderDecoderPanel.class.toString()).log(Level.INFO, "{0} {1}={2} ", new Object[]{CodecUtilities.APP_CLOSING_RESOURCES, "freeMemory", Runtime.getRuntime().freeMemory()+"\n\n"});
    }

    static public void logAppShuttingDown() {
        Logger.getLogger(EncoderDecoderMVCFrame.class.toString()).log(Level.INFO, "{0} ", new Object[]{CodecUtilities.APP_SHUTTINGDOWN});
    }

    static public void logAppByeBye(long initTime) {
        Logger.getLogger(EncoderDecoderMVCFrame.class.toString()).log(Level.INFO, "{0} ({1})\n", new Object[]{CodecUtilities.APP_BYE_BYE, new Duration((System.currentTimeMillis() - initTime)/1000)});
    }

    static public Duration getTaskDuration(long seconds){
        return new Duration(seconds);
    }

    public static void runGC() {
        Runtime.getRuntime().gc();
        Runtime.getRuntime().gc();
        Runtime.getRuntime().gc();
    }
    
}
