package pl.kumon.transfertester.utils;

public class ArgsUtils {

  public static String getArgWithDefault(String[] args, int index, String defaultValue) {
    if (index >= args.length) {
      return defaultValue;
    }
    return args[index];
  }
}
