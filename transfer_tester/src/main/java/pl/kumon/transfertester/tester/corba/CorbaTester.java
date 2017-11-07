package pl.kumon.transfertester.tester.corba;

import org.omg.CORBA.ORB;

import pl.kumon.transfertester.exception.TesterException;
import pl.kumon.transfertester.tester.AbstractTransferTester;
import pl.kumon.transfertester.tester.TestProps;
import pl.kumon.transfertester.tester.TestType;
import pl.kumon.transfertester.utils.ResponseValidator;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CorbaTester extends AbstractTransferTester {
  private final String ior;
  private ORB orb;

  public CorbaTester(CorbaProps corbaProps) {
    super(TestType.CORBA);
    Objects.requireNonNull(corbaProps);
    this.ior = Objects.requireNonNull(corbaProps.getCorbaIor());
  }

  @Override
  protected void beforeTest(TestProps testProps) {
    orb = ORB.init(new String[0], null);
    byte[] requestBytes = testProps.getRequestBytes();
    for (int i = 0; i < requestBytes.length; i++) {
      requestBytes[i] = (byte) ((Math.abs(requestBytes[i]) % 24) + 65);
    }
  }

  @Override
  protected void execute(TestProps testProps) throws TesterException {
    try {
      org.omg.CORBA.Object objRef = orb.string_to_object(ior);
      CorbaConnector corbaConnector = CorbaConnectorHelper.narrow(objRef);

      String request = new String(testProps.getRequestBytes(), StandardCharsets.US_ASCII);
      String response = corbaConnector.get(testProps.getResponseSize(), request);
      ResponseValidator.validateLength(response.length(), testProps);
    } catch (Exception e) {
      log.error("CORBA exception", e);
      throw new TesterException(e);
    }
  }

  @Override
  protected void afterTest() {
    orb.destroy();
  }
}
