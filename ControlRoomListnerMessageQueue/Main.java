import org.elasticsearch.action.delete.DeleteResponse;
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

/**
 * Created by LoStIt on 22/03/2017.
 */
public class Main {
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);

        int choice = 99;
        // Setup choice menu
        while (choice != 0) {
            System.out.print("1) Entry toevoegen/aanpassen\n" +
                    "2) Entry zoeken en tonen op scherm\n" +
                    "3) Entry verwijderen" +
                    "\n" +
                    "Keuze: ");

            choice = reader.nextInt();

            switch (choice) {
                case 1:
                    add(reader);
                    break;
                case 2:
                    get(reader);
                    break;
                case 3:
                    delete(reader);
                    break;
                default:
                    break;
            }
        }
    }

    public static void add(Scanner reader) {
        TransportClient client = setupClient();

        // Read in values from console
        System.out.print("\nIndexnaam: ");
        String index = reader.next();

        System.out.print("\nType: ");
        String type = reader.next();

        System.out.print("\nID: ");
        String id = reader.next();

        System.out.print("\n\nGebruikersnaam: ");
        reader.nextLine();
        String user = reader.nextLine();

        System.out.print("\nBericht: ");
        String message = reader.nextLine();

        // Index entry
        try {
            IndexResponse response = client.prepareIndex(index, type, id)
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
        client.close();
    }

    public static void get(Scanner reader) {
        TransportClient client = setupClient();

        System.out.print("\nIndexnaam: ");
        String index = reader.next();

        System.out.print("\nType: ");
        String type = reader.next();

        System.out.print("\nID: ");
        String id = reader.next();

        // Get entry
        GetResponse response = client.prepareGet(index, type, id).get();

        if (response.isExists()) {
            Map<String, Object> map = response.getSource();
            System.out.print(response.getSourceAsString() + "\n\n");
        }

        client.close();
    }

    public static void delete(Scanner reader) {
        TransportClient client = setupClient();

        System.out.print("\nIndexnaam: ");
        String index = reader.next();

        System.out.print("\nType: ");
        String type = reader.next();

        System.out.print("\nID: ");
        String id = reader.next();

        DeleteResponse response = client.prepareDelete(index, type, id).get();
        System.out.print(response.getResult() + "\n\n");

        client.close();
    }

    public static TransportClient setupClient() {
        // Set up transport client
        Settings settings = Settings.builder().put("cluster.name", "app").build();

        TransportClient client = null;
        try {
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return client;
    }
}
