package pl.kumon.transfertester.utils;

import pl.kumon.transfertester.exception.TesterException;
import pl.kumon.transfertester.tester.TestProps;

public class ResponseValidator {

  private ResponseValidator() {}

  public static void validateLength(byte[] response, TestProps testProps) throws TesterException {
    validateLength(response.length, testProps);
  }

  public static void validateLength(int responseSize, TestProps testProps) throws TesterException {
    int validResponseSize = testProps.getResponseSize();
    if (responseSize != validResponseSize) {
      throw new TesterException("Wrong response length: " + responseSize
          + ", required: " + validResponseSize);
    }
  }
}
