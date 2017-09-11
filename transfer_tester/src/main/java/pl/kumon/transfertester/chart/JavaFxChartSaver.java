package pl.kumon.transfertester.chart;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavaFxChartSaver extends Application {
  private static final String PNG = "PNG";
  static List<ChartData> chartDataList;

  @Override
  public void start(Stage stage) throws Exception {
    chartDataList.forEach(chartData -> {
      stage.setTitle("Line Chart Sample");
      //defining the axes
      final NumberAxis xAxis = new NumberAxis();
      final NumberAxis yAxis = new NumberAxis();
      xAxis.setLabel("Number of Month");
      //creating the chart
      LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
      lineChart.setTitle("Stock Monitoring, 2010");
      //defining a series
      XYChart.Series series = new XYChart.Series();
      series.setName("My portfolio");
      //populating the series with data
      series.getData().add(new XYChart.Data(1, 23));
      series.getData().add(new XYChart.Data(2, 14));
      series.getData().add(new XYChart.Data(3, 15));
      series.getData().add(new XYChart.Data(4, 24));
      series.getData().add(new XYChart.Data(5, 34));
      Scene scene = new Scene(lineChart, 800, 600);
      lineChart.setAnimated(false);
      lineChart.getData().add(series);
      saveAsPng(scene, chartData.getChartPath());
    });

    Platform.exit();
  }

  private void saveAsPng(Scene scene, Path path) {
    WritableImage image = scene.snapshot(null);
    try {
      ImageIO.write(SwingFXUtils.fromFXImage(image, null), PNG, path.toFile());
    } catch (IOException e) {
      log.error("Error while saving png char", e);
    }
  }
}
