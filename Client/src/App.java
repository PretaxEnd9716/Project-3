import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class App {
    public static final String[] SERVER_IP = {"10.176.69.32","10.176.69.33","10.176.69.34","10.176.69.35","10.176.69.36","10.176.69.37","10.176.69.38","10.176.69.39"};
    
    public static void main(String[] args) throws Exception {
        //System.out.println("Hello, World!");
        int port = 8000;
        // if (args.length == 0) {
        //     System.err.println("Usage: java app ser");
        //     System.exit(1);
        // }

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String message;

        while (true) {
            try {
                System.out.print("Enter a message to send to servers: ");
                message = reader.readLine();

                for (int i = 0; i < SERVER_IP.length ; i++,port++) {
                    
                    try {
                        Socket socket = new Socket(SERVER_IP[i], port);
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        out.println(message);
                        out.close();
                        socket.close();
                    } catch (UnknownHostException e) {
                        System.err.println("Unknown host");
                    } catch (IOException e) {
                        System.err.println("Error connecting to server " + e.getMessage());
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading input: " + e.getMessage());
            }
        }

    }
}
