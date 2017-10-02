package pl.kumon.transfertester.chart;

import pl.kumon.transfertester.tester.TestType;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class TestExecutionStats {
  private final TestType testType;
  private final int requestBytes;
  private final int responseBytes;
  private final long minNanos;
  private final long maxNanos;
  private final long firstQuartileNanos;
  private final long medianNanos;
  private final long thirdQuartileNanos;
  private final long percentile99Nanos;
  private final double standardDeviationNanos;
  private final double arithmeticMean;
}
