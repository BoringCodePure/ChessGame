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
    public void run(){
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
