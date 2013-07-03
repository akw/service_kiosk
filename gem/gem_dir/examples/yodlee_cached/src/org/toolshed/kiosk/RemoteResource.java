package org.toolshed.kiosk;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import javax.xml.bind.DatatypeConverter;
import org.json.simple.JSONValue;
import org.toolshed.kiosk.Kiosk;
import org.toolshed.kiosk.Poster;

public class RemoteResource {
  private String root;
  public RemoteResource(String root) {
    this.root = root;
  }

  public Object method_missing(String action, Object... arguments) {
    String data = marshall( arguments );
    //String response = Poster.executePost(url(action), data);
    String response = "[\"hello world\"]";
    response = DatatypeConverter.printBase64Binary(response.getBytes());
    System.out.println("remote method missing:");
    System.out.println("  url: " + url(action));
    System.out.println("  params: " + data);
    Object result = unmarshall(response);
    System.out.println("  result: " + (String) result);
    return result;
  }

  public String url(String action) {
    return root + "/" + action;
  }

  public String marshall(Object[] arguments) {
    Object[] parameters = new Object[0];
    if(null!=arguments) {
      parameters = arguments;
    }
    String json = JSONValue.toJSONString(parameters);
    String encoded = DatatypeConverter.printBase64Binary(json.getBytes());
    String marshalled = "data=" + encoded;
    System.out.println("marshalled: " + marshalled);
    return marshalled;
  }

  public Object unmarshall(String data) {
    String json = "";
    List result;
    try {
      json = new String(DatatypeConverter.parseBase64Binary(data));
      System.out.println("unmarshall json: " + json);
      result = (List) JSONValue.parse(json);
    } catch(Exception e) {
      throw new RuntimeException("Kiosk return was not a JSON array.\n" + json);
    }

    return (0<result.size()) ? result.get(0) : null;
  }
}
