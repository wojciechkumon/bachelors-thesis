package pl.kumon.transfertester.tester.tcp;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TcpProps {
  private String ip;
  private int port;

  public TcpProps ip(String ip) {
    this.ip = ip;
    return this;
  }

  public TcpProps port(int port) {
    this.port = port;
    return this;
  }
}
