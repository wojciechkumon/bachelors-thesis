package pl.kumon.transfertester;

import pl.kumon.transfertester.metrics.Metrics;
import pl.kumon.transfertester.tester.TransferTester;
import pl.kumon.transfertester.tester.TransferTesterBuilder;
import pl.kumon.transfertester.tester.file.FileProps;

import java.util.concurrent.TimeUnit;

public class App {

  public static void main(String[] args) {
    TransferTester tester = TransferTesterBuilder
//        .corba()
//        .tcp(new TcpProps().ip("localhost").port(5000))
//        .jni()
//        .rest(new RestProps().url("http://localhost:9098/rest_server_cpp"))
        .file(new FileProps()
            .integrationDirectory("/Users/wojtas626/Projects/praca_inz/transfer_tester/integration_files")
            .responseTimeoutUnit(TimeUnit.SECONDS)
            .responseTimeout(3)
            .scanIntervalMillis(20)
            .responseFileEnding("_response"))
        .build();

    Metrics metrics = tester.test();
    System.out.println(metrics);
  }
}

