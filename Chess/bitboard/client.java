package bitboard;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class client{
    public static void main(String[] args) throws IOException {
        Socket server = new Socket("localhost", 9000);

        sendMessage(server, "Sending Data from client1!\n");
        sendMessage(server, "Sending Data from client2!\n");
        sendMessage(server, "Sending Data from client3!\n");
        sendMessage(server, "Sending Data from client4!\n");


    }

    public static String readData(byte[] input){
        String result = "";

        for (byte eachbytes : input){
            result += (char) eachbytes;
        }
        return result;
    }

    public static void sendMessage(Socket server, String message) throws IOException{
        OutputStream out = server.getOutputStream();
        
        out.write(message.getBytes());

        out.flush();

        
    }
}