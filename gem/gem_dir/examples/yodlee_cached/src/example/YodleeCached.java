package example;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;
import org.toolshed.kiosk.Kiosk;
/*
import org.toolshed.kiosk.Kiosk;
import org.toolshed.kiosk.KioskService;
import org.toolshed.kiosk.KioskProxy;
import org.toolshed.kiosk.Greeter;
*/

public class YodleeCached {
  // bootstrap method
  public static void main(String args[]) {
    (new YodleeCached()).run();
  }

  public interface ServiceLister {
    public Map one();
    public Map two(int count, String name);
    public Map three(int count, String name);
  }

  private ServiceLister lister;
  private ServiceLister cachedLister;

  public void setLister(ServiceLister lister) {
    this.lister = lister;
  }

  public void setCache(ServiceLister cache) {
    cachedLister = cache;
  }

  public void setIndexer(ServiceLister indexer) {
  }

  public void run() {
    boolean is_good = false;
    Map list = new HashMap();
    //list.put("UW Credit Union", "5321468");

    // Bootstrap the dependencies, injecting them into this
    Kiosk.setup(this);

    // Test simple method call, no args
    list = cachedLister.one();

    String value = (String) list.get("UW Credit Union");
    if(null != value && "" != value) {
      is_good = true;
    }
    System.out.println("Pass simple no arg method call:      [ " + is_good + " ] ");

    // Test method missing
    is_good = false;
    list = cachedLister.two(5, "key");

    int result = (Integer) list.get("key");
    if(5==result) {
      is_good = true;
    }
    System.out.println("Pass method call with arguments:      [ " + is_good + " ] ");

    // Test method missing with args
    is_good = false;
    list = cachedLister.three(3, "key");

    result = (Integer) list.get("key");
    if(3==result) {
      is_good = true;
    }
    System.out.println("Pass method missing with args:      [ " + is_good + " ] ");
  }
}
