package pl.kumon.transfertester.chart;

import pl.kumon.transfertester.utils.Formatter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavaFxChartSaver extends Application {
  private static final String PNG = "PNG";
  private static final int WIDTH = 800;
  private static final int HEIGHT = 600;
  private static final String TITLE_FORMAT = "Request %s, response %s";
  private static final String X_AXIS_LABEL = "Test type";
  private static final String Y_AXIS_LABEL = "Execution time [ns]";
  private static final String MIN = "Min";
  private static final String FIRST_QUARTILE = "First quartile";
  private static final String MEDIAN = "Median";
  private static final String THIRD_QUARTILE = "Third quartile";
  private static final String MAX = "Max";

  static List<ChartData> chartDataList;

  @Override
  public void start(Stage stage) throws Exception {
    chartDataList.forEach(chartData -> {
      Scene scene = prepareChartScene(chartData);
      saveAsPng(scene, chartData.getChartPath());
    });

    Platform.exit();
  }

  private Scene prepareChartScene(ChartData data) {
    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();
    BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
    chart.setTitle(getTitle(data));
    xAxis.setLabel(X_AXIS_LABEL);
    yAxis.setLabel(Y_AXIS_LABEL);

    List<TestExecutionStats> stats = data.getStats().toList().toMaybe().blockingGet();
    List<XYChart.Series<String, Number>> seriesList = buildAllDataSeries(stats);
    return buildScene(chart, seriesList);
  }

  private String getTitle(ChartData data) {
    String requestSize = Formatter.humanReadableByteCount(data.getRequestBytes());
    String responseSize = Formatter.humanReadableByteCount(data.getResponseBytes());
    return String.format(TITLE_FORMAT, requestSize, responseSize);
  }

  private List<XYChart.Series<String, Number>> buildAllDataSeries(List<TestExecutionStats> stats) {
    XYChart.Series<String, Number> minSeries = buildDataSeries(stats, MIN,
        TestExecutionStats::getMinNanos);
    XYChart.Series<String, Number> firstQuartileSeries = buildDataSeries(stats, FIRST_QUARTILE,
        TestExecutionStats::getFirstQuartileNanos);
    XYChart.Series<String, Number> medianSeries = buildDataSeries(stats, MEDIAN,
        TestExecutionStats::getMedianNanos);
    XYChart.Series<String, Number> thirdQuartileSeries = buildDataSeries(stats, THIRD_QUARTILE,
        TestExecutionStats::getThirdQuartileNanos);
    XYChart.Series<String, Number> maxSeries = buildDataSeries(stats, MAX,
        TestExecutionStats::getMaxNanos);

    return List.of(minSeries, firstQuartileSeries, medianSeries, thirdQuartileSeries, maxSeries);
  }

  private XYChart.Series<String, Number> buildDataSeries(List<TestExecutionStats> statsList, String name,
                                                         Function<TestExecutionStats, Long> dataGetter) {
    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.setName(name);

    statsList.forEach(stats ->
        series.getData().add(new XYChart.Data<>(stats.getTestType().name(), dataGetter.apply(stats))));
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
}
