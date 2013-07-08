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
    String data = KioskUtils.marshall( arguments );
    String response = Poster.executePost(url(action), data);
    /*
    String response = "[\"hello world\"]";
    response = DatatypeConverter.printBase64Binary(response.getBytes());
    System.out.println("remote method missing:");
    System.out.println("  url: " + url(action));
    System.out.println("  params: " + data);
    */
    Object result = KioskUtils.unmarshall(response);
    System.out.println("  result: " + (String) result);
    return result;
  }

  public String url(String action) {
    return root + "/" + action;
  }
}
