package org.toolshed.kiosk;

public class KioskException extends RuntimeException {
  public KioskException(String message) {
    super(message);
  }

  public KioskException(String message, Throwable cause) {
    super(message, cause);
  }
}
