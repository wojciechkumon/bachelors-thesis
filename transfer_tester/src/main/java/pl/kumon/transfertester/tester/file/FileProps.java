package pl.kumon.transfertester.tester.file;

import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.Getter;

@Getter
public class FileProps {
  private Path integrationDirectory;

  public FileProps() {}

  public FileProps integrationDirectory(String integrationDirectory) {
    this.integrationDirectory = Paths.get(integrationDirectory);
    return this;
  }
}
