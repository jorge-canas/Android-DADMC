package dadmc.practica8_2;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.MediaController;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class MainActivity extends AppCompatActivity {
    final String DEBUGTAG = "Pract8_2";
    final int NAMED_PICTURE = 10;
    final int UNNAMED_PICTURE = 20;
    final int VIDEO = 30;

    EditText etName;
    EditText etSeconds;
    CheckBox cbHighDefinition;
    MediaController mc;

    ProgressBar pbFTP;

    LinearLayout llPicture;
    LinearLayout llVideo;

    ImageView ivPicture;
    VideoView vvVideo;

    String photoPath;
    String videoPath;

    String savePhotoPath;
    String saveVideoPath;

    private int numArchivos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initLayoutComponents();
    }

    public void initLayoutComponents() {
        etName = (EditText) findViewById(R.id.etName);
        etSeconds = (EditText) findViewById(R.id.etSeconds);
        cbHighDefinition = (CheckBox) findViewById(R.id.cbHighDefinition);

        llPicture = (LinearLayout) findViewById(R.id.llPicture);
        llVideo = (LinearLayout) findViewById(R.id.llVideo);

        pbFTP = (ProgressBar) findViewById(R.id.pbFTP);

        ivPicture = (ImageView) findViewById(R.id.ivPicture);
        vvVideo = (VideoView) findViewById(R.id.vvVideo);
        mc = new MediaController(MainActivity.this);
        mc.setAnchorView(vvVideo);
        vvVideo.setMediaController(mc);

        savePhotoPath = Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_PICTURES) + "/";

        saveVideoPath = Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_MOVIES) + "/";

        File fileFotoDir = new File(savePhotoPath);
        File fileVideoDir = new File(saveVideoPath);

        if (!fileFotoDir.exists()){
            fileFotoDir.mkdir();
        }

        if (!fileVideoDir.exists()){
            fileVideoDir.mkdir();
        }
    }

    public void btnNamedPictureClick(View v) {
        if (v.getId() == R.id.btnNamedPicture) {
            Log.i(DEBUGTAG, "Boton foto nombre click");
            llPicture.setVisibility(View.VISIBLE);
            llVideo.setVisibility(View.GONE);

            String rutaFoto = savePhotoPath;
            Log.i(DEBUGTAG, "Dir fotos: " + rutaFoto);

            if (!etName.getText().toString().equals("")) {
                photoPath = rutaFoto + etName.getText().toString() + ".jpg";
                Log.i(DEBUGTAG, "Tiene nombre "+photoPath);
                File picture = new File(photoPath);
                try{
                    picture.createNewFile();
                } catch (Exception e) {
                    Log.e(DEBUGTAG, "Error " + e.getMessage());
                }
                Uri uriPhoto = Uri.fromFile(picture);
                Log.i(DEBUGTAG, "Nombre uri"+uriPhoto);
                Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, uriPhoto);
                if (takePhoto.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(takePhoto, NAMED_PICTURE);
                }else{
                    Toast.makeText(getBaseContext(), "Hay un problema con la cámara",
                            Toast.LENGTH_LONG).show();
                }
            } else {
                Log.i(DEBUGTAG, "No tiene nombre");
                Toast.makeText(MainActivity.this, "Debe proporcionar un nombre al archivo",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void btnUnnamedPictureClick(View v) {
        if (v.getId() == R.id.btnUnnamedPicture) {
            Log.i(DEBUGTAG, "Boton foto sin nombre click");
            llPicture.setVisibility(View.VISIBLE);
            llVideo.setVisibility(View.GONE);
            String rutaFoto = savePhotoPath + setName() +".jpg";
            Log.i(DEBUGTAG, "Dir fotos: " + rutaFoto);

            photoPath = rutaFoto;
            Log.i(DEBUGTAG, "Tiene nombre"+photoPath);
            File picture = new File(photoPath);
            try{
                picture.createNewFile();
            } catch (Exception e) {
                Log.e(DEBUGTAG, "Error " + e.getMessage());
            }
            Uri uriPhoto = Uri.fromFile(picture);

            Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, uriPhoto);

            if (takePhoto.resolveActivity(getPackageManager()) != null){
                startActivityForResult(takePhoto, UNNAMED_PICTURE);
            }else{
                Toast.makeText(MainActivity.this, "Hay un problema con la cámara",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public String setName(){
        return  new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    public void btnVideoClick(View v) {
        if (v.getId() == R.id.btnVideo) {
            Log.i(DEBUGTAG, "Boton video click");
            llPicture.setVisibility(View.GONE);
            llVideo.setVisibility(View.VISIBLE);

            Intent takeVideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            //takeVideo.putExtra(MediaStore.EXTRA_OUTPUT, videoPath);

            if (cbHighDefinition.isChecked()){
                takeVideo.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            }else{
                takeVideo.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            }
            int seconds = Integer.valueOf(etSeconds.getText().toString());
            if (seconds > 0){
                takeVideo.putExtra(MediaStore.EXTRA_DURATION_LIMIT, seconds);
            }else{
                takeVideo.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
            }

            if (takeVideo.resolveActivity(getPackageManager()) != null){
                startActivityForResult(takeVideo, VIDEO);
            }else{
                Toast.makeText(MainActivity.this, "Hay un problema con la cámara",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void btnFTPClick(View v) {
        if (v.getId() == R.id.btnFTP) {

            Log.i(DEBUGTAG, "ruta fotos archivos guardar "+savePhotoPath);
            //String Temp = "";
            File f = new File(savePhotoPath);
            //Creo el array de tipo File con el contenido de la carpeta
            Log.i(DEBUGTAG, "Archivos " + f.getAbsolutePath());
            File[] files = f.listFiles();
            Log.i(DEBUGTAG,"Archivos tamaño "+files.length);
            numArchivos = files.length;
            /*StrictMode.ThreadPolicy old;
            // Deshabilitamos el modo estricto para conexiones externas temporalmente
            old = StrictMode.getThreadPolicy();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
                    .permitNetwork()
                    .build());*/
            Object params[] = {f};
            TareaAsincronaFTP tareaAsinc = new TareaAsincronaFTP();
            tareaAsinc.execute(params);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case NAMED_PICTURE:
                if (resultCode == RESULT_OK) {
                    setPic(photoPath);
                    galleryAddPic(photoPath);
                }else{
                    File f = new File(photoPath);
                    f.delete();
                }
                break;
            case UNNAMED_PICTURE:
                if (resultCode == RESULT_OK) {
                    setPic(photoPath);
                    galleryAddPic(photoPath);
                }else{
                    File f = new File(photoPath);
                    f.delete();
                }
                break;
            case VIDEO:
                if (resultCode == RESULT_OK) {
                    Uri videoUri = data.getData();
                    vvVideo.setVideoURI(videoUri);
                }
                break;
        }
    }

    private void setPic(String path) {
        // Get the dimensions of the View
        int targetW = ivPicture.getWidth();
        int targetH = ivPicture.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        ivPicture.setImageBitmap(bitmap);
    }

    private void galleryAddPic(String path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    /////////////////////////// AUTO GENERADO ///////////////////7

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

    //////////////////////////// Tarea asincrona
    public class TareaAsincronaFTP extends AsyncTask<Object, Integer, Integer> {
        final String DEBUGTAG = "Pract8_2";

        @Override
        protected Integer doInBackground(Object... params) {
            Log.i(DEBUGTAG, "doInBackground");
            Integer errores = 0;
            FTPClient ftp = new FTPClient();
            try {
                ftp.connect(InetAddress.getByName("217.76.132.191"));
                ftp.login("sed4442", "CursoDam1");
                ftp.setFileType(FTP.BINARY_FILE_TYPE);
                ftp.makeDirectory("Curso");
                ftp.changeWorkingDirectory("Curso");
                ftp.makeDirectory("Jorge");
                ftp.changeWorkingDirectory("Jorge");
                ftp.enterLocalPassiveMode();
                File f = (File) params[0];
                //Creo el array de tipo File con el contenido de la carpeta
                Log.i(DEBUGTAG, "Archivos " + f.getAbsolutePath());
                File[] files = f.listFiles();
                for (int i = 0; i < files.length; i++) {
                    //Log.i(DEBUGTAG,"Archivos "+files[i].getAbsolutePath());
                }
                if (files != null) {
                    Log.i(DEBUGTAG, "Files tiene "+files.length+ " archivos");
                    //Hacemos un Loop por cada fichero para extraer el nombre de cada uno
                    Log.i(DEBUGTAG, "Recorriendo la lista de archivos");
                    for (int i = 0; i < files.length; i++) {

                        //Sacamos del array files un fichero
                        File file = files[i];
                        if (!file.isDirectory()){
                            Log.i(DEBUGTAG, "Archivo "+file.getAbsolutePath());
                            FileInputStream is = new FileInputStream(file);
                            BufferedInputStream buffIn = new BufferedInputStream(is);
                            publishProgress(i+1);
                            Log.i(DEBUGTAG, "Almacenando foto");
                            boolean ok = ftp.storeFile(file.getName(), buffIn);
                            buffIn.close();
                            is.close();
                            if (!ok) {
                                ftp.deleteFile(file.getName());
                                errores++;
                            } else
                                file.delete();
                        }
                    }
                }
                ftp.logout();
                ftp.disconnect();
            } catch (Exception e) {
                Log.e(DEBUGTAG, "Error "+e.getMessage());
            }

            return errores;
        }

        @Override
        protected void onProgressUpdate(Integer... values){
            Log.i(DEBUGTAG, "onProgressUpdate valor "+values[0].intValue());
            pbFTP.setProgress(values[0].intValue());
        }

        @Override
        protected void onPreExecute(){
            Log.i(DEBUGTAG, "preExecute setMax "+numArchivos);
            pbFTP.setMax(numArchivos);
            pbFTP.setProgress(0);
            pbFTP.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Integer result){
            Log.i(DEBUGTAG, "postExecute");
            pbFTP.setVisibility(View.GONE);
            if(result != 0){
                Toast.makeText(MainActivity.this, "Tarea finalizada!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this, "Han habido "+result + " errores", Toast.LENGTH_SHORT).show();
            }
        }
    }

}


