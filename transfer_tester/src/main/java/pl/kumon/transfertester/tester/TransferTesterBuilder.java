package pl.kumon.transfertester.tester;

import pl.kumon.transfertester.tester.corba.CorbaProps;
import pl.kumon.transfertester.tester.corba.CorbaTester;
import pl.kumon.transfertester.tester.file.FileProps;
import pl.kumon.transfertester.tester.file.FileTester;
import pl.kumon.transfertester.tester.jni.JniProps;
import pl.kumon.transfertester.tester.jni.JniTester;
import pl.kumon.transfertester.tester.mock.MockTester;
import pl.kumon.transfertester.tester.rest.RestProps;
import pl.kumon.transfertester.tester.rest.RestTester;
import pl.kumon.transfertester.tester.tcp.TcpProps;
import pl.kumon.transfertester.tester.tcp.TcpTester;

import java.util.function.Supplier;

public class TransferTesterBuilder {
  private final Supplier<TransferTester> instanceSupplier;

  private TransferTesterBuilder(Supplier<TransferTester> instanceSupplier) {
    this.instanceSupplier = instanceSupplier;
  }

  public static TransferTesterBuilder corba(CorbaProps corbaProps) {
    return new TransferTesterBuilder(() -> new CorbaTester(corbaProps));
  }

  public static TransferTesterBuilder file(FileProps fileProps) {
    return new TransferTesterBuilder(() -> new FileTester(fileProps));
  }

  public static TransferTesterBuilder jni(JniProps jniProps) {
    return new TransferTesterBuilder(() -> new JniTester(jniProps));
  }

  public static TransferTesterBuilder mock() {
    return new TransferTesterBuilder(MockTester::new);
  }

  public static TransferTesterBuilder rest(RestProps restProps) {
    return new TransferTesterBuilder(() -> new RestTester(restProps));
  }

  public static TransferTesterBuilder tcp(TcpProps tcpProps) {
    return new TransferTesterBuilder(() -> new TcpTester(tcpProps));
  }

  public TransferTester build() {
    return instanceSupplier.get();
  }
}
