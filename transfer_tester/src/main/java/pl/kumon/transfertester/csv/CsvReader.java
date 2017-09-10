package pl.kumon.transfertester.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import pl.kumon.transfertester.metrics.Metrics;
import pl.kumon.transfertester.tester.TestType;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.StreamSupport;

import io.reactivex.Observable;
import lombok.SneakyThrows;

import static java.util.stream.Collectors.toList;

public class CsvReader {

  @SneakyThrows(IOException.class)
  public Observable<Metrics> parseMetricInDir(Path csvDir) {
    List<Path> csvFiles;
    try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(csvDir)) {
      csvFiles = StreamSupport.stream(directoryStream.spliterator(), false)
          .filter(path -> path.getFileName().toString().endsWith(".csv"))
          .collect(toList());
    }
    return Observable.fromIterable(csvFiles)
        .flatMapIterable(this::parseFromFile);
  }

  @SneakyThrows(IOException.class)
  private List<Metrics> parseFromFile(Path path) {
    try (BufferedReader reader = Files.newBufferedReader(path)) {
      CSVParser parser = CSVFormat.DEFAULT
          .withFirstRecordAsHeader()
          .parse(reader);

      return StreamSupport.stream(parser.spliterator(), false)
          .map(this::mapToMetrics)
          .collect(toList());
    }
  }

  private Metrics mapToMetrics(CSVRecord record) {
    int executionNanos = Integer.parseInt(record.get("executionNanos"));
    int requestBytes = Integer.parseInt(record.get("requestBytes"));
    int responseBytes = Integer.parseInt(record.get("responseBytes"));
    TestType testType = TestType.valueOf(record.get("testType"));
    return Metrics.of(executionNanos, requestBytes, responseBytes, testType);
  }
}
