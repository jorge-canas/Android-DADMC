package dadmc.practicaservicios;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Random;

/**
 * Created by Inmundus on 28/01/2016.
 */
public class BoundIBinder extends Service {
    private final IBinder binder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        BoundIBinder getService(){
            return BoundIBinder.this;
        }
    }

    public int getRandomNumber(){
        Random generator = new Random();
        return generator.nextInt(100);
    }
}
