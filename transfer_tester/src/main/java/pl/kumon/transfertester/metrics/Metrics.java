package pl.kumon.transfertester.metrics;

import pl.kumon.transfertester.tester.TestProps;
import pl.kumon.transfertester.tester.TestType;

public interface Metrics {
  long getExecutionTimeMillis();

  boolean isSuccess();

  TestProps getTestProps();

  TestType getTestType();

  static Metrics of(long executionTimeMillis, TestProps testProps, TestType testType) {
    return new StandardMetrics(executionTimeMillis, testProps, testType);
  }

  static Metrics error(TestProps testProps, TestType testType) {
    return new ErrorMetrics(testProps, testType);
  }
}
