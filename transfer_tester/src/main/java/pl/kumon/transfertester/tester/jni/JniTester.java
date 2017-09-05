package pl.kumon.transfertester.tester.jni;

import pl.kumon.transfertester.tester.AbstractTransferTester;
import pl.kumon.transfertester.tester.TestProps;
import pl.kumon.transfertester.tester.exception.TesterException;

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
  protected void execute(TestProps testProps) throws TesterException {
    int responseSize = testProps.getResponseSize();
    byte[] response = new JniExecutor()
        .requestJni(testProps.getRequestBytes(), responseSize);

    validateResponse(responseSize, response);
  }

  private void validateResponse(int correctResponseSize, byte[] response) throws TesterException {
    if (response.length != correctResponseSize) {
      throw new TesterException("Wrong response length: " + response.length
          + ", required: " + correctResponseSize);
    }
  }
}
