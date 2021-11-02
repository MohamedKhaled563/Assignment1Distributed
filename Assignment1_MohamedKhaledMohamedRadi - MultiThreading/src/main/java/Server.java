
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class ClientHandler implements Runnable {

    Socket s;

    public ClientHandler(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        try {
            // Create IO streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            // By exetting ftom "accept busy wait, this means a client connected to it, so send to the connected client an ACK 
            if (dis.readUTF().equals("Connect to Server")) {
                dos.writeUTF("Connected to server");
                System.out.println("A computer is cnnected to server");
                dos.flush();
            }

            System.out.println("Intermediate Computer connected ...");

            //4.perform IO with client
            while (true) {
                // Reading command from client
                String command = dis.readUTF();

                if (command.equals("Sensors Readings")) {
                    System.out.println("Sensors readings received");
                    // Process data and make recomendations 
                    // send recomendations to the client
                    dos.writeUTF("Recomendations: go throught SalahSalem street");
                    dos.flush();
                    System.out.println("Recomendations sent.");
                } else if (command.equals("End transaction with server")) {
                    dos.writeUTF("End Transaction");
                    System.out.println("Ending transaction with intermediate computer.");
                    break;
                }

                dis.close();
                dos.close();
                s.close();
                System.out.println("Ended.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}

public class Server {

    public static void main(String[] args) {
        try {
            // Create main Socket for the server with port number 2345
            ServerSocket serverSocket = new ServerSocket(2345);
            // If it is able to make the socket with 2345 port number -> it is succeed to run
            System.out.println("Server Running...");

            while (true) {
                System.out.println("Waiting for a request ...");
                // Listen to any request from any client and create socket for serve it
                Socket socket = serverSocket.accept();
                System.out.println("Client Accepted.");
                ClientHandler ch = new ClientHandler(socket);
                Thread t = new Thread(ch);
                t.start();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
