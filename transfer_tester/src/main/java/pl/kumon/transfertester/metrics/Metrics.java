package pl.kumon.transfertester.metrics;

import pl.kumon.transfertester.tester.TestProps;
import pl.kumon.transfertester.tester.TestType;

public interface Metrics {
  long getExecutionTimeNanos();

  boolean isSuccess();

  TestProps getTestProps();

  TestType getTestType();

  static Metrics of(long executionTimeNanos, TestProps testProps, TestType testType) {
    return new StandardMetrics(executionTimeNanos, testProps, testType);
  }

  static Metrics error(TestProps testProps, TestType testType) {
    return new ErrorMetrics(testProps, testType);
  }
}
