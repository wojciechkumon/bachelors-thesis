package pl.kumon.transfertester.runner;

import pl.kumon.transfertester.metrics.AggregatedMetrics;
import pl.kumon.transfertester.metrics.Metrics;
import pl.kumon.transfertester.tester.TransferTester;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class TestRunner {
  private final RunnerProps runnerProps;

  public TestRunner(RunnerProps runnerProps) {
    this.runnerProps = Objects.requireNonNull(runnerProps);
  }

  public AggregatedMetrics run(TransferTester transferTester) {
    List<Metrics> metrics = IntStream.range(0, runnerProps.getNumberOfTests())
        .boxed()
        .map(x -> transferTester.test(runnerProps.getTestProps()))
        .collect(toList());
    return new AggregatedMetrics(metrics);
  }
}
