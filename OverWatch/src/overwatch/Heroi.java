/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package overwatch;

/**
 *
 * @author tiagofraga
 */
public class Heroi {
    
    private String nome;
    private int overall;
    private boolean escolhido;
    
    public Heroi (String nome, int o){
        this.nome = nome; 
        this.overall = o;
        this.escolhido = false;
    }
   
    public String getNome(){
        return this.nome;
    }
    
    public int getOverall(){
        return this.overall;
    }
    
    public synchronized boolean getEscolhido(){
        return this.escolhido;
    }
    
    public void setNome(String nome){
        this.nome = nome;
    }
    
    public void setOverall(int o){
        this.overall = o;
    }
    
    public synchronized void setEscolhidoTrue(){
        this.escolhido = true;
    }
    
    public synchronized void setEscolhidoFalse(){
        this.escolhido = false;
    }
    
}
