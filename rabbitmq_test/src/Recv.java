import com.rabbitmq.client.*;
import com.sun.xml.internal.ws.api.model.ExceptionType;
import org.json.*;
import java.io.IOException;

public class Recv {

    private final static String QUEUE_NAME = "MonitoringLogQueue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("10.3.51.32");
        factory.setUsername("control");
        factory.setPassword("Student1");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] msg)
                    throws IOException {
                String message = new String(msg, "UTF-8");
                //String str = "{ \"name\": \"Alice\", \"age\": 20 }";
                try {
                    JSONObject obj = new JSONObject(message);
                    String type = obj.getString("Type");
                    String method = obj.getString("Method");
                    String sender = obj.getString("Sender");
                    String receiver = obj.getString("Receiver");
                    String objectType = obj.getString("ObjectType");
                    JSONObject getSth = obj.getJSONObject("Credentials");
                    String login = getSth.get("Login").toString();
                    //String body = obj.getString("Body");
                    System.out.println("sender: " + sender + "\nreceiver: " + receiver + "\ntype: " + type + "\nlogin: " + login);  // prints "Alice 20"
                    System.out.println(" [x] Received '" + message + "'");
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
