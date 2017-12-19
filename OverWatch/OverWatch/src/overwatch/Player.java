/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package overwatch;

/**
 *
 * @author
 */

public class Player {
    
    private String username;
    private String password;
    private int rank;
    
    public Player(String user, String pass){
        this.username = user;
        this.password = pass;
        this.rank = 0;
    }
    
    public Player(String user, String pass,int rank){
        this.username = user;
        this.password = pass;
        this.rank = rank;
    }
    public String getUsername(){
        return this.username;
    }
    
    public String getPassword(){
        return this.password;
    }
    
    public int getRank(){
        return this.rank;
    }
    
    public void setUsername(String user){
        this.username = user;
    }
    
    public void setPassword(String pass){
        this.password = pass;
    }
    
    public void setRank(int rank){
        this.rank = rank;
    }
    
    public synchronized void updateWinnerRank(){
        this.rank+=5;
    }
    
    public synchronized void updateLooserRank(){
        this.rank++;
    }
     
}
