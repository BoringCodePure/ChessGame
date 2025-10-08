import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread implements Runnable {
    private BufferedReader readData;

    public ClientThread(Socket server) throws IOException{
        readData = new BufferedReader(new InputStreamReader(server.getInputStream()));
        
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            try{
                if (readData.ready()){
                    System.out.println(readData.readLine());
                }
            } catch (IOException e){
                
            }
        }
    }
}
