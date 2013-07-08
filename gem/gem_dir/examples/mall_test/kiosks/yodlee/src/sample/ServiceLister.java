package sample;

import java.util.HashMap;
import java.util.Map;

public class ServiceLister {
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

  public Map listAll() {
    Map list = new HashMap();
    System.out.println("ServiceLister: listAll()");
    list.put("UW Credit Union", "53934");
    return list;
  }

  public Object method_missing(String method, Object... arguments) {
    Map list = new HashMap();
    list.put(arguments[1], arguments[0]);
    return list;
  }

}
