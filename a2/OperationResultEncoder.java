import java.io.*;   // for IOException

public interface OperationResultEncoder {
  byte[] encode(OperationResult operation) throws IOException;
}
