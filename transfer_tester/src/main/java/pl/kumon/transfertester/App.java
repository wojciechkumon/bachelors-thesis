package pl.kumon.transfertester;

import org.apache.commons.lang3.SystemUtils;

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
        .numberOfTests(1)
        .testProps(TestProps.newTestProps(1_000, 1_000))
        .build();

    TransferTester tester = defaultFileTester();

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
    String directory = null;
    if (SystemUtils.IS_OS_LINUX) {
      directory = "/home/wojtas626/IdeaProjects/praca_inz/transfer_tester/integration_files";
    } else if (SystemUtils.IS_OS_MAC) {
      directory = "/Users/wojtas626/Projects/praca_inz/transfer_tester/integration_files";
    }
    return TransferTesterBuilder
        .file(new FileProps()
            .integrationDirectory(directory)
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

