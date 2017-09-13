package pl.kumon.transfertester.chart;

import pl.kumon.transfertester.utils.Formatter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

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
  private static final String MEDIAN = "Median";
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
    XYChart.Series<String, Number> series = buildDataSeries(data);
    return buildScene(chart, series);
  }

  private String getTitle(ChartData data) {
    String requestSize = Formatter.humanReadableByteCount(data.getRequestBytes());
    String responseSize = Formatter.humanReadableByteCount(data.getResponseBytes());
    return String.format(TITLE_FORMAT, requestSize, responseSize);
  }

  private XYChart.Series<String, Number> buildDataSeries(ChartData data) {
    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.setName(MEDIAN);

    data.getStats().blockingSubscribe(stats ->
        series.getData().add(new XYChart.Data<>(stats.getTestType().name(), stats.getMedianNanos())));
    return series;
  }

  private Scene buildScene(BarChart<String, Number> chart, XYChart.Series<String, Number> series) {
    Scene scene = new Scene(chart, WIDTH, HEIGHT);
    chart.setAnimated(false);
    chart.getData().add(series);
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
