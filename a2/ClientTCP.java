import java.io.*;   // for Input/OutputStream
import java.net.*;  // for Socket
import java.util.Random; //for ID
import java.util.Scanner; //User input
public class ClientTCP {
  private static final int TIMEOUT = 3000;   // Resend timeout (milliseconds)
  private static final int MAXTRIES = 5;     // Maximum retransmissions
  public static void main(String args[]) throws Exception {

    if (args.length != 2)  // Test for correct # of args
      throw new IllegalArgumentException("Parameter(s): <Destination> <Port>");

    InetAddress serverAddress = InetAddress.getByName(args[0]);;                    // Server name or IP address
    int servPort = Integer.parseInt(args[1]);   // Port number of server
	
      Random random = new Random(); 
      int request_id = random.nextInt(128);

    while(true) {
    // Create socket that is connected to server on specified port
    Socket socket = new Socket(serverAddress, servPort);
    Scanner input = new Scanner(System.in);
    InputStream in = socket.getInputStream();
    OutputStream out = socket.getOutputStream();
	  OperationResultEncoder resultencoder = new OperationResultEncoderBin();
      OperationResultDecoder decoder = new OperationResultDecoderBin();
      OperationRequestEncoder encoder = new OperationRequestEncoderBin();
		System.out.println("\nEnter Op Code...\n0) Addition\n1) Subtraction\n2) Multiplication\n3) Division\n4) Shift Right\n5) Shift Left\n6) Complement\n\n\nOp Code: ");
		int opCode = input.nextByte();
		
		if (opCode > 6 || opCode < 0) {
			System.out.println("Enter a valid Op Code.\n");
			continue;
		}
		
		System.out.println("Enter Operand 1: ");
		short op1 = input.nextShort();
		
		short op2 = 0;
		byte num_operands = 0;
		if(opCode != 6) {
			System.out.println("Enter Operand 2: ");
			op2 = input.nextShort();
			num_operands = 1;
		}
		byte ttl = 8;
		OperationRequest request = new OperationRequest(ttl, (byte)request_id, (byte)opCode, num_operands, op1, op2);
		request_id = (request_id + 1) % 128;
		byte[] byteBuffer = encoder.encode(request);
      long sendTime = System.nanoTime();
      out.write(byteBuffer);  // Send the encoded string to the server
		
      
	  
	  // Receive the inverted string back from the server
      int totalBytesRcvd = 0;  // Total bytes received so far
      int bytesRcvd;           // Bytes received in last read'
	  boolean isRec = true;
	  OperationResult response = null;
	  response = decoder.decode(in);
      long recTime = System.nanoTime();
      if (response != null) {
		    byte[] bin = resultencoder.encode(response);
            System.out.println("Sent Packet    : " + new String(hexChars(byteBuffer, ttl)));
            System.out.println("Received Packet: " + new String(hexChars(bin, response.tml)));
            System.out.println("Request ID is: " + response.request_id);
            System.out.println("The result is: " + response.result);
         }
         else {
            System.out.println("No response -- giving up.");
         }
         System.out.println("Time elapsed: " + (recTime - sendTime) + " ns");
    }

  }
  private static char[] hexChars(byte[] bytes, int length_in) {
      char [] hexChars = new char[length_in * 2];
      char[] HEX_CHARS = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
      for (int j = 0; j < length_in; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_CHARS[v >>> 4];
            hexChars[j * 2 + 1] = HEX_CHARS[v & 0x0F];
      }
      return hexChars;
   }
}

