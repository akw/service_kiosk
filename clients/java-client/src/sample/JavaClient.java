package sample;

import java.util.LinkedHashMap;
import java.util.Map;
import org.toolshed.kiosk.Kiosk;
import org.toolshed.kiosk.KioskService;

public class JavaClient {
  public static void main(String args[]) {
    System.out.println("Starting up...");
    //Kiosk kiosk = new Kiosk("http://localhost:9902/com.countabout.yodlee.kiosk");
    Kiosk kiosk = Kiosk.open("test");
    KioskService service = kiosk.service("YodleeUserKiosk");
    LinkedHashMap map = new LinkedHashMap();
    map.put("id", "4");
    map.put("q", "Herman");
    map.put("box", "Mister");
    map.put("box?", "yes");
    map.put("top", 12);
    map.put("name", "Monty");
    Map results = service.call("lism", map);
    System.out.println("result: " + results.get("greeting"));
  }
}
