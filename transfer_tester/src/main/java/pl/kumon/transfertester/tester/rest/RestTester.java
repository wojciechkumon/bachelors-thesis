package pl.kumon.transfertester.tester.rest;

import pl.kumon.transfertester.tester.AbstractTransferTester;
import pl.kumon.transfertester.tester.TestProps;
import pl.kumon.transfertester.tester.exception.TesterException;

import java.io.IOException;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@Slf4j
public class RestTester extends AbstractTransferTester {
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
  public void execute(TestProps testProps) throws TesterException {
    Request request = buildRequest();
    try {
      httpClient.newCall(request).execute();
    } catch (IOException e) {
      throw new TesterException(e);
    }
  }

  private Request buildRequest() {
    return new Request.Builder()
        .url(restProps.getUrl())
        .build();
  }
}
