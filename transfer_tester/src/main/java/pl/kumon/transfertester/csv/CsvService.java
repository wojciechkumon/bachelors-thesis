package pl.kumon.transfertester.csv;

import pl.kumon.transfertester.metrics.Metrics;

import java.io.IOException;
import java.io.Writer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.SneakyThrows;

public class CsvService {

  @SneakyThrows
  public void writeMetrics(Stream<Metrics> metricsStream, Writer writer) {
    Stream<String> csvRecords = mapToCsvRecords(metricsStream);
    printHeaders(writer);
    csvRecords.forEach(csvRecord -> printRecord(writer, csvRecord));
  }

  private void printHeaders(Writer writer) throws IOException {
    String headers = Stream.of("testType", "requestBytes", "responseBytes", "millis")
        .collect(Collectors.joining(","));
    printLine(writer, headers);
  }

  private Stream<String> mapToCsvRecords(Stream<Metrics> metricsStream) {
    return metricsStream.map(metrics -> {
      int requestBytes = metrics.getTestProps().getRequestBytes().length;
      int responseBytes = metrics.getTestProps().getResponseSize();
      return String.format("%s,%d,%d,%d", metrics.getTestType(), requestBytes, responseBytes,
          metrics.getExecutionTimeMillis());
    });
  }

  @SneakyThrows
  private void printRecord(Writer writer, String csvRecord) {
    printLine(writer, csvRecord);
  }

  private void printLine(Writer writer, String line) throws IOException {
    writer.write(line);
    writer.write("\r\n");
  }
}
