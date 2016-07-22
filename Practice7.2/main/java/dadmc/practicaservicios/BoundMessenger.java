package dadmc.practicaservicios;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import java.util.Random;

/**
 * Created by Inmundus on 28/01/2016.
 */
public class BoundMessenger extends Service {
    final Messenger messenger = new Messenger(new IncomingHandler());
    static final int MSG_ACCION = 10;

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case MSG_ACCION:
                    Message mens = Message.obtain();
                    mens.what = MainActivity.MSG_RAMDOM_VALUE;
                    Bundle bResp = new Bundle();
                    bResp.putInt("random", getRandomNumber());
                    mens.setData(bResp);
                    try{
                        msg.replyTo.send(mens);
                    }catch (RemoteException e){
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    public int getRandomNumber(){
        Random generator = new Random();
        return generator.nextInt(100);
    }
}
