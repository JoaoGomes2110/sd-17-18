/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package overwatch;

/**
 *
 * @author
 */
public class Hero {
    
    private String name;
    private int overall;
    private boolean chosen;
    
    public Hero (String name, int o){
        this.name = name; 
        this.overall = o;
        this.chosen = false;
    }
   
    public String getName(){
        return this.name;
    }
    
    public int getOverall(){
        return this.overall;
    }
    
    public synchronized boolean getChosen(){
        return this.chosen;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public void setOverall(int o){
        this.overall = o;
    }
    
    public synchronized void setChosenTrue(){
        this.chosen = true;
    }
    
    public synchronized void setChosenFalse(){
        this.chosen = false;
    }
    
}
