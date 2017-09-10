package pl.kumon.transfertester.metrics;

import pl.kumon.transfertester.tester.TestType;

public interface Metrics {
  long getExecutionTimeNanos();

  boolean isSuccess();

  int getRequestSize();

  int getResponseSize();

  TestType getTestType();

  static Metrics of(long executionTimeNanos, int requestSize, int responseSize, TestType testType) {
    return new StandardMetrics(executionTimeNanos, requestSize, responseSize, testType);
  }

  static Metrics error(int requestSize, int responseSize, TestType testType) {
    return new ErrorMetrics(requestSize, responseSize, testType);
  }
}
