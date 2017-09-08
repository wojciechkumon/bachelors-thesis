package pl.kumon.transfertester.tester.file;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class FileCreationListener extends FileAlterationListenerAdaptor {
  private final String fileNameToWaitFor;
  private final CompletableFuture<byte[]> responseFuture;

  FileCreationListener(String fileNameToWaitFor, CompletableFuture<byte[]> responseFuture) {
    this.fileNameToWaitFor = fileNameToWaitFor;
    this.responseFuture = responseFuture;
  }

  @Override
  public void onFileCreate(File file) {
    try {
      if (file.getName().equals(fileNameToWaitFor)) {
        handleResponseFile(file);
      }
    } catch (IOException e) {
      responseFuture.cancel(true);
      log.error("onFileCreate exception", e);
    }
  }

  private void handleResponseFile(File file) throws IOException {
    byte[] response = FileUtils.readFileToByteArray(file);
    responseFuture.complete(response);
  }
}
