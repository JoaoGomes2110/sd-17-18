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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author
 */
public class Server {
    
   private ServerSocket serverSocket;
   private int port;
    
    
   private HashMap <String, Player> players;
   private HashMap <String,BufferedWriter> clients;
   private HashMap <Integer,Game> games;
   private HashMap <Integer,Hero> heroesList;
   private int number;
    
   public Server(int port) throws IOException{
	this.port = port;
        this.players = new HashMap<>();
        this.clients = new HashMap<>();
        this.games = new HashMap<>();
        this.heroesList = new HashMap<>();
        this.number = 0;
    }
    
   public void startServer(){
	try {
            System.out.println("#### OverWatch SERVER ####");
            this.serverSocket = new ServerSocket(this.port);
            loadHeroesList(this.heroesList);
            System.out.println("> Loading DataBase...");
            loadPlayersList();

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
	Player novo = new Player(nick,pass);
        if(!(players.containsKey(nick))){
            players.put(nick,novo);
            clients.put(nick, writer);
            return true;
        }else{
            return false;
        }
     
    }
    
   public synchronized boolean loginClient(String nick, String pass, BufferedWriter writer){
        if(players.containsKey(nick)){
            Player j = players.get(nick);
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

   public Player getPlayer(String username) {
        return this.players.get(username);
    }

   public synchronized Game createGame() {
        Game jogo = new Game ("Game"+String.valueOf(this.number));
        this.games.put(this.number,jogo);
        HashMap<Integer,Hero> lista = jogo.getHeroeslist();
        loadHeroesList(lista);
        this.number++;
        return jogo;     
    }

   public synchronized Game getGame() {
        for(Game j : this.games.values()){
            if(j.getQuantity()<10){
                return j;
            }
        }
        return null;
    }

   public void multicastGame(Game currentGame, String msg, String actualPlayer) {
        
       ArrayList<String> homePlayers;
        //ArrayList<String> awayPlayers;
        Team home = currentGame.getHometeam();
        //Team away = currentGame.getAwayTeam();
        homePlayers = home.getPlayers();
        //awayPlayers = away.getplayers();
        
        
        
        HashMap<String, BufferedWriter> clientsToSend = new HashMap<>();
        
        for(String s: clients.keySet()){
            for(String a: homePlayers){
                if(s.equals(a)){ 
                    clientsToSend.put(a,clients.get(s));
                }  
            } 
        }
        
        /*
        for(String s: clients.keySet()){
            for(String b: awayPlayers){
                if(s.equals(b)){
                    clientsToSend.put(b,clients.get(s));
                }
            } 
        }
        */
	for(String user : clientsToSend.keySet()) {
            
            try {
                if(!user.equals(actualPlayer)){
                    BufferedWriter bw = clientsToSend.get(user);
                    bw.write(msg);
                    bw.newLine();
                    bw.flush();
                }
            } catch (IOException e) {
		e.printStackTrace();
            }
        }
    }

   public void loadPlayersList(){
       this.players.put("tiagofraga",new Player("tiagofraga","tiago"));
       this.players.put("joaogomes",new Player("joaogomes","joao"));
       this.players.put("cesarioperneta",new Player("cesarioperneta","cesario"));
   }

   public boolean hasGame() {
        for(Game j : this.games.values()){
            if(j.getQuantity()<10){
                return true;
            }
        }
        return false;    
    }

   public void multicastTeam(Game currentGame, String username, String heroi) {
          
        ArrayList<String> currentPlayers;
        Team current = currentGame.getTeamPlayer(username);
        currentPlayers = current.getPlayers();
        
        
        
        HashMap<String, BufferedWriter> clientsToSend = new HashMap<>();
        
        for(String s: clients.keySet()){
            for(String a: currentPlayers){
                if(s.equals(a)){ 
                    clientsToSend.put(a,clients.get(s));
                }  
            } 
        }
        
	for(String user : clientsToSend.keySet()) {
            
            try {
                if(!user.equals(username)){
                    BufferedWriter bw = clientsToSend.get(user);
                    int i = Integer.parseInt(heroi);
                    bw.write(i + " -> "+ this.heroesList.get(i).getName() + "-> CHOOSEN BY: "+ username);
                    bw.newLine();
                    bw.flush();
                    
                }
            } catch (IOException e) {
		e.printStackTrace();
            }
        }
        
    }
    
   public void loadHeroesList(HashMap<Integer,Hero> heroesList){
       heroesList.put(1, new Hero("PintodaCosta", 60));
       heroesList.put(2, new Hero("Aboubakar", 80));
       heroesList.put(3, new Hero("Marega", 75));
       heroesList.put(4, new Hero("Brahimi", 79));
       heroesList.put(5, new Hero("DaniloPereira", 79));
       heroesList.put(6, new Hero("AndreAndre", 60));
       heroesList.put(7, new Hero("RicardoPereira", 70));
       heroesList.put(8, new Hero("Felipe", 85));
       heroesList.put(9, new Hero("TiquinhoSoares",70));
       heroesList.put(10, new Hero("SergioConceicao",60));
       heroesList.put(11, new Hero("BrunoCarvalho",99));
       heroesList.put(12, new Hero("GelsonMartins",99));
       heroesList.put(13, new Hero("WiliamCarvalho",93));
       heroesList.put(14, new Hero("BrunoFernandes",94));
       heroesList.put(15, new Hero("Coates",91));
       heroesList.put(16, new Hero("BasDost",92));
       heroesList.put(17, new Hero("RuiPatricio",99));
       heroesList.put(18, new Hero("FabioCoentrao",95));
       heroesList.put(19, new Hero("DanielPodence",93));
       heroesList.put(20, new Hero("JorgeJesus",97));
       heroesList.put(21, new Hero("LuisFilipeVieira",0));
       heroesList.put(22, new Hero("Jonas",10));
       heroesList.put(23, new Hero("Pizzi",5));
       heroesList.put(24, new Hero("Luisao",2));
       heroesList.put(25, new Hero("Fejsa",1));
       heroesList.put(26, new Hero("AndreAlmeida",3));
       heroesList.put(27, new Hero("Cervi",5));
       heroesList.put(28, new Hero("Jardel",1));
       heroesList.put(29, new Hero("Eliseu",15));
       heroesList.put(30, new Hero("RuiVitoria",1));
     
   }
   
}

