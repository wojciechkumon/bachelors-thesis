package pl.kumon.transfertester.metrics;

public interface Metrics {
  long getExecutionTimeMillis();

  boolean isSuccess();

  static Metrics of(long executionTimeMillis) {
    return new StandardMetrics(executionTimeMillis);
  }

  static Metrics error() {
    return new ErrorMetrics();
  }
}
