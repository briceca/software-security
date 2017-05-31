package rabbitmq;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

// Receiver for
class Receiver {
    public static void main(String[] argv) throws Exception {
        // Queue name
        final String QUEUE_NAME = "MonitoringErrorQueue";

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
                    int errorCode = obj.getInt("ErrorCode");

                    final int i = 200, j = 300, k = 400;
                    try {
                        if (errorCode < i) {
                            ElasticSendMessageError(obj);
                        } else if (errorCode >= i && errorCode < j) {
                            ElasticSendDBError(obj);
                        } else if (errorCode == j) {
                            ElasticSendAppError(obj);
                        } else if (errorCode == k) {
                            ElasticSendFraudError(obj);
                        } else {
                            System.out.println("Error code niet herkend: " + errorCode);
                        }
                    } catch (JSONException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Error code: " + errorCode);

                        if (errorCode != j) {
                            System.out.println("Sender" + obj.getString("Sender"));
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        channel.basicConsume(QUEUE_NAME, true, consumer);
    }

    private static void ElasticSendMessageError(JSONObject body) {
        String errorCode = Integer.toString(body.getInt("ErrorCode"));
        String sender = body.getString("Sender");
        String receiver = body.getString("Receiver");
        Date dateTimeFirst = new Date(body.getLong("DateTimeFirst"));
        Date dateTimeLast = new Date(body.getLong("DateTimeLast"));
        String exception = body.getString("Exception");

        TransportClient client = setupClient();

        try {
            IndexResponse response = client.prepareIndex("error", errorCode)
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("sender", sender)
                            .field("receiver", receiver)
                            .field("dateTimeFirst", dateTimeFirst)
                            .field("dateTimeLast", dateTimeLast)
                            .field("exception", exception)
                            .endObject()
                    )
                    .get();
            System.out.println(response.getResult() + "\n\n");
        } catch (IOException e) {

            e.printStackTrace();
        }
        client.close();
    }

    private static void ElasticSendDBError(JSONObject body) {
        String errorCode = Integer.toString(body.getInt("ErrorCode"));
        String sender = body.getString("Sender");
        String receiver = body.getString("Receiver");
        String objectType = body.getString("ObjectType");
        Date dateTime = new Date(body.getLong("DateTime"));
        String exception = body.getString("Exception");
        String query = body.getString("Query");

        TransportClient client = setupClient();

        try {
            IndexResponse response = client.prepareIndex("error", errorCode)
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("sender", sender)
                            .field("receiver", receiver)
                            .field("objectType", objectType)
                            .field("dateTime", dateTime)
                            .field("exception", exception)
                            .field("query", query)
                            .endObject()
                    )
                    .get();
            System.out.println(response.getResult() + "\n\n");
        } catch (IOException e) {

            e.printStackTrace();
        }
        client.close();
    }

    private static void ElasticSendAppError(JSONObject body) {
        String errorCode = Integer.toString(body.getInt("ErrorCode"));
        Date dateTime = new Date(body.getLong("DateTime"));
        String exception = body.getString("Exception");

        TransportClient client = setupClient();

        try {
            IndexResponse response = client.prepareIndex("error", errorCode)
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("dateTime", dateTime)
                            .field("exception", exception)
                            .endObject()
                    )
                    .get();
            System.out.println(response.getResult() + "\n\n");
        } catch (IOException e) {

            e.printStackTrace();
        }
        client.close();
    }

    private static void ElasticSendFraudError(JSONObject body) {
        String errorCode = Integer.toString(body.getInt("ErrorCode"));
        String sender = body.getString("Sender");
        String receiver = body.getString("Receiver");
        String objectType = body.getString("ObjectType");
        Date dateTime = new Date(body.getLong("DateTime"));
        String UUID = body.getString("UUID");
        double actualCredit = body.getDouble("ActualCredit");

        TransportClient client = setupClient();

        try {
            IndexResponse response = client.prepareIndex("error", errorCode)
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("sender", sender)
                            .field("receiver", receiver)
                            .field("objectType", objectType)
                            .field("dateTime", dateTime)
                            .field("uuid", UUID)
                            .field("actualCredit", actualCredit)
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
