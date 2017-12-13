/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package overwatch;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author tiagofraga
 */
public class Equipa {
    
    
    private HashMap<Jogador,Heroi> lista;
    private int quantidade;
    
    public Equipa(){
        this.lista = new HashMap<>();
        this.quantidade = 0;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public synchronized void addJogador(Jogador j) {
        this.lista.put(j,null);
        this.quantidade++;
    }
    
    public synchronized boolean addHeroi(Jogador j, Heroi h){
        if(!lista.containsValue(h)){
            lista.put(j,h); 
            return true;
        }
        return false;
        
    }

  
    public ArrayList<String> getJogadores(){
        ArrayList<String> nova = new ArrayList<>();
        for(Jogador j: lista.keySet()){
            nova.add(j.getUsername());
        }
        return nova;
        
    }

    public Equipa getEquipa(String username) {
        for(Jogador j : lista.keySet()){
            if(j.getUsername().equals(username)){
                return this;
            }
        }
        return null;
    }
    
}
