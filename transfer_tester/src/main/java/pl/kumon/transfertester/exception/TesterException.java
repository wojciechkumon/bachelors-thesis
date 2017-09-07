package pl.kumon.transfertester.exception;

public class TesterException extends Exception {

  public TesterException(Throwable throwable) {
    super(throwable);
  }

  public TesterException(String message) {
    super(message);
  }
}
