package test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;
import org.toolshed.kiosk.Kiosk;
import org.json.simple.JSONValue;

public class IndexerTest {
  // bootstrap method
  public static void main(String args[]) {
    try {
      (new IndexerTest()).run();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public interface Indexer {
    public Map indexAll();
  }
  private Indexer indexer;

  public void setIndexer(Indexer indexer) {
    this.indexer = indexer;
  }

  public void run() throws InterruptedException{
    boolean is_good = false;
    Map list = new HashMap();

    // Bootstrap the dependencies, injecting them into this
    Kiosk.setup(this);

    System.out.println("index dev");

    // Test search kiosk, no match
    Map resultMap = indexer.indexAll();
    if(null != resultMap && 0<resultMap.size()) {
      is_good = true;
    }
    System.out.println("indexer returns a non-empty map:   [ " + is_good + " ] ");
  }
}
