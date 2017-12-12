/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package overwatch;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Game {
    
    private String name;
    private Team home;
    private Team away;
    
    private Barrier barrier;
    private int playersQuantity;
    
    
    public Game (String name){
        this.name = name;
        this.home = new Team();
        this.away = new Team();
        this.barrier = new Barrier(5);
        this.playersQuantity = 0;
    }
    
    public String getName(){
        return this.name;
    }
    
    public Team getHomeTeam(){
        return this.home;
    }
    
    public Team getAwayTeam(){
        return this.away;
    }
   
    public Team gameSimulator(){
        int rhome =0;
        int raway = 0;
        
        for(int i=0;i<this.home.getQuantity();i++){
            rhome += this.home.getHero(i).getOverall();
        }
        
        for(int i=0; i<this.away.getQuantity();i++){
            raway += this.home.getHero(i).getOverall();
        }
        
        if(rhome>raway){
            this.home.updateRank();
            return this.home;
        } else if(raway>rhome){
            this.away.updateRank();
            return this.away;
        } else{
            this.away.updateRank();
            return this.away;
        }
    }

    public synchronized void addJogador(Player jogador) {
        if(this.home.getQuantity()<5){
            this.home.addPlayer(jogador);
        }else if(this.away.getQuantity()<5){
            this.away.addPlayer(jogador);  
        }
        
    }
   
    public synchronized int getQuantidade() {
        return this.playersQuantity;
    }

    public Barrier getBarrier() {
       return this.barrier;
    }
    
    
    
    
    
   
    
}
