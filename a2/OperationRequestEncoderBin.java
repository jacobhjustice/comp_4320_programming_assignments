import java.io.*;  // for ByteArrayOutputStream and DataOutputStream

public class OperationRequestEncoderBin implements OperationRequestEncoder {

  public OperationRequestEncoderBin() {
    // nothing to initialize
  }

  public byte[] encode(OperationRequest operation) throws IOException {
    ByteArrayOutputStream buf = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(buf);
    out.writeByte(operation.tml);
    out.writeByte(operation.request_id);
    out.writeByte(operation.op_code);
    out.writeByte(operation.num_operands);
    out.writeShort(operation.operand1);
    out.writeShort(operation.operand2);

    out.flush();
    return buf.toByteArray();
  }
}
