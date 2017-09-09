package pl.kumon.transfertester.metrics;

import pl.kumon.transfertester.tester.TestProps;
import pl.kumon.transfertester.tester.TestType;

import java.text.NumberFormat;
import java.util.Locale;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
class StandardMetrics implements Metrics {
  private final long executionTimeNanos;
  private final TestProps testProps;
  private final TestType testType;

  @Override
  public boolean isSuccess() {
    return true;
  }

  @Override
  public String toString() {
    String executionTime = NumberFormat.getNumberInstance(Locale.US).format(executionTimeNanos);
    return "Execution time: " + executionTime + "ns; testType: " + testType
        + "; testProps: " + testProps;
  }
}
