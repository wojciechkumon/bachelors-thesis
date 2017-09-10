package pl.kumon.transfertester.metrics;

import pl.kumon.transfertester.tester.TestType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ErrorMetrics implements Metrics {
  private final int requestSize;
  private final int responseSize;
  private final TestType testType;

  @Override
  public long getExecutionTimeNanos() {
    return -1;
  }

  @Override
  public boolean isSuccess() {
    return false;
  }

  @Override
  public String toString() {
    return "Execution error; testType: " + testType +
        "; requestSize: " + requestSize + "; responseSize: " + responseSize;
  }
}
