package org.toolshed.kiosk;

import java.util.HashMap;
import java.util.Map;

class ServiceLister {
  public Map listAll() {
    Map list = new HashMap();
    list.put("UW Credit Union", "53934");
    return list;
  }
}
