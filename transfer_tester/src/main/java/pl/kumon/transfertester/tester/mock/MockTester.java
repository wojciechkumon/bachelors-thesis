package pl.kumon.transfertester.tester.mock;

import pl.kumon.transfertester.exception.TesterException;
import pl.kumon.transfertester.tester.AbstractTransferTester;
import pl.kumon.transfertester.tester.TestProps;
import pl.kumon.transfertester.tester.TestType;
import pl.kumon.transfertester.utils.ResponseValidator;

public class MockTester extends AbstractTransferTester {

  public MockTester() {
    super(TestType.MOCK);
  }

  @Override
  protected void execute(TestProps testProps) throws TesterException {
    byte[] mockResponse = getMockResponse(testProps.getResponseSize());
    ResponseValidator.validateLength(mockResponse, testProps);
  }

  private byte[] getMockResponse(int responseSize) {
    return new byte[responseSize];
  }
}
