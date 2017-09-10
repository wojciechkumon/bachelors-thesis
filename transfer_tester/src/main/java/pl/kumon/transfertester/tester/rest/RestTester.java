package pl.kumon.transfertester.tester.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import pl.kumon.transfertester.exception.TesterException;
import pl.kumon.transfertester.tester.AbstractTransferTester;
import pl.kumon.transfertester.tester.TestProps;
import pl.kumon.transfertester.tester.TestType;
import pl.kumon.transfertester.utils.ResponseValidator;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class RestTester extends AbstractTransferTester {
  private static final MediaType JSON_TYPE = MediaType.parse("application/json");
  private final RestProps props;
  private final OkHttpClient httpClient;
  private final ObjectMapper objectMapper;

  public RestTester(RestProps props) {
    this(props, new OkHttpClient(), new ObjectMapper());
  }

  private RestTester(RestProps props, OkHttpClient httpClient, ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    Objects.requireNonNull(props.getUrl());
    Objects.requireNonNull(httpClient);
    this.httpClient = httpClient;
    this.props = props;
    log.info(props.toString());
  }

  @Override
  public void execute(TestProps testProps) throws TesterException {
    try {
      Request request = buildRequest(testProps);
      byte[] response = sendRequest(request);
      ResponseValidator.validateLength(response, testProps);
    } catch (IOException e) {
      throw new TesterException(e);
    }
  }

  @SuppressWarnings("unchecked")
  private byte[] sendRequest(Request request) throws IOException, TesterException {
    ResponseBody responseBody = httpClient.newCall(request).execute().body();
    if (responseBody == null) {
      throw new TesterException("Null response body");
    }
    Map<String, String> map = objectMapper.readValue(responseBody.byteStream(), Map.class);
    String result = map.get("result");
    return result.getBytes(UTF_8);
  }

  private Request buildRequest(TestProps testProps) throws IOException {
    String requestBody = buildRequestBody(testProps);
    return new Request.Builder()
        .url(props.getUrl())
        .post(RequestBody.create(JSON_TYPE, requestBody))
        .build();
  }

  private String buildRequestBody(TestProps testProps) {
    ObjectNode objectNode = objectMapper.createObjectNode();
    objectNode.put(props.getDataJsonKey(), testProps.getRequestBytes());
    objectNode.put(props.getResponseSizeJsonKey(), testProps.getResponseSize());
    return objectNode.toString();
  }

  @Override
  protected TestType testType() {
    return TestType.REST;
  }
}
