/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package overwatch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * @author tiagofraga
 */
public class Cliente {
    
    private String hostname;
    private int porto;
    
    public Socket socket;
    public BufferedReader in;
    public BufferedWriter out;
    
    private Jogador jogador;
    private Heroi heroi;
    
    
    public static void main(String[] args) {
       
    }
    
    
    public class ClientListener implements Runnable {
        
        public ClientListener(){}
		
        public void run(){
            String message;
            try {
		while((message = in.readLine()) != null){
                    System.out.println(message);
		}
            }catch (SocketException e) {}
            catch (IOException e) {
		e.printStackTrace();
            }
        }
    }
    
}
