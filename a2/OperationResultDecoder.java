import java.io.*;   // for InputStream and IOException
import java.net.*;  // for DatagramPacket

public interface OperationResultDecoder {
  OperationResult decode(InputStream source) throws IOException;
  OperationResult decode(DatagramPacket packet) throws IOException;
}
