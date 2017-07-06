package pl.kumon.transfertester.tester.tcp;

public class TcpProps {
  private String ip;
  private int port;

  public TcpProps() {}

  public TcpProps ip(String ip) {
    this.ip = ip;
    return this;
  }

  public TcpProps port(int port) {
    this.port = port;
    return this;
  }

  public String getIp() {
    return ip;
  }

  public int getPort() {
    return port;
  }
}
