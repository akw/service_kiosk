package sample;

import java.util.HashMap;
import java.util.Map;

import org.toolshed.kiosk.GenericResource;
import org.toolshed.kiosk.KioskException;
import org.toolshed.kiosk.KioskUtils;

public class CachedResource {
  private GenericResource target;
  private int timeout=0;

  public void setTarget(GenericResource target) {
    this.target = target;
  }

  public void setTimeout(String value) {
    timeout = Integer.parseInt(value);
  }

  public Integer getTimeout() {
    return timeout;
  }

  public Object method_missing(String method, Object... arguments) {
    try {
      return target.invoke_method(method, new Object().getClass(), arguments);
    } catch( Throwable e ) {
      throw new KioskException("No such method in target resource: " + method);
    }
  }
}
