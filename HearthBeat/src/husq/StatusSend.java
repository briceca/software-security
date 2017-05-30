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
    public static void DashSend(int[] delays, boolean[] status) throws Exception
    {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("10.3.51.32");
        factory.setUsername("control");
        factory.setPassword("Student1");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);


        channel.basicPublish("", QUEUE_NAME, null, obj(delays, status).toString().getBytes());
        System.out.println(" status Sent ");

        channel.close();
        connection.close();
    }
    public static void ElasticSend(int[] delays, boolean[] status) throws Exception
    {
        TransportClient client = setupClient();

            IndexResponse response = client.prepareIndex("statuscheck", "status")
                    .setSource(obj(delays, status))
                    .get();
            System.out.println(response.getResult() + "\n\n");
        client.close();
    }

    private static JsonObject obj(int[] delays, boolean[] status) {
        JsonBuilderFactory jsonFactory = Json.createBuilderFactory(null);
        JsonObject object = jsonFactory.createObjectBuilder()
                .add("Serverstatus", Json.createArrayBuilder()
                        .add(Json.createObjectBuilder()
                                .add("Naam", "master")
                                .add("Delay", delays[0])
                                .add("Online", status[0]))
                        .add(Json.createObjectBuilder()
                                .add("Naam", "jenkins")
                                .add("Delay", delays[1])
                                .add("Online", status[1]))
                        .add(Json.createObjectBuilder()
                                .add("Naam", "CRM")
                                .add("Delay", delays[2])
                                .add("Online", status[2]))
                        .add(Json.createObjectBuilder()
                                .add("Naam", "Kassa")
                                .add("Delay", delays[3])
                                .add("Online", status[3]))
                        .add(Json.createObjectBuilder()
                                .add("Naam", "Cloud")
                                .add("Delay", delays[4])
                                .add("Online", status[4]))
                        .add(Json.createObjectBuilder()
                                .add("Naam", "Frontend")
                                .add("Delay", delays[5])
                                .add("Online", status[5]))
                        .add(Json.createObjectBuilder()
                                .add("Naam", "Monitoring")
                                .add("Delay", delays[6])
                                .add("Online", status[6])))
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
