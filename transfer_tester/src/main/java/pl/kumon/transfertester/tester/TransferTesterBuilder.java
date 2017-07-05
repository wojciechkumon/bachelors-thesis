package pl.kumon.transfertester.tester;

import pl.kumon.transfertester.tester.corba.CorbaTester;
import pl.kumon.transfertester.tester.file.FileTester;
import pl.kumon.transfertester.tester.jni.JniTester;
import pl.kumon.transfertester.tester.rest.RestProps;
import pl.kumon.transfertester.tester.rest.RestTester;
import pl.kumon.transfertester.tester.tcp.TcpTester;

import java.util.function.Supplier;

public class TransferTesterBuilder {
  private final Supplier<TransferTester> instanceSupplier;

  private TransferTesterBuilder(Supplier<TransferTester> instanceSupplier) {
    this.instanceSupplier = instanceSupplier;
  }

  public static TransferTesterBuilder corba() {
    return new TransferTesterBuilder(CorbaTester::new);
  }

  public static TransferTesterBuilder file() {
    return new TransferTesterBuilder(FileTester::new);
  }

  public static TransferTesterBuilder jni() {
    return new TransferTesterBuilder(JniTester::new);
  }

  public static TransferTesterBuilder rest(RestProps restProps) {
    return new TransferTesterBuilder(() -> new RestTester(restProps));
  }

  public static TransferTesterBuilder tcp() {
    return new TransferTesterBuilder(TcpTester::new);
  }

  public TransferTester build() {
    return instanceSupplier.get();
  }
}
