package pl.kumon.transfertester.tester.rest;

import lombok.Getter;

@Getter
public class RestProps {
  private String url;
  private String dataJsonKey;
  private String responseSizeJsonKey;

  public RestProps url(String url) {
    this.url = url;
    return this;
  }

  public RestProps dataJsonKey(String dataJsonKey) {
    this.dataJsonKey = dataJsonKey;
    return this;
  }

  public RestProps responseSizeJsonKey(String responseSizeJsonKey) {
    this.responseSizeJsonKey = responseSizeJsonKey;
    return this;
  }
}
