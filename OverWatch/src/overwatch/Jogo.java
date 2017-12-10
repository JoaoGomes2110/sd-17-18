/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package overwatch;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Jogo {
    
    private String nome;
    private Equipa casa;
    private Equipa fora;
    
    private ReentrantLock lock;
    private Condition condition;
    private int quantidadeJogadores;
    
    
    public Jogo (String nome){
        this.nome = nome;
        this.casa = new Equipa();
        this.fora = new Equipa();
        this.lock = new ReentrantLock();
        this.condition = this.lock.newCondition();
        this.quantidadeJogadores = 0;
    }
    
    public String getNome(){
        return this.nome;
    }
    
    public Equipa getEquipaCasa(){
        return this.casa;
    }
    
    public Equipa getEquipaFora(){
        return this.fora;
    }
    
     
    public Equipa simularJogo(){
        int rCasa =0;
        int rFora = 0;
        
        for(int i=0;i<this.casa.getQuantidade();i++){
            rCasa += this.casa.getHeroi(i).getOverall();
        }
        
        for(int i=0; i<this.fora.getQuantidade();i++){
            rFora += this.casa.getHeroi(i).getOverall();
        }
        
        if(rCasa>rFora){
            this.casa.atualizaRank();
            return this.casa;
        } else if(rFora>rCasa){
            this.fora.atualizaRank();
            return this.fora;
        } else{
            this.fora.atualizaRank();
            return this.fora;
        }
    }

    public void addJogador(Jogador jogador) {
    
        
    }
   
    public synchronized int getQuantidade() {
        return this.quantidadeJogadores;
    }
    
    
    
    
    
   
    
}
