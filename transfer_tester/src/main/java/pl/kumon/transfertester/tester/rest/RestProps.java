package pl.kumon.transfertester.tester.rest;

import lombok.Getter;

@Getter
public class RestProps {
  private String url;

  public RestProps() {}

  public RestProps url(String url) {
    this.url = url;
    return this;
  }
}
