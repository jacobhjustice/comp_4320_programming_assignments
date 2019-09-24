import java.net.*;  // for DatagramSocket, DatagramPacket, and InetAddress
import java.io.*;   // for IOException

public class ServerUDP {

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
      

      // byte[] result = new byte [ECHOMAX];
      // for (int i=packLength-1; i >= 0; i--)  
      // { 
      // 	 result[i] = myPacket[i];
      // }


      // packet.setData(result);
      // System.out.println("Handling client at " +
      //   packet.getAddress().getHostAddress() + " on port " + packet.getPort());

      // System.out.println("Received: " + new String(myPacket));

      // DECODE OPERATION OBJECT FROM BINARY

      int TML = 0;

      // Assert that byte length recieved is equal to object's TML value
      if(TML = packLength) {
        //127
      }

      int num = 0;
      int op1 = 0;
      int op2 = 0;
      long result;
      boolean isError = false;
      switch (num) {
        // Addition
        case 0:
          result = op1 + op2;
          break;
        
        // Subtraction
        case 1:
          result = op1 - op2;
          break;
      
        // Multiplication
        case 2:
          result = op1 * op2;
          break;

        // Division
        case 3:
          result = op1 / op2;
          break;

        // Shift Right  
        case 4: 
          result = op1 >> op2;
          break;

        // Shift Left  
        case 5: 
          result = op1 << op2;
          break;
        
        // Not
        case 6:
          result = ~op1;
          break;

        default:
          isError = true;
          break;
      }

      


      socket.send(packet);       // Send the same packet back to client
      packet.setLength(ECHOMAX); // Reset length to avoid shrinking buffer
    }
    /* NOT REACHED */
  }


}

