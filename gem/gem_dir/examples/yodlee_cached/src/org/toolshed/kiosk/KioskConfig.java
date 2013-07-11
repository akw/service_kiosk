package org.toolshed.kiosk;

import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.net.URLClassLoader;
import java.net.URL;
import java.io.File;
import org.json.simple.JSONValue;


public class KioskConfig {
  static private HashMap kiosks = new HashMap();

  String endpoint;

  public static Map readConfig() {
    return readConfigFromFile("kioskfile.json");
  }

  public static Map readConfig(String dependency, Map config) {
    return readConfigFromFile(kioskfile(dependency, config));
  }

  public static String kioskfile(String dependency, Map config) {
    return( "./kiosks/" + 
            kioskName(dependencyConfig(dependency, config)) +
            "/kioskfile.json" );
  }

  private static Map readConfigFromFile(String name) {
    String content = "{}";
    try {
      content = new Scanner(new File(name)).useDelimiter("\\Z").next();
    } catch( java.io.FileNotFoundException e ) {
      throw new KioskException("Couldn't read config file: " + name + "\n" + e.getMessage());
    }
    Map result_map = (Map) JSONValue.parse(content);
    return result_map;
  }

  public static URL[] findJars(String dependency, Map config) {
    List<URL> result = jars( kioskLibrary( dependency, config ));
    return (URL[]) result.toArray(new URL[result.size()]);
  }

  public static List<URL> jars( String path ) {
    ArrayList<URL> result = new ArrayList();
    try {
      File root = new File( path );
      File[] list = root.listFiles();

      for ( File f : list ) {
        if ( f.isDirectory() ) {
          result.addAll( jars( f.getAbsolutePath() ));

        } else if(f.getName().endsWith(".jar")) {
          result.add(f.toURL());
        }
      }
    } catch( Exception e ) {
      throw new KioskException("Trouble loading jars from '" + path + "'");
    }
    return result;
  }

  public static String kioskLibrary(String dependency, Map config) {
    return( "./kiosks/" + kioskName(dependencyConfig(dependency, config)) + "/lib/" );
  }

  public static Map dependencies(Map config) {
    return (Map) config.get("dependencies");
  }

  public static Set getDependencies(Map config) {
    return !config.containsKey("dependencies") ?
            new HashSet() :
            ((Map) config.get("dependencies")).keySet();
  }

  public static Set getSettings(Map config) {
    return !config.containsKey("settings") ?
            new HashSet() :
            ((Map) config.get("settings")).keySet();
  }

  public static String kioskName(Map config) {
    // TBD: allow name to be specified instead of an array
    return (String) ((List) config.get("kiosk")).get(0);
  }

  public static Map dependencyConfig(String dependency, Map config) {
    return (Map) ((Map) config.get("dependencies")).get(dependency);
  }

  public static String findResourceClass(String dependencyName, Map config, Map kioskConfig) {
    return namespace(kioskConfig) + "." + resourceName(dependencyName, config);
  }

  public static String namespace(Map config) {
    try {
      return (String) config.get("namespace");
    } catch(Exception e) {
      throw new KioskException("Couldn't find namespace for: " + config.get("name"));
    }
  }

  public static String resourceName(String key, Map config) {
    try {
      return (String) dependencyConfig(key, config).get("resource");
    } catch(Exception e) {
      throw new KioskException("Couldn't find resource name for dependency: " + key);
    }
  }

  public static Map env() {
    Map result = new HashMap();
    Map<String,String> env = System.getenv();
    for(String key : env.keySet() ) {
      if(key.startsWith("KIOSK_")) {
        result.put(key, env.get(key));
      }
    }
    return result;
  }

  public static String resourceLabel(String name, String prefix) {
    if(null==prefix || 0==prefix.length()) {
      return name;
    }
    return prefix + "__" + name;
  }

  public static String baseName(String name, String prefix) {
    return ("KIOSK_" + resourceLabel(name, prefix)).toUpperCase();
  }

  public static String dependencyUrl(String name, Map env, String prefix) {
    return !env.containsKey(baseName(name, prefix)) ?
            null : 
            (String) env.get(baseName(name, prefix));
  }

  public static String dependencyTarget(String url) {
    String[] parts = url.split("://");

    if(null!=parts && 2==parts.length && parts[0].equals("kiosk")) {
      return parts[1];
    }
    return null;
  }

  public static String setting(String name, Map env, String prefix) {
    return !env.containsKey(baseName(name, prefix)) ?
            null : 
            (String) env.get(baseName(name, prefix));
  }
}
