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
    String data = KioskUtils.marshallArguments( arguments );
    String response = Poster.executePost(url(action), data);
    System.out.println("  raw result: " + response);
    Object result = KioskUtils.unmarshallReturn(response);
    System.out.println("  result: " + JSONValue.toJSONString(result));
    return result;
  }

  public String url(String action) {
    return root + "/" + action;
  }
}
