import java.net.*;  // for Socket, ServerSocket, and InetAddress
import java.io.*;   // for IOException and Input/OutputStream

public class myFirstTCPServer {

  private static final int BUFSIZE = 128;   // Size of receive buffer

  public static void main(String[] args) throws IOException {

    if (args.length != 1)  // Test for correct # of args
      throw new IllegalArgumentException("Parameter(s): <Port>");

    int servPort = Integer.parseInt(args[0]);

    // Create a server socket to accept client connection requests
    ServerSocket servSock = new ServerSocket(servPort);

    int recvMsgSize;   // Size of received message
    byte[] byteBuffer = new byte[BUFSIZE];  // Receive buffer

    for (;;) { // Run forever, accepting and servicing connections
      Socket clntSock = servSock.accept();     // Get client connection

      System.out.println("Handling client at " +
        clntSock.getInetAddress().getHostAddress() + " on port " +
             clntSock.getPort());

      InputStream in = clntSock.getInputStream();
      OutputStream out = clntSock.getOutputStream();

      // Receive until client closes connection, indicated by -1 return
      while ((recvMsgSize = in.read(byteBuffer)) != -1) {
        String original = new String(byteBuffer);
        StringBuilder sb = new StringBuilder(original);
        String reversed = "";

        // Start from the back of the recieved data, and traverse down to the beginning
        // in order to build the string backwards.
        for(int i = recvMsgSize - 1; i >= 0; i--) {
          reversed = reversed + sb.charAt(i);
        }
        System.out.println("Received: " + original);
        System.out.println("Reversed: " + reversed);
        out.write(reversed.getBytes(), 0, recvMsgSize);
        byteBuffer = new byte[BUFSIZE]; // clear out byte buffer before next loop itr
      }

      clntSock.close();  // Close the socket.  We are done with this client!
    }
    /* NOT REACHED */
  }
}
