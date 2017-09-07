package pl.kumon.transfertester.tester.file;

import org.apache.commons.io.FileUtils;

import pl.kumon.transfertester.tester.AbstractTransferTester;
import pl.kumon.transfertester.tester.TestProps;
import pl.kumon.transfertester.exception.TesterException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Future;

import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;

public class FileTester extends AbstractTransferTester {
  private final FileProps props;
  private final ResponseFileWatcher fileWatcher;

  public FileTester(FileProps fileProps) {
    Objects.requireNonNull(fileProps.getIntegrationDirectory());
    Objects.requireNonNull(fileProps.getResponseTimeoutUnit());
    Objects.requireNonNull(fileProps.getResponseTimeout());
    Objects.requireNonNull(fileProps.getScanIntervalMillis());
    Objects.requireNonNull(fileProps.getIntegrationDirectory());
    this.props = fileProps;
    this.fileWatcher = new ResponseFileWatcher(props.getIntegrationDirectory(), props.getScanIntervalMillis());
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
    Future<String> responseFuture = fileWatcher.getFileResponseWhenCreated(responseFileName);
    try {
      writeToNewFile(randomPath);
      fakeResponseFile(randomName, responseFileName);
      responseFuture.get(this.props.getResponseTimeout(), this.props.getResponseTimeoutUnit());
    } catch (Exception e) {
      throw new TesterException(e);
    }
  }

  private void fakeResponseFile(String randomName, String responseFileName) {
    new Thread(() -> {
      try {
        File testFile = new File(props.getIntegrationDirectory() + "/" + randomName + "_tmp");
        FileUtils.writeStringToFile(testFile, "response file content", StandardCharsets.UTF_8);
        testFile.renameTo(new File(testFile.getParentFile(), responseFileName));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }).start();
  }

  private void writeToNewFile(Path tmpFilePath) throws IOException {
    try (BufferedWriter writer = Files.newBufferedWriter(tmpFilePath, StandardOpenOption.CREATE)) {
      writer.write("request file content");
      writer.flush();
    }
    Path requestPath = tmpFilePath.getParent().resolve(tmpFilePath.getFileName() + "_request");
    Files.move(tmpFilePath, requestPath, ATOMIC_MOVE);
  }

  @Override
  protected void afterTest() {
    this.fileWatcher.stop();
  }
}
