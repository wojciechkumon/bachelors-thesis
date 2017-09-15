package pl.kumon.transfertester.runner;


import com.google.common.base.Preconditions;

import pl.kumon.transfertester.tester.TestProps;

import java.util.Objects;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class RunnerProps {
  private final TestProps testProps;
  private final int numberOfTests;
  private final int warmUpTests;
  private final int delayBetweenTestsMillis;

  private RunnerProps(TestProps testProps, int numberOfTests, int warmUpTests,
                      int delayBetweenTestsMillis) {
    Preconditions.checkArgument(numberOfTests > 0,
        "Number of tests must be greater then 0 but was: %s", numberOfTests);
    Preconditions.checkArgument(warmUpTests >= 0,
        "Number of warm-up tests must be greater or equal to 0 but was: %s", warmUpTests);
    Preconditions.checkArgument(delayBetweenTestsMillis >= 0,
        "Delay between tests millis must be greater or equal to 0 but was: %s",
        delayBetweenTestsMillis);
    this.testProps = Objects.requireNonNull(testProps);
    this.numberOfTests = numberOfTests;
    this.warmUpTests = warmUpTests;
    this.delayBetweenTestsMillis = delayBetweenTestsMillis;
  }
}
