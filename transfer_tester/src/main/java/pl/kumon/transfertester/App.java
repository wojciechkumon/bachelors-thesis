package pl.kumon.transfertester;

import pl.kumon.transfertester.runner.RunnerProps;
import pl.kumon.transfertester.runner.TestRunner;
import pl.kumon.transfertester.tester.TestProps;
import pl.kumon.transfertester.tester.TransferTester;
import pl.kumon.transfertester.tester.TransferTesterBuilder;
import pl.kumon.transfertester.tester.file.FileProps;
import pl.kumon.transfertester.tester.protobuf.ProtobufProps;
import pl.kumon.transfertester.tester.rest.RestProps;
import pl.kumon.transfertester.tester.tcp.TcpProps;

import java.util.concurrent.TimeUnit;

public class App {

  public static void main(String[] args) {
    RunnerProps runnerProps = RunnerProps.builder()
        .numberOfTests(10)
        .testProps(TestProps.newTestProps(1_000, 2_000_000))
        .build();

    TransferTester tester = defaultJniTester();

    new TestRunner(runnerProps)
        .run(tester)
        .getMetrics()
        .forEach(System.out::println);
  }

  private static TransferTester defaultCorbaTester() {
    return TransferTesterBuilder
        .corba()
        .build();
  }

  private static TransferTester defaultFileTester() {
    return TransferTesterBuilder
        .file(new FileProps()
            .integrationDirectory("/Users/wojtas626/Projects/praca_inz/transfer_tester/integration_files")
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

