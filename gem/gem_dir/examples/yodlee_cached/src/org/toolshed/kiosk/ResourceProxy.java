package org.toolshed.kiosk;

import java.lang.reflect.*;
import java.lang.reflect.InvocationTargetException;
import org.json.simple.JSONValue;

public class ResourceProxy implements InvocationHandler {
  private final Object delegate;

  public static Object create(Class targetClass, Object targetObject) {
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    return Proxy.newProxyInstance(cl, new Class[]{targetClass},
        new ResourceProxy(targetObject));
  }

  private ResourceProxy(Object delegate) {
    this.delegate = delegate;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    //System.out.println("ResourceProxy invoke: " + method.getName());
    return invoke(proxy, method.getName(), method.getReturnType(), args);
  }

  public static Object invokeMethod(Object target, String methodName, Class returnType, Object[] args) throws Throwable {
    Method actual = KioskUtils.findMethod(target, methodName, args);
    if(methodName.equals("invoke_method")) {
      methodName = (String) args[0];
      returnType = (Class) args[1];
      args = (Object[]) args[2];
      actual = KioskUtils.findMethod(target, methodName, args);
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
        e.printStackTrace();
        throw new RuntimeException("No such method: " + methodName);
      }

    } else {
      //System.out.println("invoke on: " + actual);
      result = actual.invoke(target, args);
    }
    //System.out.println("invoke called: " + result);
    return (null==returnType) ? result : returnType.cast(result);
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
