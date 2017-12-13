/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package overwatch;

import java.util.HashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Game {
    
    private String name;
    private Team home;
    private Team away;
    private HashMap<Integer,Hero> heroeslist;
    private int chosen;
    
    private Barrier barrier;
    private int quantity;
    
  
    
    
    public Game (String name){
        this.name = name;
        this.home = new Team();
        this.away = new Team();
        this.heroeslist = new HashMap<>();
        this.barrier = new Barrier(5);
        this.quantity = 0;
        this.chosen = 1;
        
    }
    
    public String getName(){
        return this.name;
    }
    
    public Team getHometeam(){
        return this.home;
    }
    
    public Team getAwayteam(){
        return this.away;
    }
    
    public HashMap<Integer,Hero> getHeroeslist(){
        return this.heroeslist;
    }
  
    public synchronized void addPlayer(Player player) {
        if(this.home.getQuantity()<5){
            this.home.addPlayer(player);
        }else if(this.away.getQuantity()<5){
            this.away.addPlayer(player);  
        }
        
    }
   
    public synchronized int getQuantity() {
        return this.quantity;
    }

    public Barrier getBarrier() {
       return this.barrier;
    } 

    public boolean updateHero(Player player, String message) {
        Team actual = null;
        actual = this.home.getTeam(player.getUsername());
        if(actual == null){
            actual = this.away.getTeam(player.getUsername());
        }
        int n = Integer.parseInt(message);
        Hero hero = this.heroeslist.get(n);
        boolean bol = actual.addHero(player,hero);
        if(bol == true){
            synchronized(this){
                System.out.println("chosen *********************** : "+this.chosen);
                this.chosen++;
            }
            
        }
        return bol;
    }

    public Team getTeamPlayer(String username) { 
        Team actual = null;
        actual = this.home.getTeam(username);
        if(actual == null){
            actual = this.away.getTeam(username);
        }
        return actual;
    }

    public int getChosen() {
       return this.chosen;
    }

    
   
    
   
    
    
    
   
    
}
