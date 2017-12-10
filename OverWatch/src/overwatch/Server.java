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
    private HashMap <Integer,Jogo> games;
    private HashMap <String,Heroi> listaherois;
    private int number;
    
    public Server(int porta) throws IOException{
	this.porta = porta;
        this.jogadores = new HashMap<>();
        this.clients = new HashMap<>();
        this.games = new HashMap<>();
        this.listaherois = new HashMap<>();
        this.number = 0;
    }
    
    public void startServer(){
	try {
            System.out.println("#### OverWatch SERVER ####");
            this.serverSocket = new ServerSocket(this.porta);
            System.out.println("> Loading Herois...");
            carregarListaHerois();
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
        this.number++;
        return jogo;     
    }

    public synchronized Jogo getGame() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void multicastGame(Jogo actualGame, String msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   public void carregarListaJogadores(){
       this.jogadores.put("tiagofraga",new Jogador("tiagofraga","tiago"));
       this.jogadores.put("joaogomes",new Jogador("joaogomes","joao"));
       this.jogadores.put("cesarioperneta",new Jogador("cesarioperneta","cesario"));
   }
    
   public void carregarListaHerois(){
       this.listaherois.put("PintodaCosta", new Heroi("PintodaCosta", 99));
       this.listaherois.put("Aboubakar", new Heroi("Aboubakar", 83));
       this.listaherois.put("Marega", new Heroi("Marega", 99));
       this.listaherois.put("Brahimi", new Heroi("Brahimi", 94));
       this.listaherois.put("DaniloPereira", new Heroi("DaniloPereira", 93));
       this.listaherois.put("AndreAndre", new Heroi("AndreAndre", 85));
       this.listaherois.put("RicardoPereira", new Heroi("RicardoPereira", 92));
       this.listaherois.put("Felipe", new Heroi("Felipe", 91));
       this.listaherois.put("JoseSa", new Heroi("JoseSa",93));
       this.listaherois.put("TiquinhoSoares", new Heroi("TiquinhoSoares",90));
       this.listaherois.put("SergioConceicao", new Heroi("SergioConceicao",95));
       this.listaherois.put("BrunoCarvalho", new Heroi("BrunoCarvalho",70));
       this.listaherois.put("GelsonMartins", new Heroi("GelsonMartins",80));
       this.listaherois.put("WiliamCarvalho", new Heroi("WiliamCarvalho",75));
       this.listaherois.put("BrunoFernandes", new Heroi("BrunoFernandes",79));
       this.listaherois.put("Coates", new Heroi("Coates",60));
       this.listaherois.put("BasDost", new Heroi("BasDost",70));
       this.listaherois.put("RuiPatricio", new Heroi("RuiPatricio",77));
       this.listaherois.put("FabioCoentrao", new Heroi("FabioCoentrao",50));
       this.listaherois.put("DanielPodence", new Heroi("DanielPodence",70));
       this.listaherois.put("JorgeJesus", new Heroi("JorgeJesus",70));
       this.listaherois.put("LuisFilipeVieira", new Heroi("LuisFilipeVieira",0));
       this.listaherois.put("Jonas", new Heroi("Jonas",10));
       this.listaherois.put("Pizzi", new Heroi("Pizzi",5));
       this.listaherois.put("Luisao", new Heroi("Luisao",2));
       this.listaherois.put("Fejsa", new Heroi("Fejsa",1));
       this.listaherois.put("AndreAlmeida", new Heroi("AndreAlmeida",3));
       this.listaherois.put("Cervi", new Heroi("Cervi",5));
       this.listaherois.put("Jardel", new Heroi("Jardel",1));
       this.listaherois.put("Eliseu", new Heroi("Eliseu",15));
       this.listaherois.put("RuiVitoria", new Heroi("RuiVitoria",1));
     
   }
   
}
