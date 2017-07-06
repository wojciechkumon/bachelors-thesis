package pl.kumon.transfertester.tester.tcp;

import pl.kumon.transfertester.metrics.Metrics;
import pl.kumon.transfertester.tester.TestProps;
import pl.kumon.transfertester.tester.TransferTester;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class TcpTester implements TransferTester {
  private final TcpProps tcpProps;

  public TcpTester(TcpProps tcpProps) {
    Objects.requireNonNull(tcpProps.getIp());
    Objects.requireNonNull(tcpProps.getPort());
    this.tcpProps = tcpProps;
  }

  // TODO TcpTester
  public Metrics test() {
    try (Socket socket = new Socket(tcpProps.getIp(), tcpProps.getPort())) {
      socket.getOutputStream().write(120);
      socket.getOutputStream().flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Metrics test(TestProps props) {
    return null;
  }
}
