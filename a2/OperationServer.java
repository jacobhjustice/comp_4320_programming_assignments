public class Operation {

  public byte tml;
  public byte request_id;
  public byte error;
  public int result;

  public Operation(byte tml, byte request_id, byte error, int result)  {
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

  // public long calculateResult() {
  //   switch (op_code) {
  //     case 0:
  //       // +
  //       return operand1 + operand2;
  //       break;
  //     case 1:
  //       // -
  //       return operand1 - operand2;
  //       break;
  //     case 2:
  //       // *
  //       return operand1 * operand2;
  //       break;
  //     case 3:
  //       // /
  //       return operand1 / operand2;
  //       break;
  //     case 4:
  //       // >>
  //       return operand1 >> operand2;
  //       break;
  //     case 5:
  //       // <<
  //       return operand1 << operand2;
  //       break;
  //     case 6:
  //       // ~
  //       return ~operand1;
  //       break;
  //   }
  // }
}
