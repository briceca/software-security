package husq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;

/**
 * Created by brice on 30/05/2017.
 */
public class StatusCheckSend {
    private static String QUEUE_NAME;
    public static void CheckSend(long timestamp, int [] var) throws Exception {
        Send("CRMQueue", timestamp, var[0], "CRM");
        Send("FrontendQueue", timestamp, var[1], "FRE");
        Send("KassaQueue", timestamp, var[2], "KAS");
        Send("PlanningQueue", timestamp, var[3], "CLP");
    }
    public static void Send(String name, long timestamp, int var, String recName) throws Exception {
        QUEUE_NAME = name;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("10.3.51.32");
        factory.setUsername("control");
        factory.setPassword("Student1");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);


        channel.basicPublish("", QUEUE_NAME, null, MakeCheckObject(timestamp, var, recName ).toString().getBytes());
        System.out.println(" check Sent to :" + name);

        channel.close();
        connection.close();
    }

    private static JsonObject MakeCheckObject(long timestamp, int var, String recName)
    {
        JsonBuilderFactory jsonFactory = Json.createBuilderFactory(null);
        JsonObject object = jsonFactory.createObjectBuilder()
                .add("Type", "HBT")
                .add("Receiver", recName)
                .add("Sender", "Mon")
                .add("Body", jsonFactory.createObjectBuilder()
                        .add("uuid", recName)
                        .add("timestampsnd", timestamp)
                        .add("timestampres", "null")
                        .add("var", var)
                        .add("version", "0"))
                .build();
        return object;
    }
}
