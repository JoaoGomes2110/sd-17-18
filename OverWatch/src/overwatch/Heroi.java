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
    
    public Heroi (String nome, int o){
        this.nome = nome; 
        this.overall = o;
    }
   
    public String getNome(){
        return this.nome;
    }
    
    public int getOverall(){
        return this.overall;
    }
    
    public void setNome(String nome){
        this.nome = nome;
    }
    
    public void setOverall(int o){
        this.overall = o;
    }
    
}
