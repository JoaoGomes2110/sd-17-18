/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package overwatch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author
 */
public class Server_Worker implements Runnable {
    
    private Server server;
    private Socket socket;
    public BufferedReader in;
    private BufferedWriter out;
    
    private String username;
    private String password;
   

    
    public Server_Worker(Server server, Socket socket) throws IOException {
        this.server =  server;
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));  
       

    }

    
    @Override
    public void run() {
        try {
            String op;
            boolean success= false;
            
//************************************FAZER O REGISTO**********************************************************
            op = in.readLine();
            username = in.readLine();
            password = in.readLine();
            
            while(!success){
                
                if(op.equals("1")){
                    success = this.server.loginClient(username, password, out);
                } else if(op.equals("2")){
                    success = this.server.registerClient(username, password, out);
                }
                
                if(op.equals("1")&& success==false){
                    System.out.println("Wrong nickname or password, ask client for other try");
                    out.write("0");
                    out.newLine();
                    out.write("Wrong username or password, please try again!");
                    out.newLine();
                    out.flush();
                    
                    op = in.readLine();
                    username = in.readLine();
                    password = in.readLine();
                }
                else if(op.equals("2")&& success == false){
                    System.out.println("This username is already used, ask client for other try");     
                    out.write("0");
                    out.newLine();
                    out.write("This username is already, please choose a diferent one!");
                    out.newLine();
                    out.flush();
                    
                    op = in.readLine();
                    username = in.readLine();
                    password = in.readLine();
                    
                }
               
                
            }
            
            if(op.equals("1")&&success == true){
                System.out.println("SUCCESS Login : Client entered in the system !!");
                out.write("1");
                out.newLine();
                out.write("Login Complete!");
                out.newLine();
                out.flush();
                
            }
            else if (op.equals("2")&&success== true){
                System.out.println("SUCCESS Register : Client entered in the system !!");       
                out.write("1");
                out.newLine();       
                out.write("Register Complete!");
                out.newLine();        
                out.flush();
            }
 
            Player player = this.server.getPlayer(username);
  //********************************************************************************************************************          
  
  //*********************************************Enter the Game - Select Hero ******************************************
    
            op = in.readLine();
            
            while(op.equals("1")){
                
                if(op.equals("1")){
                    boolean testGame = this.server.hasGame(player);
                    Game actualGame = null;
                    if(testGame == true){
                        actualGame = this.server.getGame();
                        out.write("You have been added to the game:  " + actualGame.getName());
                        out.newLine();
                        out.flush();
                        
                    }else if(testGame == false){
                        actualGame = this.server.createGame();
                        out.write("You have been added to the game:  " + actualGame.getName());
                        out.newLine();
                        out.flush();
                    }
                    
                    actualGame.addPlayer(player);
                    Barrier b = actualGame.getBarrier();
                    b.waiting();

                    try { 
                        System.out.println("BEFORE THE SLEEP");
                        out.write("SLEEP");
                        out.newLine();
                        out.flush();
                        
                        HashMap<Integer,Hero> list = actualGame.getHeroeslist();
                        for (int i=1; i<31;i++){
                            String s = list.get(i).getName();
                            int lvl = list.get(i).getOverall();
                            out.write(i + " -> " + s + " Overall : " + lvl);
                            out.newLine();
                            out.flush();
                        }
                        
                        Thread t1 = new Thread(new SW_Listener(actualGame, player, this.server));
                        t1.start();
                        sleep(30000);
                        
                        System.out.println("Select Hero SLEEP ENDED");
                        out.write("END");
                        out.newLine();
                        out.flush();
                        t1.join();

                  
                    } catch (InterruptedException ex) {
                        System.out.println(ex.getMessage());
                    }
                     
                    
//********************************************************************************************************************          
  
//********************************************* Enter the Game - Chat with the team **********************************               
                
                    String message;
                    message = in.readLine();
                    
                    if(message.equals("TEAMS")){


                        out.write("**********************" + actualGame.getName() + "***********************************");
                        out.newLine();
                        out.flush();
                    
                        HashMap<String,String> pH = actualGame.getHometeam().getList();
                        
                        out.write("Home Team:");
                        out.newLine();
                        out.flush();
                    
                        for(String s: pH.keySet()){
                            String msg = "Username: " + s + " -> " + "Hero: " + pH.get(s);
                            out.write(msg);
                            out.newLine();
                            out.flush();
                        }
                        
                        pH = actualGame.getAwayteam().getList();
                        out.write("Away Team:");
                        out.newLine();
                        out.flush();

                        for(String s: pH.keySet()){
                            String msg = "Username: " + s + " -> " + "Hero: " + pH.get(s);
                            out.write(msg);
                            out.newLine();
                            out.flush();
                        }
                        
                        out.write("Game has started !! You can now chat with your teammates");
                        out.newLine();
                        out.flush();
                    }

                    
                    message = in.readLine();
                    
                    if(message.equals("CHAT")){
                        
                        try {
                        
                            Thread t2 = new Thread(new SW_Listener_Chat(actualGame, player, this.server));
                            t2.start();
                            sleep(60000);

                            System.out.println("Chat Game SLEEP ENDED");
                            
                            out.write("END");
                            out.newLine();
                            out.flush();
                            t2.join();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Server_Worker.class.getName()).log(Level.SEVERE, null, ex);
                        }
                     
                    }
                    
                    Team winner = server.simulateGame(actualGame);
                    boolean isWinner = winner.inTeam(this.username);
                    message = in.readLine();
                    if(message.equals("RESULT")){
                        if(isWinner == true){
                            player.updateWinnerRank();
                            
                            out.write("*********************** Congratulations!! **************************");
                            out.newLine();
                            out.flush();

                            out.write("Result : WINNER");
                            out.newLine();
                            out.flush();
                            
                            
                            out.write("Your last Rank : " + (player.getRank()-5));
                            out.newLine();
                            out.flush();

                            out.write("Your NEW Rank : " + player.getRank());
                            out.newLine();
                            out.flush();

                            out.write("*********************************************************************");
                            out.newLine();
                            out.flush();

                            out.write("*********************************************************************");
                            out.newLine();
                            out.flush();
                            
                            out.write("RETURN");
                            out.newLine();
                            out.flush();
                            
                        }
                        else{
                            player.updateLoserRank();
                            
                            out.write("*********************** Better Luck Next Time!! **************************");
                            out.newLine();
                            out.flush();
                            
                            out.write("Result : LOSER");
                            out.newLine();
                            out.flush();
                                        
                            out.write("Your last Rank : " + (player.getRank()-1));
                            out.newLine();
                            out.flush();

                            out.write("Your NEW Rank : " + player.getRank());
                            out.newLine();
                            out.flush();
                            
                            out.write("*********************************************************************");
                            out.newLine();
                            out.flush();

                            out.write("*********************************************************************");
                            out.newLine();
                            out.flush();
                            
                            out.write("RETURN");
                            out.newLine();
                            out.flush();
                        }
                    }
            
                }
                
                op = in.readLine();
            }
            
            System.out.println("> Client "+ this.username + " exit the system");
            this.server.shutdownClient(this.username);
            
            this.socket.shutdownOutput();
            this.socket.shutdownInput();
            this.socket.close();
  
  //********************************************Shutdown Client********************************+++****+++++++++++*++         
        }catch (IOException e) {
            e.printStackTrace();
	}
        
    }

    public class SW_Listener_Chat implements Runnable {
        
    private Game game;
    private Player player;
    private Server server;
        
    public SW_Listener_Chat( Game actualGame, Player j, Server server) {
        this.game = actualGame;
        this.player = j;
        this.server = server; 
    }

        @Override
        public void run() {
        try {
            String message;
            while(!(message = in.readLine()).equals("CHAT_END")){
                this.server.multicastTeam(game, this.player.getUsername(), message,"2");
            }
        } catch (IOException ex) {
            Logger.getLogger(Server_Worker.class.getName()).log(Level.SEVERE, null, ex);
        }
             
        }
}
    
    
    public class SW_Listener implements Runnable {
        
  
    private Game game;
    private Player player;
    private Server server;
    
    public SW_Listener( Game actualGame, Player j, Server server){
        this.game = actualGame;
        this.player = j;
        this.server = server;
    }
	
    @Override
    public void run() {
        String message;
        boolean bol;
        try {
            while(!(message = in.readLine()).equals("TIMEOUT")){
                bol = processMessage(message);
                if(bol ==true){
                    this.server.multicastTeam(game, this.player.getUsername(), message,"1");
                    System.out.println("CLIENT: "+ this.player.getUsername() +" in the game " + this.game.getName() + " send " + message);
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
}

