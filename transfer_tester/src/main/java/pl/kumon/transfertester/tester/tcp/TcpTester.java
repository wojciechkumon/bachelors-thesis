package pl.kumon.transfertester.tester.tcp;

import org.apache.commons.io.IOUtils;

import pl.kumon.transfertester.exception.TesterException;
import pl.kumon.transfertester.tester.AbstractTransferTester;
import pl.kumon.transfertester.tester.TestProps;
import pl.kumon.transfertester.tester.TestType;
import pl.kumon.transfertester.utils.IntConverter;
import pl.kumon.transfertester.utils.ResponseValidator;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TcpTester extends AbstractTransferTester {
  private final TcpProps tcpProps;

  public TcpTester(TcpProps tcpProps) {
    super(TestType.TCP);
    Objects.requireNonNull(tcpProps);
    Objects.requireNonNull(tcpProps.getIp());
    if (tcpProps.getPort() < 0 || tcpProps.getPort() > 65535) {
      throw new IllegalArgumentException("Port value must be between 0 and 65535 but was: "
          + tcpProps.getPort());
    }
    this.tcpProps = tcpProps;
    log.info(tcpProps.toString());
  }

  @Override
  protected void execute(TestProps testProps) throws TesterException {
    try (Socket socket = new Socket(tcpProps.getIp(), tcpProps.getPort())) {
      writeRequest(testProps, socket);
      int actualResponseLength = readResponse(socket, testProps.getResponseSize());
      ResponseValidator.validateLength(actualResponseLength, testProps);
    } catch (IOException e) {
      throw new TesterException(e);
    }
  }

  private void writeRequest(TestProps testProps, Socket socket) throws IOException {
    OutputStream outputStream = socket.getOutputStream();
    writeRequestBytesSize(testProps, outputStream);
    writeResponseSize(testProps, outputStream);
    outputStream.flush();
    writeRequestBytes(testProps, outputStream);
    outputStream.flush();
  }

  private void writeRequestBytesSize(TestProps testProps, OutputStream outputStream) throws IOException {
    writeIntegerAsBytes(testProps.getRequestBytes().length, outputStream);
  }

  private void writeResponseSize(TestProps testProps, OutputStream outputStream) throws IOException {
    writeIntegerAsBytes(testProps.getResponseSize(), outputStream);
  }

  private void writeIntegerAsBytes(int intValue, OutputStream outputStream) throws IOException {
    outputStream.write(IntConverter.intToBytes(intValue));
  }

  private void writeRequestBytes(TestProps testProps, OutputStream outputStream) throws IOException {
    IOUtils.write(testProps.getRequestBytes(), outputStream);
  }

  private int readResponse(Socket socket, int responseSize) throws IOException {
    byte[] buffer = new byte[responseSize];
    return IOUtils.read(socket.getInputStream(), buffer);
  }
}
