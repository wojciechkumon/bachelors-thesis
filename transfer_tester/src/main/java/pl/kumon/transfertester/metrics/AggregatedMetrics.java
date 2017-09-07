package pl.kumon.transfertester.metrics;

import java.util.Collections;
import java.util.List;

import lombok.Getter;

@Getter
public class AggregatedMetrics {
  private final List<Metrics> metrics;

  public AggregatedMetrics(List<Metrics> metrics) {
    this.metrics = Collections.unmodifiableList(metrics);
  }
}
