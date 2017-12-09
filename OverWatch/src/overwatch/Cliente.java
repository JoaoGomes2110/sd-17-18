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
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author tiagofraga
 */
public class Cliente {
    
    private String hostname;
    private int porta;
    
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    
    private String username;
    private String password;
    
   
    
    
    public static void main(String[] args) {
        Cliente c = new Cliente("127.0.0.1",12345);
        c.clientStart();
    }

    public Cliente(String string, int i) {
        this.hostname=string;
        this.porta=i;
    }

    public void clientStart() {
        try {	
            System.out.println("########################### OverWatch v1.0 ################################");
            System.out.println("> Connecting to server...");
            this.socket = new Socket(this.hostname, this.porta);
            System.out.println("> Connection accepted!");
			
            
            BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
            System.out.println("********* Choose what do you want ***********");
            System.out.println("> 1 -> Login");
            System.out.println("> 2 -> Register");
            String op = systemIn.readLine();
            String opResponse = "0";
            String msgResponse;
            
            while(opResponse.equals("0")){
                System.out.println("> Username: ");
                this.username = systemIn.readLine();
            
                System.out.println("> Password: ");
                this.password = systemIn.readLine();
            
                out.write(op);
                out.newLine();
            
                out.write(this.username);
                out.newLine();
            
                out.write(this.password);
                out.newLine();
                
                out.flush();
                
                //Server Response
                opResponse= in.readLine();
                msgResponse = in.readLine();
                System.out.println(msgResponse);
                    
            }
            
    //*************************************** Login/Register Complete ********************************************************
    
    //*************************************** Enter in a  Match *********************************************************
            System.out.println("********* Choose what do you want ***********");
            System.out.println("> 1 -> Enter in a Match");
            System.out.println("> 2 -> Exit the Game");
            
            op = systemIn.readLine();
            out.write(op);
            out.newLine();
            out.flush();
            
            while(!op.equals("2")){
                System.out.println("> Loading ...");
                msgResponse = in.readLine();
                System.out.println(msgResponse);
                
                
                
            }
           

            //fechar sockets
            systemIn.close();
            socket.shutdownOutput();
            socket.shutdownInput();
            socket.close();

            } catch (UnknownHostException e) {
                System.out.println("ERRO: Server doesn't exist!");
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    
    
    public class ClientListener implements Runnable {
        
        public ClientListener(){}
		
        public void run(){
            String message;
            try {
		while((message = in.readLine()) != null){
                    System.out.println(message);
		}
            }catch (SocketException e) {}
            catch (IOException e) {
		e.printStackTrace();
            }
        }
    }
    
}
