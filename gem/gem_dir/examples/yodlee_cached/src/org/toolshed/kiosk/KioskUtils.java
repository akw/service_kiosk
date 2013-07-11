package org.toolshed.kiosk;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.DatatypeConverter;
import org.json.simple.JSONValue;

public class KioskUtils {
  public static Object[] concatParams(String method, Object[] arguments) {
    if(null==arguments) {
      //System.out.println("concatParams null args");
      return new Object[]{method};
    }
    //System.out.println("concatParams args: " + JSONValue.toJSONString(arguments));
    //System.out.println("concatParams #args: " + arguments.length);
    Object[] parameters = new Object[arguments.length+1];
    parameters[0] = method;
    for(int i=0; i<arguments.length; i++) {
      parameters[i+1] = arguments[i];
    }
    return parameters;
  }

  public static String marshallArguments(Object[] arguments) {
    if(null==arguments) {
      arguments = new Object[0];
    }
    return "data=" + marshall(Arrays.asList(arguments));
  }

  public static String marshallReturn(Object result) {
    List resultList = new ArrayList(1);
    resultList.add(result);
    return marshall(resultList);
  }

  public static String marshall(Object data) {
    String json = JSONValue.toJSONString(data);
    //System.out.println("marshall json: " + json);
    String encoded = DatatypeConverter.printBase64Binary(json.getBytes());
    return encoded;
  }

  public static Object unmarshallReturn(String data) {
    List resultList = new ArrayList();
    Object result = null;
    try {
      //System.out.println("unmarshall data: " + data);
      resultList = (List) unmarshall(data);
    } catch(Exception e) {
      throw new KioskException("Invalid arguments.  Invalid JSON.  " + e.getMessage());
    }
    if(null==resultList.get(0) && 2==resultList.size()) {
      result = resultList.get(1);
    } else if(resultList.size() > 0) {
      result = resultList.get(0);
    }
    return result;
  }

  public static Object[] unmarshallArguments(String data) {
    List resultList = new ArrayList();
    Object result = null;
    try {
      resultList = (List) unmarshall(data);
    } catch(Exception e) {
      throw new KioskException("Invalid arguments.  Invalid JSON.  " + e.getMessage());
    }
    if(null==resultList || 0==resultList.size()) {
      return null;
    }
    return resultList.toArray();
  }

  public static Object unmarshall(String data) {
    if(null==data) {
      data = "[]";
    }
    String json = "";
    Object result;
    try {
      json = new String(DatatypeConverter.parseBase64Binary(data));
      //System.out.println("unmarshall json: " + json);
      result = JSONValue.parse(json);
    } catch(Exception e) {
      throw new RuntimeException("Kiosk return was not a JSON array.\n" + json);
    }

    return result;
  }

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
      //System.out.println("  findMethod: " + name + " ?= " + method.getName());
      int parameter_count = method.getParameterTypes().length;
      //System.out.println("    parameter count: " + parameter_count);
      //System.out.println("    args: " + ((null==args) ? 0 : args[0]));
      if(name.equals(method.getName()) && ((null==args && 0==parameter_count) || (null!= args && args.length==parameter_count))) {
        //System.out.println("  >>> match");
        return method;
      }
    }
    return null;
  }

  public static Method findMethodWithParameterCount(Object delegate, String name, int count) {
    Method[] methods = delegate.getClass().getMethods();
    for(Method method : methods) {
      int parameter_count = method.getParameterTypes().length;
      //System.out.println("  findMethod: " + name + " ?= " + method.getName() + ", " + count + " ?= " + parameter_count);
      if(name.equals(method.getName()) && count==parameter_count) {
        //System.out.println("  >>> match");
        return method;
      }
    }
    return null;
  }

  public static String capitalize(String input) {
    return(input.substring(0, 1).toUpperCase() + input.substring(1));
  }
}
