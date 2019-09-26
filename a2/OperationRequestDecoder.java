import java.io.*;   // for InputStream and IOException
import java.net.*;  // for DatagramPacket

public interface OperationRequestDecoder {
  OperationRequest decode(InputStream source) throws IOException;
  OperationRequest decode(DatagramPacket packet) throws IOException;
}
