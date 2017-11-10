package pl.kumon.transfertester.chart;

import pl.kumon.transfertester.tester.TestType;

class I18n {
  static final String TITLE_FORMAT = "Żądanie %s, odpowiedź %s";
  static final String TEST_TYPE = "Typ testu";
  static final String EXECUTION_TIME_NANOS = "Czas wykonania [ns]";
  static final String MEDIAN = "Mediana";
  static final String STANDARD_DEVIATION = "Odchylenie standardowe";
  static final String OCCURRENCES = "Wystąpienia w przedziale";
  static final String EXECUTION_TIME_MICROS = "Czas wykonania [\u00B5s]";

  private I18n() {}

  static String t(TestType testType) {
    switch (testType) {
      case FILE:
        return "Pliki";
      case MOCK:
        return "Mock";
      default:
        return testType.name();
    }
  }
}