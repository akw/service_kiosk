package org.toolshed.kiosk;

import java.lang.reflect.*;
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
    Method actual = KioskUtils.findMethod(delegate, method.getName(), args);
    Object result;
    if(actual==null) {
      actual = KioskUtils.findMethodByName(delegate, "method_missing");
      if(actual==null) {
        throw new RuntimeException("No such method: " + method.getName());
      }
      result = actual.invoke(delegate, combinedArgs(method.getName(), args));

    } else {
      result = actual.invoke(delegate, args);
    }
    return method.getReturnType().cast(result);
  }

  private Object[] combinedArgs(String name, Object[] args) {
    int count=0;
    if(null==args) {
      return new Object[]{name, null};
    }

    Object[] result = new Object[]{name, args};
    return result;
  }
}
