package pl.kumon.transfertester.tester.file;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import lombok.Getter;

@Getter
public class FileProps {
  private Path integrationDirectory;
  private TimeUnit responseTimeoutUnit;
  private Long responseTimeout;
  private Long scanIntervalMillis;
  private String responseFileEnding;

  public FileProps() {}

  public FileProps integrationDirectory(String integrationDirectory) {
    this.integrationDirectory = Paths.get(integrationDirectory);
    return this;
  }

  public FileProps responseTimeoutUnit(TimeUnit responseTimeoutUnit) {
    this.responseTimeoutUnit = responseTimeoutUnit;
    return this;
  }

  public FileProps responseTimeout(long responseTimeout) {
    this.responseTimeout = responseTimeout;
    return this;
  }

  public FileProps scanIntervalMillis(long scanIntervalMillis) {
    this.scanIntervalMillis = scanIntervalMillis;
    return this;
  }

  public FileProps responseFileEnding(String responseFileEnding) {
    this.responseFileEnding = responseFileEnding;
    return this;
  }
}
