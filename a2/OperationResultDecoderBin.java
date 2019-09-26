import java.io.*;  // for ByteArrayInputStream
import java.net.*; // for DatagramPacket

public class OperationResultDecoderBin implements OperationResultDecoder {

    public OperationResultDecoderBin() {
    }

    public OperationResult decode(InputStream wire) throws IOException {

        DataInputStream src = new DataInputStream(wire);
        byte tml            = src.readByte(); // probably need to use this value somewhere
        byte request_id     = src.readByte();
        byte error          = src.readByte();
        int result          = src.readInt();

        return new OperationResult(tml, request_id, error, result);
    }

    public OperationResult decode(DatagramPacket p) throws IOException {
        ByteArrayInputStream payload =
            new ByteArrayInputStream(p.getData(), p.getOffset(), p.getLength());
        return decode(payload);
    }
}
