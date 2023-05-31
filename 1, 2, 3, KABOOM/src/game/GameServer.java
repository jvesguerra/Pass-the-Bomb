package game;
import java.io.*;
import java.net.*;

public class GameServer {

    private ServerSocket ss;
    private int numPlayers;
    private int maxPlayers;

    private Socket p1Socket;
    private Socket p2Socket;
    private Socket p3Socket;
    private Socket p4Socket;
    private ReadFromClient p1ReadRunnable;
    private ReadFromClient p2ReadRunnable;
    private ReadFromClient p3ReadRunnable;
    private ReadFromClient p4ReadRunnable;
    private WriteToClient p1WriteRunnable;
    private WriteToClient p2WriteRunnable;
    private WriteToClient p3WriteRunnable;
    private WriteToClient p4WriteRunnable;

    private double p1x, p1y, p2x, p2y, p3x, p3y, p4x, p4y;

    public GameServer(){
        System.out.println("===== GAME SERVER =====");
        numPlayers = 0;
        maxPlayers = 4;

        p1x = 100;
        p1y = 250;
        p2x = 200;
        p2y = 500;
        p3x = 200;
        p3y = 250;
        p4x = 300;
        p4y = 500;

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

                ReadFromClient rfc = new ReadFromClient(numPlayers, in);
                WriteToClient wtc = new WriteToClient(numPlayers, out);

                if(numPlayers==1){
                    p1Socket = s;
                    p1ReadRunnable = rfc;
                    p1WriteRunnable = wtc;
                }else if(numPlayers==2){
                    p2Socket = s;
                    p2ReadRunnable = rfc;
                    p2WriteRunnable = wtc;

                }else if(numPlayers==3){
                    p3Socket = s;
                    p3ReadRunnable = rfc;
                    p3WriteRunnable = wtc;

                }else{
                    p4Socket = s;
                    p4ReadRunnable = rfc;
                    p4WriteRunnable = wtc;
                    // since this is the last player for now
                    // sync when the game starts
                    p1WriteRunnable.sendStartMsg();
                    p2WriteRunnable.sendStartMsg();
                    p3WriteRunnable.sendStartMsg();
                    p4WriteRunnable.sendStartMsg();

                    Thread readThread1 = new Thread(p1ReadRunnable);
                    Thread readThread2 = new Thread(p2ReadRunnable);
                    Thread readThread3 = new Thread(p3ReadRunnable);
                    Thread readThread4 = new Thread(p4ReadRunnable);

                    readThread1.start();
                    readThread2.start();
                    readThread3.start();
                    readThread4.start();

                    Thread writeThread1 = new Thread(p1WriteRunnable);
                    Thread writeThread2 = new Thread(p2WriteRunnable);
                    Thread writeThread3 = new Thread(p3WriteRunnable);
                    Thread writeThread4 = new Thread(p4WriteRunnable);

                    writeThread1.start();
                    writeThread2.start();
                    writeThread3.start();
                    writeThread4.start();
                }
            }


            System.out.println("No longer accepting responses");

        }catch(IOException ex){
            System.out.println("IOException from acceptConnections()");
        }
    }

    private class WriteToClient implements Runnable{
        private int playerID;
        private DataOutputStream dataOut;

        public WriteToClient(int pid, DataOutputStream out){
            playerID = pid;
            dataOut = out;
            System.out.println("WTC"+playerID+" Runnable created");
        }

        public void run(){
            try{
                while(true){
                    if(playerID == 1){
                        dataOut.writeDouble(p2x);
                        dataOut.writeDouble(p2y);

                        dataOut.writeDouble(p3x);
                        dataOut.writeDouble(p3y);

                        dataOut.writeDouble(p4x);
                        dataOut.writeDouble(p4y);
                        dataOut.flush();
                    }
                    else if(playerID == 2){
                        dataOut.writeDouble(p1x);
                        dataOut.writeDouble(p1y);

                        dataOut.writeDouble(p3x);
                        dataOut.writeDouble(p3y);

                        dataOut.writeDouble(p4x);
                        dataOut.writeDouble(p4y);
                        dataOut.flush();
                    }else if(playerID == 3){  // playerID 3
                        dataOut.writeDouble(p1x);
                        dataOut.writeDouble(p1y);

                        dataOut.writeDouble(p2x);
                        dataOut.writeDouble(p2y);

                        dataOut.writeDouble(p4x);
                        dataOut.writeDouble(p4y);
                        dataOut.flush();
                    }else{
                        dataOut.writeDouble(p1x);
                        dataOut.writeDouble(p1y);

                        dataOut.writeDouble(p2x);
                        dataOut.writeDouble(p2y);

                        dataOut.writeDouble(p3x);
                        dataOut.writeDouble(p3y);
                        dataOut.flush();

                    }

                    try{
                        Thread.sleep(25);
                    }catch(InterruptedException ex){
                        System.out.println("InterruptedException from WTC run()");
                    }
                }
            }catch(IOException ex){
                System.out.println("IOException from WTC run()");
            }
        }

        public void sendStartMsg(){
            try{
                dataOut.writeUTF("We now have 4 players. GO!");
            }catch(IOException ex){
                System.out.println("IOException from sendStartMsg()");
            }
        }
    }

    private class ReadFromClient implements Runnable{
        private int playerID;
        private DataInputStream dataIn;

        public ReadFromClient(int pid, DataInputStream in){
            playerID = pid;
            dataIn = in;
            System.out.println("RFC"+playerID+" Runnable created");
        }

        public void run(){
            try{
                while(true){
                    if(playerID == 1){
                        p1x = dataIn.readDouble();
                        p1y = dataIn.readDouble();    
                    }
                    else if(playerID == 2){
                        p2x = dataIn.readDouble();
                        p2y = dataIn.readDouble();

                    }else if(playerID == 3){
                        p3x = dataIn.readDouble();
                        p3y = dataIn.readDouble();
                    }
                    else{
                        p4x = dataIn.readDouble();
                        p4y = dataIn.readDouble();
                    }
                }
            }catch(IOException ex){
                System.out.println("IOException from RFC run()");
            }
        }
    }

    public static void main(String[] args) {
        //system.out.println("Hello, World!");
        GameServer gs = new GameServer();
        gs.acceptConnections();
    }

}
