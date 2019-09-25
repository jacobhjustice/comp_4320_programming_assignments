public interface OperationRequestEncoder {
  byte[] encode(OperationRequest operation) throws Exception;
}
