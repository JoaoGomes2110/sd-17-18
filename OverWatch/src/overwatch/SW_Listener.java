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
 * @author tiagofraga
 */
public class SW_Listener implements Runnable {
        
    private BufferedReader in;
    private BufferedWriter out;
    private Jogo game;
    private Jogador jogador;
    private Server server;
    
    public SW_Listener(BufferedReader in, BufferedWriter out, Jogo actualGame, Jogador j, Server server){
        this.in = in;
        this.out = out;
        this.game = actualGame;
        this.jogador = j;
        this.server = server;
    }
	
    @Override
    public void run() {
        String message;
        boolean bol;
        try {
            while((message = in.readLine()) != null){
                bol = processaMenssagem(message);
                if(bol ==true){
                    this.server.multicastTeam(game, this.jogador.getUsername(), message);
                    System.out.println("CLIENT: "+ this.jogador.getUsername() +"in the game " + this.game.getNome() + "send " + message);
                }
                
            }
        }catch (SocketException e) {}
        catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public boolean processaMenssagem(String message) {
        return this.game.atualizaHeroi(this.jogador, message);
        
    }
}
            
        
    