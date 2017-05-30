/**
 * Created by brice on 25/05/2017.
 */
package husq;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;


import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class Main extends Thread {
    public static void main(String[] argv) throws Exception {
        StatusSender statSnd = new StatusSender();
        StatusCheckListener statLst = new StatusCheckListener();
        try
        {
           statSnd.senderthread.start();
           statSnd.senderthread.run();
            statLst.listenerthread.start();
            statLst.listenerthread.run();



        }
        catch(Exception e)
        {
            System.out.println("Main thread interrupted");
        }
        System.out.println("Main thread run is over" );
    }
}
