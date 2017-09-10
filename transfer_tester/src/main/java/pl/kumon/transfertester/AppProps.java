package pl.kumon.transfertester;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class AppProps {
  private static final Pattern PROPS_PATTERN = Pattern.compile("([^=]+)=(.+)");
  private final Map<String, String> props;

  AppProps(String[] args) {
    Map<String, String> map = Arrays.stream(args)
        .map(this::parseEntry)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    this.props = Collections.unmodifiableMap(map);
  }

  private Map.Entry<String, String> parseEntry(String arg) {
    Matcher matcher = PROPS_PATTERN.matcher(arg);
    if (matcher.matches()) {
      return ImmutablePair.of(matcher.group(1), matcher.group(2));
    }
    throw new RuntimeException("This arg is not property: " + arg);
  }

  String getOrDefault(String key, String defaultValue) {
    return props.getOrDefault(key, defaultValue);
  }

  int getOrDefault(String key, int defaultValue) {
    return Optional.ofNullable(props.get(key))
        .map(this::parseInt)
        .orElse(defaultValue);
  }

  private Integer parseInt(String value) {
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  Map<String, String> getProps() {
    return props;
  }
}
