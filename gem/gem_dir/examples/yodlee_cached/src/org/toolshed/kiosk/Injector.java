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

  protected Map resourceMap() {
    return resources;
  }

  public void createAllResources(Object node, Map config, Map env, String prefix) {
    Object resource = null;

    for(Object dependency : KioskConfig.getDependencies(config)) {
      String dependencyName = (String) dependency;
      if(null==KioskConfig.dependencyUrl(dependencyName, env, prefix)) {
        throw new KioskException("No env var (" + KioskConfig.baseName(dependencyName, prefix) + ") for " + dependencyName);
      }
      resource = createResource(dependency, config, env, prefix);
      resources.put(dependency, resource);
    }
  }

  public void injectAll(Object node, Map config, Map env, String prefix) {
    Object resource = null;

    for(Object dependency : KioskConfig.getDependencies(config)) {
      String dependencyName = (String) dependency;
      String label = KioskConfig.resourceLabel(dependencyName, prefix);

      if(null==KioskConfig.dependencyUrl(dependencyName, env, prefix)) {
        throw new KioskException("No env var (" + KioskConfig.baseName(dependencyName, prefix) + ")");
      }
      String url = KioskConfig.dependencyUrl(dependencyName, env, prefix);
      String dependencyReference = KioskConfig.dependencyTarget(url);
      if(isInternalResource(url)) {
        if(null==dependencyReference || !resources.containsKey(dependencyReference)) {
          throw new KioskException("Invalid Kiosk URL: " + url);
        }
        resource = resources.get(dependencyReference);
        if(null==prefix || 0==prefix.length()) {
          Map kioskConfig = KioskConfig.readConfig(dependencyName, config);
          injectAll(resource, kioskConfig, env, dependencyName);
          if(prefix.equals("")) {
            injectAllSettings(resource, kioskConfig, env, dependencyName);
          }
        }

      } else {
        resource = createResource(dependency, config, env, prefix);
        resources.put(dependency, resource);
      }
      String injectorName = injectorName(dependency, config);
      inject(node, resource, injectorName, prefix, url);
    }
  }

  public Object createResource(Object nameObject, Map config, Map env, String prefix) {
    String dependencyName = (String) nameObject;
    String dependencyUrl = KioskConfig.dependencyUrl(dependencyName, env, prefix);
    Object resource = null;

    if(isInternalResource(dependencyUrl)) {

      if(resources.containsKey(resourceKey(dependencyUrl))) {
        resource = resources.get(resourceKey(dependencyUrl));

      } else {
        URL[] jars = KioskConfig.findJars(dependencyName, config);

        Map kioskConfig = KioskConfig.readConfig(dependencyName, config);
        String resourceClass = KioskConfig.findResourceClass(dependencyName, config, kioskConfig);
        resource = internalResource(dependencyName, resourceClass, jars);
      }

    } else {
      resource = new RemoteResource( dependencyUrl );
      //  create remote resource
    }

    if(null==resource) {
      throw new KioskException("Couldn't create resource '" + dependencyName + "'");
    }
    return resource;
  }

  public void injectAllSettings(Object resource, Map kioskConfig, Map env, String prefix) {
    for(Object settingKey : KioskConfig.getSettings(kioskConfig)) {
      injectSetting(resource, settingKey, kioskConfig, env, prefix);
    }
  }

  public void injectSetting(Object resource, Object settingKey, Map kioskConfig, Map env, String prefix) {
    String settingName = (String) settingKey;
    String injectorName = injectorName(settingName, kioskConfig);
    String host = (prefix.equals("")) ? "root" : prefix;
    try {
      String value = KioskConfig.setting(settingName, env, prefix);
      Method injectorMethod = KioskUtils.findMethodWithParameterCount(resource, injectorName, 1);
      injectorMethod.invoke(resource, value);
      Log.info("    " + host +" . " + injectorName(settingKey, kioskConfig) + " <- " + value);
    } catch(Exception e) {
      // e.printStackTrace();
      throw new KioskException("No injector found: " + injectorName);
    }
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

  public void inject(Object target, Object resource, String injector, String prefix, String url) {
    String host = (prefix.equals("")) ? "root" : prefix;
    try {
      Method injectorMethod = KioskUtils.findMethodWithParameterCount(target, injector, 1);
      if(null==injectorMethod) {
        Log.info("  " + host + " . " + injector + " -- method not found.");
        return;
      }
      Class[] types = injectorMethod.getParameterTypes();
      Class dependency = types[0];
      Object proxiedKiosk = ResourceProxy.create(dependency, resource, dependency.getClassLoader());
      injectorMethod.invoke(target, proxiedKiosk);
      Log.info("  " + host + " . " + injector + " <- " + url);
    } catch(Exception e) {
      Log.info("  " + host + " . " + injector + " -- method not found.");
      e.printStackTrace();
    }
  }

  public Boolean isInternalResource(String url) {
    return url.startsWith("kiosk://");
  }
}
