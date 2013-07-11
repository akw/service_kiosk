package org.toolshed.kiosk;

import java.lang.Integer;
import java.lang.Long;
import java.lang.reflect.*;
import java.lang.reflect.InvocationTargetException;
import org.json.simple.JSONValue;

public class ResourceProxy implements InvocationHandler {
  private final Object delegate;

  public static Object create(Class targetClass, Object targetObject, ClassLoader loader) {
    return Proxy.newProxyInstance(loader, new Class[]{targetClass},
        new ResourceProxy(targetObject));
  }

  private ResourceProxy(Object delegate) {
    this.delegate = delegate;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    //System.out.println("ResourceProxy invoke: " + method.getName());
    return invokeMethod(delegate, method.getName(), method.getReturnType(), args);
  }

  public static Object invokeMethod(Object target, String methodName, Class returnType, Object[] args) throws Throwable {
    Method actual = KioskUtils.findMethod(target, methodName, args);
    //System.out.println("  >>> invokeMethod: " + methodName);
    if(null!=args) {
      //System.out.println("  >>> invokeMethod arg0 " + (String) args[0]);
      //System.out.println("  >>> invokeMethod arg1 " + (String) args[1]);
      //System.out.println("  >>> invokeMethod arg2 " + (String) args[2]);
    }
    if(methodName.equals("invoke_method")) {
      //System.out.println("invokeMethod is generic: args " + JSONValue.toJSONString(args));
      methodName = (String) args[0];
      returnType = (Class) args[1];
      if(2 < args.length) {
        args = (Object[]) args[2];
      } else {
        args = null;
      }
      //System.out.println("  finding invoke_method" );
      actual = KioskUtils.findMethod(target, methodName, args);
    } else {
      //System.out.println("invokeMethod is specific: args " + JSONValue.toJSONString(args));
    }
    Object result;
    if(actual==null) {
      actual = KioskUtils.findMethodByName(target, "method_missing");
      if(actual==null) {
        throw new RuntimeException("No such method: " + methodName);
      }
      try {
        //System.out.println("invoke method_missing: " + methodName);
        result = actual.invoke(target, combinedArgs(methodName, args));
      } catch(InvocationTargetException e) {
        //e.printStackTrace();
        throw new RuntimeException("No such method: " + methodName);
      }

    } else {
      //System.out.println("invoke on: " + actual);
      try {
        result = actual.invoke(target, typedArguments(actual, args));
      } catch(InvocationTargetException e) {
        //e.printStackTrace();
        throw new KioskException("Exception during method: " + methodName, e);
      }
    }
    //System.out.println("invoke called: " + result);
    return (null==returnType) ? result : returnType.cast(result);
  }

  public static Object[] typedArguments(Method target, Object[] data) {
    int i=0;
    for(Class argumentType : target.getParameterTypes()) {
      //System.out.println("typedArguments: " + argumentType.getName() + " ?= " + data[i].getClass().getName());
      Class argumentClass = data[i].getClass();
      if(int.class==argumentType && Integer.class==data[i].getClass()) {
        //System.out.println("  casting Integer to int");
        data[i] = ((Integer) data[i]).intValue();
      } else if(int.class==argumentType && Long.class==data[i].getClass()) {
        //System.out.println("  casting Long to int");
        data[i] = ((Long) data[i]).intValue();
      }
      i++;
    }
    return data;
  }

  public Object invoke(Object proxy, String methodName, Class returnType, Object[] args) throws Throwable {
    return invokeMethod(delegate, methodName, returnType, args);
  }

  private static Object[] combinedArgs(String name, Object[] args) {
    int count=0;
    if(null==args) {
      return new Object[]{name, null};
    }

    Object[] result = new Object[]{name, args};
    return result;
  }
}
