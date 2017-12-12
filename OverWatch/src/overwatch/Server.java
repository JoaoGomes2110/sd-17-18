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
 * @author tiagofraga
 */
public class Server {
    
   private ServerSocket serverSocket;
   private int port;
    
    
   private HashMap <String, Player> players;
   private HashMap <String,BufferedWriter> clients;
   private HashMap <Integer,Game> games;
   private HashMap <String,Hero> heroesList;
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
            System.out.println("> Loading heros...");
            carregarheroesList();
            System.out.println("> Loading DataBase...");
            carregarListaplayers();

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
    
   public synchronized boolean clientRegister(String nick, String pass, BufferedWriter writer){
	Player novo = new Player(nick,pass);
        if(!(players.containsKey(nick))){
            players.put(nick,novo);
            clients.put(nick, writer);
            return true;
        }else{
            return false;
        }
     
    }
    
   public synchronized boolean clientLogin(String nick, String pass, BufferedWriter writer){
        if(players.containsKey(nick)){
            Player j = players.get(nick);
            if(j.getPassword().equals(pass)){
                clients.put(nick, writer);
                return true;
            }
        }
        
        return false;
    }
    
   public synchronized void clientShutdown(String nick){
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
        this.number++;
        return jogo;     
    }

   public synchronized Game getGame() {
        for(Game j : this.games.values()){
            if(j.getQuantidade()<10){
                return j;
            }
        }
        return null;
    }

   public synchronized void multicastGame(Game actualGame, String msg, String actualPlayer) {
        
       ArrayList<String> homePlayers;
        //ArrayList<String> awayPlayers;
        Team home = actualGame.getHomeTeam();
        //Team away = actualGame.getAwayTeam();
        homePlayers = home.getPlayers();
        //awayPlayers = fora.getplayers();
        
        
        
        HashMap<String, BufferedWriter> clientsToSend = new HashMap<>();
        
        for(String s: clients.keySet()){
            for(String a: homePlayers){
                if(s.equals(a)){ 
                    clientsToSend.put(a,clients.get(s));
                }  
            } 
        }
        
        /*
        for(Map.Entry<String, BufferedWriter> s: clients.entrySet()){
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
   
   public synchronized void showHeroes(Game actualGame){
       
        ArrayList<String> homePlayers;
        //ArrayList<String> awayPlayers;
        Team home = actualGame.getHomeTeam();
        //Equipa fora = actualGame.getAwayTeam();
        homePlayers = home.getPlayers();
        //awayPlayers = fora.getplayers();

       HashMap<String, BufferedWriter> clientsToSend = new HashMap<>();
        
        for(String s: clients.keySet()){
            for(String a: homePlayers){
                if(s.equals(a)){ 
                    clientsToSend.put(a,clients.get(s));
                }  
            } 
        }
        
        /*
        for(Map.Entry<String, BufferedWriter> s: clients.entrySet()){
            for(String b: awayPlayers){
                if(s.equals(b)){
                    clientsToSend.put(b,clients.get(s));
                }
            } 
        }
        */
       for(BufferedWriter out : clientsToSend.values()){ 
           int i = 1;
           for(String s: this.heroesList.keySet()){
              try{
                String hero = i + " - " + s;   
                out.write(hero);
                out.newLine();
                out.flush();
                i++;
              }catch(IOException e){
                  System.out.println(e.getMessage());
              }
            }
       }
   }

   public void carregarListaplayers(){
       this.players.put("tiagofraga",new Player("tiagofraga","tiago"));
       this.players.put("joaogomes",new Player("joaogomes","joao"));
       this.players.put("cesarioperneta",new Player("cesarioperneta","cesario"));
   }
    
   public void carregarheroesList(){
       this.heroesList.put("PintodaCosta", new Hero("PintodaCosta", 70));
       this.heroesList.put("Aboubakar", new Hero("Aboubakar", 75));
       this.heroesList.put("Marega", new Hero("Marega", 80));
       this.heroesList.put("Brahimi", new Hero("Brahimi", 79));
       this.heroesList.put("DaniloPereira", new Hero("DaniloPereira", 60));
       this.heroesList.put("AndreAndre", new Hero("AndreAndre", 70));
       this.heroesList.put("RicardoPereira", new Hero("RicardoPereira", 50));
       this.heroesList.put("Felipe", new Hero("Felipe", 82));
       this.heroesList.put("JoseSa", new Hero("JoseSa",88));
       this.heroesList.put("TiquinhoSoares", new Hero("TiquinhoSoares",19));
       this.heroesList.put("SergioConceicao", new Hero("SergioConceicao",60));
       this.heroesList.put("BrunoCarvalho", new Hero("BrunoCarvalho",97));
       this.heroesList.put("GelsonMartins", new Hero("GelsonMartins",100));
       this.heroesList.put("WiliamCarvalho", new Hero("WiliamCarvalho",96));
       this.heroesList.put("BrunoFernandes", new Hero("BrunoFernandes",94));
       this.heroesList.put("Coates", new Hero("Coates",93));
       this.heroesList.put("BasDost", new Hero("BasDost",92));
       this.heroesList.put("RuiPatricio", new Hero("RuiPatricio",99));
       this.heroesList.put("FabioCoentrao", new Hero("FabioCoentrao",91));
       this.heroesList.put("DanielPodence", new Hero("DanielPodence",94));
       this.heroesList.put("JorgeJesus", new Hero("JorgeJesus",89));
       this.heroesList.put("LuisFilipeVieira", new Hero("LuisFilipeVieira",0));
       this.heroesList.put("Jonas", new Hero("Jonas",10));
       this.heroesList.put("Pizzi", new Hero("Pizzi",5));
       this.heroesList.put("Luisao", new Hero("Luisao",2));
       this.heroesList.put("Fejsa", new Hero("Fejsa",1));
       this.heroesList.put("AndreAlmeida", new Hero("AndreAlmeida",3));
       this.heroesList.put("Cervi", new Hero("Cervi",5));
       this.heroesList.put("Jardel", new Hero("Jardel",1));
       this.heroesList.put("RuiVitoria", new Hero("RuiVitoria",1));
     
   }

   public boolean hasGame() {
        for(Game j : this.games.values()){
            if(j.getQuantidade()<10){
                return true;
            }
        }
        return false;    
    }
   
}
