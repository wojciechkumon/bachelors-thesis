package pl.kumon.transfertester.chart;

import pl.kumon.transfertester.utils.Formatter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static java.util.stream.Collectors.toList;
import static pl.kumon.transfertester.chart.I18n.EXECUTION_TIME_MICROS;
import static pl.kumon.transfertester.chart.I18n.EXECUTION_TIME_NANOS;
import static pl.kumon.transfertester.chart.I18n.MEDIAN;
import static pl.kumon.transfertester.chart.I18n.OCCURRENCES;
import static pl.kumon.transfertester.chart.I18n.STANDARD_DEVIATION;
import static pl.kumon.transfertester.chart.I18n.TEST_TYPE;
import static pl.kumon.transfertester.chart.I18n.TITLE_FORMAT;

@Slf4j
public class JavaFxChartSaver extends Application {
  private static final String PNG = "PNG";
  private static final int WIDTH = 800;
  private static final int HEIGHT = 480;
  private static final int NUMBER_OF_GROUPS = 40;

  static List<ChartData> chartDataList;

  @Override
  public void start(Stage stage) {
    chartDataList.forEach(chartData -> {
      List<TestExecutionStats> stats = chartData.getStats()
          .sorted(Comparator.comparing(TestExecutionStats::getTestType))
          .toList()
          .toMaybe()
          .blockingGet();
      logChartData(stats);
      Scene comparisionChart = prepareChartScene(chartData, stats);
      saveAsPng(comparisionChart, chartData.getChartPath());
      stats.forEach(testTypeStats -> saveTestTypeChart(testTypeStats, chartData));
    });

    Platform.exit();
  }

  private Scene prepareChartScene(ChartData data, List<TestExecutionStats> stats) {
    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();
    BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
    chart.setTitle(getTitle(data));
    xAxis.setLabel(TEST_TYPE);
    yAxis.setLabel(EXECUTION_TIME_NANOS);

    List<XYChart.Series<String, Number>> seriesList = buildAllDataSeries(stats);
    return buildScene(chart, seriesList);
  }

  private String getLowerCasedTitle(ChartData data) {
    return getTitle(data, TITLE_FORMAT.toLowerCase());
  }

  private String getTitle(ChartData data) {
    return getTitle(data, TITLE_FORMAT);
  }

  private String getTitle(ChartData data, String format) {
    String requestSize = Formatter.humanReadableByteCount(data.getRequestBytes());
    String responseSize = Formatter.humanReadableByteCount(data.getResponseBytes());
    return String.format(format, requestSize, responseSize);
  }

  private List<XYChart.Series<String, Number>> buildAllDataSeries(List<TestExecutionStats> stats) {
    XYChart.Series<String, Number> medianSeries = buildDataSeries(stats, MEDIAN,
        TestExecutionStats::getMedianNanos);
    XYChart.Series<String, Number> standardDeviationSeries = buildDataSeries(stats, STANDARD_DEVIATION,
        TestExecutionStats::getStandardDeviationNanos);
    return List.of(medianSeries, standardDeviationSeries);
  }

  private XYChart.Series<String, Number> buildDataSeries(List<TestExecutionStats> statsList, String name,
                                                         Function<TestExecutionStats, Number> dataGetter) {
    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.setName(name);

    statsList.forEach(stats ->
        series.getData().add(
            new XYChart.Data<>(I18n.t(stats.getTestType()), dataGetter.apply(stats))));
    return series;
  }

  private Scene buildScene(BarChart<String, Number> chart, List<XYChart.Series<String, Number>> series) {
    Scene scene = new Scene(chart, WIDTH, HEIGHT);
    chart.setAnimated(false);
    chart.getData().addAll(series);
    return scene;
  }

  @SneakyThrows(IOException.class)
  private void saveAsPng(Scene scene, Path path) {
    Files.createDirectories(path.getParent());
    WritableImage image = scene.snapshot(null);
    try {
      ImageIO.write(SwingFXUtils.fromFXImage(image, null), PNG, path.toFile());
    } catch (IOException e) {
      log.error("Error while saving png char", e);
    }
  }

  private void saveTestTypeChart(TestExecutionStats testTypeStats, ChartData chartData) {
    NumberAxis xAxis = new NumberAxis();
    NumberAxis yAxis = new NumberAxis();
    LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
    chart.setTitle(I18n.t(testTypeStats.getTestType()) + " - " + getLowerCasedTitle(chartData));
    yAxis.setLabel(OCCURRENCES);

    XYChart.Series<Number, Number> series = new XYChart.Series<>();

    int toSkip = (int) (testTypeStats.getDataSetNanos().size() * 0.05);
    List<Long> sortedData = testTypeStats.getDataSetNanos()
        .stream()
        .sorted()
        .skip(toSkip)
        .limit(testTypeStats.getDataSetNanos().size() - (2 * toSkip))
        .collect(toList());

    boolean microseconds = !sortedData.isEmpty()
        && (sortedData.get(sortedData.size() - 1) - sortedData.get(0) > 100_000);
    if (microseconds) {
      sortedData = sortedData.stream()
          .map(x -> x / 1000)
          .collect(toList());
    }

    long maxValue = sortedData.isEmpty() ? 0 : sortedData.get(sortedData.size() - 1);
    long minValue = sortedData.isEmpty() ? 0 : sortedData.get(0);
    minValue = minValue - (3 * ((maxValue - minValue) / NUMBER_OF_GROUPS));
    long xStep = (maxValue - minValue) / NUMBER_OF_GROUPS;
    long xStart = minValue + xStep / 2;
    List<XYChart.Data<Number, Number>> data = new ArrayList<>();
    addDataToList(sortedData, minValue, xStep, xStart, data);

    int lastIndex = getLastIndex(sortedData, data);

    maxValue = minValue + (lastIndex + 1) * xStep;
    xStep = (maxValue - minValue) / NUMBER_OF_GROUPS;
    xStart = minValue + xStep / 2;
    data = new ArrayList<>();
    addDataToList(sortedData, minValue, xStep, xStart, data);

    if (microseconds) {
      xAxis.setLabel(EXECUTION_TIME_MICROS);
    } else {
      xAxis.setLabel(EXECUTION_TIME_NANOS);
    }
    xAxis.setAutoRanging(false);
    xAxis.setLowerBound(minValue);
    xAxis.setUpperBound(maxValue);
    xAxis.setTickUnit((maxValue - minValue) / NUMBER_OF_GROUPS);
    xAxis.setMinorTickVisible(false);
    series.getData().addAll(data);
    Scene scene = new Scene(chart, WIDTH, HEIGHT);
    chart.setAnimated(false);
    chart.setLegendVisible(false);
    chart.getData().add(series);

    Path path = chartData.getChartPath()
        .getParent()
        .resolve(testTypeStats.getTestType() + "_" + chartData.getChartPath().getFileName());
    saveAsPng(scene, path);
  }

  private void addDataToList(List<Long> sortedData, long minValue, long xStep,
                             long xStart, List<XYChart.Data<Number, Number>> data) {
    for (int i = 0; i <= NUMBER_OF_GROUPS; i++) {
      final int j = i;
      final long min = minValue;
      final long step = xStep;
      long numberOfMatchingData = sortedData.stream()
          .filter(x -> x >= min + j * step && x < min + (j + 1) * step)
          .count();

      long xValue = xStart + i * xStep;
      data.add(new XYChart.Data<>(xValue, numberOfMatchingData));
    }
  }

  private int getLastIndex(List<Long> sortedData, List<XYChart.Data<Number, Number>> data) {
    int lastIndex = data.isEmpty() ? 0 : data.size() - 1;
    for (int i = data.size() - 1; i >= 0; i--) {
      if (data.get(i).getYValue().longValue() <= sortedData.size() * 0.01) {
        lastIndex = i;
      } else {
        break;
      }
    }
    lastIndex = lastIndex + 3 < data.size() ? lastIndex + 1 : data.size() - 1;
    return lastIndex;
  }

  private void logChartData(List<TestExecutionStats> testExecutionStats) {
      testExecutionStats.stream()
              .map(this::statsToString)
              .forEach(log::info);
  }

  private String statsToString(TestExecutionStats stats) {
      return stats.getTestType() + " "
              + "requestSize=" + stats.getRequestBytes() + " "
              + "responseSize=" + stats.getResponseBytes() + " "
              + "medianNanos=" + stats.getMedianNanos() + " "
              + "standardDeviationNanos=" + stats.getStandardDeviationNanos();
  }
}
