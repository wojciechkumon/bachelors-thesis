package pl.kumon.transfertester.tester.protobuf;

import com.google.protobuf.ByteString;

import org.apache.commons.io.IOUtils;

import pl.kumon.transfertester.tester.AbstractTransferTester;
import pl.kumon.transfertester.tester.TestProps;
import pl.kumon.transfertester.tester.exception.TesterException;
import pl.kumon.transfertester.utils.IntConverter;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;

public class ProtobufTester extends AbstractTransferTester {
  private final ProtobufProps protobufProps;

  public ProtobufTester(ProtobufProps protobufProps) {
    Objects.requireNonNull(protobufProps);
    Objects.requireNonNull(protobufProps.getIp());
    if (protobufProps.getPort() < 0 || protobufProps.getPort() > 65535) {
      throw new IllegalArgumentException("Port value must be between 0 and 65535 but was: "
          + protobufProps.getPort());
    }
    this.protobufProps = protobufProps;
  }

  @Override
  protected void execute(TestProps testProps) throws TesterException {
    Protobuf.Request request = buildRequest(testProps);
    Protobuf.Response response = sendRequest(request);
    validateResponse(testProps, response);
  }

  private Protobuf.Request buildRequest(TestProps testProps) {
    return Protobuf.Request.newBuilder()
        .setResponseSize(testProps.getResponseSize())
        .setRequest(ByteString.copyFrom(testProps.getRequestBytes()))
        .build();
  }

  private Protobuf.Response sendRequest(Protobuf.Request request) throws TesterException {
    try (Socket socket = new Socket(protobufProps.getIp(), protobufProps.getPort())) {
      writeRequest(request, socket);
      return readResponse(socket);
    } catch (IOException e) {
      throw new TesterException(e);
    }
  }

  private void writeRequest(Protobuf.Request request, Socket socket) throws IOException {
    OutputStream outputStream = socket.getOutputStream();
    byte[] requestBytes = request.toByteArray();
    writeRequestBytesSize(requestBytes, outputStream);
    writeRequestBytes(requestBytes, outputStream);
    outputStream.flush();
  }

  private void writeRequestBytesSize(byte[] request, OutputStream outputStream) throws IOException {
    outputStream.write(IntConverter.intToBytes(request.length));
  }

  private void writeRequestBytes(byte[] requestBytes, OutputStream outputStream) throws IOException {
    IOUtils.writeChunked(requestBytes, outputStream);
  }

  private Protobuf.Response readResponse(Socket socket) throws IOException {
    return Protobuf.Response.parseFrom(socket.getInputStream());
  }

  private void validateResponse(TestProps testProps, Protobuf.Response response)
      throws TesterException {
    int actualSize = response.getResponse().size();
    if (actualSize != testProps.getResponseSize()) {
      throw new TesterException("Wrong response length: " + actualSize
          + ", required: " + testProps.getResponseSize());
    }
  }
}
