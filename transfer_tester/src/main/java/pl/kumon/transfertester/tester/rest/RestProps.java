package pl.kumon.transfertester.tester.rest;

public class RestProps {
  private String url;

  public RestProps() {}

  public RestProps url(String url) {
    this.url = url;
    return this;
  }

  String getUrl() {
    return url;
  }
}
