import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Map.Entry;

public class Server{

    private static final int PORT = 8000;
    private static final String[] ipArr = {};

    private static int serverID;
    private static ServerSocket serverSocket;

    private static Socket client;
    private static BufferedReader fromClient;
    private static String data;
    private static int messages = 0;

    private static final String[] SERVER_IP = {"10.176.69.32","10.176.69.33","10.176.69.34","10.176.69.35","10.176.69.36","10.176.69.37","10.176.69.38","10.176.69.39"};
    private static HashMap<Integer, Socket> connectedSockets;
    private static ArrayList<Integer> partition;
    private static int versionNum = 1;
    private static int replicasUpdate = 8;
    private static int distinguishedSite = 1;

    public static void main(String[] args) throws Exception {

        //Create Server
        serverSocket = new ServerSocket(PORT);

        //Obtain Server ID
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Enter Server ID");
        serverID = Integer.parseInt(keyboard.nextLine());
        keyboard.close();

        //Accept Client
        // client = serverSocket.accept();
        // fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
        
        try {
        //Connect to Initial Partition
        ArrayList<Integer> newPartition = new ArrayList<>(List.of(0,1,2));
        newPartition(newPartition);

        //Run Server
        // while(messages >= 8)
        // {
        //     //Receive messages
        //     receiveMessage(fromClient);
        //     messages++;

        //     //Create new partition
        //     if(messages % 2 == 0)
        //     {

        //     }
        // }

        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            System.out.println("Closing Server");

            //Close connections
            client.close();

            for(Entry<Integer, Socket> e : connectedSockets.entrySet()) {
                e.getValue().close();
            }
        }
    }

    private static String receiveMessage(BufferedReader reader) throws IOException {
        String output = reader.readLine();

        if(output != null) 
            return output;
        

        return output;
    }

    private static void newPartition(ArrayList<Integer> newPartition) throws UnknownHostException, IOException {

        //Hashmap to store connection
        HashMap<Integer, Socket> newPartitionSockets = new HashMap<Integer, Socket>();
        
        //Create partition hashmap
        for(int s : newPartition) {
            //Connect to the server
            if(s != serverID) {
                String ip = SERVER_IP[s];

                newPartitionSockets.put(s, new Socket(ip, PORT));
            }

            //Accept all connections in the partition
            else {
                for(int i = 0; i < partition.size() - 1; i++) {
                    serverSocket.accept();
                }
            }
        }

        partition = newPartition;
        connectedSockets = newPartitionSockets;
    }
}
