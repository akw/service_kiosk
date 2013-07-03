package org.toolshed.kiosk;

import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.net.URLClassLoader;
import java.net.URL;
import java.lang.reflect.*;
import org.json.simple.JSONValue;

public class Injector {
  private static Injector injector = new Injector();

  public static Injector instance() {
    return injector;
  }

  private Map resources = new HashMap();

  public void injectAll(Object node, Map config, Map env, String prefix) {
    Object resource = null;

    for(Object dependency : KioskConfig.getDependencies(config)) {
      String dependencyName = (String) dependency;
      if(!KioskConfig.isWired(dependencyName, config, env, prefix)) {
        throw new KioskException("No dependency defined for " + dependencyName);
      }
      if(resources.containsKey(dependency)) {
        resource = resources.get(dependency);
        injectAll(resource, KioskConfig.readConfig(dependencyName, config), env, dependencyName);

      } else {
        resource = createResource(dependency, config, env, prefix);
        resources.put(dependency, resource);
      }
      inject(node, resource, injectorName(dependency, config));
    }
  }

  public Object createResource(Object nameObject, Map config, Map env, String prefix) {
    String dependencyName = (String) nameObject;
    String dependencyUrl = KioskConfig.dependencyUrl(dependencyName, config, env, prefix);
    Object resource = null;

    if(isInternalResource(dependencyUrl)) {
      System.out.println("[Kiosk] injecting: " + dependencyUrl);

      if(resources.containsKey(resourceKey(dependencyUrl))) {
        resource = resources.get(resourceKey(dependencyUrl));

      } else {
        URL[] jars = KioskConfig.findJars(dependencyName, config);

        Map kioskConfig = KioskConfig.readConfig(dependencyName, config);
        String resourceClass = KioskConfig.findResourceClass(dependencyName, config, kioskConfig);
        resource = internalResource(dependencyName, resourceClass, jars);
      }

    } else {
      System.out.println("[Kiosk] injecting remote: " + dependencyUrl);
      //  create remote resource
    }

    if(null==resource) {
      throw new KioskException("Couldn't create resource '" + dependencyName + "'");
    }
    return resource;
  }

  public String resourceKey(String url) {
    try {
      String[] parts = url.split("/");
      return parts[parts.length-1];
    } catch( Exception e ) {
      throw new KioskException("Invalid resource url: " + url);
    }
  }

  public Object internalResource(String resourceName, String className, URL[] jars) {
    Class resourceClass;
    Object result;
    try {
      URLClassLoader child = new URLClassLoader( jars, this.getClass().getClassLoader() );
      resourceClass = Class.forName(className, true, child);
      result = resourceClass.newInstance();
    } catch( Exception e ) {
      throw new KioskException("Couldn't create " + resourceName + ": " + e.getMessage());
    }
    return result;
  }

  public String injectorName(Object key, Map config) {
    // TBD: handle custom injectors
    String injector = "set" + KioskUtils.capitalize((String)key);
    return injector;
  }

  public void inject(Object target, Object resource, String injector) {
    try {
      Method injectorMethod = KioskUtils.findMethodWithParameterCount(target, injector, 1);
      Class dependency = injectorMethod.getParameterTypes()[0];
      Object proxiedKiosk = ResourceProxy.create(dependency, resource);
      injectorMethod.invoke(target, proxiedKiosk);
    } catch(Exception e) {
      System.out.println("[Kiosk] WARNING: Unused dependency.  No injector '" + injector + "'");
      e.printStackTrace();
    }
  }

  public Boolean isInternalResource(String url) {
    return url.startsWith("kiosk://");
  }
}
