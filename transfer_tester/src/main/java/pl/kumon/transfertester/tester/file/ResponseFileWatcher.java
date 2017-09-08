package pl.kumon.transfertester.tester.file;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import lombok.SneakyThrows;

class ResponseFileWatcher {
  private final FileAlterationObserver observer;
  private final FileAlterationMonitor monitor;

  ResponseFileWatcher(Path integrationDirectory, long scanInterval) {
    this.observer = new FileAlterationObserver(integrationDirectory.toFile());
    this.monitor = new FileAlterationMonitor(scanInterval);
    this.monitor.addObserver(observer);
  }

  @SneakyThrows
  void start() {
    monitor.start();
  }

  @SneakyThrows
  void stop() {
    monitor.stop();
  }

  Future<byte[]> getFileResponseWhenCreated(String responseFileName) {
    CompletableFuture<byte[]> responseFuture = new CompletableFuture<>();
    FileCreationListener fileCreationListener = new FileCreationListener(responseFileName, responseFuture);
    addListener(fileCreationListener);
    responseFuture.thenRun(this::clearListeners);
    return responseFuture;
  }

  private void addListener(FileAlterationListener listener) {
    this.observer.addListener(listener);
  }

  private void clearListeners() {
    Iterable<FileAlterationListener> listeners = this.observer.getListeners();
    listeners.forEach(this.observer::removeListener);
  }
}
