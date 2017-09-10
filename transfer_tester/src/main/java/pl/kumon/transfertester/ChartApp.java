package pl.kumon.transfertester;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import pl.kumon.transfertester.csv.CsvReader;
import pl.kumon.transfertester.metrics.Metrics;
import pl.kumon.transfertester.tester.TestType;

import java.nio.file.Path;
import java.nio.file.Paths;

import io.reactivex.observables.GroupedObservable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChartApp implements Runnable {
  private final AppProps appProps;

  @Override
  public void run() {
    Path csvDir = Paths.get(appProps.getOrDefault("csvSourceDirectory", "csv"));
    new CsvReader()
        .parseMetricInDir(csvDir)
        .groupBy(metrics -> ImmutablePair.of(metrics.getRequestSize(), metrics.getResponseSize()))
        .subscribe(
            metricsGroup -> handleMetricsGroupedByDataSize(metricsGroup,
                metricsGroup.getKey().getLeft(),
                metricsGroup.getKey().getRight()));

  }

  private void handleMetricsGroupedByDataSize(GroupedObservable<? extends Pair<Integer, Integer>, Metrics> metricsGroup,
                                              int requestSize, int responseSize) {
    metricsGroup
        .groupBy(Metrics::getTestType)
        .subscribe(metrics -> handleMetricsGroupedByDataSizeAnTestType(metrics, requestSize,
            responseSize, metrics.getKey()));
  }

  private void handleMetricsGroupedByDataSizeAnTestType(GroupedObservable<TestType, Metrics> groupedMetrics,
                                                        int requestSize, int responseSize, TestType testType) {
    groupedMetrics.reduce(0, (integer, metrics) -> integer + 1)
        .subscribe(amount -> System.out.println(testType + " "
            + requestSize + " " + responseSize + " " + amount));
  }
}
