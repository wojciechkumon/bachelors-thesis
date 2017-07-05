package pl.kumon.transfertester.tester.jni;

import pl.kumon.transfertester.metrics.Metrics;
import pl.kumon.transfertester.tester.TransferTester;
import pl.kumon.transfertester.tester.TestProps;

public class JniTester implements TransferTester {

  // TODO JniTester
  public Metrics test() {
    String libraryPath = "java.library.path";
    String old = System.getProperty(libraryPath);
//    System.setProperty(libraryPath, old + ":/Users/wojtas626/Projects/praca_inz/transfer_tester/target/classes");
//    System.loadLibrary("JniExecutor");
    System.load("/Users/wojtas626/Projects/praca_inz/transfer_tester/target/classes/JniExecutor.jnilib");
    System.out.println(new JniExcecutor().stringFromJni());
    return null;
  }

  public Metrics test(TestProps props) {
    return null;
  }
}
