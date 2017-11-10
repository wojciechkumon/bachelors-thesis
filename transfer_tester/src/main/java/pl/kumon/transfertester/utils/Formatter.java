package pl.kumon.transfertester.utils;

public class Formatter {
  private static final int UNIT = 1024;

  private Formatter() {}

  public static String humanReadableByteCount(long bytes) {
    if (bytes < UNIT) {
      return bytes + "B";
    }
    int exp = (int) (Math.log(bytes) / Math.log(UNIT));
    String pre = "KMGTPE".charAt(exp - 1) + "i";
    return String.format("%.1f%sB", bytes / Math.pow(UNIT, exp), pre);
  }
}
