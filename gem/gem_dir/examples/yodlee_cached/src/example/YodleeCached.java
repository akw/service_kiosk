package example;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;
import org.toolshed.kiosk.Kiosk;
import org.json.simple.JSONValue;

public class YodleeCached {
  // bootstrap method
  public static void main(String args[]) {
    try {
      (new YodleeCached()).run();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public interface ServiceLister {
    public Map one();
    public Map two(int count, String name);
    public Map three(int count, String name);
    public String four(int count, String name);
  }

  public interface CachedLister {
    public Map listAll();
    public Integer getTimeout();
  }

  public interface Searcher {
    public Map search(String query);
  }

  private Searcher searcher;
  private ServiceLister lister;
  private ServiceLister indexer;
  private CachedLister cachedLister;

  public void setSearch(Searcher searcher) {
    this.searcher = searcher;
  }

  public void setLister(ServiceLister lister) {
    this.lister = lister;
  }

  public void setCache(CachedLister cache) {
    cachedLister = cache;
  }

  public void setIndexer(ServiceLister indexer) {
    this.indexer = indexer;
  }

  public void run() throws InterruptedException{
    boolean is_good = false;
    Map list = new HashMap();
    //list.put("UW Credit Union", "5321468");

    // Bootstrap the dependencies, injecting them into this
    Kiosk.setup(this);

    // Test search kiosk, no match
    Map resultMap = searcher.search("bob");
    if(null != resultMap && 0==resultMap.size()) {
      is_good = true;
    }
    System.out.println("Search Kiosk non-matching search:   [ " + is_good + " ] ");

    // Test search kiosk, with match
    is_good = false;
    resultMap = searcher.search("onli");
    if(null != resultMap && 1==resultMap.size()) {
      is_good = true;
    }
    System.out.println("Search Kiosk matching search:       [ " + is_good + " ] ");

    // Test cached call, no args
    is_good = false;
    list = cachedLister.listAll();
    //System.out.println("list: " + JSONValue.toJSONString(list));
    String value = (String) list.get("UW Credit Union");
    if(null != value && "" != value) {
      is_good = true;
    }
    System.out.println("Pass no arg cached call:            [ " + is_good + " ] ");

    // Test cached call twice, no args
    is_good = false;
    list = cachedLister.listAll();
    //System.out.println("list: " + JSONValue.toJSONString(list));
    value = (String) list.get("UW Credit Union");
    if(null != value && "" != value) {
      is_good = true;
    }
    System.out.println("Pass no arg cached call again:      [ " + is_good + " ] ");

    // Test cached call thrice, no args
    is_good = false;
    Thread.sleep(1000);
    list = cachedLister.listAll();
    //System.out.println("list: " + JSONValue.toJSONString(list));
    value = (String) list.get("UW Credit Union");
    if(null != value && "" != value) {
      is_good = true;
    }
    System.out.println("Pass no arg cached call 3rd time:   [ " + is_good + " ] ");

    // Test simple method call, no args
    is_good = false;
    list = lister.one();
    value = (String) list.get("UW Credit Union");
    if(null != value && "" != value) {
      is_good = true;
    }
    System.out.println("Pass simple no arg method call:     [ " + is_good + " ] ");

    // Test method missing
    is_good = false;
    list = indexer.two(5, "key");
    int result = ((Number) list.get("key")).intValue();
    if(5==result) {
      is_good = true;
    }
    System.out.println("Pass remote method call with args:  [ " + is_good + " ] ");

    // Test method missing
    is_good = false;
    list = lister.two(5, "key");
    result = ((Number) list.get("key")).intValue();
    if(5==result) {
      is_good = true;
    }
    System.out.println("Pass method call with arguments:    [ " + is_good + " ] ");

    // Test method missing with args
    is_good = false;
    list = lister.three(3, "key");
    result = ((Number) list.get("key")).intValue();
    if(3==result) {
      is_good = true;
    }
    System.out.println("Pass method missing with args:      [ " + is_good + " ] ");

    // Test retrieving setting
    is_good = false;
    Integer timeout = cachedLister.getTimeout();
    if(1==timeout.intValue()) {
      is_good = true;
    }
    System.out.println("Timeout setting set:                [ " + is_good + " ] ");
  }
}
