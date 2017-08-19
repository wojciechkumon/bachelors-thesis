package pl.kumon.transfertester.tester.tcp;

import pl.kumon.transfertester.metrics.Metrics;
import pl.kumon.transfertester.tester.TestProps;
import pl.kumon.transfertester.tester.TransferTester;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Objects;

public class TcpTester implements TransferTester {
  private final TcpProps tcpProps;

  public TcpTester(TcpProps tcpProps) {
    Objects.requireNonNull(tcpProps);
    Objects.requireNonNull(tcpProps.getIp());
    if (tcpProps.getPort() < 0 || tcpProps.getPort() > 65535) {
      throw new IllegalArgumentException("Port value must be between 0 and 65535 but was: " + tcpProps.getPort());
    }
    this.tcpProps = tcpProps;
  }

  public Metrics test() {
    long start = System.currentTimeMillis();
    try (Socket socket = new Socket(tcpProps.getIp(), tcpProps.getPort())) {
      OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
      writer.write("hello");
      writer.flush();
      BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      reader.readLine();
    } catch (IOException e) {
      return Metrics.error();
    }
    long time = System.currentTimeMillis() - start;
    return Metrics.of(time);
  }

  public Metrics test(TestProps props) {
    return null;
  }
}
