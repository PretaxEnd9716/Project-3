import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Arrays;

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
    private static HashMap<Integer, Socket> connectedSockets = new HashMap<Integer, Socket>();
    private static HashMap<Integer, PrintWriter> writeHash;
    private static HashMap<Integer, BufferedReader> readHash;
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
        ArrayList<Integer> initPartition = new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6,7));
        newPartition(initPartition);
        System.out.println(updatePartitionData(writeHash, readHash));

        messages = 2;
        disconnect(connectedSockets, writeHash, readHash);
        newPartition(generatePartition());
        System.out.println(updatePartitionData(writeHash, readHash));

        //Run Server
        // while(messages >= 8) {
        //     //Receive messages
        //     receiveMessage(fromClient);
        //     messages++;

        //     //Print VN, RU, DS

        //     //Create new partition
        //     if(messages % 2 == 0) {
        //         newPartition(generatePartition());
        //     }
        // }

        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            System.out.println("Closing Server");

            //Close connections
            // client.close();

            for(Entry<Integer, Socket> e : connectedSockets.entrySet()) {
                System.out.println(e.getKey() + ":" + e.getValue().getInetAddress().getHostAddress());
                e.getValue().close();
            }
        }
    }

    private static ArrayList<Integer> generatePartition() {
        ArrayList<Integer> newPartition;

        if(messages == 2) {
            if(serverID <= 3)
                newPartition = new ArrayList<>(Arrays.asList(0,1,2,3));
            else
                newPartition = new ArrayList<>(Arrays.asList(4,5,6,7));
        }
        else if(messages == 4) {
            if(serverID == 0)
                newPartition = new ArrayList<>(Arrays.asList(0));
            else if(serverID <= 3)
                newPartition = new ArrayList<>(Arrays.asList(1,2,3));
            else if(serverID < 7)
                newPartition = new ArrayList<>(Arrays.asList(4,5,6)); 
            else
                newPartition = new ArrayList<>(Arrays.asList(7));  
        }
        else {
            if(serverID ==  0 || serverID == 7)
                newPartition = partition;
            else
                newPartition = new ArrayList<>(Arrays.asList(1,2,3,4,5,6));  
        }

        System.out.println(newPartition);
        return newPartition;
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
        HashMap<Integer, PrintWriter> newWriteHash = new HashMap<Integer, PrintWriter>();
        HashMap<Integer, BufferedReader> newReadHash = new HashMap<Integer, BufferedReader>();
        
        //Create partition hashmap
        if(newPartition.size() == 1) {
            partition = newPartition;
            connectedSockets = newPartitionSockets;
            writeHash = newWriteHash;
            readHash = newReadHash;
        }

        else {
            for(int s : newPartition) {
                //Connect to the server
                if(s != serverID) {
                    System.out.println("Connecting to " + s);
                    String ip = SERVER_IP[s];

                    newPartitionSockets.put(s, new Socket(ip, PORT));

                    //Create read and write hashmaps
                    newWriteHash.put(s,new PrintWriter(newPartitionSockets.get(s).getOutputStream(), true));
                    newReadHash.put(s, new BufferedReader(new InputStreamReader(newPartitionSockets.get(s).getInputStream())));
                }

                //Accept all connections in the partition
                else {
                    System.out.println("Accepting Connections");
                    for(int i = 0; i < newPartition.size() - 1; i++) {
                        System.out.println(i);

                        serverSocket.accept();     
                    }
                }
            }

            partition = newPartition;
            connectedSockets = newPartitionSockets;
            writeHash = newWriteHash;
            readHash = newReadHash;
        }
    }

    private static void disconnect(HashMap<Integer, Socket> socketHash, HashMap<Integer, PrintWriter> writeHash, HashMap<Integer, BufferedReader> readHash) throws IOException {
        for(Entry<Integer, Socket> e : socketHash.entrySet()) {
            e.getValue().close();
        }

        for(Entry<Integer, PrintWriter> e : writeHash.entrySet()) {
            e.getValue().close();
        }

        for(Entry<Integer, BufferedReader> e : readHash.entrySet()) {
            e.getValue().close();
        }
    }

    private static ArrayList<String> updatePartitionData(HashMap<Integer, PrintWriter> writeHash, HashMap<Integer, BufferedReader> readHash) throws IOException {
        ArrayList<String> result = new ArrayList<>();

        for(int s : partition) {
            if(s != serverID)
                writeHash.get(s).println(serverID);
            else {
                for(int i = 0; i < partition.size()-1; i++) {
                    result.add(receiveMessage(readHash.get(partition.get(i))));
                }
            }
        }

        return result;
    }
}