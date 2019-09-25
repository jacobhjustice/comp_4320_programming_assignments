public class OperationResult {

  public byte tml;
  public byte request_id;
  public byte error;
  public int result;

  public OperationResult(byte tml, byte request_id, byte error, int result)  {
    this.tml = tml;
    this.request_id = request_id;
    this.error = error;
    this.result = result;
  }

  public String toString() {
    final String EOLN = java.lang.System.getProperty("line.separator");
    String value = "TML        = " + tml + EOLN +
                   "request id = " + request_id + EOLN +
                   "error      = " + error + EOLN +
                   "result     = " + result + EOLN;
    return value;
  }
  
}
