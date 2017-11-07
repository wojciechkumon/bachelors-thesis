package pl.kumon.transfertester.tester.corba;

import pl.kumon.transfertester.exception.TesterException;
import pl.kumon.transfertester.tester.AbstractTransferTester;
import pl.kumon.transfertester.tester.TestProps;
import pl.kumon.transfertester.tester.TestType;

import java.util.Objects;

public class CorbaTester extends AbstractTransferTester {
  private final String ior;

  public CorbaTester(CorbaProps corbaProps) {
    super(TestType.CORBA);
    Objects.requireNonNull(corbaProps);
    this.ior = Objects.requireNonNull(corbaProps.getCorbaIor());
  }

  @Override
  protected void execute(TestProps testProps) throws TesterException {

  }
}
