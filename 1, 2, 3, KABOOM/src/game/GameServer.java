package game;
import java.io.*;
import java.net.*;

public class GameServer {

    private ServerSocket ss;
    private int numPlayers;
    private int maxPlayers;

    public GameServer(){
        System.out.println("===== GAME SERVER =====");
        numPlayers = 0;
        maxPlayers = 2;

        try{
            ss = new ServerSocket(12345); // connect
        }catch(IOException ex){
            System.out.println("IOException from GameServer constructor");
        }
    }

    public void acceptConnections(){
        try{
            System.out.println("Waiting for connections....");

            while(numPlayers < maxPlayers){
                Socket s = ss.accept();
                DataInputStream in = new DataInputStream(s.getInputStream());
                DataOutputStream out = new DataOutputStream(s.getOutputStream());

                numPlayers++;
                out.writeInt(numPlayers);
                System.out.println("Player #" + numPlayers + " has connected.");
            }


            System.out.println("No longer accepting responses");

        }catch(IOException ex){
            System.out.println("IOException from acceptConnections()");
        }
    }

    public static void main(String[] args) {
        //system.out.println("Hello, World!");
        GameServer gs = new GameServer();
        gs.acceptConnections();
    }

}
