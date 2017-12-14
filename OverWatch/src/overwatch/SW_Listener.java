/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package overwatch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author
 */
public class SW_Listener implements Runnable {
        
    private BufferedReader in;
    private Game game;
    private Player player;
    private Server server;
    
    public SW_Listener(BufferedReader in, Game actualGame, Player j, Server server){
        this.in = in;
        this.game = actualGame;
        this.player = j;
        this.server = server;
    }
	
    @Override
    public void run() {
        String message;
        boolean bol;
        try {
            while((message = in.readLine()) != null){
                bol = processMessage(message);
                if(bol ==true){
                    this.server.multicastTeam(game, this.player.getUsername(), message);
                    System.out.println("CLIENT: "+ this.player.getUsername() +"in the game " + this.game.getName() + "send " + message);
                }
                
            }
        }catch (SocketException e) {}
        catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public boolean processMessage(String message) {
        return this.game.updateHero(this.player, message);
        
    }
}
            
        
    