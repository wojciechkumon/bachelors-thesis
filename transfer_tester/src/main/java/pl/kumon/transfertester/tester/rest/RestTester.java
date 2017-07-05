package pl.kumon.transfertester.tester.rest;

import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import pl.kumon.transfertester.metrics.Metrics;
import pl.kumon.transfertester.tester.TestProps;
import pl.kumon.transfertester.tester.TransferTester;

import java.util.Objects;

public class RestTester implements TransferTester {
  private final RestOperations restOperations;
  private final RestProps restProps;

  public RestTester(RestProps restProps) {
    this(restProps, new RestTemplate());
  }

  public RestTester(RestProps restProps, RestOperations restOperations) {
    Objects.requireNonNull(restProps.getUrl());
    this.restOperations = restOperations;
    this.restProps = restProps;
  }

  // TODO RestTester
  public Metrics test() {
    String str = restOperations.getForObject(restProps.getUrl(), String.class);
    return null;
  }

  public Metrics test(TestProps props) {
    return null;
  }
}
