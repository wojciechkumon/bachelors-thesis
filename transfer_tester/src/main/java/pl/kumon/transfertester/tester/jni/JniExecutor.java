package pl.kumon.transfertester.tester.jni;

public class JniExecutor {
  public native byte[] requestJni(byte[] requestBytes, int responseSize);
}
