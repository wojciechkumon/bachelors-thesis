package pl.kumon.transfertester.chart;

import java.nio.file.Path;

import io.reactivex.Observable;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChartData {
  private final int requestBytes;
  private final int responseBytes;
  private final Observable<TestExecutionStats> stats;
  private final Path chartPath;
}
