package sample;

import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONValue;

public class ContentServiceSearch {
  private ServiceIndexer indexer;
  private int timeout=0;
  private Map cacheData = new HashMap();

  public void setIndexer(ServiceIndexer indexer) {
    this.indexer = indexer;
  }

  public Map search(String query) {
    if(null==indexer) {
      throw new RuntimeException("No indexer set for ContentServiceSearch");
    }
    Map data = indexer.indexAll();
    Map results = new HashMap();
    for(Object name : data.keySet()) {
      if(matchesQuery(query, (String) name)) {
        results.put(name, data.get(name));
      }
    }
    return results;
  }

  public boolean matchesQuery(String query, String source) {
    return -1!=source.indexOf(query);
  }
}
