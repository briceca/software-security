import com.rabbitmq.client.*;

//import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

import static org.elasticsearch.common.xcontent.XContentFactory.*;

public class Recv {

    private final static String QUEUE_NAME = "ControlRoom";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("10.3.51.32");
        factory.setUsername("control");
        factory.setPassword("Student1");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String user = "user";
        String index = "index";

        String type = "type";

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages to ControlRoom. To exit press CTRL+C");
        TransportClient client = setupClient();
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                TransportClient client = setupClient();

                try {
                    IndexResponse response = client.prepareIndex(index, type)
                            .setSource(jsonBuilder()
                                    .startObject()
                                    .field("user", user)
                                    .field("postDate", new Date())
                                    .field("message", message)
                                    .endObject()
                            )
                            .get();
                    System.out.println(response.getResult() + "\n\n");
                } catch (IOException e) {

                    e.printStackTrace();
                }

                // Close client

            }
        };
        client.close();

        channel.basicConsume(QUEUE_NAME, true, consumer);
    }

    public static TransportClient setupClient() {
        // Set up transport client
        Settings settings = Settings.builder().put("cluster.name", "IntegrationProject").build();

        TransportClient client = null;
        try {

            client = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

        } catch (UnknownHostException e) {

            e.printStackTrace();
        }

        return client;
    }
}

