import java.net.*;  // for Socket
import java.io.*;   // for IOException and Input/OutputStream
import java.util.Scanner;

public class TCPEchoClient {

  public static void main(String[] args) throws IOException {

    if (args.length != 2)  // Test for correct # of args
      throw new IllegalArgumentException("Parameter(s): <Host> <Port>");

    String server = args[0];                    // Server name or IP address
    int servPort = Integer.parseInt(args[1]);   // Port number of server

    // Create socket that is connected to server on specified port
    Socket socket = new Socket(server, servPort);
    Scanner input = new Scanner(System.in);
    InputStream in = socket.getInputStream();
    OutputStream out = socket.getOutputStream();

    while(true) {
      System.out.println("\nEnter a sentence to reverse: ");
      String sentence = input.nextLine();
      byte[] byteBuffer = sentence.getBytes();
      long sendTime = System.currentTimeMillis();
      out.write(byteBuffer);  // Send the encoded string to the server

      // Receive the inverted string back from the server
      int totalBytesRcvd = 0;  // Total bytes received so far
      int bytesRcvd;           // Bytes received in last read
      while (totalBytesRcvd < byteBuffer.length) {
        if ((bytesRcvd = in.read(byteBuffer, totalBytesRcvd,  
                          byteBuffer.length - totalBytesRcvd)) == -1)
          throw new SocketException("Connection close prematurely");
        totalBytesRcvd += bytesRcvd;
      }
      long recTime = System.currentTimeMillis();
      
      System.out.println("Result: " + new String(byteBuffer));
      System.out.println("Time elapsed: " + (recTime - sendTime) + "ms");
    }
  }
}
