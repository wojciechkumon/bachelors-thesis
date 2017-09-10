package pl.kumon.transfertester.runner;

import pl.kumon.transfertester.metrics.Metrics;
import pl.kumon.transfertester.tester.TransferTester;

import java.util.Objects;

import io.reactivex.Observable;

public class TestRunner {
  private final RunnerProps runnerProps;

  public TestRunner(RunnerProps runnerProps) {
    this.runnerProps = Objects.requireNonNull(runnerProps);
  }

  public Observable<Metrics> run(TransferTester transferTester) {
    return Observable.range(0, runnerProps.getNumberOfTests())
        .map(x -> transferTester.test(runnerProps.getTestProps()));
  }
}
