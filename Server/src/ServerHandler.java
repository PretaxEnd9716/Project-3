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

        new Thread(this).start();
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
        while(running) {
            if(request) {
                try {
                    //Obtain VN,RU,DS
                    toServer.println(data);
                    message = receiveMessage(fromServer);

                    //Wait for a new request
                    this.wait();
                }
                catch (Exception e) {
                    terminate();
                    e.printStackTrace();
                }
            }
        }

        //Close Sockets
        try {
            s.close();
            toServer.close();
            fromServer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void request() {
        request = true;
        this.notify();
    }

    public String obtainMessage() {
        String output;

        //Wait for the message
        while(true) {
            if(message != "EMPTY") {
                output = message;
                break;
            }
        }

        //Reset message for the next request
        message = "EMPTY";

        return output;
    }
}
