import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ServerHandler implements Runnable{

    private static final int PORT = 8000;
    private static final String[] ipArr = {};

    private static int serverID;
    private static ServerSocket serverSocket;

    private static Socket client;
    private static BufferedReader fromClient;
    private static PrintWriter toClient;
    private static String data;
    private static int messages = 0;

    private static ArrayList<Socket> servers;

    private static ArrayList<Integer> partition;
    private static int versionNum = 1;
    private static int replicasUpdate = 8;
    private static int distinguishedSite = 1;

    public ServerHandler() {
    }

    private static String receiveMessage(BufferedReader reader) throws IOException {
        String output = reader.readLine();

        if(output != null) 
            return output;
        

        return output;
    }
    
    //Accept if lowest in partition, connect if not
    private Socket connect(String serverID) {
        return null;
    }

    private void disconnect(String serverID) {
      
    }

    public void run() {
        
    }
    
}