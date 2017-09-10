package pl.kumon.transfertester;

import pl.kumon.transfertester.utils.ArgsUtils;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

  public static void main(String[] args) {
    chooseAppByArg(args).run();
  }

  private static Runnable chooseAppByArg(String[] args) {
    String command = ArgsUtils.getArgWithDefault(args, 0, "test");
    AppProps appProps = new AppProps(args);
    printAppProps(appProps.getProps());
    if ("chart".equals(command)) {
      return new ChartApp(appProps);
    }
    return new TesterApp(appProps);
  }

  private static void printAppProps(Map<String, String> appProps) {
    log.info("AppProps ({}):", appProps.size());
    appProps
        .forEach((key, value) -> log.info(key + "=" + value));
  }
}

