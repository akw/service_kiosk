package org.toolshed.kiosk;

import java.lang.reflect.*;

public class KioskProxy implements InvocationHandler {
  private final Object delegate;

  public static Object create(Class targetClass, Object targetObject) {
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    return Proxy.newProxyInstance(cl, new Class[]{targetClass},
        new KioskProxy(targetObject));
  }

  private KioskProxy(Object delegate) {
    this.delegate = delegate;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Method actual = findMethod(delegate, method.getName(), args);
    if(actual==null) {
      throw new RuntimeException("No such method: " + method.getName());
    }

    Object result = actual.invoke(delegate, args);
    return method.getReturnType().cast(result);
  }

  private Method findMethod(Object delegate, String name, Object[] args) {
    Method[] methods = delegate.getClass().getMethods();
    for(Method method : methods) {
      System.out.println(method.getName() + " : " + method.toString());
      if(name.equals(method.getName()) && args.length==method.getParameterTypes().length) {
        return method;
      }
    }
    return null;
  }
}
