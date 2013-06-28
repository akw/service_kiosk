package org.toolshed.kiosk;

import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import org.json.simple.JSONValue;

public class Kiosk {
  static private HashMap kiosks = new HashMap();

  String endpoint;

  public static void setup(Object root) {
    Map config = readConfig();
    Injector.instance().injectAll(root, config);
  }

  private static Map readConfig() {
    String content = "{}";
    try {
      content = new Scanner(new File("Kioskfile.json")).useDelimiter("\\Z").next();
    } catch( java.io.FileNotFoundException e ) {
      throw new RuntimeException(e.getMessage());
    }
    Map result_map = (Map) JSONValue.parse(content);
    return result_map;
  }

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

  public Object service(String serviceName) {
    if(is_remote()) {
      return new RemoteService(endpoint, serviceName);
    }
    return new InternalService(endpoint, serviceName);
  }

  public Boolean is_remote() {
    return ! endpoint.startsWith("java://");
  }
}
