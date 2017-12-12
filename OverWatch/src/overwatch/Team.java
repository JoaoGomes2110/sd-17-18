/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package overwatch;

import java.util.ArrayList;

/**
 *
 * @author 
 */
public class Team {
    
    
    private Player[] players;
    private Hero[] avatars;
    private int quantity;
    
    public Team(){
        this.players = new Player[5];
        this.avatars = new Hero[5];
        this.quantity = 0;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public synchronized void addPlayer(Player j) {
        this.players[quantity]=j;
        this.quantity++;
    }

    public Player getPlayer(int i){
        return this.players[i];
    }
    
    public Hero getHero(int i) {
        return this.avatars[i];
    }
    
    public void updateRank(){
        for(int i=0; i<this.quantity; i++){
            this.players[i].updateRank();
        }
    }
    
    public ArrayList<String> getPlayers(){
        ArrayList<String> nova = new ArrayList<>();
        for(Player j: players){
            nova.add(j.getUsername());
        }
        return nova;
        
    }
    
}
