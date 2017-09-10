package pl.kumon.transfertester.tester.file;

import pl.kumon.transfertester.exception.TesterException;
import pl.kumon.transfertester.tester.AbstractTransferTester;
import pl.kumon.transfertester.tester.TestProps;
import pl.kumon.transfertester.tester.TestType;
import pl.kumon.transfertester.utils.IntConverter;
import pl.kumon.transfertester.utils.ResponseValidator;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Future;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

@Slf4j
public class FileTester extends AbstractTransferTester {
  private final FileProps props;
  private final ResponseFileWatcher fileWatcher;

  @SneakyThrows(IOException.class)
  public FileTester(FileProps fileProps) {
    Objects.requireNonNull(fileProps.getIntegrationDirectory());
    Objects.requireNonNull(fileProps.getResponseTimeoutUnit());
    Objects.requireNonNull(fileProps.getResponseTimeout());
    Objects.requireNonNull(fileProps.getScanIntervalMillis());
    Objects.requireNonNull(fileProps.getIntegrationDirectory());
    this.props = fileProps;
    this.fileWatcher = new ResponseFileWatcher(props.getIntegrationDirectory(), props.getScanIntervalMillis());
    Files.createDirectories(props.getIntegrationDirectory());
    log.info(fileProps.toString());
  }

  @Override
  protected void beforeTest() {
    this.fileWatcher.start();
  }

  @Override
  protected void execute(TestProps testProps) throws TesterException {
    String randomName = UUID.randomUUID().toString();
    Path randomPath = this.props.getIntegrationDirectory().resolve(randomName);
    String responseFileName = randomName + this.props.getResponseFileEnding();
    Future<byte[]> responseFuture = fileWatcher.getFileResponseWhenCreated(responseFileName);
    try {
      writeToNewFile(randomPath, testProps);
      byte[] response = responseFuture.get(this.props.getResponseTimeout(),
          this.props.getResponseTimeoutUnit());
      ResponseValidator.validateLength(response, testProps);
    } catch (Exception e) {
      throw new TesterException(e);
    } finally {
      removeCreatedFiles(randomName);
    }
  }

  private void writeToNewFile(Path basePath, TestProps testProps) throws IOException {
    Path tmpPath = basePath.getParent().resolve(basePath.getFileName() + "_request_tmp");
    try (OutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(tmpPath, CREATE, WRITE))) {
      outputStream.write(IntConverter.intToBytes(testProps.getResponseSize()));
      outputStream.write(testProps.getRequestBytes());
      outputStream.flush();
    }
    Path requestPath = basePath.getParent().resolve(basePath.getFileName() + "_request");
    Files.move(tmpPath, requestPath, ATOMIC_MOVE);
  }

  @Override
  protected void afterTest() {
    this.fileWatcher.stop();
  }

  @Override
  protected TestType testType() {
    return TestType.FILE;
  }

  @SneakyThrows(IOException.class)
  private void removeCreatedFiles(String randomName) {
    Files.list(this.props.getIntegrationDirectory())
        .filter(path -> path.getFileName().toString().startsWith(randomName))
        .forEach(this::deleteFile);
  }

  @SneakyThrows(IOException.class)
  private void deleteFile(Path path) {
    Files.deleteIfExists(path);
  }
}
