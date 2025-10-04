package bitboard;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;



public class server{
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(9000);
        
        while (true){
            Socket clientSocket = server.accept();

            InputStream in = clientSocket.getInputStream();

            int r = in.read();

            String message = "";

            while (r != -1){
                message += (char) r;
                r = in.read();
            }

            System.out.println(message);


        }
    }

    public static String readData(byte[] input){
        String result = "";

        for (byte eachbytes : input){
            result += (char) eachbytes;
        }
        return result;
    }

}