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
            boolean sucesso= false;
            
//************************************FAZER O REGISTO**********************************************************
            op = in.readLine();
            username = in.readLine();
            password = in.readLine();

            while(!(sucesso)){
                if(op.equals("1")){
                    sucesso = this.server.loginClient(username, password, out);
                    if(!sucesso){
                        System.out.println("Wrong nickname or password, ask client for other try");
                        out.write("Wrong username or password, please try again!");
                        out.newLine();
                        out.flush();
                    }
                    else{
                        System.out.println("SUCCESS Login : Client entered in the system !!");
                        out.write("Login Complete!");
                        out.newLine();
                        out.flush();
                    }
                }
            
                else if (op.equals("2")){
                    sucesso = this.server.registerClient(username, password, out);
                    if(!sucesso){
                        System.out.println("This username is already used, ask client for other try");
                        out.write("This username is already, please choose a diferent one!");
                        out.newLine();
                        out.flush();
                    }
                    else{
                        System.out.println("SUCCESS Register : Client entered in the system !!");
                        out.write("Register Complete!");
                        out.newLine();
                        out.flush();
                    }
                }
                else if (sucesso==false){
                    op= in.readLine();
                }
            }
            
            Jogador jogador = this.server.getJogador(username);
  //********************************************************************************************************************          
  
  //*********************************************Enter the Game******************************************************
    
            op = in.readLine();
            sucesso = false;
            String gameName;
            while(!sucesso){
                if(op.equals("1")){
                    boolean game = this.server.hasGame();
                    Jogo actualGame;
                    if(game){
                        actualGame = this.server.getGame();
                        gameName=actualGame.addJogador(jogador);
                    }else{
                        actualGame = this.server.createGame();
                        gameName = actualGame.addJogador(jogador);    
                    }
                    
                    System.out.println("Game "+ gameName+ "has one more player : " +this.username);
                    out.write("You entered in the game: "+gameName);
                    out.newLine();
                    out.flush();
                    
                    
                    //continuar aqui!!!
                    while(actualGame.getQuantidade()<10){
                        String msg = "Waiting for other players...";
                        this.server.multicastGame(actualGame,msg);
                        System.out.println("Game "+gameName+" is waiting for the 10 players");
                    }
                    
                     
                    
                    
                    
                    
                    
                    
                    
                    sucesso = false;
                    
                }else if(op.equals("2")){
                    sucesso = true;
                }
                
            }
       
  
  
  
  //*+*******************************************Shutdown Client********************************+++****+++++++++++*++         
        }catch (IOException e) {
			e.printStackTrace();
	}
	finally {
            this.server.shutdownClient(this.username);			
	}
        
    }
    
}
