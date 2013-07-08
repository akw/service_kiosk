package org.toolshed.kiosk;

import java.lang.reflect.*;
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

  public static String marshall(Object[] arguments) {
    Object[] parameters = new Object[0];
    if(null!=arguments) {
      parameters = arguments;
    }
    String json = JSONValue.toJSONString(parameters);
    String encoded = DatatypeConverter.printBase64Binary(json.getBytes());
    String marshalled = "data=" + encoded;
    //System.out.println("marshalled: " + marshalled);
    return marshalled;
  }

  public static Object unmarshall(String data) {
    String json = "";
    List result;
    try {
      json = new String(DatatypeConverter.parseBase64Binary(data));
      //System.out.println("unmarshall json: " + json);
      result = (List) JSONValue.parse(json);
    } catch(Exception e) {
      throw new RuntimeException("Kiosk return was not a JSON array.\n" + json);
    }

    return (0<result.size()) ? result.get(0) : null;
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
      //System.out.println("  findMethod: " + method);
      int parameter_count = method.getParameterTypes().length;
      //System.out.println("    parameter count: " + parameter_count);
      //System.out.println("    args: " + ((null==args) ? 0 : args[0]));
      if(name.equals(method.getName()) && ((null==args && 0==parameter_count) || (args.length==parameter_count))) {
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
