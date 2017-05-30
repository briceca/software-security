package husq; /**
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

public class StatusCheckListener implements Runnable{
    SendObject objects[] = new SendObject[4];
    SendObject MObjects[] = Main.obj;
    private final static String QUEUE_NAME = "HeartBeatQueue";
    Thread listenerthread ;
    StatusCheckListener()
    {
        listenerthread = new Thread(this, "my runnable thread");
        System.out.println("my thread created" + listenerthread);
        listenerthread.start();
    }
    public void run() {
try{
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
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] msg) throws IOException {
                String message = new String(msg, "UTF-8");



                    JSONObject obj = new JSONObject(message);
                    String type = obj.getString("Type");
                    JSONObject body = obj.getJSONObject("Body");
                    String uuid = body.get("uuid").toString();
                long timestampsnd = body.getLong("timestampsnd");
                long timestampres = body.getLong("timestampres");
                int var = body.getInt("var");
                int version = body.getInt("version");

                    switch(uuid) {
                        case "60b55993-MAD-HRB-642a-5368-8e04-1fdc38a27658":
                            if(var == MObjects[0].getVar()*MObjects[0].getVar()) {
                                objects[0] = new SendObject(uuid, version, "CLP", true, var, timestampres, timestampsnd);
                            }
                            else
                            {
                                objects[0] = new SendObject(uuid, version, "CLP", false, var, timestampres, timestampsnd);
                            }
                            break;
                        case "f9d8ffdb-MAD-HRB-2dec-5a2c-ab56-986cef72b742":
                            if(var == MObjects[0].getVar()*MObjects[0].getVar()) {
                                objects[0] = new SendObject(uuid, version, "CRM", true, var, timestampres, timestampsnd);
                            }
                            else
                            {
                                objects[0] = new SendObject(uuid, version, "CRM", false, var, timestampres, timestampsnd);
                            }
                            break;
                        case "34935273-MAD-HRB-e1c2-5bf4-a1a8-e2f13203e1e0":
                            if(var == MObjects[0].getVar()*MObjects[0].getVar()) {
                                objects[0] = new SendObject(uuid, version, "FRE", true, var, timestampres, timestampsnd);
                            }
                            else
                            {
                                objects[0] = new SendObject(uuid, version, "FRE", false, var, timestampres, timestampsnd);
                            }
                            break;
                        case "cbaf0d22-MAD-HRB-7adf-5757-ab24-52424196cfbf":
                            if(var == MObjects[0].getVar()*MObjects[0].getVar()) {
                                objects[0] = new SendObject(uuid, version, "KAS", true, var, timestampres, timestampsnd);
                            }
                            else
                            {
                                objects[0] = new SendObject(uuid, version, "KAS", false, var, timestampres, timestampsnd);
                            }
                            break;
                    }

if(objects[0] != null && objects[1] != null && objects[2] != null && objects[3] != null)
{

    try {
        StatusSend.ElasticSend(objects);
        StatusSend.DashSend(objects);
    } catch (Exception e) {
        e.printStackTrace();
    }
    for(int i = 0; i < 4; i++) {
        objects[i] = null;
    }
}

                }
            };
    channel.basicConsume(QUEUE_NAME, true, consumer);
    }catch (Exception e) {
        e.printStackTrace();
    }
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
