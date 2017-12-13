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
   private int porta;
    
    
   private HashMap <String, Jogador> jogadores;
   private HashMap <String,BufferedWriter> clients;
   private HashMap <Integer,Jogo> games;
   private HashMap <Integer,Heroi> listaHerois;
   private int number;
    
   public Server(int porta) throws IOException{
	this.porta = porta;
        this.jogadores = new HashMap<>();
        this.clients = new HashMap<>();
        this.games = new HashMap<>();
        this.listaHerois = new HashMap<>();
        this.number = 0;
    }
    
   public void startServer(){
	try {
            System.out.println("#### OverWatch SERVER ####");
            this.serverSocket = new ServerSocket(this.porta);
            carregarListaHerois(this.listaHerois);
            System.out.println("> Loading DataBase...");
            carregarListaJogadores();

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
        if(!(jogadores.containsKey(nick))){
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

   public synchronized Jogo createGame() {
        Jogo jogo = new Jogo ("Game"+String.valueOf(this.number));
        this.games.put(this.number,jogo);
        HashMap<Integer,Heroi> lista = jogo.getListaHerois();
        carregarListaHerois(lista);
        this.number++;
        return jogo;     
    }

   public synchronized Jogo getGame() {
        for(Jogo j : this.games.values()){
            if(j.getQuantidade()<10){
                return j;
            }
        }
        return null;
    }

   public void multicastGame(Jogo actualGame, String msg, String actualPlayer) {
        
       ArrayList<String> jogadoresCasa;
        //ArrayList<String> jogadoresFora;
        Equipa casa = actualGame.getEquipaCasa();
        //Equipa fora = actualGame.getEquipaFora();
        jogadoresCasa = casa.getJogadores();
        //jogadoresFora = fora.getJogadores();
        
        
        
        HashMap<String, BufferedWriter> clientsToSend = new HashMap<>();
        
        for(String s: clients.keySet()){
            for(String a: jogadoresCasa){
                if(s.equals(a)){ 
                    clientsToSend.put(a,clients.get(s));
                }  
            } 
        }
        
        /*
        for(Map.Entry<String, BufferedWriter> s: clients.entrySet()){
            for(String b: jogadoresFora){
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

   public void carregarListaJogadores(){
       this.jogadores.put("tiagofraga",new Jogador("tiagofraga","tiago"));
       this.jogadores.put("joaogomes",new Jogador("joaogomes","joao"));
       this.jogadores.put("cesarioperneta",new Jogador("cesarioperneta","cesario"));
   }
    
   public void carregarListaHerois(HashMap<Integer,Heroi> listaherois){
       listaherois.put(1, new Heroi("PintodaCosta", 99));
       listaherois.put(2, new Heroi("Aboubakar", 83));
       listaherois.put(3, new Heroi("Marega", 99));
       listaherois.put(4, new Heroi("Brahimi", 94));
       listaherois.put(5, new Heroi("DaniloPereira", 93));
       listaherois.put(6, new Heroi("AndreAndre", 85));
       listaherois.put(7, new Heroi("RicardoPereira", 92));
       listaherois.put(8, new Heroi("Felipe", 91));
       listaherois.put(9, new Heroi("TiquinhoSoares",90));
       listaherois.put(10, new Heroi("SergioConceicao",95));
       listaherois.put(11, new Heroi("BrunoCarvalho",70));
       listaherois.put(12, new Heroi("GelsonMartins",80));
       listaherois.put(13, new Heroi("WiliamCarvalho",75));
       listaherois.put(14, new Heroi("BrunoFernandes",79));
       listaherois.put(15, new Heroi("Coates",60));
       listaherois.put(16, new Heroi("BasDost",70));
       listaherois.put(17, new Heroi("RuiPatricio",77));
       listaherois.put(18, new Heroi("FabioCoentrao",50));
       listaherois.put(19, new Heroi("DanielPodence",70));
       listaherois.put(20, new Heroi("JorgeJesus",70));
       listaherois.put(21, new Heroi("LuisFilipeVieira",0));
       listaherois.put(22, new Heroi("Jonas",10));
       listaherois.put(23, new Heroi("Pizzi",5));
       listaherois.put(24, new Heroi("Luisao",2));
       listaherois.put(25, new Heroi("Fejsa",1));
       listaherois.put(26, new Heroi("AndreAlmeida",3));
       listaherois.put(27, new Heroi("Cervi",5));
       listaherois.put(28, new Heroi("Jardel",1));
       listaherois.put(29, new Heroi("Eliseu",15));
       listaherois.put(30, new Heroi("RuiVitoria",1));
     
   }

   public boolean hasGame() {
        for(Jogo j : this.games.values()){
            if(j.getQuantidade()<10){
                return true;
            }
        }
        return false;    
    }

    public void multicastTeam(Jogo actualGame, String username, String heroi) {
          
        ArrayList<String> jogadoresAtual;
        Equipa atual = actualGame.getEquipaJogador(username);
        jogadoresAtual = atual.getJogadores();
        
        
        
        HashMap<String, BufferedWriter> clientsToSend = new HashMap<>();
        
        for(String s: clients.keySet()){
            for(String a: jogadoresAtual){
                if(s.equals(a)){ 
                    clientsToSend.put(a,clients.get(s));
                }  
            } 
        }
        
	for(String user : clientsToSend.keySet()) {
            
            try {
                if(!user.equals(username)){
                    BufferedWriter bw = clientsToSend.get(user);
                    for(int i =1; i<31; i++){
                        if(i == Integer.parseInt(heroi)){
                            bw.write(i + " -> "+ this.listaHerois.get(i) + "-> CHOOSEN BY: "+ username);
                            bw.newLine();
                            bw.flush();
                        }else{
                            bw.write(i + " -> "+ this.listaHerois.get(i));
                            bw.newLine();
                            bw.flush();
                            
                        }
                    }
                }
            } catch (IOException e) {
		e.printStackTrace();
            }
        }
        
    }

   
}

