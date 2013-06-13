package org.toolshed.kiosk;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.bind.DatatypeConverter;
import org.json.simple.JSONValue;
import org.toolshed.kiosk.Kiosk;
import org.toolshed.kiosk.Poster;

public class KioskService {
  private String root;
  public KioskService(String endpoint, String serviceName) {
    root = endpoint + "/" + serviceName;
  }

  public Map call(String action, Map input) {
    String data = marshall(input);
    String response = Poster.executePost(url(action), data);
    Map return_map = unmarshall(response);
    return return_map;
  }

  public String url(String action) {
    return root + "/" + action;
  }

  public String marshall(Map input) {
    String json = JSONValue.toJSONString(input);
    String encoded = DatatypeConverter.printBase64Binary(json.getBytes());
    String marshalled = "data=" + encoded;
    return marshalled;
  }

  public Map unmarshall(String data) {
    String json = "";
    Map result_map;
    try {
      json = new String(DatatypeConverter.parseBase64Binary(data));
    System.out.println("json: " + json);
      result_map = (Map) JSONValue.parse(json);
    } catch(Exception e) {
      throw new RuntimeException("Kiosk return was not a JSON map.\n" + json);
    }

    return result_map;
  }
}
