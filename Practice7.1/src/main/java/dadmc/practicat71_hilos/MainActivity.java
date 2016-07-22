package dadmc.practicat71_hilos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import java.io.InputStream;
import java.net.URL;

public class MainActivity extends Activity {

    private ProgressBar pbarProgreso;
    private ImageView mIView;
    private EditText texturl;
    private UIHandler hndl;

    private static final String DEBUGTAG = "PRACTICA 7.1";

    private static final int SET_PROGRESS = 0;

    private TareaAsinc tareaAsinc = null;
    private TareaAsincPD tareaAsincPD = null;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pbarProgreso = (ProgressBar) findViewById(R.id.pbarProgreso);
        pbarProgreso.setMax(100);
        mIView = (ImageView) findViewById(R.id.imageView);
        texturl = (EditText) findViewById(R.id.editText1);
        hndl = new UIHandler(new WeakReference<MainActivity>(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void tareaLarga() {
        try {
            Thread.sleep(1000);
            Log.i(DEBUGTAG, Thread.currentThread().getName()+" EJECUTANDO TAREA LARGA ...");
        } catch (InterruptedException e) {
        }
    }

    private void cargaimagen(Bitmap bm) {
        if (null != bm) {
            mIView.setImageBitmap(bm);
        }
    }

    private Bitmap loadImageFromNetwork(String url) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.android_threads);
        try {
            // HAY QUE TENER EL PERMISO DE INTERNET PARA QUE ESTO FUNCIONE
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;

    }

    private void borraimagen() {
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.vacio);
        if (null != mBitmap) {
            mIView.setImageBitmap(mBitmap);
        }
    }

    public void SinHilosClick(View v) {
        pbarProgreso.setProgress(0);
        borraimagen();

        for (int i = 1; i <= 10; i++) {
            tareaLarga();
            pbarProgreso.incrementProgressBy(10);
        }
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.android_threads);

        cargaimagen(bitmap);
        Toast.makeText(MainActivity.this, "Tarea finalizada!", Toast.LENGTH_SHORT).show();
    }

    public void respondeClick(View v) {
        Toast.makeText(getApplicationContext(), "Responde ...", Toast.LENGTH_SHORT).show();

    }

    public void ConHilosClick(View v) {
        Log.i(DEBUGTAG,"ConHilos click");
        new Thread(new Runnable(){
           public void run() {
               //Tarea 1: Iniciar barra progreso con mensaje a un handler
               Message msg = hndl.obtainMessage(SET_PROGRESS, 0);
               hndl.sendMessage(msg);
               //Tarea 2: borrado de imagen inicial enviando runnable al handler
               clearImage();

               for (int i = 0; i < 10; i++) {
                   tareaLarga();
                   //Tarea 3: Evolucion de progress bar con metodo post del UI
                   pbarProgreso.post(new Runnable(){
                       public void run(){
                           pbarProgreso.incrementProgressBy(10);
                       }
                   });
               }
               //Tarea 4: Visualizar imagen usando runOnUiThread
               runOnUiThread(new Runnable() {
                   public void run() {
                       Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.android_threads);
                       cargaimagen(bitmap);
                   }
               });
           }
        }).start();
    }

    public void TareaAsincronaClick(View v){
        Log.i(DEBUGTAG, "Tarea asincrona sin dialog click");
        tareaAsinc = new TareaAsinc();
        tareaAsinc.execute();
    }

    public void TareaAsincronaCancelClick(View v){
        Log.i(DEBUGTAG,"Cancelar tarea asincrona sin dialog click");
        if (tareaAsinc != null)
            tareaAsinc.cancel(true);
    }

    public void TareaAsincronaDialogClick(View v){
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setMessage("Procesando...");
        pDialog.setCancelable(true);
        pDialog.setMax(100);

        tareaAsincPD = new TareaAsincPD();
        tareaAsincPD.execute();
    }

    public ProgressBar getProgressBar(){
        return pbarProgreso;
    }

    //Clase UIHandler
    static class UIHandler extends Handler {
        private final WeakReference<MainActivity> mParent;

        public UIHandler(WeakReference<MainActivity> parent){
            mParent = parent;
        }

        @Override
        public void handleMessage(Message msg){
            MainActivity parent = mParent.get();
            switch (msg.what){
                case SET_PROGRESS:
                    Log.i(DEBUGTAG, "Argumento "+ msg.arg1);
                    parent.getProgressBar().setProgress((Integer) msg.arg1);
                    break;
            }
        }
    }

    //Tarea asincrona con boton cancelar
    private class TareaAsinc extends AsyncTask<Void, Integer, Boolean>{
        @Override
        protected Boolean doInBackground(Void... params) {
            for (int i = 1; i < 10; i++){
                tareaLarga();
                publishProgress(i * 10);
                if (isCancelled())
                    break;
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values){
            pbarProgreso.setProgress(values[0].intValue());
        }

        @Override
        protected void onPreExecute(){
            pbarProgreso.setMax(100);
            pbarProgreso.setProgress(0);
            clearImage();
        }

        @Override
        protected void onPostExecute(Boolean result){
            if(result){
                Toast.makeText(MainActivity.this, "Tarea finalizada!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled(){
            Toast.makeText(MainActivity.this, "Tarea cancelada", Toast.LENGTH_SHORT).show();
        }
    }

    //Tarea asincrona con boton cancelar
    private class TareaAsincPD extends AsyncTask<Void, Integer, Boolean>{
        @Override
        protected Boolean doInBackground(Void... params) {
            for (int i = 1; i < 10; i++){
                tareaLarga();
                publishProgress(i * 10);
                if (isCancelled())
                    break;
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values){
            pDialog.setProgress(values[0].intValue());
        }

        @Override
        protected void onPreExecute(){
            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    TareaAsincPD.this.cancel(true);
                }
            });
            clearImage();
            pDialog.setProgress(0);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result){
            if(result){
                pDialog.dismiss();
                Toast.makeText(MainActivity.this, "Tarea finalizada!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled(){
            //pDialog.cancel();
            Toast.makeText(MainActivity.this, "Tarea cancelada", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearImage(){
        hndl.post(new Runnable() {
            @Override
            public void run() {
                mIView.setImageResource(android.R.color.transparent);
            }
        });
    }

    /*
    //Tarea 1: Iniciar barra progreso con mensaje a un handler
    private void initProgressBar(){
        hndl.sendMessage(hndl.obtainMessage(SET_PROGRESS, 0));
    }

    //Tarea 2: borrado de imagen inicial enviando runnable al handler
    private void clearImage(){
        hndl.post(new Runnable() {
            @Override
            public void run() {
                mIView.setImageResource(android.R.color.transparent);
            }
        });
    }

    //Tarea 3: Evolucion de progress bar con metodo post del UI
    private void progressBarUpdate(){
        pbarProgreso.post(new Runnable(){
            public void run(){
                pbarProgreso.incrementProgressBy(10);
            }
        });
    }

    //Tarea 4: Visualizar imagen usando runOnUiThread
    private void showImage(){
        runOnUiThread(new Runnable(){
            public void run(){
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.android_threads);
                cargaimagen(bitmap);
            }
        });
    }
    */

}
