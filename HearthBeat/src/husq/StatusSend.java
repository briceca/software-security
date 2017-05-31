package husq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.json.JSONObject;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by brice on 30/05/2017.
 */
public class StatusSend {
    private final static String QUEUE_NAME = "DashBoardQueue";
    public static void DashSend(SendObject objects[]) throws Exception
    {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("10.3.51.32");
        factory.setUsername("control");
        factory.setPassword("Student1");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);


        channel.basicPublish("", QUEUE_NAME, null, obj(objects).toString().getBytes());
        System.out.println(" status Sent ");

        channel.close();
        connection.close();
    }
    public static void ElasticSend(SendObject objects[]) throws Exception
    {
        TransportClient client = setupClient();

            IndexResponse response = client.prepareIndex("statuscheck", "status")
                    .setSource(obj(objects))
                    .get();
            System.out.println(response.getResult() + "\n\n");
        client.close();
    }

    private static JsonObject obj(SendObject objects[]) {
        JsonBuilderFactory jsonFactory = Json.createBuilderFactory(null);
        JsonObject object = jsonFactory.createObjectBuilder()
                .add("Serverstatus", Json.createArrayBuilder()
                        .add(Json.createObjectBuilder()
                                .add("Naam", "master")
                                .add("Delay", "-1")
                                .add("Online", true))
                        .add(Json.createObjectBuilder()
                                .add("Naam", "jenkins")
                                .add("Delay", "-1")
                                .add("Online", "-1"))
                        .add(Json.createObjectBuilder()
                                .add("Naam", "CRM")
                                .add("Delay", objects[0].getDelay())
                                .add("Online", objects[0].getStatus()))
                        .add(Json.createObjectBuilder()
                                .add("Naam", "Kassa")
                                .add("Delay", objects[1].getDelay())
                                .add("Online", objects[1].getStatus()))
                        .add(Json.createObjectBuilder()
                                .add("Naam", "Cloud")
                                .add("Delay", objects[2].getDelay())
                                .add("Online", objects[2].getStatus()))
                        .add(Json.createObjectBuilder()
                                .add("Naam", "Frontend")
                                .add("Delay", objects[3].getDelay())
                                .add("Online", objects[3].getStatus()))
                        .add(Json.createObjectBuilder()
                                .add("Naam", "Monitoring")
                                .add("Delay", "-1")
                                .add("Online", "true")))
                .build();
        return object;
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
