public interface OperationResultEncoder {
  byte[] encode(OperationResult operation) throws Exception;
}
