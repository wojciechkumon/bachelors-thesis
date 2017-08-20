package pl.kumon.transfertester.tester.file;

import pl.kumon.transfertester.metrics.Metrics;
import pl.kumon.transfertester.tester.AbstractTransferTester;
import pl.kumon.transfertester.tester.TestProps;
import pl.kumon.transfertester.tester.exception.TesterException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.UUID;

public class FileTester extends AbstractTransferTester {
  private final FileProps fileProps;

  public FileTester(FileProps fileProps) {
    Objects.requireNonNull(fileProps.getIntegrationDirectory());
    this.fileProps = fileProps;
  }

  @Override
  protected void execute() throws TesterException {
    String newFileName = UUID.randomUUID().toString();
    Path newFile = fileProps.getIntegrationDirectory().resolve(newFileName);
    try (BufferedWriter writer = Files.newBufferedWriter(newFile, StandardOpenOption.CREATE)) {
      writer.write("file tester call");
    } catch (IOException e) {
      throw new TesterException(e);
    }
  }

  @Override
  public Metrics test(TestProps props) {
    return null;
  }
}
