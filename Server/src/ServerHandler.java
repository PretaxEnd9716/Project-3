import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerHandler implements Runnable {

    private static final int PORT = 8000;
    private Socket s;
    private BufferedReader fromServer;
    private PrintWriter toServer;
    private boolean running, request;
    private String ip;
    private String data;
    private String message;
    private Thread t;

    public ServerHandler(String ip)
    {
        this.ip = ip;
    }

    public void init() throws UnknownHostException, IOException
    {
        //Initialize Thread
        this.s = new Socket(ip, PORT);
        this.fromServer = new BufferedReader(new InputStreamReader(s.getInputStream()));
        this.toServer = new PrintWriter(s.getOutputStream());
        running = true;
        request = false;
        message = "EMPTY";

        t = new Thread(this);
        t.start();
    }

    private static String receiveMessage(BufferedReader reader) throws IOException {
        String output = reader.readLine();

        if(output != null) 
            return output;
        

        return output;
    }
    
    public void terminate() {
        running = false;
    }

    public void run() {

        try {
        
            while(running) {
                if(request) {
                    //Obtain VN,RU,DS
                    toServer.println(data);
                    message = receiveMessage(fromServer);
                }
            }

            s.close();
            toServer.close();
            fromServer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public void request(String message) {
        request = true;
        this.message = message;
    }

    public String obtainMessage() {
        String output = message;

        message = "EMPTY";

        return output;
    }

    public Socket getSocket() { return s; }
}
