package pl.kumon.transfertester.metrics;

import pl.kumon.transfertester.tester.TestProps;

public interface Metrics {
  long getExecutionTimeMillis();

  boolean isSuccess();

  TestProps getTestProps();

  static Metrics of(long executionTimeMillis, TestProps testProps) {
    return new StandardMetrics(executionTimeMillis, testProps);
  }

  static Metrics error(TestProps testProps) {
    return new ErrorMetrics(testProps);
  }
}
