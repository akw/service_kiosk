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
    Map config = KioskConfig.readConfig();
    Map env = KioskConfig.env();
    Injector injector = Injector.instance();

    // Call injectAll twice.  Second time to inject secondary dependencies once all primaries
    // have been created.
    System.out.println("[Kiosk] --- Injecting primary dependencies ---");
    injector.injectAll(root, config, env, "");
    System.out.println("[Kiosk] --- Injecting secondary dependencies ---");
    injector.injectAll(root, config, env, "");
  }

  public Kiosk(String handle) {
    endpoint = handle;
  }
}
