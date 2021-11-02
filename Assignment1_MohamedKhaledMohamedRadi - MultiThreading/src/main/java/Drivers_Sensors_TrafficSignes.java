
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Drivers_Sensors_TrafficSignes {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("Driver, Sensors and Traffic lights Running ...");
            /*
              ***************** Starting Transaction ******************
             */
            // Create a socket to connect to Intermediate computer.
            // IP = IP of current device (local host) and port number = 1234
            System.out.println("Connecting to Intermediate computers ... ");
            Socket intermediateComputerSocket = new Socket("127.0.0.1", 1234);
            // Create IO streams
            DataInputStream driverToComputersIS = new DataInputStream(intermediateComputerSocket.getInputStream());
            DataOutputStream driverToComputersOS = new DataOutputStream(intermediateComputerSocket.getOutputStream());
            driverToComputersOS.writeUTF("Connect to Intermediate Computers");
            // Checking Connection
            String DriverToComputerConnectAck = driverToComputersIS.readUTF();
            if ("Connected to Intermediate Computer".equals(DriverToComputerConnectAck)) {
                System.out.println(DriverToComputerConnectAck);
            }

            while (true) {
                /*
              ***************** Asking For Best Route ******************
                 */
                System.out.println("Requesting best route from computer ");
                driverToComputersOS.writeUTF("Request best route");
                String intermediateComputerMSG = driverToComputersIS.readUTF();
                if (intermediateComputerMSG.equals("Provide me with sensors readings")) {
                    System.out.println("Computer: " + intermediateComputerMSG);
                    driverToComputersOS.writeUTF("Sensors Readings");
                    driverToComputersOS.flush();
                    System.out.println("Driver: Sensors Readings");
                    String recomendations = driverToComputersIS.readUTF();
                    System.out.println(recomendations);
                }

                /*
              ***************** Ending Transaction with Intermediate Computers ******************
                 */
                driverToComputersOS.writeUTF("End transaction with Intermediate computers");
                intermediateComputerMSG = driverToComputersIS.readUTF();
                if (intermediateComputerMSG.equals("End Transaction")) {
                    driverToComputersOS.writeUTF("Ending Transaction");
                    break;
                }
            }
            System.out.println("Transaction Ended.");
            //4.close connections
            driverToComputersIS.close();
            driverToComputersOS.close();
            intermediateComputerSocket.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
