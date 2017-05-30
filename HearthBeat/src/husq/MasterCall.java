package husq;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
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

    public static void PutRequest() {
        try {
            /*control  -   pLep3U*/
            JsonBuilderFactory jsonFactory = Json.createBuilderFactory(null);
            JsonObject object = jsonFactory.createObjectBuilder()
                    .add("login", "control")
                    .add("password", MD5("PUT"))
                    .build();
            URL url = new URL("http://10.3.51.41/api/v1/heartbeat");
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("PUT");
            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());
            out.write("Resource content");
            out.close();
            System.out.println(httpCon.getInputStream());


        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
