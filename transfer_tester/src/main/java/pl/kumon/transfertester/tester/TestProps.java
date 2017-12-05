package pl.kumon.transfertester.tester;

import com.google.common.base.Preconditions;

import java.util.Objects;
import java.util.Random;

import lombok.Getter;

@Getter
public class TestProps {
  private final byte[] requestBytes;
  private final int responseSize;

  private TestProps(byte[] requestBytes, int responseSize) {
    Preconditions.checkArgument(responseSize >= 0,
        "Response size must be greater or equal 0 but was: %s", responseSize);
    this.requestBytes = Objects.requireNonNull(requestBytes);
    this.responseSize = responseSize;
  }

  @Override
  public String toString() {
    return "request size: " + requestBytes.length + "B" + ", response size: " + responseSize + "B";
  }

  public static TestProps newTestProps(int requestSize, int responseSize) {
    byte[] requestBytes = new byte[requestSize];
    new Random().nextBytes(requestBytes);
    return new TestProps(requestBytes, responseSize);
  }
}
