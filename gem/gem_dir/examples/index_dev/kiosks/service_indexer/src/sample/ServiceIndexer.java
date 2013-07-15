package sample;

import java.util.HashMap;
import java.util.Map;

public class ServiceIndexer {
  // This interface tells the infrastructure what calls you want to make against the
  // dependency 'lister'.  It does not have to match any actual interfaces provided by the 
  // Classes defined in the dependent kiosk.  Only the method signatures (name and arg count) 
  // have to match.
  public interface ServiceLister {
    public String listAll();
  }

  private ServiceLister lister;

  // This 'injector' is used by the infrastructure to inject the dependent object so that you 
  // can use it.
  public void setLister(ServiceLister lister) {
    this.lister = lister;
  }

  /*
   * This method needs to:
   * read xml from the provided ServiceLister
   * parse data out of the xml 
   * produce a map of content_service_display_name -> {content_service_id=x, container_type=y, mfa_type=z}
   *   that can be converted into JSON
   */
  public Map indexAll() {
    String xml = lister.listAll();
    System.out.println("Mock lister provided data: " + (null!=xml));
    Map results = parseList(xml);
    return results;
  }

  public Map parseList(String xml) {
    Map results = new HashMap();
    results.put("UW Credit Union", record("123456", "bank", "SECURITY_QUESTION"));
    return results;
  }

  public Map record(String id, String containerType, String mfaType) {
    Map record = new HashMap();
    record.put("id", id);
    record.put("container_type", containerType);
    record.put("mfa_type", mfaType);

    return record;
  }
}
