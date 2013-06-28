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
    public Map listAll();
  }

  private ServiceLister lister;

  public void setLister(ServiceLister lister) {
    this.lister = lister;
  }

  public void run() {
    boolean is_good = false;
    Map list = new HashMap();
    //list.put("UW Credit Union", "5321468");

    Kiosk.setup(this);
    list = lister.listAll();

    String value = (String) list.get("UW Credit Union");
    if(null != value && "" != value) {
      is_good = true;
    }
    System.out.println("pull value yodlee cached:      [ " + is_good + " ] ");
  }
}
