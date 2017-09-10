package pl.kumon.transfertester;

import pl.kumon.transfertester.csv.CsvReader;
import pl.kumon.transfertester.metrics.Metrics;

import java.nio.file.Path;
import java.nio.file.Paths;

import io.reactivex.Observable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChartApp implements Runnable {
  private final AppProps appProps;

  @Override
  public void run() {
    Path csvDir = Paths.get(appProps.getOrDefault("csvSourceDirectory", "csv"));
    Observable<Metrics> metrics = new CsvReader()
        .parseMetricInDir(csvDir);
    System.out.println("Number of metrics: " + metrics.count().blockingGet());
  }
}
