package pl.kumon.transfertester.chart;

import java.util.List;

import javafx.application.Application;

public class ChartService {

  public void saveCharts(List<ChartData> chartDataList) {
    JavaFxChartSaver.chartDataList = chartDataList;
    Application.launch(JavaFxChartSaver.class);
  }
}
