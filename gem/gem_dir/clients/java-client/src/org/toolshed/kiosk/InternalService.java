package org.toolshed.kiosk;

import java.lang.reflect.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class InternalService {
  private Object service;

  public InternalService(String kiosk, String service) {
    //service = connect(kiosk, service);
  }

  private Object connect(String kiosk, String service) {
    Class kiosk_class;
    Object result;
    try {
      kiosk_class = Class.forName(class_name_from_locator(kiosk, service));
      result = kiosk_class.newInstance();
    } catch( Exception e ) {
      throw new RuntimeException(e.getMessage());
    }
    return result;
  }

  private String class_name_from_locator(String kiosk_locator, String service_name) {
    Pattern pattern = Pattern.compile("java:\\/\\/\\/([\\w\\.]*)");
    Matcher matcher = pattern.matcher(kiosk_locator);
    if(matcher.find()) {
      String kiosk_name = matcher.group(1);
      String name = kiosk_name.toLowerCase() + "." + service_name;
      return name;
    }
    return service_name;
  }
}

