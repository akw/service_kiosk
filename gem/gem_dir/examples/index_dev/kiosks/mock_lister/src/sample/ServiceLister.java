package sample;

import java.util.HashMap;
import java.util.Map;

public class ServiceLister {
  public String listAll() {
    String result = 
      "<entry>" +
      "  <string>11819</string>" +
      "  <linked-hash-map>" +
      "    <entry>" +
      "      <string>Content Service Id</string>" +
      "      <long>11819</long>" +
      "    </entry>" +
      "    <entry>" +
      "      <string>Display name</string>" +
      "      <string>UW CU - Bank</string>" +
      "    </entry>" +
      "    <entry>" +
      "      <string>Container type</string>" +
      "      <string>bank</string>" +
      "    </entry>" +
      "    <entry>" +
      "      <string>Registration URL</string>" +
      "      <string>https://secure.uwcu.org/NewServices/?</string>" +
      "    </entry>" +
      "    <entry>" +
      "      <string>MFA Type</string>" +
      "      <string>MULTI_LEVEL</string>" +
      "    </entry>" +
      "  </linked-hash-map>" +
      "</entry>";

    return result;
  }
}
