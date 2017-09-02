package pl.kumon.transfertester.tester;

import lombok.Data;

@Data
public class TestProps {
  private static final TestProps SIMPLE_TEST_PROPS = new TestProps(0, 0);
  private final int requestBytes;
  private final int responseBytes;

  public static TestProps simpleTestProps() {
    return SIMPLE_TEST_PROPS;
  }
}
