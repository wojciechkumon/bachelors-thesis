package pl.kumon.transfertester.utils;

import java.nio.ByteBuffer;

public class IntConverter {

  private IntConverter() {}

  public static byte[] intToBytes(int value) {
    return ByteBuffer.allocate(Integer.BYTES)
        .putInt(value)
        .array();
  }
}
