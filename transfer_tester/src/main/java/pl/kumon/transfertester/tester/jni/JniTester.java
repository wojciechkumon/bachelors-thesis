package pl.kumon.transfertester.tester.jni;

import pl.kumon.transfertester.exception.TesterException;
import pl.kumon.transfertester.tester.AbstractTransferTester;
import pl.kumon.transfertester.tester.TestProps;
import pl.kumon.transfertester.tester.TestType;
import pl.kumon.transfertester.utils.ResponseValidator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.SneakyThrows;

public class JniTester extends AbstractTransferTester {

  @SneakyThrows(IOException.class)
  public JniTester(JniProps jniProps) {
    String libraryName = System.mapLibraryName("JniExecutor");
    Path libraryPath = copyLibraryToUserHome(jniProps, libraryName);
    System.load(libraryPath.toString());
  }

  private Path copyLibraryToUserHome(JniProps jniProps, String libraryName) throws IOException {
    Path dirToSaveLib = Paths.get(System.getProperty("user.home"), jniProps.getAppDirName());
    Path pathToSaveLib = dirToSaveLib.resolve(libraryName);

    Files.deleteIfExists(pathToSaveLib);
    Files.createDirectories(dirToSaveLib);

    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(libraryName)) {
      Files.copy(inputStream, pathToSaveLib);
    }
    return pathToSaveLib;
  }

  @Override
  protected void execute(TestProps testProps) throws TesterException {
    byte[] response = new JniExecutor()
        .requestJni(testProps.getRequestBytes(), testProps.getResponseSize());
    ResponseValidator.validateLength(response, testProps);
  }

  @Override
  protected TestType testType() {
    return TestType.JNI;
  }
}
