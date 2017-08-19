package pl.kumon.transfertester.tester.rest;

import pl.kumon.transfertester.metrics.Metrics;
import pl.kumon.transfertester.tester.TestProps;
import pl.kumon.transfertester.tester.TransferTester;

import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class RestTester implements TransferTester {
  private final RestProps restProps;
  private final OkHttpClient httpClient;

  public RestTester(RestProps restProps) {
    this(restProps, new OkHttpClient());
  }

  private RestTester(RestProps restProps, OkHttpClient httpClient) {
    Objects.requireNonNull(restProps.getUrl());
    Objects.requireNonNull(httpClient);
    this.httpClient = httpClient;
    this.restProps = restProps;
  }

  @Override
  public Metrics test() {
    Request request = buildRequest();
    try {
      long start = System.currentTimeMillis();
      httpClient.newCall(request).execute();
      long time = System.currentTimeMillis() - start;
      return Metrics.of(time);
    } catch (Exception e) {
      e.printStackTrace();
      return Metrics.error();
    }
  }

  private Request buildRequest() {
    return new Request.Builder()
        .url(restProps.getUrl())
        .build();
  }

  public Metrics test(TestProps props) {
    return null;
  }
}
