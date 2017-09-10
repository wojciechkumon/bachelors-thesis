package pl.kumon.transfertester.csv;

import pl.kumon.transfertester.metrics.Metrics;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.reactivex.Observable;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

@Slf4j
public class CsvWriter {
  private final Path reportPath;

  @SneakyThrows(IOException.class)
  public CsvWriter(Path reportPath) {
    this.reportPath = reportPath;
    if (!Files.exists(reportPath.getParent())) {
      Files.createDirectory(reportPath.getParent());
    }
    log.info("Csv report path: " + reportPath);
  }

  @SneakyThrows(IOException.class)
  public void writeMetrics(Observable<Metrics> metricsObservable) {
    try (BufferedWriter writer = Files.newBufferedWriter(reportPath, CREATE, WRITE, TRUNCATE_EXISTING)) {
      Observable<String> csvRecords = mapToCsvRecords(metricsObservable);
      printHeaders(writer);
      csvRecords.forEach(csvRecord -> printRecord(writer, csvRecord));
    }
  }

  private void printHeaders(Writer writer) throws IOException {
    String headers = Stream.of("testType", "requestBytes", "responseBytes", "executionNanos")
        .collect(Collectors.joining(","));
    printLine(writer, headers);
  }

  private Observable<String> mapToCsvRecords(Observable<Metrics> metricsObservable) {
    return metricsObservable.map(metrics -> {
      int requestBytes = metrics.getRequestSize();
      int responseBytes = metrics.getResponseSize();
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
