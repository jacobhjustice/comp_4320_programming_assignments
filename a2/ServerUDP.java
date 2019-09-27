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
    OperationRequestDecoder decoder = new OperationRequestDecoderBin();
    OperationResultEncoder encoder = new OperationResultEncoderBin();

    for (;;) {  // Run forever, receiving and echoing datagrams
      socket.receive(packet);     // Receive packet from client

      int packLength  = packet.getLength();
      // DECODE OPERATION OBJECT FROM BINARY

      OperationRequest request = decoder.decode(packet);
      byte error_code = 0;

      // Assert that byte length recieved is equal to object's TML value
      if(request.tml != packLength) {
        error_code = 127;
      }

      int num = 0;
      short op1 = request.operand1;
      short op2 = request.operand2;
      int opResult = -1;
      switch (request.op_code) {
        // Addition
        case 0:
          opResult = op1 + op2;
          break;
        
        // Subtraction
        case 1:
          opResult = op1 - op2;
          break;
      
        // Multiplication
        case 2:
          opResult = op1 * op2;
          break;

        // Division
        case 3:
          opResult = op1 / op2;
          break;

        // Shift Right  
        case 4: 
          opResult = op1 >> op2;
          break;

        // Shift Left  
        case 5: 
          opResult = op1 << op2;
          break;
        
        // Not
        case 6:
          opResult = ~op1;
          break;

        default:
          error_code = 127;
          break;
      }

      byte tml = 7;
      
      OperationResult result = new OperationResult(tml, request.request_id, error_code, opResult);

      byte[] bin = encoder.encode(result);
      packet.setData(bin);
      socket.send(packet);       // Send the same packet back to client
      packet.setLength(ECHOMAX); // Reset length to avoid shrinking buffer
    }
    /* NOT REACHED */
  }


}

