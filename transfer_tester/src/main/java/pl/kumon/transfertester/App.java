package pl.kumon.transfertester;

import pl.kumon.transfertester.utils.ArgsUtils;

public class App {

  public static void main(String[] args) {
    chooseAppByArg(args).run();
  }

  private static Runnable chooseAppByArg(String[] args) {
    String command = ArgsUtils.getArgWithDefault(args, 0, "test");
    if ("chart".equals(command)) {
      return new ChartApp(args);
    }
    return new TesterApp(args);
  }
}

