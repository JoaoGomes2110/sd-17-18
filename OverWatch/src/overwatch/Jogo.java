/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package overwatch;

import java.util.HashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Jogo {
    
    private String nome;
    private Equipa casa;
    private Equipa fora;
    private HashMap<Integer,Heroi> listaherois;
    
    private Barreira barreira;
    private int quantidade;
    
  
    
    
    public Jogo (String nome){
        this.nome = nome;
        this.casa = new Equipa();
        this.fora = new Equipa();
        this.listaherois = new HashMap<>();
        this.barreira = new Barreira(5);
        this.quantidade = 0;
        
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
    
    public HashMap<Integer,Heroi> getListaHerois(){
        return this.listaherois;
    }
   

    public synchronized void addJogador(Jogador jogador) {
        if(this.casa.getQuantidade()<5){
            this.casa.addJogador(jogador);
        }else if(this.fora.getQuantidade()<5){
            this.fora.addJogador(jogador);  
        }
        
    }
   
    
    public synchronized int getQuantidade() {
        return this.quantidade;
    }

    public Barreira getBarreira() {
       return this.barreira;
    } 

    public boolean atualizaHeroi(Jogador jogador, String message) {
        Equipa atual = null;
        atual = this.casa.getEquipa(jogador.getUsername());
        if(atual == null){
            atual = this.fora.getEquipa(jogador.getUsername());
        }
        int n = Integer.parseInt(message);
        Heroi heroi = this.listaherois.get(n);
         return atual.addHeroi(jogador,heroi);
    }

    public Equipa getEquipaJogador(String username) { 
        Equipa atual = null;
        atual = this.casa.getEquipa(username);
        if(atual == null){
            atual = this.fora.getEquipa(username);
        }
        return atual;
    }

    
   
    
   
    
    
    
   
    
}
