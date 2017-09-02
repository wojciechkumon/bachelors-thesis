package pl.kumon.transfertester.tester.tcp;

import pl.kumon.transfertester.tester.AbstractTransferTester;
import pl.kumon.transfertester.tester.TestProps;
import pl.kumon.transfertester.tester.exception.TesterException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Objects;

public class TcpTester extends AbstractTransferTester {
  private final TcpProps tcpProps;

  public TcpTester(TcpProps tcpProps) {
    Objects.requireNonNull(tcpProps);
    Objects.requireNonNull(tcpProps.getIp());
    if (tcpProps.getPort() < 0 || tcpProps.getPort() > 65535) {
      throw new IllegalArgumentException("Port value must be between 0 and 65535 but was: " + tcpProps.getPort());
    }
    this.tcpProps = tcpProps;
  }

  @Override
  protected void execute(TestProps testProps) throws TesterException {
    try (Socket socket = new Socket(tcpProps.getIp(), tcpProps.getPort())) {
      OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
      writer.write("hello");
      writer.flush();
      BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      reader.readLine();
    } catch (IOException e) {
      throw new TesterException(e);
    }
  }
}
