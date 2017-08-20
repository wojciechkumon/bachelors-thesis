package pl.kumon.transfertester.tester;

import pl.kumon.transfertester.metrics.Metrics;
import pl.kumon.transfertester.tester.exception.TesterException;

public abstract class AbstractTransferTester implements TransferTester {

  @Override
  public Metrics test() {
    long start = System.currentTimeMillis();

    try {
      execute();
    } catch (TesterException e) {
      e.printStackTrace();
      return Metrics.error();
    }

    long time = System.currentTimeMillis() - start;
    return Metrics.of(time);
  }

  protected abstract void execute() throws TesterException;
}
