package org.toolshed.kiosk;

public class Log {
  public static int FINEST=0;
  public static int FINER=1;
  public static int FINE=2;
  public static int DEBUG=3;
  public static int INFO=4;
  public static int WARN=5;
  public static int ERROR=6;
  public static int FATAL=7;
  public static int visible = DEBUG;

  public static void log(int level, String message) {
    if(level >= visible) {
      System.out.println("[Kiosk] " + label(level) + message);
    }
  }

  private static String label(int level) {
    if(level==WARN) {return "WARNING: ";}
    if(level==ERROR) {return "ERROR: ";}
    if(level==FATAL) {return "FATAL ERROR: ";}
    return "";
  }

  public static void debug(String message) { log(DEBUG, message); }
  public static void info(String message) { log(INFO, message); }
  public static void warn(String message) { log(WARN, message); }
  public static void error(String message) { log(ERROR, message); }
  public static void fatal(String message) { log(FATAL, message); }
}
