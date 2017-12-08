/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package overwatch;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


/**
 *
 * @author tiagofraga
 */
public class Server {
    
    private ServerSocket serverSocket;
    private int porta;
    
    
    private HashMap <String, Jogador> jogadores;
    private HashMap <String,BufferedWriter> clients;
    private HashMap <String,Jogo> games;
    
    public Server(int porta) throws IOException{
	this.porta = porta;
        this.jogadores = new HashMap<>();
        this.clients = new HashMap<>();
        this.games = new HashMap<>();
    }
    
    public void startServer(){
	try {
            System.out.println("#### OverWatch SERVER ####");
            this.serverSocket = new ServerSocket(this.porta);

            while(true) {
		System.out.println("ServerMain > Server is running waiting for a new connections...");
		Socket socket = serverSocket.accept();
		
                System.out.println("ServerMain > Connection received! Create worker thread to handle connection.");
		Thread t = new Thread(new Server_Worker(this, socket));
		t.start();			
			
            }
        
        } catch (IOException e) {
            System.out.println("Error accepting connection: " + e.getMessage());
        }
	
    }
    
    public synchronized boolean registerClient(String nick, String pass, BufferedWriter writer){
	Jogador novo = new Jogador(nick,pass);
        if(!(jogadores.containsKey(nick) && clients.containsKey(nick))){
            jogadores.put(nick,novo);
            clients.put(nick, writer);
            return true;
        }else{
            return false;
        }
     
    }
    
    public synchronized boolean loginClient(String nick, String pass, BufferedWriter writer){
        if(jogadores.containsKey(nick)){
            Jogador j = jogadores.get(nick);
            if(j.getPassword().equals(pass)){
                clients.put(nick, writer);
                return true;
            }
        }
        
        return false;
    }
    
    public synchronized void shutdownClient(String nick){
        clients.remove(nick);
    }
    
    public static void main(String[] args) throws IOException {
        Server s = new Server(12345);
        s.startServer();
       
    }

    public Jogador getJogador(String username) {
        return this.jogadores.get(username);
    }

    public boolean hasGame() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Jogo createGame() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Jogo getGame() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void multicastGame(Jogo actualGame, String msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
    
}
