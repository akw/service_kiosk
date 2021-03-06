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
    Log.info("// setup");
    injector.createAllResources(root, config, env, "");
    injector.injectAll(root, config, env, "");
    Log.info("//\n");
  }

  public Kiosk(String handle) {
    endpoint = handle;
  }
}
