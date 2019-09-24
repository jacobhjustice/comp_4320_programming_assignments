import java.net.*;  // for DatagramSocket, DatagramPacket, and InetAddress
import java.io.*;   // for IOException
import java.util.Scanner;
public class myFirstUDPClient {

   private static final int TIMEOUT = 3000;   // Resend timeout (milliseconds)
   private static final int MAXTRIES = 5;     // Maximum retransmissions

   public static void main(String[] args) throws IOException {
   
      if (args.length != 2)  // Test for correct # of args
         throw new IllegalArgumentException("Parameter(s): <Server> <Port>");
   
      InetAddress serverAddress = InetAddress.getByName(args[0]);  // Server address
   
      int servPort = Integer.parseInt(args[1]);
      Scanner input = new Scanner(System.in);
   
      for(;;) {
         System.out.println("\nEnter a sentence to reverse: ");
         String sentence = input.nextLine();
         if (sentence.length() > 128) {
            System.out.println("Error, string too long!");
            continue;
         }
         byte[] bytesToSend = sentence.getBytes();
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
      
         if (receivedResponse)
            System.out.println("Received: " + new String(receivePacket.getData()));
         else
            System.out.println("No response -- giving up.");
         System.out.println("Time elapsed: " + (recTime - sendTime) + " ns");
         
         socket.close();         
      }
   
    
      //socket.close();
   }
}
