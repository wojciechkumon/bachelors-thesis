package pl.kumon.transfertester.runner;

import pl.kumon.transfertester.metrics.Metrics;
import pl.kumon.transfertester.tester.TransferTester;

import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TestRunner {
  private final RunnerProps runnerProps;

  public TestRunner(RunnerProps runnerProps) {
    this.runnerProps = Objects.requireNonNull(runnerProps);
  }

  public Stream<Metrics> run(TransferTester transferTester) {
    return IntStream.range(0, runnerProps.getNumberOfTests())
        .boxed()
        .map(x -> transferTester.test(runnerProps.getTestProps()));
  }
}
