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
    
    public HashMap<String,String> getList(){
        HashMap<String,String> newList = new HashMap<>();
        for(Player p: list.keySet()){
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
    
}
