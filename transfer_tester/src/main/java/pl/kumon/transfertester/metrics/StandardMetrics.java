package pl.kumon.transfertester.metrics;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
class StandardMetrics implements Metrics {
  private final long executionTimeMillis;

  @Override
  public boolean isSuccess() {
    return true;
  }

  @Override
  public String toString() {
    return "Execution time: " + executionTimeMillis + "ms";
  }
}
