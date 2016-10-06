package br.edu.undra.view;
/**
* Example program for Introduction to Swing
* @author Paul Fischer, IMM, DTU
* @version 1.0
*/
import javax.swing.JFrame;

public class SimpleFrame extends JFrame
{
  public SimpleFrame()
  {
     this.setSize(200,200);/*@\label{setsize:line}@*/
     this.setLocation(200,200);/*@\label{setloc:line}@*/
     this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);/*@\label{closeOp:line}@*/
  }

  // Makes the frame visible.
  public void showIt(){
    this.setVisible(true);
  }

  // Makes the frame visible and sets the title text.
  public void showIt(String title){
    this.setTitle(title);
    this.setVisible(true);
  }

  // Makes the frame visible and sets the title text
  // and the position of the window.

  public void showIt(String title,int x, int y){
    this.setTitle(title);
    this.setLocation(x,y);
    this.setVisible(true);
  }

  // Makes the frame invisible.
  public void hideIt(){
    this.setVisible(false);
  }
  
  // Moves to the x , y coordinates
  public void moveTo(int x, int y){
    this.setLocation(x,y);
  }
  
}
