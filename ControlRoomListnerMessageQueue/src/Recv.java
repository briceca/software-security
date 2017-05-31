import com.rabbitmq.client.*;


import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import org.json.*;
//import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.elasticsearch.common.xcontent.XContentFactory.*;

public class Recv {

    private final static String QUEUE_NAME = "MonitoringLogQueue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("10.3.51.32");
        factory.setUsername("control");
        factory.setPassword("Student1");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        System.out.println(" [*] Waiting for messages to ControlRoom. To exit press CTRL+C");
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] msg)
                    throws IOException {
                String message = new String(msg, "UTF-8");






                try {
                    JSONObject obj = new JSONObject(message);
                    String type = obj.getString("Type");
                    String method = obj.getString("Method");
                    String sender = obj.getString("Sender").toLowerCase();
                    String receiver = obj.getString("Receiver");
                    String objectType = obj.getString("ObjectType");
                    JSONObject getSth = obj.getJSONObject("Credentials");
                    String login = getSth.get("login").toString();
                    JSONObject body = obj.getJSONObject("Body");
                    switch(objectType) {
                        case "VST":
                            ElasticSendVST(sender, objectType, method, receiver, type, login, body);
                            ElasticSend("summary", objectType, method, sender, receiver, type, login);
                            break;
                        case "COL":
                            ElasticSendCOL(sender, objectType, method, receiver, type, login, body);
                            ElasticSend("summary", objectType, method, sender, receiver, type, login);
                            break;
                        case "TMS":
                            ElasticSendTMS(sender, objectType, method, receiver, type, login, body);
                            ElasticSend("summary", objectType, method, sender, receiver, type, login);
                            break;
                        case "TMM":
                            ElasticSendTMM(sender, objectType, method, receiver, type, login, body);
                            ElasticSend("summary", objectType, method, sender, receiver, type, login);
                            break;
                        case "SPK":
                            ElasticSendSPK(sender, objectType, method, receiver, type, login, body);
                            ElasticSend("summary", objectType, method, sender, receiver, type, login);
                            break;
                        case "EVT":
                            ElasticSendEVT(sender, objectType, method, receiver, type, login, body);
                            ElasticSend("summary", objectType, method, sender, receiver, type, login);
                            break;
                        case "SPO":
                            ElasticSendSPO(sender, objectType, method, receiver, type, login, body);
                            ElasticSend("summary", objectType, method, sender, receiver, type, login);
                            break;
                        case "PLN":
                            ElasticSendPLN(sender, objectType, method, receiver, type, login, body);
                            ElasticSend("summary", objectType, method, sender, receiver, type, login);
                            break;
                        case "ORD":
                            ElasticSendORD(sender, objectType, method, receiver, type, login, body);
                            ElasticSend("summary", objectType, method, sender, receiver, type, login);
                            break;
                        case "SHT":
                            ElasticSendSHT(sender, objectType, method, receiver, type, login, body);
                            ElasticSend("summary", objectType, method, sender, receiver, type, login);
                        default: System.out.println("objectType Niet herkend: " + objectType);
                            break;
                    }
                    System.out.println("sender: " + sender + "\nreceiver: " + receiver + "\ntype: " + type+ "\nmethod: " + method + "\nobjectType: " + objectType + "\nlogin: " + login);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }


    private static void ElasticSendVST(String index, String objectType, String method, String receiver, String type, String login, JSONObject body)
    {
       String UUID = body.getString("uuid");
        String name = body.getString("name");
        String surname = body.getString("surname");
        String address = body.getString("address");
        String email = body.getString("email");
        String tel = body.getString("tel");
        String cable = body.getString("cable");
        String breakfast = body.getString("breakfast");
        String version = body.getString("version");

        TransportClient client = setupClient();

        try {
            IndexResponse response = client.prepareIndex(index, type)
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("uuid", UUID)
                            .field("name", name)
                            .field("surname", surname)
                            .field("address", address)
                            .field("email", email)
                            .field("tel", tel)
                            .field("cable", cable)
                            .field("breakfast", breakfast)
                            .field("version", version)
                            .field("Receiver", receiver)
                            .field("method", method)
                            .field("objectType", objectType)
                            .field("login", login)
                            .endObject()
                    )
                    .get();
            System.out.println(response.getResult() + "\n\n");
        } catch (IOException e) {

            e.printStackTrace();
        }
        client.close();
    }
    private static void ElasticSendCOL(String index, String objectType, String method, String receiver, String type, String login, JSONObject body)
    {

        String UUID = body.getString("uuid");
        String name = body.getString("name");
        String surname = body.getString("surname");
       // String address = body.getString("address");
        String email = body.getString("email");
        String tel = body.getString("tel");//optioneel
        String function = body.getString("functio");
        String cable = body.getString("cable");
      //  String breakfast = body.getString("breakfast");
        String version = body.getString("version");

        TransportClient client = setupClient();

        try {
            IndexResponse response = client.prepareIndex(index, type)
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("UUID", UUID)
                            .field("name", name)
                            .field("surname", surname)
                            .field("email", email)
                            .field("tel", tel)
                            .field("function", function)
                            .field("cable", cable)
                            .field("version", version)
                            .field("Receiver", receiver)
                            .field("method", method)
                            .field("objectType", objectType)
                            .field("login", login)
                            .endObject()
                    )
                    .get();
            System.out.println(response.getResult() + "\n\n");
        } catch (IOException e) {

            e.printStackTrace();
        }
        client.close();
    }
    private static void ElasticSendTMS(String index, String objectType, String method, String receiver, String type, String login, JSONObject body)
    {
        String UUID = body.getString("UUID");
        String name = body.getString("name");
        String leader = body.getString("teamleader");
        String version = body.getString("version");

        TransportClient client = setupClient();

        try {
            IndexResponse response = client.prepareIndex(index, type)
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("UUID", UUID)
                            .field("name", name)
                            .field("Leader", leader)
                            .field("version", version)
                            .field("Receiver", receiver)
                            .field("method", method)
                            .field("objectType", objectType)
                            .field("login", login)
                            .endObject()
                    )
                    .get();
            System.out.println(response.getResult() + "\n\n");
        } catch (IOException e) {

            e.printStackTrace();
        }
        client.close();
    }
    private static void ElasticSendTMM(String index, String objectType, String method, String receiver, String type, String login, JSONObject body)
    {
        String UUID = body.getString("uuid");
        String team_id = body.getString("team_id");
        String user_id = body.getString("user_id");
        String version = body.getString("version");

        TransportClient client = setupClient();

        try {
            IndexResponse response = client.prepareIndex(index, type)
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("uuid", UUID)
                            .field("team_id", team_id)
                            .field("user_id", user_id)
                            .field("version", version)
                            .field("Receiver", receiver)
                            .field("method", method)
                            .field("objectType", objectType)
                            .field("login", login)
                            .endObject()
                    )
                    .get();
            System.out.println(response.getResult() + "\n\n");
        } catch (IOException e) {

            e.printStackTrace();
        }
        client.close();
    }
    private static void ElasticSendSPK(String index, String objectType, String method, String receiver, String type, String login, JSONObject body)
    {
        String UUID = body.getString("uuid");
        String name = body.getString("name");
        String surname = body.getString("surname");
        String tel = body.getString("tel");
        String email = body.getString("email");
        String topic = body.getString("topic");
        String desc_short = body.getString("desc_short");
        String version = body.getString("version");

        TransportClient client = setupClient();

        try {
            IndexResponse response = client.prepareIndex(index, type)
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("UUID", UUID)
                            .field("name", name)
                            .field("surname", surname)
                            .field("email", email)
                            .field("topic", topic)
                            .field("desc_short", desc_short)
                            .field("version", version)
                            .field("Receiver", receiver)
                            .field("method", method)
                            .field("objectType", objectType)
                            .field("login", login)
                            .endObject()
                    )
                    .get();
            System.out.println(response.getResult() + "\n\n");
        } catch (IOException e) {

            e.printStackTrace();
        }
        client.close();
    }
    private static void ElasticSendEVT(String index, String objectType, String method, String receiver, String type, String login, JSONObject body)
    {
        String UUID = body.getString("uuid");
        String topic = body.getString("topic");
        String topic_desc = body.getString("topic_description");
        String spk_uuid = body.getString("spk_uuid");
        String start = body.getString("start");
        String end = body.getString("end");
        String location = body.getString("location");
        int version = body.getInt("version");

        TransportClient client = setupClient();

        try {
            IndexResponse response = client.prepareIndex(index, type)
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("UUID", UUID)
                            .field("topic", topic)
                            .field("desc", topic_desc)
                            .field("start", start)
                            .field("end", end)
                            .field("spk_uuid", spk_uuid)
                            .field("location", location)
                            .field("version", version)
                            .field("Receiver", receiver)
                            .field("method", method)
                            .field("objectType", objectType)
                            .field("login", login)
                            .endObject()
                    )
                    .get();
            System.out.println(response.getResult() + "\n\n");
        } catch (IOException e) {

            e.printStackTrace();
        }
        client.close();
    }
    private static void ElasticSendSPO(String index, String objectType, String method, String receiver, String type, String login, JSONObject body)
    {
        String UUID = body.getString("uuid");
        String website = body.getString("website");
        String image_url = body.getString("image_url");
        String tel = body.getString("tel");
        int version = body.getInt("version");

        TransportClient client = setupClient();

        try {
            IndexResponse response = client.prepareIndex(index, type)
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("UUID", UUID)
                            .field("website", website)
                            .field("image_url", image_url)
                            .field("tel", tel)
                            .field("version", version)
                            .field("Receiver", receiver)
                            .field("method", method)
                            .field("objectType", objectType)
                            .field("login", login)
                            .endObject()
                    )
                    .get();
            System.out.println(response.getResult() + "\n\n");
        } catch (IOException e) {

            e.printStackTrace();
        }
        client.close();
    }
    private static void ElasticSendPLN(String index, String objectType, String method, String receiver, String type, String login, JSONObject body)
    {
       /* String UUID = body.getString("UUID");
        String website = body.getString("website");
        String image_url = body.getString("image_url");
        String tel = body.getString("tel");
        String version = body.getString("version");*/

        TransportClient client = setupClient();

        try {
            IndexResponse response = client.prepareIndex(index, type)
                    .setSource(jsonBuilder()
                            .startObject()
                           /* .field("UUID", UUID)
                            .field("website", website)
                            .field("image_url", image_url)
                            .field("tel", tel)
                            .field("version", version)*/
                            .field("Receiver", receiver)
                            .field("method", method)
                            .field("objectType", objectType)
                            .field("login", login)
                            .endObject()
                    )
                    .get();
            System.out.println(response.getResult() + "\n\n");
        } catch (IOException e) {

            e.printStackTrace();
        }
        client.close();
    }
    private static void ElasticSend(String index, String objectType, String method, String sender, String receiver, String type, String login)
    {
        TransportClient client = setupClient();
        try {
            IndexResponse response = client.prepareIndex(index, type)
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("Sender", sender)
                            .field("Receiver", receiver)
                            .field("method", method)
                            .field("objectType", objectType)
                            .field("login", login)
                            .endObject()
                    )
                    .get();
            System.out.println(response.getResult() + "\n\n");
        } catch (IOException e) {

            e.printStackTrace();
        }

        client.close();}
    private static void ElasticSendORD(String index, String objectType, String method, String receiver, String type, String login, JSONObject body)
    {
        String UUID = body.getString("uuid");
        String name = body.getString("name");
        int version = body.getInt("version");
        JSONArray ords = body.getJSONArray("productlines");
        int id[] = new int[ords.length()];
        String names[] = new String[ords.length()];
        double price[] = new double[ords.length()];
        int quantity[] = new int[ords.length()];
        for (int i = 0; i < ords.length(); ++i) {
            JSONObject ord = ords.getJSONObject(i);
            id[i] = ord.getInt("id");
            price[i] = ord.getDouble("price");
            quantity[i] = ord.getInt("quantity");
        }

        TransportClient client = setupClient();

        try {
            IndexResponse response = client.prepareIndex(index, type)
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("uuid", UUID)
                            .field("klant", name)
                            .array("product_id", id)
                            .array("product_naam", names)
                            .array("product_prijs", price)
                            .array("aantal", quantity)
                            .endObject()
                    )
                    .get();
            System.out.println(response.getResult() + "\n\n");
        } catch (IOException e) {

            e.printStackTrace();
        }
        client.close();
    }
    private static void ElasticSendSHT(String index, String objectType, String method, String receiver, String type, String login, JSONObject body)
    {
        String UUID = body.getString("uuid");
        String col_uuid = body.getString("col_uuid");
        String start = body.getString("start");
        String end = body.getString("end");
        String location = body.getString("location");
        int version = body.getInt("version");


        TransportClient client = setupClient();

        try {
            IndexResponse response = client.prepareIndex(index, type)
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("UUID", UUID)
                            .field("col_uuid", col_uuid)
                            .field("start", start)
                            .field("version", version)
                            .field("end", end)
                            .field("Receiver", receiver)
                            .field("method", method)
                            .field("objectType", objectType)
                            .field("login", login)
                            .endObject()
                    )
                    .get();
            System.out.println(response.getResult() + "\n\n");
        } catch (IOException e) {

            e.printStackTrace();
        }
        client.close();
    }

    private static TransportClient setupClient() {
        // Set up transport client
        Settings settings = Settings.builder().put("cluster.name", "IntegrationProject").build();

        TransportClient client = null;
        try {

            client = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.1.2"), 9300));

        } catch (UnknownHostException e) {

            e.printStackTrace();
        }

        return client;
    }
}

