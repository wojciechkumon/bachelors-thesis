package pl.kumon.transfertester.runner;

import pl.kumon.transfertester.metrics.Metrics;
import pl.kumon.transfertester.tester.TransferTester;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Notification;
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
        .doOnEach(this::sleepDelayMillis)
        .skip(runnerProps.getWarmUpTests());
  }

  private void sleepDelayMillis(Notification<Metrics> metrics) {
    try {
      TimeUnit.MILLISECONDS.sleep(runnerProps.getDelayBetweenTestsMillis());
    } catch (Exception e) {
      log.error("Error while waiting delay time", e);
    }
  }
}
