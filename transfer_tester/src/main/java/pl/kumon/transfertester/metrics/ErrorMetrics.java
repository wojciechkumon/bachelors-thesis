package pl.kumon.transfertester.metrics;

import pl.kumon.transfertester.tester.TestProps;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ErrorMetrics implements Metrics {
  private final TestProps testProps;

  @Override
  public long getExecutionTimeMillis() {
    return -1;
  }

  @Override
  public boolean isSuccess() {
    return false;
  }

  @Override
  public String toString() {
    return "Execution error, testProps: " + testProps;
  }
}
