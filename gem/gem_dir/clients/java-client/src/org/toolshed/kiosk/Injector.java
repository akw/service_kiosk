package org.toolshed.kiosk;

import java.util.HashMap;
import java.util.Map;

public class Injector {
  private static Injector injector = new Injector();

  public static Injector instance() {
    System.out.println(">>> Injector.instance");
    return injector;
  }

  public void injectAll(Object node, Map config) {
    System.out.println(">>> HERE");
    Object[] fakeArgs = new Object[1];
    Method m = KioskUtils.findMethod(node, "setLister", fakeArgs);
    Class c = m.getParameterTypes()[0];
    Object proxiedKiosk = KioskProxy.create(c, new Lister());
    System.out.println(">>> HERE");
    //loop over dependencies
      //lookup resource or create resource
      //wrap the service object with a kiosk proxy that implements the declared class and uses invokedynamic to expose the methods
      //inject child into node
      //injectAll(child, childConfig)
  }

  class Lister {
    public Map listAll() {
      Map list = new HashMap();
      list.put("UW Credit Union", "53934");
      return list;
    }
  }
}
