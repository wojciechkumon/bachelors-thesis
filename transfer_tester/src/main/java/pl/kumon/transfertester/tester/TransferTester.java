package pl.kumon.transfertester.tester;

import pl.kumon.transfertester.metrics.Metrics;

public interface TransferTester {

  Metrics test();

  Metrics test(TestProps props);
}
