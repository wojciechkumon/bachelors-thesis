package pl.kumon.transfertester.tester.jni;

import pl.kumon.transfertester.metrics.Metrics;
import pl.kumon.transfertester.tester.TestProps;
import pl.kumon.transfertester.tester.TransferTester;

import java.net.URL;

public class JniTester implements TransferTester {

  public JniTester() {
    String libraryName = System.mapLibraryName("JniExecutor");
    URL libraryUrl = getClass().getClassLoader().getResource(libraryName);
    if (libraryUrl == null) {
      throw new RuntimeException("Error while obtaining Jni library");
    }
    System.load(libraryUrl.getPath());
  }

  public Metrics test() {
    long startMillis = System.currentTimeMillis();
    new JniExecutor().stringFromJni();
    long time = System.currentTimeMillis() - startMillis;
    return Metrics.of(time);
  }

  public Metrics test(TestProps props) {
    return null;
  }
}
