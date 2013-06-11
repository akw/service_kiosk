package org.toolshed.kiosk;

import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Map;

public class Kiosk {
  static private HashMap kiosks = new HashMap();

  String endpoint;

  public static Kiosk open(String handle_raw) {
    String handle = handle_raw.toUpperCase();
    Kiosk kiosk = (Kiosk) kiosks.get(handle);
    if(null==kiosk) {
      String locator = System.getenv().get("KIOSK_" + handle);
      if(null!=locator) {
        kiosk = new Kiosk(locator);
        kiosks.put(handle, kiosk);
      }
    }
    if(null==kiosk) {
      throw new RuntimeException("No Kiosk location found in environment for " + handle);
    }
    return kiosk;
  }

  public Kiosk(String handle) {
    endpoint = handle;
  }

  public KioskService service(String serviceName) {
    return new KioskService(endpoint, serviceName);
  }
}
