import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
//import org.json.*;
import javax.json.*;

public class Send {
    private final static String QUEUE_NAME = "MonitoringLogQueue";
    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("10.3.51.32");
        factory.setUsername("control");
        factory.setPassword("Student1");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);


            channel.basicPublish("", QUEUE_NAME, null, PLNObject().toString().getBytes());
        System.out.println(" [x] Sent ");

        channel.close();
        connection.close();
    }
    private static JsonObject VSTObject()
    {
        JsonBuilderFactory jsonFactory = Json.createBuilderFactory(null);
        JsonObject object = jsonFactory.createObjectBuilder()
                .add("Type", "Request")
                .add("Method", "PUT")
                .add("Sender", "KAS")
                .add("Receiver", "CLP")
                .add("ObjectType", "VST")
                .add("Credentials", jsonFactory.createObjectBuilder()
                        .add("login", "joskeNA")
                        .add("password", "joske123"))
                .add("Body", jsonFactory.createObjectBuilder()
                        .add("UUID", "KDN34576HDBHRBCR0879879")
                        .add("name", "Freddy")
                        .add("surname", "Vermeulen")
                        .add("address", "Boerinnenlaan 20, 9000 PLOP")
                        .add("email", "Freddy.Vermeulen@hotmail.com")
                        .add("tel", "0478562998")
                        .add("cable", "TRUE")
                        .add("breakfast", "FALSE")
                        .add("version", "0"))
                .build();
        return object;
    }
    private static JsonObject COLObject()
    {
        JsonBuilderFactory jsonFactory = Json.createBuilderFactory(null);
        JsonObject object = jsonFactory.createObjectBuilder()
                .add("Type", "Request")
                .add("Method", "PUT")
                .add("Sender", "KAS")
                .add("Receiver", "CLP")
                .add("ObjectType", "COL")
                .add("Credentials", jsonFactory.createObjectBuilder()
                        .add("login", "joskeNA")
                        .add("password", "joske123"))
                .add("Body", jsonFactory.createObjectBuilder()
                        .add("UUID", "UUID")
                        .add("name", "name")
                        .add("surname", "surname")
                        .add("address", "address")
                        .add("email", "email")
                        .add("tel", "tel")
                        .add("payed_amnt", "payed_amnt")
                        .add("cable", "cable")
                        .add("breakfast", "breakfast")
                        .add("version", "version")
                        .add("Receiver", "receiver")
                        .add("method", "method")
                        .add("objectType", "objectType")
                        .add("login", "login"))
                .build();
        return object;
    }
    private static JsonObject TMSObject()
    {
        JsonBuilderFactory jsonFactory = Json.createBuilderFactory(null);
        JsonObject object = jsonFactory.createObjectBuilder()
                .add("Type", "Request")
                .add("Method", "PUT")
                .add("Sender", "KAS")
                .add("Receiver", "CLP")
                .add("ObjectType", "TMS")
                .add("Credentials", jsonFactory.createObjectBuilder()
                        .add("login", "joskeNA")
                        .add("password", "joske123"))
                .add("Body", jsonFactory.createObjectBuilder()
                        .add("UUID", "UUID")
                        .add("name", "name")
                    .add("leader", "leader")
                    .add("version", "version"))
                .build();
        return object;
    }
    private static JsonObject TMMObject()
    {
        JsonBuilderFactory jsonFactory = Json.createBuilderFactory(null);
        JsonObject object = jsonFactory.createObjectBuilder()
                .add("Type", "Request")
                .add("Method", "PUT")
                .add("Sender", "KAS")
                .add("Receiver", "CLP")
                .add("ObjectType", "TMM")
                .add("Credentials", jsonFactory.createObjectBuilder()
                        .add("login", "joskeNA")
                        .add("password", "joske123"))
                .add("Body", jsonFactory.createObjectBuilder()
                        .add("team_id", "team_id")
                        .add("user_id", "user_id")
                        .add("version", "version"))
                .build();
        return object;
    }
    private static JsonObject SPKObject()
    {
        JsonBuilderFactory jsonFactory = Json.createBuilderFactory(null);
        JsonObject object = jsonFactory.createObjectBuilder()
                .add("Type", "Request")
                .add("Method", "PUT")
                .add("Sender", "KAS")
                .add("Receiver", "CLP")
                .add("ObjectType", "SPK")
                .add("Credentials", jsonFactory.createObjectBuilder()
                        .add("login", "joskeNA")
                        .add("password", "joske123"))
                .add("Body", jsonFactory.createObjectBuilder()
                        .add("UUID", "UUID")
                        .add("name", "name")
                        .add("surname", "surname")
                        .add("email", "email")
                        .add("topic", "topic")
                        .add("desc_short", "desc_short")
                        .add("version", "version"))
                .build();
        return object;
    }
    private static JsonObject EVTObject()
    {
        JsonBuilderFactory jsonFactory = Json.createBuilderFactory(null);
        JsonObject object = jsonFactory.createObjectBuilder()
                .add("Type", "Request")
                .add("Method", "PUT")
                .add("Sender", "KAS")
                .add("Receiver", "CLP")
                .add("ObjectType", "EVT")
                .add("Credentials", jsonFactory.createObjectBuilder()
                        .add("login", "joskeNA")
                        .add("password", "joske123"))
                .add("Body", jsonFactory.createObjectBuilder()
                        .add("UUID", "UUID")
                        .add("name", "name")
                        .add("desc", "desc")
                        .add("start", "start")
                        .add("end", "end")
                        .add("location", "location")
                        .add("version", "version"))
                .build();
        return object;
    }
    private static JsonObject SPOObject()
    {
        JsonBuilderFactory jsonFactory = Json.createBuilderFactory(null);
        JsonObject object = jsonFactory.createObjectBuilder()
                .add("Type", "Request")
                .add("Method", "PUT")
                .add("Sender", "KAS")
                .add("Receiver", "CLP")
                .add("ObjectType", "SPO")
                .add("Credentials", jsonFactory.createObjectBuilder()
                        .add("login", "joskeNA")
                        .add("password", "joske123"))
                .add("Body", jsonFactory.createObjectBuilder()
                        .add("UUID", "UUID")
                        .add("website", "website")
                        .add("image_url", "image_url")
                        .add("tel", "tel")
                        .add("version", "version"))
                .build();
        return object;
    }
    private static JsonObject PLNObject()
    {
        JsonBuilderFactory jsonFactory = Json.createBuilderFactory(null);
        JsonObject object = jsonFactory.createObjectBuilder()
                .add("Type", "Request")
                .add("Method", "PUT")
                .add("Sender", "KAS")
                .add("Receiver", "CLP")
                .add("ObjectType", "PLN")
                .add("Credentials", jsonFactory.createObjectBuilder()
                        .add("login", "joskeNA")
                        .add("password", "joske123"))
                .add("Body", jsonFactory.createObjectBuilder()
                        .add("UUID", "KDN34576HDBHRBCR0879879")
                        .add("name", "KOEN")
                        .add("surname", "VANDERVELDE")
                        .add("address", "Bosstraat 20, 9000 PLOP")
                        .add("email", "Koen.vandervelde@hotmail.com")
                        .add("tel", "0478562998")
                        .add("cable", "TRUE")
                        .add("breakfast", "FALSE")
                        .add("version", "0"))
                .build();
        return object;
    }
}
