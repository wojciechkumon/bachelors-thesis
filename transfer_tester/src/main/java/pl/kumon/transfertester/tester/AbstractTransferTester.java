package pl.kumon.transfertester.tester;

import pl.kumon.transfertester.metrics.Metrics;
import pl.kumon.transfertester.tester.exception.TesterException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTransferTester implements TransferTester {

  @Override
  public Metrics test() {
    return test(TestProps.simpleTestProps());
  }

  @Override
  public Metrics test(TestProps testProps) {
    beforeTest();
    long start = System.currentTimeMillis();

    try {
      execute(testProps);
    } catch (TesterException e) {
      log.error("TesterException", e);
      afterTest();
      return Metrics.error(testProps);
    }

    long time = System.currentTimeMillis() - start;
    afterTest();
    return Metrics.of(time, testProps);
  }

  protected void beforeTest() {}

  protected abstract void execute(TestProps testProps) throws TesterException;

  protected void afterTest() {}
}
