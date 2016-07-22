package dadmc.practicaservicios;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    public static final String DEBUGTAG="Pract7_2";
    TextView tvIBinder;
    TextView tvMessenger;
    BoundIBinder boundIBinder = null;
    boolean ibinder = false;

    Messenger boundMessenger = null;
    boolean messenger = false;

    public static final int MSG_RAMDOM_VALUE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayoutComponents();
    }

    public void initLayoutComponents(){
        // Obtener botón de monitoreo de memoria
        ToggleButton buttonB = (ToggleButton) findViewById(R.id.toggleButtonBinder);
        tvIBinder = (TextView) findViewById(R.id.tvIBinder);
        tvMessenger = (TextView) findViewById(R.id.tvMessenger);
        // Setear escucha de acción
        buttonB.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            IniciarServiceBinder(); //Iniciar servicio
                        } else {
                            PararServiceBinder(); // Detener servicio
                        }
                    }
                }
        );

        ToggleButton buttonM = (ToggleButton) findViewById(R.id.toggleButtonMessenger);
        // Setear escucha de acción
        buttonM.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            IniciarServiceMessenger(); //Iniciar servicio
                        } else {
                            PararServiceMessenger(); // Detener servicio
                        }
                    }
                }
        );
    }

    //Para el bound ibinder
    private ServiceConnection mConnectionB = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(DEBUGTAG, "Conectar servicio ibinder");
            BoundIBinder.LocalBinder binder = (BoundIBinder.LocalBinder) service;
            boundIBinder = binder.getService();
            ibinder = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(DEBUGTAG, "Desconectar servicio ibinder");
            ibinder = false;
        }
    };

    public void onButtonClick1(View V){
        Log.d(DEBUGTAG, "Boton ibinder");
        if (ibinder){
            int rand = boundIBinder.getRandomNumber();
            Log.d(DEBUGTAG, "ibinder activo con numero " + rand);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tvIBinder.setText(String.valueOf(rand));
        }
    }

    public void onButtonClick2(View V){
        Log.d(DEBUGTAG, "Boton messenger");
        if(messenger){
            Log.d(DEBUGTAG, "messenger activo");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message mens = Message.obtain();
            mens.what = BoundMessenger.MSG_ACCION;
            mens.replyTo = respuesta;
            try{
                boundMessenger.send(mens);
            }catch (RemoteException e){
                e.printStackTrace();
            }
        }
    }

    public class IncomingHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case MSG_RAMDOM_VALUE:
                    int rand = msg.getData().getInt("random");
                    Log.d(DEBUGTAG, "Valor recogido " + rand);
                    tvMessenger.setText(String.valueOf(rand));
                    break;
                default:
            }
        }
    }

    private void IniciarServiceBinder(){
        Log.d(DEBUGTAG, "Iniciar service binder");
        Intent intent = new Intent(this, BoundIBinder.class);
        //getApplicationContext().bindService(intent, mConnectionB, Context.BIND_AUTO_CREATE);
        bindService(intent, mConnectionB, Context.BIND_AUTO_CREATE);
    }

    private void PararServiceBinder(){
        if (ibinder) {
            Log.d(DEBUGTAG, "Parar service binder");
            //getApplicationContext().unbindService(mConnectionB);
            unbindService(mConnectionB);
            ibinder = false;
        }
    }

    //Para el bound messenger
    private ServiceConnection mConnectionM = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(DEBUGTAG, "Conectar servicio messenger");
            boundMessenger = new Messenger(service);
            messenger = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(DEBUGTAG, "Desconectar servicio messenger");
            boundMessenger = null;
            messenger = false;
        }
    };

    private void IniciarServiceMessenger(){
        Log.d(DEBUGTAG, "Iniciar service messenger");
        Intent intent = new Intent(this, BoundMessenger.class);
        //getApplicationContext().bindService(intent, mConnectionM, Context.BIND_AUTO_CREATE);
        bindService(intent, mConnectionM, Context.BIND_AUTO_CREATE);
    }

    private void PararServiceMessenger(){
        if (messenger){
            Log.d(DEBUGTAG, "Parar service messenger");
            //getApplicationContext().unbindService(mConnectionM);
            unbindService(mConnectionM);
            messenger = false;
        }
    }

    Messenger respuesta = new Messenger(new IncomingHandler());
}