import java.io.*;   // for Input/OutputStream
import java.net.*;  // for Socket and ServerSocket

public class ServerTCP {
	private static final int BUFSIZE = 255;   // Size of receive buffer
  public static void main(String args[]) throws Exception {

    if (args.length != 1)  // Test for correct # of args
      throw new IllegalArgumentException("Parameter(s): <Port>");

    int servPort = Integer.parseInt(args[0]);

    // Create a server socket to accept client connection requests
    
	
    OperationRequestDecoder decoder = new OperationRequestDecoderBin();
    OperationResultEncoder encoder = new OperationResultEncoderBin();
    int recvMsgSize;   // Size of received message
    byte[] byteBuffer = new byte[BUFSIZE];  // Receive buffer

    for (;;) { // Run forever, accepting and servicing connections
	  ServerSocket servSock = new ServerSocket(servPort);
      Socket clntSock = servSock.accept();     // Get client connection

      System.out.println("Handling client at " + clntSock.getInetAddress().getHostAddress() + " on port " + clntSock.getPort());
      InputStream in = clntSock.getInputStream();
      OutputStream out = clntSock.getOutputStream();

	  //int packLength  = in.getLength();
	  OperationRequest request = decoder.decode(in);
	  byte error_code = 0;

      // Assert that byte length recieved is equal to object's TML value
      //if(request.tml != packLength) {
      //  error_code = 127;
      //}

      int num = 0;
      short op1 = request.operand1;
      short op2 = request.operand2;
      int opResult = -1;
      switch ((int)request.op_code) {
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
	  out.write(bin);
      clntSock.close();  // Close the socket.  We are done with this client!
	  servSock.close();
    }
  }
}
