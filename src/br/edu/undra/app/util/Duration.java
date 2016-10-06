/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.undra.app.util;

/**
 *
 * @author alexandre
 */
public class Duration {

    private final long years;
    private final long days;
    private final long hours;
    private final long min;
    private final long secs;
    private final long seconds;

    public Duration(long seconds) {
        this.seconds = seconds;
        this.years = seconds / (3600 * 24 * 365);
        this.days = (seconds % (3600 * 24 * 365)) / (3600 * 24);
        this.hours = (seconds % (3600 * 24)) / 3600;
        this.min = ((seconds % (3600 * 24)) % 3600) / 60;
        this.secs = (((seconds % (3600 * 24)) % 3600) % 60);
    }

    public long getDays() {
        return days;
    }

    public long getHours() {
        return hours;
    }

    public long getMin() {
        return min;
    }

    public long getSecs() {
        return secs;
    }

    @Override
    public String toString() {
        
        String toString;
        
        if(seconds==0){
            toString =  "?a:" + "?d:" + "?h:" + "?m:" + "?s calculando...";
        }else{
            toString =  years + "a:" + days + "d:" + hours + "h:" + min + "m:" + secs + "s";
            //          years + "y:" + days + "d:" + hours + "h:" + min + "m:" + secs + "s";
         
        }
        return toString;
    }

    public static void main(String[] args) {
        System.out.println(new Duration(System.currentTimeMillis() / 1000));
    }

}
