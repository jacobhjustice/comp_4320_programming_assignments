import java.net.*;  // for DatagramSocket, DatagramPacket, and InetAddress
import java.io.*;   // for IOException
import java.util.Scanner;
import java.util.Random; 

public class ClientUDP {

   private static final int TIMEOUT = 3000;   // Resend timeout (milliseconds)
   private static final int MAXTRIES = 5;     // Maximum retransmissions

   public static void main(String[] args) throws IOException {
   
      if (args.length != 2)  // Test for correct # of args
         throw new IllegalArgumentException("Parameter(s): <Server> <Port>");
   
      InetAddress serverAddress = InetAddress.getByName(args[0]);  // Server address
   
      int servPort = Integer.parseInt(args[1]);
      Random random = new Random(); 
      int request_id = random.nextInt(128);
      Scanner input = new Scanner(System.in);

      OperationResultDecoder decoder = new OperationResultDecoderBin();
      OperationRequestEncoder encoder = new OperationRequestEncoderBin();
   
      for(;;) {
         System.out.println("\nEnter Op Code...\n0) Addition\n1) Subtraction\n2) Multiplication\n3) Division\n4) Shift Right\n5) Shift Left\n6) Complement\n\n\nOp Code: ");
         int opCode = input.nextByte();

         if (opCode > 6 || opCode < 0) {
            System.out.println("Enter a valid Op Code.\n");
            continue;
         }

         System.out.println("Enter Operand 1: ");
         short op1 = input.nextShort();

         short op2 = 0;
         byte num_operands = 1;
         if(opCode != 6) {
            System.out.println("Enter Operand 2: ");
            op2 = input.nextShort();
            num_operands = 2;
         }

         byte ttl = 8;
         OperationRequest request = new OperationRequest(ttl, (byte)request_id, (byte)opCode, num_operands, op1, op2);

         request_id = (request_id + 1) % 128;

         byte[] bytesToSend = encoder.encode(request);
         DatagramSocket socket = new DatagramSocket();
      
         socket.setSoTimeout(TIMEOUT);  // Maximum receive blocking time (milliseconds)
         long sendTime = System.nanoTime();
         DatagramPacket sendPacket = new DatagramPacket(bytesToSend,  // Sending packet
            bytesToSend.length, serverAddress, servPort);
      
         DatagramPacket receivePacket =                              // Receiving packet
            new DatagramPacket(new byte[bytesToSend.length], bytesToSend.length);
      
         int tries = 0;      // Packets may be lost, so we have to keep trying
         boolean receivedResponse = false;
         do {
            socket.send(sendPacket);          // Send the echo string
            try {
               socket.receive(receivePacket);  // Attempt echo reply reception
            
               if (!receivePacket.getAddress().equals(serverAddress))  // Check source
                  throw new IOException("Received packet from an unknown source");
            
               receivedResponse = true;
            } catch (InterruptedIOException e) {  // We did not get anything
               tries += 1;
               System.out.println("Timed out, " + (MAXTRIES-tries) + " more tries...");
            }
         } while ((!receivedResponse) && (tries < MAXTRIES));
         long recTime = System.nanoTime();
      
         if (receivedResponse) {
            OperationResult response = decoder.decode(receivePacket);
            
            byte[] bytes = receivePacket.getData();
            
            System.out.println("Sent Packet    : " + new String(hexChars(bytesToSend, ttl)));
            System.out.println("Received Packet: " + new String(hexChars(bytes, response.tml)));
            System.out.println("Request ID is: " + response.request_id);
            System.out.println("The result is: " + response.result);
         }
         else {
            System.out.println("No response -- giving up.");
         }
         System.out.println("Time elapsed: " + (recTime - sendTime) + " ns");
         
         socket.close();         
      }
   
    
      //socket.close();
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
