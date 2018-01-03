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
 * @author
 */
public class Team {
    
    
    private HashMap<Player,Hero> list;
    private int quantity;
    
    
    public Team(){
        this.list = new HashMap<>();
        this.quantity = 0;
    }

    public int getQuantity() {
        return this.quantity;
    }
    
    public ArrayList<Player> getPlayersList(){
        ArrayList<Player> players = new ArrayList<>();
        for(Player p: this.list.keySet()){
            players.add(p);
        }
        return players;
    }
    
    
    public synchronized void addPlayer(Player j) {
        this.list.put(j,null);
        this.quantity++;
    }
    
    public synchronized boolean addHero(Player j, Hero h){
        if(!list.containsValue(h)){
            list.put(j,h); 
            return true;
        }
        return false;
    }
    
    public HashMap<Player,Hero> getPlayerHeroList(){
        HashMap<Player,Hero> newList = new HashMap<>();
        for(Player p : list.keySet()){
            newList.put(p,this.list.get(p));
        }
        return newList;
    }
    public HashMap<String,String> getList(){
        HashMap<String,String> newList = new HashMap<>();
        for(Player p: list.keySet()){
            if(list.get(p) != null)
                newList.put(p.getUsername(),this.list.get(p).getName());
        }
        return newList;
    }
  
    public ArrayList<String> getPlayers(){
        ArrayList<String> neww = new ArrayList<>();
        for(Player j: list.keySet()){
            neww.add(j.getUsername());
        }
        return neww;
        
    }

    public Team getTeam(String username) {
        for(Player j : list.keySet()){
            if(j.getUsername().equals(username)){
                return this;
            }
        }
        return null;
    }

    public boolean verificaHero(Player player) {
        boolean b = false;
        Hero h = list.get(player);
        if(!(h == null)){
            b=true;
        }
        
        return b;
    }

    public int getTeamOverall() {
        int total=0;
        int n;
        for(Player p : list.keySet()){
            n = list.get(p).getOverall();
            total+=n; 
        }
        return total;
    }
    
    public int getMediumRank(){
        int rank = 0;
        int sum = 0;
        if(this.quantity == 0){
            return 0;
        }
        for(Player p: list.keySet()){
            sum += p.getRank();
        }
        rank = sum/this.quantity;
        return rank;
    }
    public synchronized void updateRank(String op) {
        if(op.equals("1")){
           for(Player p : list.keySet()) {
               p.updateWinnerRank();
           }
        }else if(op.equals("2")){
            for(Player p : list.keySet()){
                p.updateLoserRank();
            }
            
        }
    }

    public boolean inTeam(String username) {
       for (Player p : list.keySet()){
           if(p.getUsername().equals(username)){
               return true;
           }
       }
       return false;
    }
    
}
