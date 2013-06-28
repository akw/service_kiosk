package org.toolshed.kiosk;

import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.*;

public class Injector {
  private static Injector injector = new Injector();

  public static Injector instance() {
    System.out.println(">>> Injector.instance");
    return injector;
  }

  private Map resources = new HashMap();
  private Map kiosks = new HashMap();

  public void injectAll(Object node, Map config) {
    Object kiosk;
    for(Object name : getWiring(config)) {
      System.out.println("name = " + (String)name);
      if(resources.containsKey(name)) {
        kiosk = resources.get(name);
      } else {
        // need to build this kiosk now
      }
      inject(node, kiosk, injectorName((String) name, config));
    }
    /*
    System.out.println(">>> HERE");
    Object[] fakeArgs = new Object[] {node};
    fakeArgs[0] = node;
    System.out.println("fakeArgs.length = " + fakeArgs.length);
    Method m = KioskUtils.findMethod(node, "setLister", fakeArgs);
    Class c = m.getParameterTypes()[0];
    Object proxiedKiosk = KioskProxy.create(c, new Lister());
    try {
      m.invoke(node, proxiedKiosk);
    } catch(Exception e) {
      e.printStackTrace();
    }
    */
    System.out.println(">>> HERE");
    //loop over dependencies
      //lookup resource or create resource
      //wrap the service object with a kiosk proxy that implements the declared class and uses invokedynamic to expose the methods
      //inject child into node
      //injectAll(child, childConfig)
  }

  public Set getWiring(Map config) {
    return ((Map) config.get("wiring")).keySet();
  }

  class Lister {
    public Map listAll() {
      Map list = new HashMap();
      list.put("UW Credit Union", "53934");
      return list;
    }
  }
}
