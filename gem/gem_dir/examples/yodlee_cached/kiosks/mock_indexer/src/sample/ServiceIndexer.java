package sample;

import java.util.HashMap;
import java.util.Map;

public class ServiceIndexer {
  public Map indexAll() {
    Map map = new HashMap();
    Map record = new HashMap();
    record.put("id", "21257");
    record.put("container_type", "credits");
    map.put("Citi Bank (online.citibank.com", record);

    record = new HashMap();
    record.put("id", "12074");
    record.put("container_type", "credits");
    map.put("Citi Bank (India) - Credit Card", record);

    return map;
  }
}
