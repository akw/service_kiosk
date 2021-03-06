package org.toolshed.kiosk;

import java.util.LinkedHashMap;
import java.util.Map;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;

public class Poster {
  public static String executePost(String targetURL, String body) {
    URL url;
    HttpURLConnection connection = null;
    try {
      //Create connection
      url = new URL(targetURL);
      connection = (HttpURLConnection)url.openConnection();
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type",
           "application/x-www-form-urlencoded");
			
      connection.setRequestProperty("Content-Length", "" +
               Integer.toString(body.getBytes().length));
      connection.setRequestProperty("Content-Language", "en-US");
			
      connection.setUseCaches(false);
      connection.setDoInput(true);
      connection.setDoOutput(true);

      //Send request
      DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
      wr.writeBytes(body);
      wr.flush();
      wr.close();

      //Get Response	
      InputStream is = connection.getInputStream();
      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
      String line;
      StringBuffer response = new StringBuffer();
      while((line = rd.readLine()) != null) {
        response.append(line);
        response.append("\n");
      }
      rd.close();
      return response.toString();

    } catch (Exception e) {
      e.printStackTrace();
      return null;

    } finally {
      if(connection != null) {
        connection.disconnect();
      }
    }
  }
}
