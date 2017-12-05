package pl.kumon.transfertester.tester;

import pl.kumon.transfertester.exception.TesterException;
import pl.kumon.transfertester.metrics.Metrics;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTransferTester implements TransferTester {
  private final TestType testType;

  protected AbstractTransferTester(TestType testType) {
    this.testType = testType;
  }

  @Override
  public Metrics test(TestProps testProps) {
    beforeTest(testProps);
    long start = System.nanoTime();

    try {
      execute(testProps);
    } catch (TesterException e) {
      log.error("TesterException", e);
      afterTest();
      return Metrics.error(testProps.getRequestBytes().length, testProps.getResponseSize(), testType);
    }

    long time = System.nanoTime() - start;
    afterTest();
    return Metrics.of(time, testProps.getRequestBytes().length, testProps.getResponseSize(), testType);
  }

  protected void beforeTest(TestProps testProps) {}

  protected abstract void execute(TestProps testProps) throws TesterException;

  protected void afterTest() {}
}
