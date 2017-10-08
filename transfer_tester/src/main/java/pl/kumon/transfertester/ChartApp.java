package pl.kumon.transfertester;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import pl.kumon.transfertester.chart.ChartData;
import pl.kumon.transfertester.chart.ChartService;
import pl.kumon.transfertester.chart.TestExecutionStats;
import pl.kumon.transfertester.csv.CsvReader;
import pl.kumon.transfertester.metrics.Metrics;
import pl.kumon.transfertester.tester.TestType;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observables.GroupedObservable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChartApp implements Runnable {
  private final AppProps appProps;

  @Override
  public void run() {
    Path csvDir = Paths.get(appProps.getOrDefault("csvSourceDirectory", "csv"));
    Path chartDir = Paths.get(appProps.getOrDefault("chartOutputDirectory", "chart"));

    ChartService chartService = new ChartService();
    new CsvReader()
        .parseMetricInDir(csvDir)
        .groupBy(this::byTestTypeAndDataSize)
        .flatMap(this::toTestStats)
        .groupBy(stats -> ImmutablePair.of(stats.getRequestBytes(), stats.getResponseBytes()))
        .map(groupedStats -> toChartData(groupedStats, chartDir))
        .toList()
        .subscribe(chartService::saveCharts);
  }

  private Triple<TestType, Integer, Integer> byTestTypeAndDataSize(Metrics metrics) {
    return ImmutableTriple.of(metrics.getTestType(), metrics.getRequestSize(),
        metrics.getResponseSize());
  }

  private Observable<TestExecutionStats> toTestStats(
      GroupedObservable<Triple<TestType, Integer, Integer>, Metrics> groupedMetrics) {

    TestType testType = groupedMetrics.getKey().getLeft();
    int requestBytes = groupedMetrics.getKey().getMiddle();
    int responseBytes = groupedMetrics.getKey().getRight();

    return groupedMetrics
        .map(Metrics::getExecutionTimeNanos)
        .toList()
        .map(list -> toStats(testType, requestBytes, responseBytes, list))
        .toObservable();
  }

  private TestExecutionStats toStats(TestType testType, int requestBytes, int responseBytes,
                                     List<Long> dataSetNanos) {
    DescriptiveStatistics stats = new DescriptiveStatistics();
    dataSetNanos.forEach(stats::addValue);
    long min = (long) stats.getMin();
    long max = (long) stats.getMax();
    long firstQuartile = (long) stats.getPercentile(25);
    long median = (long) stats.getPercentile(50);
    long thirdQuartile = (long) stats.getPercentile(75);
    long percentile99Nanos = (long) stats.getPercentile(99);
    double standardDeviation = stats.getStandardDeviation();
    double mean = stats.getMean();

    return TestExecutionStats.builder()
        .testType(testType)
        .dataSetNanos(dataSetNanos)
        .requestBytes(requestBytes)
        .responseBytes(responseBytes)
        .minNanos(min)
        .maxNanos(max)
        .firstQuartileNanos(firstQuartile)
        .medianNanos(median)
        .thirdQuartileNanos(thirdQuartile)
        .percentile99Nanos(percentile99Nanos)
        .standardDeviationNanos(standardDeviation)
        .arithmeticMean(mean)
        .build();
  }

  private ChartData toChartData(GroupedObservable<ImmutablePair<Integer, Integer>, TestExecutionStats> groupedStats,
                                Path chartDir) {
    int requestBytes = groupedStats.getKey().getLeft();
    int responseBytes = groupedStats.getKey().getRight();
    Path path = chartDir.resolve("chart_" + requestBytes + "_" + responseBytes + ".png");

    return ChartData.builder()
        .requestBytes(requestBytes)
        .responseBytes(responseBytes)
        .stats(groupedStats)
        .chartPath(path)
        .build();
  }
}
