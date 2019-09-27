import java.io.*;  // for ByteArrayInputStream
import java.net.*; // for DatagramPacket

public class OperationRequestDecoderBin implements OperationRequestDecoder {

    public OperationRequestDecoderBin() {
    }

    public OperationRequest decode(InputStream wire)  throws IOException  {

        DataInputStream src = new DataInputStream(wire);
        byte tml = src.readByte();
        byte request_id = src.readByte();
        byte op_code = src.readByte();
        byte num_operands = src.readByte();
        short operand1 = src.readShort();
        short operand2 = src.readShort();

        return new OperationRequest(tml, request_id, op_code, num_operands, operand1, operand2);
    }

    public OperationRequest decode(DatagramPacket p) throws IOException {
        ByteArrayInputStream payload =
            new ByteArrayInputStream(p.getData(), p.getOffset(), p.getLength());
        return decode(payload);
    }
}
