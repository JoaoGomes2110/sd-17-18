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
    private BufferedReader in;
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
  
  //*********************************************Enter the Game******************************************************
    
            op = in.readLine();
            
            while(op.equals("1")){
                
                if(op.equals("1")){
                    boolean testGame = this.server.hasGame();
                    Game actualGame = null;
                    if(testGame == true){
                        actualGame = this.server.getGame();
                        out.write("You have been added to the game: "+ actualGame.getName());
                        out.newLine();
                        out.flush();
                        
                    }else if(testGame == false){
                        actualGame = this.server.createGame();
                        out.write("You have been added to the game: "+ actualGame.getName());
                        out.newLine();
                        out.flush();
                    }
                    
                    actualGame.addPlayer(player);
                    Barrier b = actualGame.getBarrier();
                    b.waiting();
                    
                    // Have the 10 players
                    
                    HashMap<Integer,Hero> list = actualGame.getHeroeslist();
                    for (int i=1; i<31;i++){
                       String s = list.get(i).getName();
                       out.write(i + " -> "+ s);
                       out.newLine();
                       out.flush();
                    }
                    
                    int chosen = actualGame.getChosen();
                    while(chosen<5){
                        Thread t1 = new Thread(new SW_Listener(this.in, this.out, actualGame, player, this.server));
                        t1.start();
                        try { 
                            System.out.println("BEFORE THE SLEEP");
                            sleep(30000);
                            System.out.println("SLEEP ENDED");
                            chosen = actualGame.getChosen();
                            if(chosen<5){
                                out.write("ALL HEROES WERE NOT CHOSEN, CHOOSE ANOTHER:");
                                out.newLine();
                                out.flush();
                            }
                        } catch (InterruptedException ex) {
                        Logger.getLogger(Server_Worker.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                    System.out.println("ENDDDDDDDDDD");
                    out.write("END");
                    out.newLine();
                    out.flush();
                    out.write("END");
                    out.newLine();
                    out.flush();
                    
                    
                        
                    
                    
                    

                    
                    
                        
                }else if(op.equals("2")){
                
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

}

