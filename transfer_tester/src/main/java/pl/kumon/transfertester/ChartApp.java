package pl.kumon.transfertester;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

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
        .groupBy(this::byTestTypeAndDataSize)
        .subscribe(this::handleMetricsGroupedByDataSizeAndTestType);
  }

  private Triple<TestType, Integer, Integer> byTestTypeAndDataSize(Metrics metrics) {
    return ImmutableTriple.of(metrics.getTestType(), metrics.getRequestSize(), metrics.getResponseSize());
  }

  private void handleMetricsGroupedByDataSizeAndTestType(
      GroupedObservable<Triple<TestType, Integer, Integer>, Metrics> groupedMetrics) {

    TestType testType = groupedMetrics.getKey().getLeft();
    int requestBytes = groupedMetrics.getKey().getMiddle();
    int responseBytes = groupedMetrics.getKey().getRight();

    groupedMetrics.reduce(0, (integer, metrics) -> integer + 1)
        .subscribe(amount -> System.out.println(testType + " " + requestBytes
            + " " + responseBytes + " " + amount));
  }
}
