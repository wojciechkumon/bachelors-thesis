package pl.kumon.transfertester.tester.jni;

import pl.kumon.transfertester.exception.TesterException;
import pl.kumon.transfertester.tester.AbstractTransferTester;
import pl.kumon.transfertester.tester.TestProps;
import pl.kumon.transfertester.tester.TestType;
import pl.kumon.transfertester.utils.ResponseValidator;

import java.net.URL;

public class JniTester extends AbstractTransferTester {

  public JniTester() {
    String libraryName = System.mapLibraryName("JniExecutor");
    URL libraryUrl = getClass().getClassLoader().getResource(libraryName);
    if (libraryUrl == null) {
      throw new RuntimeException("Error while obtaining Jni library");
    }
    // TODO copy library from jar to user dir, remove old if exists and load from user dir
    System.load(libraryUrl.getPath());
  }

  @Override
  protected void execute(TestProps testProps) throws TesterException {
    byte[] response = new JniExecutor()
        .requestJni(testProps.getRequestBytes(), testProps.getResponseSize());
    ResponseValidator.validateLength(response, testProps);
  }

  @Override
  protected TestType testType() {
    return TestType.JNI;
  }
}
