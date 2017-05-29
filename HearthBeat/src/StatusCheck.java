/**
 * Created by brice on 24/05/2017.
 */
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

public class StatusCheck {
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
                    System.out.println("sender: " + sender + "\nreceiver: " + receiver + "\ntype: " + type + "\nmethod: " + method + "\nobjectType: " + objectType + "\nlogin: " + login);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
    private static TransportClient setupClient() {
        // Set up transport client
        Settings settings = Settings.builder().put("cluster.name", "IntegrationProject").build();

        TransportClient client = null;
        try {

            client = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.1.2"), 8080));

        } catch (UnknownHostException e) {

            e.printStackTrace();
        }

        return client;
    }
}
