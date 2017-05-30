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
        // MasterCall.PutRequest();
        long millis = System.currentTimeMillis();
        int var[] = new int[4];
        for(int i = 0; i <4; i++)
        {
            var[i] = (int)(Math.random() * 50);
        }

        //moet nog berekend worden!!
        boolean status[] = new boolean[7];
        for(int i = 0; i <7; i++)
        {
            status[i] = true;
        }
        int delays[] = new int[7];
        for(int i = 0; i <7; i++)
        {
            delays[i] = (int)(Math.random() * 50);
        }
        //bereken
        try {
            StatusCheckSend.CheckSend(millis, var);
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
