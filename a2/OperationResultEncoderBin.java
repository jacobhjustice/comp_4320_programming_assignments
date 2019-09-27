import java.io.*;  // for ByteArrayOutputStream and DataOutputStream

public class OperationResultEncoderBin implements OperationResultEncoder {

  public OperationResultEncoderBin() {
    // nothing to initialize
  }

  public byte[] encode(OperationResult operation) throws IOException {

    ByteArrayOutputStream buf = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(buf);
    out.writeByte(operation.tml);
    out.writeByte(operation.request_id);
    out.writeByte(operation.error);
    out.writeInt(operation.result);

    out.flush();
    return buf.toByteArray();
  }
}
