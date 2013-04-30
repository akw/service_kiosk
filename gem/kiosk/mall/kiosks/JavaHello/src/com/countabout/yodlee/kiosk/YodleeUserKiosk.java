package com.countabout.yodlee.kiosk;

import java.util.LinkedHashMap;
import java.util.Map;

public class YodleeUserKiosk {
  public Map list(Map input) {
    System.out.println(">>> jh.list: " );
    Map result = new LinkedHashMap();
    if(input!=null && input.containsKey("q")) {
      result.put("greeting", "Hello " + input.get("q"));
    } else {
      result.put("greeting", "Hello World");
    }
    return result;
  }

  public Map read(Map input) {
    Map result = new LinkedHashMap();
    String id = (String) input.get("id");
    result.put("greeting", "Hello Reader " + id);
    return result;
  }

  public Map create(Map input) {
    Map result = new LinkedHashMap();
    result.put("greeting", "Hello " + input.get("box"));
    return result;
  }

  public Map update(Map input) {
    Map result = new LinkedHashMap();
    String id = (String) input.get("id");
    result.put("greeting", "Hello " + input.get("box"));
    return result;
  }

  public Map delete(Map input) {
    Map result = new LinkedHashMap();
    String id = (String) input.get("id");
    result.put("status", "ok");
    return result;
  }

  public static void main(String args[]) {
  }
}
