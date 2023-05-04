import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.Arrays;
import java.util.Collections;

public class Server {
    private static int serverID;
    private static ServerSocket serverSocket;

    private static Socket client;
    private static BufferedReader fromClient;
    private static String data;
    private static int messages = 0;

    private static final int PORT = 8000;
    private static final String[] SERVER_IP = {"10.176.69.32","10.176.69.33","10.176.69.34","10.176.69.35","10.176.69.36","10.176.69.37","10.176.69.38","10.176.69.39"};
    private static HashMap<Integer, ServerHandler> connectedSockets;
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
        ArrayList<Integer> initPartition = new ArrayList<>(Arrays.asList(0,1));
        connectedSockets = newPartition(initPartition);

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

            //Close Connections
            disconnect(connectedSockets);

            // client.close();
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

    private static HashMap<Integer, ServerHandler> newPartition (ArrayList<Integer> newPartition) throws UnknownHostException, IOException {
        HashMap<Integer, ServerHandler> newConnection = new HashMap<Integer, ServerHandler>();

        //Check the size of the partition
        if(newPartition.size() != 1) {
            //Go through each servers
            for(int server : newPartition) {
                if(server != serverID) {
                    newConnection.put(server,new ServerHandler(SERVER_IP[server]));
                    newConnection.get(server).init();
                }
                else {
                    for(int i = 0; i < newPartition.size() - 1; i++)
                    {
                        serverSocket.accept();
                    }
                }
            }
        }

        return newConnection;
    }

    //Terminates all server handler threads
    private static void disconnect(HashMap<Integer, ServerHandler> servers) throws IOException {
        for(Entry<Integer, ServerHandler> e : servers.entrySet()) {
            e.getValue().terminate();
        }
    }

    private static void updatePartitionData(HashMap<Integer, ServerHandler> servers) throws IOException {
        ArrayList<String> serverData = new ArrayList<>();

        //Go through each connected server and request for VN, RU, DS
        for(Entry<Integer, ServerHandler> e : servers.entrySet()) {
            e.getValue().request();
            serverData.add(e.getValue().obtainMessage());
        }

        //Go through each message and obtain the data 
        ArrayList<Integer> VNList = new ArrayList<>();
        ArrayList<Integer> RUList = new ArrayList<>();
        ArrayList<Integer> DSList = new ArrayList<>();

        for(String s : serverData)
        {
            String[] split = s.split(s);
            int serverVN = Integer.parseInt(split[0]);
            int serverRU = Integer.parseInt(split[1]);
            int serverDS = Integer.parseInt(split[2]);

            VNList.add(serverVN);
            RUList.add(serverRU);
            DSList.add(serverDS);
        }

        //Update VN,RU,and DS
        versionNum = Collections.max(VNList);
        replicasUpdate = partition.size();
        distinguishedSite = Collections.min(DSList);
    }
}