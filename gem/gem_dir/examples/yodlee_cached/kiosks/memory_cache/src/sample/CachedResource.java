package sample;

import java.util.HashMap;
import java.util.Map;

import org.toolshed.kiosk.GenericResource;
import org.toolshed.kiosk.KioskException;
import org.toolshed.kiosk.KioskUtils;

public class CachedResource {
  private GenericResource target;
  private int timeout=0;
  private Map cacheData = new HashMap();

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
      Object result = cachedVersion(method, arguments);
      if(null==result) {
        System.out.println("  (result not cached)");
        result = target.invoke_method(method, new Object().getClass(), arguments);
        cache(method, arguments, result);
      } else {
        System.out.println("  (result cached)");
      }
      return result;
    } catch( Throwable e ) {
      throw new KioskException("No such method in target resource: " + method);
    }
  }

  public Object cachedVersion(String method, Object[] arguments) {
    Object[] data = (Object[]) cacheData.get(signature(method, arguments));
    if(null==data) {
      return null;
    } else if(isStale((Long)data[0])) {
      cacheData.remove(signature(method, arguments));
      return null;
    }
    return data[1];
  }

  public boolean isStale(Long expiration_timestamp) {
    return expiration_timestamp <= now();
  }

  public void cache(String method, Object[] arguments, Object result) {
    cacheData.put(signature(method, arguments), new Object[]{timeout + now(), result});
  }

  public long now() {
    return System.currentTimeMillis()/1000;
  }

  public String signature(String method, Object[] arguments) {
    String result = method + KioskUtils.marshallArguments(arguments);
    return result;
  }
}
