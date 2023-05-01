import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.Callable;

public class ServerHandler implements Callable<String> {

    BufferedReader reader;
    
    public ServerHandler(BufferedReader reader) {
        this.reader = reader;
    }

    private static String receiveMessage(BufferedReader reader) throws IOException {
        String output = reader.readLine();

        if(output != null) 
            return output;
        

        return output;
    }

    public String call() throws Exception {
        // TODO Auto-generated method stub
        return receiveMessage(reader);
    }
    
}
