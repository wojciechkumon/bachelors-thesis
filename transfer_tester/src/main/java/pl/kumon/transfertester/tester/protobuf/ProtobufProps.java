package pl.kumon.transfertester.tester.protobuf;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ProtobufProps {
  private String ip;
  private int port;

  public ProtobufProps() {}

  public ProtobufProps ip(String ip) {
    this.ip = ip;
    return this;
  }

  public ProtobufProps port(int port) {
    this.port = port;
    return this;
  }
}
