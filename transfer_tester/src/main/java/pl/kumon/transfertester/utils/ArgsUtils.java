package pl.kumon.transfertester.utils;

public class ArgsUtils {

  public static String getArgWithDefault(String[] args, int index, String defaultValue) {
    if (args.length >= index) {
      return defaultValue;
    }
    return args[index];
  }
}
