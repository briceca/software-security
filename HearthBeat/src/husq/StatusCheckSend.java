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
    public static void CheckSend(SendObject[] sndObj) throws Exception {

        Send("CRMQueue", sndObj[0].getTimestampSnd(), sndObj[0].getVar(), sndObj[0].getSystemName(), sndObj[0].getUuid(), sndObj[0].getVersion());
        Send("FrontendQueue", sndObj[1].getTimestampSnd(), sndObj[1].getVar(), sndObj[1].getSystemName(), sndObj[1].getUuid(), sndObj[1].getVersion());
        Send("KassaQueue", sndObj[2].getTimestampSnd(), sndObj[2].getVar(), sndObj[2].getSystemName(), sndObj[2].getUuid(), sndObj[2].getVersion());
        Send("PlanningQueue", sndObj[3].getTimestampSnd(), sndObj[3].getVar(), sndObj[3].getSystemName(), sndObj[3].getUuid(), sndObj[3].getVersion());
    }
    public static void Send(String name, long timestamp, int var, String recName, String uuid, int version) throws Exception {
        QUEUE_NAME = name;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("10.3.51.32");
        factory.setUsername("control");
        factory.setPassword("Student1");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.basicPublish("", QUEUE_NAME, null, MakeCheckObject(timestamp, var, recName, uuid, version ).toString().getBytes());
        System.out.println(" check Sent to :" + name);

        channel.close();
        connection.close();
    }

    private static JsonObject MakeCheckObject(long timestamp, int var, String recName, String uuid, int version)
    {
        JsonBuilderFactory jsonFactory = Json.createBuilderFactory(null);
        JsonObject object = jsonFactory.createObjectBuilder()
                .add("Type", "HBT")
                .add("Receiver", recName)
                .add("Sender", "Mon")
                .add("Body", jsonFactory.createObjectBuilder()
                        .add("uuid", uuid)
                        .add("timestampsnd", timestamp)
                        .add("timestampres", "null")
                        .add("var", var)
                        .add("version", version))
                .build();
        return object;
    }
}
