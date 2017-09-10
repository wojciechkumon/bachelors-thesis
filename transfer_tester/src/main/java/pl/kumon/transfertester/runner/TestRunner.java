package pl.kumon.transfertester.runner;

import pl.kumon.transfertester.metrics.Metrics;
import pl.kumon.transfertester.tester.TransferTester;

import java.util.Objects;

import io.reactivex.Observable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestRunner {
  private final RunnerProps runnerProps;

  public TestRunner(RunnerProps runnerProps) {
    this.runnerProps = Objects.requireNonNull(runnerProps);
    log.info(runnerProps.toString());
  }

  public Observable<Metrics> run(TransferTester transferTester) {
    int totalTests = runnerProps.getNumberOfTests() + runnerProps.getWarmUpTests();
    return Observable.range(0, totalTests)
        .map(x -> transferTester.test(runnerProps.getTestProps()))
        .skip(runnerProps.getWarmUpTests());
  }
}
