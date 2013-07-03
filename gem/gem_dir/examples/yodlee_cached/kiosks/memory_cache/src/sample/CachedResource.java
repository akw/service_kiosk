package sample;

import java.util.HashMap;
import java.util.Map;

import org.toolshed.kiosk.GenericResource;

public class CachedResource {
  private Object target;

  public void setTarget(GenericResource target) {
    this.target = target;
  }

  public Object one() {
    Map list = new HashMap();
    list.put("UW Credit Union", "53934");
    return list;
  }

  public Map two(int count, String name) {
    Map list = new HashMap();
    list.put(name, count);
    return list;
  }

  public Object method_missing(String method, Object... arguments) {
    Map list = new HashMap();
    list.put(arguments[1], arguments[0]);
    return list;
  }
}
