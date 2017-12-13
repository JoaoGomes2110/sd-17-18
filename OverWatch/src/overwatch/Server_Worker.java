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
            
            while(!sucesso){
                
                if(op.equals("1")){
                    sucesso = this.server.loginClient(username, password, out);
                } else if(op.equals("2")){
                    sucesso = this.server.registerClient(username, password, out);
                }
                
                if(op.equals("1")&& sucesso==false){
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
                else if(op.equals("2")&& sucesso == false){
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
            
            if(op.equals("1")&&sucesso == true){
                System.out.println("SUCCESS Login : Client entered in the system !!");
                out.write("1");
                out.newLine();
                out.write("Login Complete!");
                out.newLine();
                out.flush();
                
            }
            else if (op.equals("2")&&sucesso== true){
                System.out.println("SUCCESS Register : Client entered in the system !!");       
                out.write("1");
                out.newLine();       
                out.write("Register Complete!");
                out.newLine();        
                out.flush();
            }
 
            Jogador jogador = this.server.getJogador(username);
  //********************************************************************************************************************          
  
  //*********************************************Enter the Game******************************************************
    
            op = in.readLine();
            
            while(op.equals("1")){
                
                if(op.equals("1")){
                    boolean testGame = this.server.hasGame();
                    Jogo actualGame = null;
                    if(testGame == true){
                        actualGame = this.server.getGame();
                        out.write("Foste adicionado ao jogo: "+ actualGame.getNome());
                        out.newLine();
                        out.flush();
                        
                    }else if(testGame == false){
                        actualGame = this.server.createGame();
                        out.write("Foste adicionado ao jogo: "+ actualGame.getNome());
                        out.newLine();
                        out.flush();
                    }
                    
                    actualGame.addJogador(jogador);
                    Barreira b = actualGame.getBarreira();
                    b.esperar();
                    
                    // Have the 10 players
                    
                    HashMap<Integer,Heroi> lista = actualGame.getListaHerois();
                    for (int i=1; i<31;i++){
                       String s = lista.get(i).getNome();
                       out.write(i + " -> "+ s);
                       out.newLine();
                       out.flush();
                    }
                    
                    Thread t1 = new Thread(new SW_Listener(this.in, this.out, actualGame, jogador, this.server));
                    t1.start();
                    try {                    
                        sleep(30000);
                        out.write("PARAR");
                        out.newLine();
                        out.flush();
                        t1.join();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Server_Worker.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    
                    

                    
                    
                        
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

