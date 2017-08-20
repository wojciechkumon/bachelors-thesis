package pl.kumon.transfertester.tester.jni;

import pl.kumon.transfertester.metrics.Metrics;
import pl.kumon.transfertester.tester.AbstractTransferTester;
import pl.kumon.transfertester.tester.TestProps;

import java.net.URL;

public class JniTester extends AbstractTransferTester {

  public JniTester() {
    String libraryName = System.mapLibraryName("JniExecutor");
    URL libraryUrl = getClass().getClassLoader().getResource(libraryName);
    if (libraryUrl == null) {
      throw new RuntimeException("Error while obtaining Jni library");
    }
    System.load(libraryUrl.getPath());
  }

  @Override
  protected void execute() {
    new JniExecutor().stringFromJni();
  }

  @Override
  public Metrics test(TestProps props) {
    return null;
  }
}
