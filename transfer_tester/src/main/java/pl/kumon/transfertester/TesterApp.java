package pl.kumon.transfertester;

import pl.kumon.transfertester.csv.CsvService;
import pl.kumon.transfertester.metrics.Metrics;
import pl.kumon.transfertester.runner.RunnerProps;
import pl.kumon.transfertester.runner.TestRunner;
import pl.kumon.transfertester.tester.TestProps;
import pl.kumon.transfertester.tester.TestType;
import pl.kumon.transfertester.tester.TransferTester;
import pl.kumon.transfertester.tester.TransferTesterBuilder;
import pl.kumon.transfertester.tester.file.FileProps;
import pl.kumon.transfertester.tester.jni.JniProps;
import pl.kumon.transfertester.tester.protobuf.ProtobufProps;
import pl.kumon.transfertester.tester.rest.RestProps;
import pl.kumon.transfertester.tester.tcp.TcpProps;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TesterApp implements Runnable {
  private final AppProps appProps;

  @Override
  public void run() {
    TestProps testProps = TestProps.newTestProps(
        appProps.getOrDefault("requestSize", 8_192),
        appProps.getOrDefault("responseSize", 65_536));

    RunnerProps runnerProps = RunnerProps.builder()
        .numberOfTests(appProps.getOrDefault("numberOfTests", 10))
        .warmUpTests(appProps.getOrDefault("warmUpTests", 10))
        .testProps(testProps)
        .build();

    TestType testType = TestType.valueOf(appProps.getOrDefault("testType", "JNI").toUpperCase());
    TransferTester tester = byTestType(testType);

    Observable<Metrics> metricsObservable = new TestRunner(runnerProps)
        .run(tester)
        .doOnNext(System.out::println);

    Path path = Paths.get(appProps.getOrDefault("csvFilePath", "csv/report.csv")).toAbsolutePath();
    new CsvService(path)
        .writeMetrics(metricsObservable);
  }

  private TransferTester byTestType(TestType testType) {
    switch (testType) {
      case CORBA:
        return corbaTester();
      case FILE:
        return fileTester();
      case JNI:
        return jniTester();
      case PROTOBUF:
        return protobufTester();
      case REST:
        return restTester();
      case TCP:
        return tcpTester();
    }
    throw new IllegalArgumentException("Unknown testType: " + testType);
  }

  private TransferTester corbaTester() {
    return TransferTesterBuilder
        .corba()
        .build();
  }

  private TransferTester fileTester() {
    String integrationDir = appProps.getOrDefault("fileIntegrationDirectory", "integration_files");
    int timeoutMillis = appProps.getOrDefault("fileResponseTimeoutMillis", 3_000);
    int scanIntervalMillis = appProps.getOrDefault("fileScanIntervalMillis", 20);
    return TransferTesterBuilder
        .file(new FileProps()
            .integrationDirectory(Paths.get(integrationDir))
            .responseTimeoutUnit(TimeUnit.MILLISECONDS)
            .responseTimeout(timeoutMillis)
            .scanIntervalMillis(scanIntervalMillis)
            .responseFileEnding("_response"))
        .build();
  }

  private TransferTester jniTester() {
    return TransferTesterBuilder
        .jni(new JniProps().appDirName(".transfer-tester"))
        .build();
  }

  private TransferTester protobufTester() {
    String ip = appProps.getOrDefault("protobufIp", "localhost");
    int port = appProps.getOrDefault("protobufPort", 5_000);
    return TransferTesterBuilder
        .protobuf(new ProtobufProps().ip(ip).port(port))
        .build();
  }

  private TransferTester restTester() {
    String url = appProps.getOrDefault("restUrl", "http://localhost:9098/rest_server_cpp/testResult");
    return TransferTesterBuilder
        .rest(new RestProps()
            .url(url)
            .dataJsonKey("data")
            .responseSizeJsonKey("responseSize"))
        .build();
  }

  private TransferTester tcpTester() {
    String ip = appProps.getOrDefault("tcpIp", "localhost");
    int port = appProps.getOrDefault("tcpPort", 5_000);
    return TransferTesterBuilder
        .tcp(new TcpProps().ip(ip).port(port))
        .build();
  }
}
