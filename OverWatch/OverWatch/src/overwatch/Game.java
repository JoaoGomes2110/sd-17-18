/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package overwatch;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {
    
    private String name;
    private Team home;
    private Team away;
    private HashMap<Integer,Hero> heroeslist;
   
    private Barrier barrier;
    private int quantity;
    
    public Game (String name){
        this.name = name;
        this.home = new Team();
        this.away = new Team();
        this.heroeslist = new HashMap<>();
        this.barrier = new Barrier(5);
        this.quantity = 0;    
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
        int homeDifference = Math.abs(this.home.getMediumRank() - player.getRank());
        int awayDifference = Math.abs(this.away.getMediumRank() - player.getRank());
        if(this.home.getQuantity()<5 && homeDifference <= awayDifference){
            this.home.addPlayer(player);
        }else if(this.away.getQuantity()<5){
            this.away.addPlayer(player);  
        }
        
    }
    
    public synchronized int getMinRank(){
        ArrayList<Player> pH = this.home.getPlayersList();
        int min = pH.get(0).getRank();
        for(Player p: pH){
            if(min > p.getRank())
                min = p.getRank();
        }
        ArrayList<Player> pA = this.away.getPlayersList();
        for(Player p: pA){
            if(min > p.getRank())
                min = p.getRank();
        }
        return min;
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
        
        if(bol == false){
            System.out.println("A player tried to choose a hero already choosen!!");
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

    public void giveHero(Player player) {
        boolean b = false;
        while(b == false){
            int randomNum = 1 + (int)(Math.random() * ((30 - 1) + 1));
            b = updateHero(player, Integer.toString(randomNum));
        }
    }
}
