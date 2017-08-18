package pl.kumon.transfertester.metrics;

public class ErrorMetrics implements Metrics {

  @Override
  public long getExecutionTimeMillis() {
    return -1;
  }

  @Override
  public boolean isSuccess() {
    return false;
  }
}
