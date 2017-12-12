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
import java.net.Socket;

/**
 *
 * @author tiagofraga
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
                    success = this.server.clientLogin(username, password, out);
                } else if(op.equals("2")){
                    success = this.server.clientRegister(username, password, out);
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
 
            Player jogador = this.server.getPlayer(username);
  //********************************************************************************************************************          
  
  //*********************************************Enter the Game*********************************************************
    
            op = in.readLine();
                
                if(op.equals("1")){
                    boolean testGame = this.server.hasGame();
                    Game actualGame = null;
                    if(testGame == true){
                        actualGame = this.server.getGame();
                        out.write("Foste adicionado ao jogo: "+ actualGame.getName());
                        out.newLine();
                        out.flush();
                        
                    }else if(testGame == false){
                        actualGame = this.server.createGame();
                        out.write("Foste adicionado ao jogo: "+ actualGame.getName());
                        out.newLine();
                        out.flush();
                    }
                    
                    actualGame.addJogador(jogador);
                    Barrier b = actualGame.getBarrier();
                    b.esperar();
                    out.write("Ide todos po crl");
                    out.newLine();
                    out.flush();
//                    this.server.multicastGame(actualGame, "Ide todos po crl", jogador.getUsername());
//                    this.server.showHerois(actualGame);
                        
                }else if(op.equals("2")){
                    
                }
               
            
            
            System.out.println("> Client "+ this.username + " exit the system");
            this.server.clientShutdown(this.username);
            
            this.socket.shutdownOutput();
            this.socket.shutdownInput();
            this.socket.close();
  
  //*+*******************************************Shutdown Client********************************+++****+++++++++++*++         
        }catch (IOException e) {
			e.printStackTrace();
	}
        
    }
    
}
