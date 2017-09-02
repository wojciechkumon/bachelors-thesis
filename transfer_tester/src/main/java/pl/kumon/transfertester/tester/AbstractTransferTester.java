package pl.kumon.transfertester.tester;

import pl.kumon.transfertester.metrics.Metrics;
import pl.kumon.transfertester.tester.exception.TesterException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTransferTester implements TransferTester {

  @Override
  public Metrics test() {
    beforeTest();
    long start = System.currentTimeMillis();

    try {
      execute();
    } catch (TesterException e) {
      log.error("TesterException", e);
      afterTest();
      return Metrics.error();
    }

    long time = System.currentTimeMillis() - start;
    afterTest();
    return Metrics.of(time);
  }

  protected void beforeTest() {}

  protected void afterTest() {}

  protected abstract void execute() throws TesterException;
}
