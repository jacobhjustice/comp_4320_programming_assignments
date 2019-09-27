import java.io.*;   // for IOExceptions

public interface OperationRequestEncoder {
  byte[] encode(OperationRequest operation) throws IOException;
}
