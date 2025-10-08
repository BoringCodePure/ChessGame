import java.io.*;
import java.net.Socket;

public class ServerThread implements Runnable{

    private BufferedReader readData;
    private Socket Client;
    
    public ServerThread(Socket clientSocket) throws IOException{
        readData = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            
            try{
                if (readData.ready()){

                    String message = readData.readLine();

                    if (message.equals("end")){
                        Thread.currentThread().interrupt();
                    } else{
                        System.out.println(message);
                    }
                }
            } catch (IOException e){
                
            }
        }
        try{
            readData.close();
        } catch (IOException e){

        }

        Main.receivedDisconnection();
    }
}