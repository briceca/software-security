package husq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;

import org.json.*;
import sun.security.provider.MD5;

import javax.json.*;
/**
 * Created by brice on 25/05/2017.
 */
public class MasterCall {

    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static SendObject[] PutRequest() {
        SendObject objects[] = new SendObject[4];
        try {
            /*control  -   pLep3U*/
            JsonBuilderFactory jsonFactory = Json.createBuilderFactory(null);
            JsonObject object = jsonFactory.createObjectBuilder()
                    .add("login", "control")
                    .add("password", MD5("pLep3U"))
                    .build();
            try {
                URL url = new URL("http://10.3.51.41/api/v1/heartbeat");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                osw.write(object.toString());
                osw.flush();
                osw.close();
                System.err.println(connection.getResponseCode());
                System.err.println(connection.getResponseMessage());
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) { // success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            connection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // print result
                    JSONObject jsonObj = new JSONObject(response.toString());
                    JSONArray jsonarr = jsonObj.getJSONArray("StatusMessage");
                    for(int i=0; i<jsonarr.length(); i++) {
                        JSONObject jsonObject = jsonarr.getJSONObject(i);
                        objects[i] = new SendObject(jsonObject.getString("uuid"), jsonObject.getInt("version"), jsonObject.getString("uniq"));

                    }

                } else {
                    System.out.println("GET request not worked");
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        } catch (Exception e) {
            System.out.println(e);
        }
        return objects;
    }
}
