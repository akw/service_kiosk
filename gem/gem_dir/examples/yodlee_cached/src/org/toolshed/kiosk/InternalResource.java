package org.toolshed.kiosk;

import java.lang.reflect.*;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class InternalResource {
  private Object resource;

  public static Object create(String dependency, Map config) {
    return null;
  }

  public InternalResource(String kiosk, String resource) {
    //this.resource = connect(kiosk, resource);
  }

  private Object connect(String kiosk, String resource) {
    Class kiosk_class;
    Object result;
    try {
      kiosk_class = Class.forName(class_name_from_locator(kiosk, resource));
      result = kiosk_class.newInstance();
    } catch( Exception e ) {
      throw new RuntimeException(e.getMessage());
    }
    return result;
  }

  private String class_name_from_locator(String kiosk_locator, String resource_name) {
    Pattern pattern = Pattern.compile("java:\\/\\/\\/([\\w\\.]*)");
    Matcher matcher = pattern.matcher(kiosk_locator);
    if(matcher.find()) {
      String kiosk_name = matcher.group(1);
      String name = kiosk_name.toLowerCase() + "." + resource_name;
      return name;
    }
    return resource_name;
  }
}

