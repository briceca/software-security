import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
public class Send {
    private final static String QUEUE_NAME = "ControlRoom";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("10.3.51.32");
        factory.setUsername("control");
        factory.setPassword("Student1");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        for(int i = 0; i< 3; i++) {
            String message = ("message" + i).toString();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
        }
        System.out.println(" [x] Sent ");

        channel.close();
        connection.close();
    }
}
