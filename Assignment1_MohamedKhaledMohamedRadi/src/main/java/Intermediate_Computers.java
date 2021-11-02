
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Intermediate_Computers {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            // Creating Server Socket with local IP and port number 1234
            ServerSocket intermediateComputerSocket = new ServerSocket(1234);
            System.out.println("Intermediate Computer Running...");
            while (true) {
                System.out.println("Waiting For Requests ...");
                // Wait for any request from a driver
                Socket driverSocket = intermediateComputerSocket.accept();

                DataInputStream computersDriverIS = new DataInputStream(driverSocket.getInputStream());
                DataOutputStream computersDriverOS = new DataOutputStream(driverSocket.getOutputStream());
                // Send ACK message to the driver 
                if(computersDriverIS.readUTF().equals("Connect to Intermediate Computers"))
                {
                    computersDriverOS.writeUTF("Connected to Intermediate Computer");
                computersDriverOS.flush();
                System.out.println("A driver connected ... ");
                }
                

                /*
              ***************** Connecting To Server ******************
                 */
                System.out.println("Connecting To server ... ");
                // Connect to server (with port number 2345)as a client
                Socket socketToServer = new Socket("127.0.0.1", 2345);
                // Creating IO stream with Server
                DataInputStream computersServerIS = new DataInputStream(socketToServer.getInputStream());
                DataOutputStream computersServerOS = new DataOutputStream(socketToServer.getOutputStream());
                // Send ACK message to the driver 
                computersServerOS.writeUTF("Connect to Server");
                computersServerOS.flush();
                if (computersServerIS.readUTF().equals("Connected to server")) {
                    System.out.println("Connected to Server ");
                }

                while (true) {
                    String driverMsg = computersDriverIS.readUTF();

                    System.out.println("Driver to Computer: " + driverMsg);

                    if (driverMsg.equals("Request best route")) {
                        // Ask for sensors readings
                        String computerMsg = new String("Provide me with sensors readings");
                        computersDriverOS.writeUTF(computerMsg);
                        computersDriverOS.flush();
                        System.out.println("Computer to driver: " + computerMsg);

                    } else if (driverMsg.equals("Sensors Readings")) {
                        // Provide Readings to server and get recomendations, then send it to the driver
                        System.out.println("Sending sensors readings to server");
                        computersServerOS.writeUTF(driverMsg);
                        computersDriverOS.flush();
                        String recomendations = computersServerIS.readUTF();
                        System.out.println("Server: " + recomendations);

                        computersDriverOS.writeUTF(recomendations);
                        computersDriverOS.flush();
                    } else if (driverMsg.equals("End transaction with Intermediate computers")) {
                        // Ending transaction with server 
                        System.out.println("Ending transaction with Server");
                        computersServerOS.writeUTF("End transaction with server");
                        computersDriverOS.flush();
                        String ServerMsg = computersServerIS.readUTF();
                        if (ServerMsg.equals("End Transaction")) {
                            System.out.println("Transaction with server ended");
                        }
                        // Ending transcation with driver
                        System.out.println("Ending transaction with driver");
                        computersDriverOS.writeUTF("End Transaction");
                        computersDriverOS.flush();
                        driverMsg = computersDriverIS.readUTF();
                        if (driverMsg.equals("End Transaction")) {
                            System.out.println("Transaction with driver ended");
                            break;
                        }
                    }
                }

                computersServerIS.close();
                computersServerOS.close();
                computersDriverIS.close();
                computersDriverOS.close();
                socketToServer.close();
                driverSocket.close();
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }
}
