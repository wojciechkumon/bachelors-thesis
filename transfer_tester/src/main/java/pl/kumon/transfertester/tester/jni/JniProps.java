package pl.kumon.transfertester.tester.jni;

import lombok.Getter;

@Getter
public class JniProps {
  private String appDirName;

  public JniProps appDirName(String appDirName) {
    this.appDirName = appDirName;
    return this;
  }
}
