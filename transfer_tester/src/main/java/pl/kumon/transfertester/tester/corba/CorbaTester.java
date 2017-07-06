package pl.kumon.transfertester.tester.corba;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;

import pl.kumon.transfertester.metrics.Metrics;
import pl.kumon.transfertester.tester.TestProps;
import pl.kumon.transfertester.tester.TransferTester;

public class CorbaTester implements TransferTester {

  // TODO corba tester
  @Override
  public Metrics test() {

    try {

      // Create and initialize the ORB
      ORB orb = ORB.init();

      // Get the root naming context
      org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
      NamingContext ncRef = NamingContextHelper.narrow(objRef);

      // Resolve the object reference in naming
      NameComponent nc = new NameComponent("Hello", "");
      NameComponent path[] = {nc};
      CorbaConnector corbaConnector = CorbaConnectorHelper.narrow(ncRef.resolve(path));

      // Call the Hello server object and print results
      String hello = corbaConnector.get();
      System.out.println(hello);

    } catch (Exception e) {
      System.out.println("ERROR : " + e);
      e.printStackTrace(System.out);
    }


    return null;
  }

  @Override
  public Metrics test(TestProps props) {
    return null;
  }
}
