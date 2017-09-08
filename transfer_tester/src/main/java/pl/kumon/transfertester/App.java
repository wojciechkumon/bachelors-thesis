package pl.kumon.transfertester;

import pl.kumon.transfertester.csv.CsvService;
import pl.kumon.transfertester.metrics.Metrics;
import pl.kumon.transfertester.runner.RunnerProps;
import pl.kumon.transfertester.runner.TestRunner;
import pl.kumon.transfertester.tester.TestProps;
import pl.kumon.transfertester.tester.TransferTester;
import pl.kumon.transfertester.tester.TransferTesterBuilder;
import pl.kumon.transfertester.tester.file.FileProps;
import pl.kumon.transfertester.tester.protobuf.ProtobufProps;
import pl.kumon.transfertester.tester.rest.RestProps;
import pl.kumon.transfertester.tester.tcp.TcpProps;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

public class App {

  public static void main(String[] args) throws IOException {
    RunnerProps runnerProps = RunnerProps.builder()
        .numberOfTests(10)
        .testProps(TestProps.newTestProps(10_000, 50_000))
        .build();

    TransferTester tester = defaultFileTester();

    Stream<Metrics> metricsStream = new TestRunner(runnerProps)
        .run(tester)
        .peek(System.out::println);

    Path path = Paths.get("csv/report.csv").toAbsolutePath();
    if (!Files.exists(path.getParent())) {
      Files.createDirectory(path.getParent());
    }
    try (BufferedWriter writer = Files.newBufferedWriter(path, CREATE, WRITE, TRUNCATE_EXISTING)) {
      new CsvService().writeMetrics(metricsStream, writer);
    }
  }

  private static TransferTester defaultCorbaTester() {
    return TransferTesterBuilder
        .corba()
        .build();
  }

  private static TransferTester defaultFileTester() {
    return TransferTesterBuilder
        .file(new FileProps()
            .integrationDirectory(Paths.get("./integration_files"))
            .responseTimeoutUnit(TimeUnit.SECONDS)
            .responseTimeout(3)
            .scanIntervalMillis(20)
            .responseFileEnding("_response"))
        .build();
  }

  private static TransferTester defaultJniTester() {
    return TransferTesterBuilder
        .jni()
        .build();
  }

  private static TransferTester defaultProtobufTester() {
    return TransferTesterBuilder
        .protobuf(new ProtobufProps().ip("localhost").port(5000))
        .build();
  }

  private static TransferTester defaultRestTester() {
    return TransferTesterBuilder
        .rest(new RestProps()
            .url("http://localhost:9098/rest_server_cpp/testResult")
            .dataJsonKey("data")
            .responseSizeJsonKey("responseSize"))
        .build();
  }

  private static TransferTester defaultTcpTester() {
    return TransferTesterBuilder
        .tcp(new TcpProps().ip("localhost").port(5000))
        .build();
  }
}

