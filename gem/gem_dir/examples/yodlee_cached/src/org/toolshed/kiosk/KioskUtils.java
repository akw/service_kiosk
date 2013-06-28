package org.toolshed.kiosk;

import java.lang.reflect.*;

public class KioskUtils {
  public static Method findMethod(Object delegate, String name, Object[] args) {
    Method[] methods = delegate.getClass().getMethods();
    for(Method method : methods) {
      System.out.println(method.getName() + " : " + method.toString());
      int parameter_count = method.getParameterTypes().length;
      if(name.equals(method.getName()) && ((null==args && 0==parameter_count) || (args.length==parameter_count))) {
        return method;
      }
    }
    return null;
  }
}
