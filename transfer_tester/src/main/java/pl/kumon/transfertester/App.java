package pl.kumon.transfertester;

import pl.kumon.transfertester.metrics.Metrics;
import pl.kumon.transfertester.tester.TransferTester;
import pl.kumon.transfertester.tester.TransferTesterBuilder;
import pl.kumon.transfertester.tester.rest.RestProps;

public class App {

  public static void main(String[] args) {
    TransferTester tester = TransferTesterBuilder
//        .corba()
//        .tcp(new TcpProps().ip("localhost").port(5000))
//        .jni()
        .rest(new RestProps().url("http://localhost:9098/rest_server_cpp"))
        .build();

    Metrics metrics = tester.test();
    System.out.println("Execution time: " + metrics.getExecutionTimeMillis() + "ms");
  }
}

