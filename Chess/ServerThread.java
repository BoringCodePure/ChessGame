import java.io.*;
import java.net.Socket;

public class ServerThread implements Runnable{

    private BufferedReader readData;
    
    public ServerThread(Socket clientSocket) throws IOException{
        readData = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
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