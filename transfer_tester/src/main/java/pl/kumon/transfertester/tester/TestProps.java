package pl.kumon.transfertester.tester;

import java.util.Random;

import lombok.Getter;

@Getter
public class TestProps {
  private static final TestProps SIMPLE_TEST_PROPS = new TestProps(new byte[0], 0);
  private final byte[] requestBytes;
  private final int responseSize;

  private TestProps(byte[] requestBytes, int responseSize) {
    this.requestBytes = requestBytes;
    this.responseSize = responseSize;
  }

  @Override
  public String toString() {
    return "request size: " + requestBytes.length + "B" + ", response size: " + responseSize + "B";
  }

  static TestProps simpleTestProps() {
    return SIMPLE_TEST_PROPS;
  }

  public static TestProps newTestProps(int requestSize, int responseSize) {
    byte[] requestBytes = new byte[requestSize];
    new Random().nextBytes(requestBytes);
    return new TestProps(requestBytes, responseSize);
  }
}
