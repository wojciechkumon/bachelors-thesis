package pl.kumon.transfertester.metrics;

import pl.kumon.transfertester.tester.TestProps;
import pl.kumon.transfertester.tester.TestType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
class StandardMetrics implements Metrics {
  private final long executionTimeMillis;
  private final TestProps testProps;
  private final TestType testType;

  @Override
  public boolean isSuccess() {
    return true;
  }

  @Override
  public String toString() {
    return "Execution time: " + executionTimeMillis + "ms, testType: " + testType
        + ", testProps: " + testProps;
  }
}
