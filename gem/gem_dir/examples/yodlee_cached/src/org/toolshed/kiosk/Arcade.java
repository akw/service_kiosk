package org.toolshed.kiosk;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONValue;

import static spark.Spark.*;
import spark.*;

public class Arcade {
  public static int port() {
    String value = System.getenv("KIOSK_PORT");
    int result=9000;
    try {
      if(null!=value) {
        result = Integer.parseInt(value);
      }
    } catch(Exception e) {
    }
    return result;
  }

  public static interface Root {
    public Object kiosk(String name);
  }

  private static Root root;
  private Map resources;

  public void run() {
    setPort(port());
    Kiosk.setup(this);
    resources = Injector.instance().resourceMap();
    post(new Route("/:resource/:action") {
      @Override
      public Object handle(Request request, Response response) {
        Object resource = resources.get(request.params(":resource"));
        List resultList = new ArrayList(1);
        Object result;
        if(null==resource) {
          return "[{\"_ERROR\": \"No such resource: " + request.params(":resource") + "\"}]";
        }
        String methodName = action(request.url());
        Object[] args = parseArgs(request.body());
        try {
          result = ResourceProxy.invokeMethod(resource, methodName, null, args);
        } catch(Throwable e) {
          Map errors = new HashMap();
          errors.put("_ERROR", e.getMessage());
          result = errors;
          e.printStackTrace();
        }
        return KioskUtils.marshallReturn(result);
      }
    });
  }

  public static String resource(String url) {
    String[] parts = url.split("/");
    return parts[parts.length-2];
  }

  public static String action(String url) {
    String[] parts = url.split("/");
    return parts[parts.length-1];
  }

  public static Object[] parseArgs(String body) {
    String[] parts = body.split("=");
    if(null==parts || 2!=parts.length || !parts[0].equals("data")) {
      return null;
    }
    return KioskUtils.unmarshallArguments(parts[1]);
  }

  public static void main(String[] args) {
    new Arcade().run();
  }
}
