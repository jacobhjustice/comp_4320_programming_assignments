import java.net.*;  // for DatagramSocket, DatagramPacket, and InetAddress
import java.io.*;   // for IOException

public class myFirstUDPServer {

  private static final int ECHOMAX = 255;  // Maximum size of echo datagram

  public static void main(String[] args) throws IOException {

    if (args.length != 1)  // Test for correct argument list
      throw new IllegalArgumentException("Parameter(s): <Port>");

    int servPort = Integer.parseInt(args[0]);

    DatagramSocket socket = new DatagramSocket(servPort);
    DatagramPacket packet = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
    
    for (;;) {  // Run forever, receiving and echoing datagrams
      socket.receive(packet);     // Receive packet from client
      // System.out.println(packet.getData());
      byte[] myPacket = packet.getData();
      int packLength  = packet.getLength();
      int j = 0;
      byte[] result = new byte [ECHOMAX];
      for (int i=packLength-1; i >= 0; i--)  
      { 
      	 result[j] = myPacket[i];
          j = j+1;
      }
      packet.setData(result);
      System.out.println("Handling client at " +
        packet.getAddress().getHostAddress() + " on port " + packet.getPort());

      System.out.println("Received: " + new String(myPacket));
      System.out.println("Reversed: " + new String(result));
      socket.send(packet);       // Send the same packet back to client
      packet.setLength(ECHOMAX); // Reset length to avoid shrinking buffer
    }
    /* NOT REACHED */
  }
}
