package pl.kumon.transfertester.csv;

import pl.kumon.transfertester.metrics.Metrics;

import java.io.IOException;
import java.io.Writer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.reactivex.Observable;
import lombok.SneakyThrows;

public class CsvService {

  @SneakyThrows(IOException.class)
  public void writeMetrics(Observable<Metrics> metricsObservable, Writer writer) {
    Observable<String> csvRecords = mapToCsvRecords(metricsObservable);
    printHeaders(writer);
    csvRecords.forEach(csvRecord -> printRecord(writer, csvRecord));
  }

  private void printHeaders(Writer writer) throws IOException {
    String headers = Stream.of("testType", "requestBytes", "responseBytes", "executionNanos")
        .collect(Collectors.joining(","));
    printLine(writer, headers);
  }

  private Observable<String> mapToCsvRecords(Observable<Metrics> metricsObservable) {
    return metricsObservable.map(metrics -> {
      int requestBytes = metrics.getTestProps().getRequestBytes().length;
      int responseBytes = metrics.getTestProps().getResponseSize();
      return String.format("%s,%d,%d,%d", metrics.getTestType(), requestBytes, responseBytes,
          metrics.getExecutionTimeNanos());
    });
  }

  @SneakyThrows(IOException.class)
  private void printRecord(Writer writer, String csvRecord) {
    printLine(writer, csvRecord);
  }

  private void printLine(Writer writer, String line) throws IOException {
    writer.write(line);
    writer.write("\r\n");
  }
}
