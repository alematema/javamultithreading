package br.edu.undra.model.runnables;

import br.edu.undra.app.util.CodecUtilities;
import br.edu.undra.model.MessageEncoder;
import br.edu.undra.view.ProgressLine;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 * <b>Runnable/Código executado em paralelo.</b><br>
 * Este runnable processa cada uma das linhas de messages.<br>Escreve no arquivo
 * de saída resultado do processamento da linha<br>
 * Escreverá no arquivo relatório estatísticas como a duraçao do processamento dessa Thread<br>
 * Além disso, porque o projeto implementa <b>MVC</b>, este runnable atualiza a
 * <b>VIEW</b>  de acordo com requisitos.
 *
 */
public class MessageEncoderRunnable implements Runnable {

    static int count = 0;
    private final MessageEncoder encoder;
    private String message;
    private List<String> messages;
    private final OutputStreamWriter report;
    private ProgressLine progressLine;
    private Map<Integer, Long> pausedMap;

    
      public MessageEncoderRunnable(MessageEncoder encoder, List<String> messages, OutputStreamWriter report) {
        this.encoder = encoder;
        this.messages = messages;
        this.report = report;
        count++;
    }

    
    public MessageEncoderRunnable(MessageEncoder encoder, String message) {
        this.encoder = encoder;
        this.message = message;
        this.report = null;
        count++;
    }

    public MessageEncoderRunnable(MessageEncoder encoder, List<String> messages) {
        this.encoder = encoder;
        this.messages = messages;
        this.report = null;
        count++;
    }
    
    public MessageEncoder getEncoder() {
        return encoder;
    }

    public ProgressLine getProgress() {
        return progressLine;
    }

    /**
     * <b>Runnable/Código executado em paralelo.</b><br>
     * Este runnable processa cada uma das linhas de messages.<br>Escreve no
     * arquivo de saída resultado do processamento da linha<br>
     * Escreverá no arquivo relatório estatísticas como a duraçao do processamento dessa
     * Thread<br>
     * Além disso, porque o projeto implementa <b>MVC</b>, este runnable
     * atualiza a <b>VIEW</b> de acordo com requisitos.
     *
     */
    @Override
    public void run() {

        long init = System.currentTimeMillis();
        
        updatePlay();//ATUALIZA A VIEW : o projeto implementa MVC, este runnable atualiza a VIEW de acordo com requisitos.
        if (encoder.isLongTankRunning()) {
             //progressLine.getProgressText().setText(Thread.currentThread().getName() + " : PROCESSING : LONG TASK : WAIT ");
             progressLine.getProgressText().setForeground(Color.RED);
             progressLine.getProgressText().setText(CodecUtilities.getLongTaskRunningTitle(Thread.currentThread().getName().split("-")[1]));
         }
        processMessages();
        updateDoneProgressLine();//ATUALIZA A VIEW : o projeto implementa MVC, este runnable atualiza a VIEW de acordo com requisitos.

        //ESCREVE NO ARQUIVO REPORT ESTATISTICAS DE PROCESSAMENTO DESSA THREAD
        synchronized (this.report) {
            if (this.report != null) {
                writeToReport(init);
            }
        }
    }

    /**
     * Processa as messages
     */
    private void processMessages() throws NumberFormatException {
        int line = 0;
        long init = System.currentTimeMillis();
        long lastTimeBeforePausing = 0;
        long pausedPeriod = 0;
        int lastIndexBeforePausing = 0;
        int index = lastIndexBeforePausing;
        pausedMap = new HashMap<>();

        for (; index < messages.size(); index++) {

            if (isPlaying()) {//se play is selected entao PAUSE nao foi solicitado

                lastIndexBeforePausing = index;// guarda último indice processado antes de PAUSE ser solicitado

                encoder.encode(messages.get(index));// encodes msg

                line++;

                updateProgressLine(init, pausedMap, line);

                lastTimeBeforePausing = System.currentTimeMillis();

            } else {// PAUSE foi solicitada

                index = processPausing(index, lastIndexBeforePausing, lastTimeBeforePausing, pausedMap);

            }
        }
    }

    /**
     * Indica se NAO estah pausado o processamento desse runnable. <br>
     * Isto eh se is playing
     */
    private boolean isPlaying() {
        return progressLine.isPlaying();
    }

    /**
     * Processa pausing
     */
    private int processPausing(int index, int lastIndexBeforePausing, long lastTimeBeforePausing, Map<Integer, Long> pausedMap) {
        long pausedPeriod;
        //user has paused processing
        index = lastIndexBeforePausing;
        pausedPeriod = (System.currentTimeMillis() - lastTimeBeforePausing);
        pausedMap.put(index, pausedPeriod);
        return index;
    }

    /**
     * Escreve no relatório as estatísticas sobre a finalizaçao do processamento
     * dessa Thread
     */
    private void writeToReport(long init) {
        try {
            //String report_ = Thread.currentThread().getName() + " took " + ((System.currentTimeMillis() - init) / 1000) + " secs for processing " + messages.size() + " tokens.";
            long tookUntilNow = System.currentTimeMillis() - init - getPausedPeriod(pausedMap);//calcula quanto tempo durou o processamento ateh agora , descontando eventual periodo de PAUSE ,
            this.report.write(String.format("[%tT] : " + Thread.currentThread().getName() + " : " + (tookUntilNow / 1000) + " secs para processar " + messages.size() + " tokens", Calendar.getInstance()) + "\n");
            this.report.flush();
        } catch (IOException ex) {
            Logger.getLogger(MessageEncoderRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * ATUALIZA A VIEW : o projeto implementa MVC, este runnable atualiza a VIEW
     * de acordo com requisitos.
     * <br><b>ProgressLine</b> corresponde a um mix de swing components
     * <br>ProgressLine eh atualizada para corresponder a um status de tarefa
     * FINALIZADA
     */
    private void updateDoneProgressLine() {
        //ATUALIZA A VIEW : o projeto implementa MVC, este runnable atualiza a VIEW de acordo com requisitos.
        progressLine.getPlay().setVisible(false);
        //progressLine.getPlay().setText("");
        progressLine.getProgressBar().setForeground(CodecUtilities.COMPLETED_TASK_COLOR);
        progressLine.getProgressBar().setMaximum(CodecUtilities.PROGRESS_BAR_MAXIMUM_VALUE);
        progressLine.getProgressText().setForeground(Color.GRAY);
        //FIM ATUALIZA A VIEW 
    }

    /**
     * ATUALIZA A VIEW : o projeto implementa MVC, este runnable atualiza a VIEW
     * de acordo com requisitos.
     * <br><b>ProgressLine</b> corresponde a um mix de swing components
     */
    private void updateProgressLine(long init, Map<Integer, Long> pausedMap, int line) throws NumberFormatException {

        long tookUntilNow = System.currentTimeMillis() - init - getPausedPeriod(pausedMap);//calcula quanto tempo durou o processamento ateh agora , descontando eventual periodo de PAUSE ,
        String remains = Long.toString(getRemainingTime(line, tookUntilNow) / 1000);//time in seconds
        tookUntilNow /= 1000;//in seconds

        //ATUALIZA A VIEW : o projeto implementa MVC, este runnable atualiza a VIEW de acordo com requisitos.
        progressLine.setValue(Math.max((int) (100 * ((float) line / messages.size())), 1));
        //progressLine.setText(Thread.currentThread().getName() + " : [" + tookUntilNow + "secs / " + remains + "secs] : [" + line + " / " + messages.size() + " lines]");

        if (isPlaying()) {//ensures not damaging other thread`s work... thats why progressLine.getPlay().isSelected() is RE CHECKED
            progressLine.getProgressText().setForeground(Color.BLACK);
            progressLine.setText(CodecUtilities.getRunningTaskMessage(Thread.currentThread().getName().split("-")[1], tookUntilNow, remains, line, messages.size()));
        }
        progressLine.setRemainingTime(Long.parseLong(remains));//time in seconds
    }

    /**
     * ATUALIZA A VIEW : o projeto implementa MVC, este runnable atualiza a VIEW
     * de acordo com requisitos.
     * <br><b>Play</b> corresponde a um JButton
     */
    private void updatePlay() {
        //ATUALIZA A VIEW : o projeto implementa MVC, este runnable atualiza a VIEW de acordo com requisitos.
        progressLine.getPlay().setVisible(true);
        progressLine.getPlay().setEnabled(true);
        progressLine.getPlay().setSelectedIcon(new ImageIcon("images/pause.16x16.png"));
        //FIM ATUALIZA A VIEW :
    }

    /**
     * Calcula o tempo que resta para esta Thread finalizar.
     *
     * @param line um índice para estimar tempo total de execução da Thread.
     * @param tookUntilNow a duração em <b>mili segundos</b> que o processamento
     * da Thread levou ateh agora.
     * @return o tempo em <b>mili segundos</b> que resta para esta Thread
     * finalizar
     */
    private long getRemainingTime(int line, long tookUntilNow) {
        long totalTimeForComplete = messages.size() * tookUntilNow / line;
        return totalTimeForComplete - tookUntilNow;
    }

    /**
     * Calcula e retorna a quantidade TOTAL de <b>mili segundos</b> que esta
     * Thread ficou pausada
     *
     * @param pausedMap par chave/valor : chave=indice da tarefa pausada |
     * valor=tempo em milis que tarefa cujo indice eh chave ficou pausada
     * @return a quantidade TOTAL de <b>mili segundos</b> que esta Thread ficou
     * pausada
     */
    private long getPausedPeriod(Map<Integer, Long> pausedMap) {

        long pausedPeriod = 0;

        pausedPeriod = pausedMap.keySet().stream().map((key) -> pausedMap.get(key)).reduce(pausedPeriod, (accumulator, _item) -> accumulator + _item);

        return pausedPeriod;
    }
    
    public void doViewBinding(ProgressLine progressLine){
        this.progressLine = progressLine;
    }

}
