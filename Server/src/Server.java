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
    private static int messages = 0;

    private static final int PORT = 8000;
    private static final String[] SERVER_IP = {"10.176.69.32","10.176.69.33","10.176.69.34","10.176.69.35","10.176.69.36","10.176.69.37","10.176.69.38","10.176.69.39"};
    private static HashMap<Integer, ServerHandler> connectedSockets;
    private static ArrayList<Integer> partition;
    private static HashMap<Integer, BufferedReader> serverReaders;

    private static int versionNum = 1;
    private static int replicasUpdate = 8;
    private static int distinguishedSite = 0;
    private static boolean hasMajority = true;

    public static void main(String[] args) throws Exception {

        //Create Server
        serverSocket = new ServerSocket(PORT);

        //Obtain Server ID
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Enter Server ID");
        serverID = Integer.parseInt(keyboard.nextLine());
        keyboard.close();
        
        try {
        //Connect to Initial Partition
        ArrayList<Integer> initPartition = new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6,7));
        connectedSockets = newPartition(initPartition);

        //Run Server
        while(messages != 8) {
            //Receive messages
            client = serverSocket.accept();
            fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));

            System.out.println("\nMessage: " + receiveMessage(fromClient));
            messages++;

            if(hasMajority) {
                System.out.println("Write Successful");
                versionNum++;
                replicasUpdate++;
            }
            else {
                System.out.println("Write Unsuccessful");
            }

            //Print VN, RU, DS
            System.out.println("Version Number: " + versionNum);
            System.out.println("Replicas Updated: " + replicasUpdate);
            System.out.println("Distinguished Site: " + distinguishedSite);

            //Create new partition
            if(messages % 2 == 0 && messages > 0 && messages < 8) {
                newPartition(generatePartition());
                updatePartitionData(connectedSockets);
            }

            client.close();
        }
            disconnect(connectedSockets);
        }
        catch(Exception e) {
            e.printStackTrace();
            
        }
        finally {
            System.out.println("Closing Server");
            disconnect(connectedSockets);
            client.close();
            fromClient.close();
            System.exit(0);
        }
    }

    private static ArrayList<Integer> generatePartition() {
        ArrayList<Integer> newPartition = new ArrayList<>();;

        hasMajority = false;

        if(messages == 2) {
            if(serverID <= 3) {
                newPartition = new ArrayList<>(Arrays.asList(0,1,2,3));
                hasMajority = true;
            }
                
            else
                newPartition = new ArrayList<>(Arrays.asList(4,5,6,7));
        }
        else if(messages == 4) {
            if(serverID == 0)
                newPartition = new ArrayList<>(Arrays.asList(0));
            else if(serverID <= 3)
            {
                newPartition = new ArrayList<>(Arrays.asList(1,2,3));
                hasMajority = true;
            }
            else if(serverID < 7)
                newPartition = new ArrayList<>(Arrays.asList(4,5,6)); 
            else
                newPartition = new ArrayList<>(Arrays.asList(7));  
        }
        else if(messages < 8){
            if(serverID ==  0 || serverID == 7)
                newPartition = new ArrayList<>(Arrays.asList(serverID));
            else {
                newPartition = new ArrayList<>(Arrays.asList(1,2,3,4,5,6)); 
                hasMajority = true;
            }
                 
        }

        System.out.println("\nNew Partition: " + newPartition);
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
        HashMap<Integer, BufferedReader> serverReaders = new HashMap<Integer, BufferedReader>();

        //Check the size of the partition
        if(newPartition.size() != 1) {
            //Go through each servers
            for(int server : newPartition) {
                if(server != serverID) {
                    newConnection.put(server,new ServerHandler(SERVER_IP[server]));
                    newConnection.get(server).init();
                    System.out.println("Connected to Server " + server);
                }
                else {
                    for(int i = 0; i < newPartition.size() - 1; i++)
                    {
                        Socket cs = serverSocket.accept();
                    }
                }
            }
        }

        partition = newPartition;
        return newConnection;
    }

    //Terminates all server handler threads
    private static void disconnect(HashMap<Integer, ServerHandler> servers) throws Exception {
        for(Entry<Integer, ServerHandler> e : servers.entrySet()) {
            e.getValue().terminate();
        }
    }

    private static void updatePartitionData(HashMap<Integer, ServerHandler> servers) throws IOException {
        ArrayList<String> serverData = new ArrayList<>();

        String data = versionNum + ":" + replicasUpdate;
        //Go through each connected server and request for VN, RU, DS
        for(Entry<Integer, ServerHandler> e : servers.entrySet()) {
            e.getValue().request(data);
            serverData.add(e.getValue().obtainMessage());
        }

        //Go through each message and obtain the data 
        ArrayList<Integer> VNList = new ArrayList<>();
        ArrayList<Integer> RUList = new ArrayList<>();

        for(String s : serverData)
        {
            String[] split = s.split(":");
            int serverVN = Integer.parseInt(split[0]);
            int serverRU = Integer.parseInt(split[1]);

            VNList.add(serverVN);
            RUList.add(serverRU);
        }

        if(hasMajority) {
            //Update VN,RU,and DS
            versionNum = Collections.max(VNList);
            replicasUpdate = Collections.max(RUList);
            distinguishedSite = Collections.min(partition);
        }
        
        if(hasMajority && versionNum < messages + 1) {
            versionNum = messages + 1;
            replicasUpdate = messages + 8;
        }
    }
}