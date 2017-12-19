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
    public BufferedReader systemIn;
  
    
    private String username;
    private String password;
    
    public boolean bol;
    public boolean flag;
    
    
   
    
    
    public static void main(String[] args) {
        Client c = new Client("127.0.0.1",12345);
        c.clientStart();
    }

    public Client(String string, int i) {
        this.hostname=string;
        this.port=i;
    }

    public Client getClient(){
        return this;
    }
    
    public void clientStart() {
        
        try {	
            System.out.println("########################### OverWatch v1.0 ################################");
            System.out.println("> Connecting to server...");
            this.socket = new Socket(this.hostname, this.port);
            System.out.println("> Connection accepted!");
			
            
            systemIn = new BufferedReader(new InputStreamReader(System.in));
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
            System.out.println("********* Choose what do you want ***********");
            System.out.println("> 1 -> Login");
            System.out.println("> 2 -> Register");
            String op = systemIn.readLine();
            while(!(op.equals("1") || op.equals("2"))){
                System.out.println("ERROR : Please insert a correct option !!");
                op = systemIn.readLine();
            }
            
            String opResponse = "0";
            String msgResponse = null;
            
            while(opResponse.equals("0")){
                
                System.out.println("> Username: ");
                this.username = systemIn.readLine();
                while(this.username.equals("")){
                    System.out.println("ERROR : Please enter a valid username !!");
                    System.out.println("> Username: ");
                    this.username = systemIn.readLine();
                }
            
                System.out.println("> Password: ");
                this.password = systemIn.readLine();
                while(this.username.equals("")){
                    System.out.println("ERROR : Please enter a valid password !!");
                    System.out.println("> Password: ");
                    this.password = systemIn.readLine();
                }
            
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
                    while(!(op.equals("1") || op.equals("2"))){
                        System.out.println("ERROR : Please insert a correct option !!");
                        op = systemIn.readLine();
                    }
                }
                    
            }
            
            
           
            
    //*************************************** Login/Register Complete ********************************************************
    
    //*************************************** Enter in a  Match *********************************************************
            
            System.out.println("********* Choose what do you want ***********");
            System.out.println("> 1 -> Enter in a Match");
            System.out.println("> 2 -> Exit the Game");
            
            op = systemIn.readLine();
            while(!(op.equals("1") || op.equals("2"))){
                System.out.println("ERROR : Please insert a correct option !!");
                op = systemIn.readLine();
            }
            out.write(op);
            out.newLine();
            out.flush();
            msgResponse = in.readLine();
            System.out.println(msgResponse);
            
            if(op.equals("1")){
                System.out.println("> Waiting for the other players... ");
                System.out.println("> Loading ...");
                
                msgResponse = in.readLine();
             
                
            if(msgResponse.equals("VAIDORMIR")){
                    
                Thread listener = new Thread(new ClientListener(this));
                Thread writer = new Thread(new ClientWriter());
                listener.start();
                writer.start();
                this.bol = false;
                    
                while(!this.bol){
                    synchronized(this){
                        wait();
                    }
                }
            }
            
            System.out.println("***************************************************************");
            System.out.println("TIME EXPRIRED...");
            System.out.println("Game start in a few minutes. Loading...");
            System.out.println("***************************************************************");
            
            out.write("TIMEOUT");
            out.newLine();
            out.flush();
            
            out.write("TEAMS");
            out.newLine();
            out.flush();
            
            
            out.write("CHAT");
            out.newLine();
            out.flush();
            
            bol = false;
            while(!this.bol){
                synchronized(this){
                        wait();
                    }
            }
            flag = true;
            System.out.println("***************************************************************");
            System.out.println("***************************************************************");
            System.out.println("***************************************************************");
            
            out.write("CHAT_END");
            out.newLine();
            out.flush();
            
            out.write("RESULT");
            out.newLine();
            out.flush();
            
            
            op = systemIn.readLine();
            out.write(op);
            out.newLine();
            out.flush();
            
            }
            
            } catch (UnknownHostException e) {
                System.out.println("ERRO: Server doesn't exist!");
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    
    
    public class ClientListener implements Runnable {
        
        private final Client c;
        
        public ClientListener(Client c){
            this.c = c;
        }
        
        public void run(){
            String message;
            try{
		while((message = in.readLine()) != null){
                    
                    if(message.equals("END")){
                       try{
                            synchronized(c){
                                bol = true;
                                c.notifyAll();
                           }
                       }catch(Exception e){
                           e.printStackTrace();
                       }
                    }else if(message.equals("RETURN")){
                               System.out.println("********* Choose what do you want ***********");
                               System.out.println("> 1 -> Enter in a Match");
                               System.out.println("> 2 -> Exit the Game");
                    }else{
                        System.out.println(message);
                    }
                }
            
            }catch (SocketException e) {}
            catch (IOException e) {
		e.printStackTrace();
            }
        }
    }
    
    public class ClientWriter implements Runnable {
        
        
        public ClientWriter(){
        }
		
        public void run(){
            String message;
            
            try {
                flag = false;
		while((message = systemIn.readLine()) != null){
                    
                    out.write(message);
                    out.newLine();
                    out.flush();
                    
                    if(flag == true){
                        break;
                    }
                }
            }catch (SocketException e) {}
            catch (IOException e) {
		e.printStackTrace();
            }
        }
    }
}
