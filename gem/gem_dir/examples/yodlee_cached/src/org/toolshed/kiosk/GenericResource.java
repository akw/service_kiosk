package org.toolshed.kiosk;

public interface GenericResource {
  public Object invoke_method(String methodName, Class returnType, Object[] args) throws Throwable;
}
