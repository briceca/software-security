package husq;

import static java.lang.Thread.sleep;

/**
 * Created by brice on 30/05/2017.
 */
public class StatusSender implements Runnable {


    Thread senderthread ;
    StatusSender()
    {
        senderthread = new Thread(this, "my runnable thread");
        System.out.println("my thread created" + senderthread);
        senderthread.start();
    }

    public void run() {
        while(senderthread.isAlive()){

        SendObject objects[] = MasterCall.PutRequest();
        Main.setObj(objects);

        try {
            StatusCheckSend.CheckSend(objects);
        }catch (Exception e) {
            e.printStackTrace();
        }

            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
