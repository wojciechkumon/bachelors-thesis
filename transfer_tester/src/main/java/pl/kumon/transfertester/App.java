package pl.kumon.transfertester;

import pl.kumon.transfertester.tester.TransferTester;
import pl.kumon.transfertester.tester.TransferTesterBuilder;
import pl.kumon.transfertester.tester.rest.RestProps;

public class App {

  public static void main(String[] args) {
    TransferTester tester = TransferTesterBuilder
        .rest(new RestProps().url("https://new-cook-up.herokuapp.com/api/ingredients"))
        .build();

    tester.test();
  }
}

