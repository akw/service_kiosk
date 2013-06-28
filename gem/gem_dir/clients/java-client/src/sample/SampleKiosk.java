package sample;

import org.toolshed.kiosk.Hellos;

public class SampleKiosk implements Hellos {
  public String greeting(String target) {
    return "Hello, " + target;
  }
}
