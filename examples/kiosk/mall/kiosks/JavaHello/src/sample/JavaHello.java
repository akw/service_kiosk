package sample;

import java.util.LinkedHashMap;
import java.util.Map;

public class JavaHello {
  public Map list(Map input) {
    Map result = new LinkedHashMap();
    if(input!=null && input.containsKey("q")) {
      result.put("greeting", "Hello " + input.get("q"));
    } else {
      result.put("greeting", "Hello World");
    }
    return result;
  }

  public Map read(String id, Map input) {
    Map result = new LinkedHashMap();
    result.put("greeting", "Hello Reader " + id);
    return result;
  }

  public Map create(Map input) {
    Map result = new LinkedHashMap();
    result.put("greeting", "Hello " + input.get("box"));
    return result;
  }

  public Map update(String id, Map input) {
    Map result = new LinkedHashMap();
    result.put("greeting", "Hello " + input.get("box"));
    return result;
  }

  public Map delete(String id) {
    Map result = new LinkedHashMap();
    result.put("status", "ok");
    return result;
  }

  public static void main(String args[]) {
  }
}
