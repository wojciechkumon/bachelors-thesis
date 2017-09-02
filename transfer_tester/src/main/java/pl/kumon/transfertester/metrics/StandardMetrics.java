package pl.kumon.transfertester.metrics;

import pl.kumon.transfertester.tester.TestProps;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
class StandardMetrics implements Metrics {
  private final long executionTimeMillis;
  private final TestProps testProps;

  @Override
  public boolean isSuccess() {
    return true;
  }

  @Override
  public String toString() {
    return "Execution time: " + executionTimeMillis + "ms, testProps: " + testProps;
  }
}
