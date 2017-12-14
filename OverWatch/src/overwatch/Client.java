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
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 * @author
 */
public class Client {
    
    private String hostname;
    private int port;
    
    public Socket socket;
    public BufferedReader in;
    public BufferedWriter out;
  
    
    public String username;
    private String password;
    
    public boolean bol;
    
    
    
   
    
    
    public static void main(String[] args) {
        Client c = new Client("127.0.0.1",12345);
        c.clientStart();
    }

    public Client(String string, int i) {
        this.hostname=string;
        this.port=i;
    }

    public void clientStart() {
        try {	
            System.out.println("########################### OverWatch v1.0 ################################");
            System.out.println("> Connecting to server...");
            this.socket = new Socket(this.hostname, this.port);
            System.out.println("> Connection accepted!");
			
            
            BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
            System.out.println("********* Choose what do you want ***********");
            System.out.println("> 1 -> Login");
            System.out.println("> 2 -> Register");
            String op = systemIn.readLine();
            String opResponse = "0";
            String msgResponse = null;
            
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
                
                if(opResponse.equals("0")){
                    System.out.println("********* Choose what do you want ***********");
                    System.out.println("> 1 -> Login");
                    System.out.println("> 2 -> Register");
                    op = systemIn.readLine(); 
                }
                    
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
            msgResponse = in.readLine();
            System.out.println(msgResponse);
            if(op.equals("1")){
                System.out.println("> Waiting for the other players... ");
                System.out.println("> Loading ...");
                
                msgResponse = in.readLine();
                System.out.println("PUTAS : "+msgResponse);
                
                if(msgResponse.equals("VAIDORMIR")){
                    
                    Thread listener = new Thread(new ClientListener());
                    Thread writer = new Thread(new ClientWriter(systemIn));
                    listener.start();
                    writer.start();
                    System.out.println("Before the wait");
                    this.bol = false;
                    while(this.bol == false){
                        synchronized (this){
                            wait();
                        }   
                    }
                    System.out.println("after the wait");
                    
                    systemIn.close();
                    
                    
                    
                }
                else{
                    System.out.println("PANADOS");
                }
               
                System.out.println("EXIT THE THE SELECT HERO");
                
            }
           

           

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
                    if(message.equals("FIM")){
                        bol = true;
                        System.out.println("antes do notify");
                        synchronized(this){
                            notifyAll();
                        }
                        System.out.println("depois do notify");
                        
                    }
                      
                    System.out.println("*************************MSG_ "+ message);
                        
                    
		}
            }catch (SocketException e) {}
            catch (IOException e) {
		e.printStackTrace();
            }
        }
    }
    
    public class ClientWriter implements Runnable {
        
        private BufferedReader in;
        public ClientWriter(BufferedReader in){
            this.in = in;
        }
		
        public void run(){
            String message;
            
            try {
		while((message = in.readLine()) != null){
                      
                    out.write(message);
                    out.newLine();
                    out.flush();
                        
                    
		}
            }catch (SocketException e) {}
            catch (IOException e) {
		e.printStackTrace();
            }
        }
    }
}
