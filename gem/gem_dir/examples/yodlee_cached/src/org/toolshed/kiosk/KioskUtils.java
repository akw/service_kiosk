package org.toolshed.kiosk;

import java.lang.reflect.*;

public class KioskUtils {
  public static Method findMethodByName(Object delegate, String name) {
    Method[] methods = delegate.getClass().getMethods();
    for(Method method : methods) {
      if(name.equals(method.getName())) {
        return method;
      }
    }
    return null;
  }

  public static Method findMethod(Object delegate, String name, Object[] args) {
    Method[] methods = delegate.getClass().getMethods();
    for(Method method : methods) {
      int parameter_count = method.getParameterTypes().length;
      if(name.equals(method.getName()) && ((null==args && 0==parameter_count) || (args.length==parameter_count))) {
        return method;
      }
    }
    return null;
  }

  public static Method findMethodWithParameterCount(Object delegate, String name, int count) {
    Method[] methods = delegate.getClass().getMethods();
    for(Method method : methods) {
      int parameter_count = method.getParameterTypes().length;
      if(name.equals(method.getName()) && count==parameter_count) {
        return method;
      }
    }
    return null;
  }

  public static String capitalize(String input) {
    return(input.substring(0, 1).toUpperCase() + input.substring(1));
  }
}
