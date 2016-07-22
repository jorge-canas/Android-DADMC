package dadmc.practica35;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.jar.Manifest;

public class Practica3_5 extends AppCompatActivity {

    private Button btnGetContacts;
    private Button btnLaunchCrash;
    private TextView txtContacts;
    private final String DEBUGTAG = "Practica3.5_app";
    private final int PERMISO_CONTACTS = 10;
    private final int PERMISO_CRASH = 20;

    private void initLayoutComponents(){
        txtContacts = (TextView) findViewById(R.id.txtContacts);
        btnGetContacts = (Button) findViewById(R.id.btnGetContacts);
        btnLaunchCrash = (Button) findViewById(R.id.btnLaunchCrash);

        btnGetContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchReadContacts();
            }
        });

        btnLaunchCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryLaunchCrash();
            }
        });
    }

    public void launchReadContacts(){
        if(ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_CONTACTS)){
                Log.i(DEBUGTAG, "Read Contacts necesita explicación");
                Toast.makeText(getBaseContext(), "Esta aplicación necesita acceso a la lista de " +
                        "contactos", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_CONTACTS}, PERMISO_CONTACTS);
            }else{
                Log.i(DEBUGTAG, "Read Contacts no necesita explicación, pedir permiso");
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_CONTACTS}, PERMISO_CONTACTS);
            }
        }else{
            Log.i(DEBUGTAG, "Read Contacts tiene permiso");
            txtContacts.setText(leeBookmarks());
        }
    }

    public void tryLaunchCrash(){
        if(ContextCompat.checkSelfPermission(this,
                "dadmc.practica35crash.CRASH_PERM") != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    "dadmc.practica35crash.CRASH_PERM")){
                Log.i(DEBUGTAG, "Read Contacts necesita explicación");
                Toast.makeText(getBaseContext(), "Esta aplicación necesita acceso al programa Crash",
                        Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{"dadmc.practica35crash.CRASH_PERM"}, PERMISO_CRASH);
            }else{
                Log.i(DEBUGTAG, "Crash no necesita explicación, pedir permiso");
                ActivityCompat.requestPermissions(this,
                        new String[]{"dadmc.practica35crash.CRASH_PERM"}, PERMISO_CRASH);
            }
        }else{
            Log.i(DEBUGTAG, "Crash tiene permiso");
            launchCrash();
        }
    }

    public void launchCrash(){
        if(ContextCompat.checkSelfPermission(this, "dadmc.practica35crash.CRASH_PERM") ==
                PackageManager.PERMISSION_GRANTED){
            Intent crash = new Intent();
            crash.setAction("dadmc.practica35crash.Crash.ACTION_CRASH");
            crash.addCategory("android.intent.category.DEFAULT");
            startActivity(crash);
        }else{
            Toast.makeText(getBaseContext(), "Esta aplicación necesita acceso a la aplicación " +
                    "crash", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch(requestCode){
            case PERMISO_CONTACTS:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.i(DEBUGTAG, "Read Contacts ha obtenido permiso");
                    txtContacts.setText(leeBookmarks());
                }else{
                    Log.i(DEBUGTAG, "Se ha denegado el permiso a Read Contacts");
                }
                break;
            case PERMISO_CRASH:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.i(DEBUGTAG, "Crash ha obtenido permiso");
                    launchCrash();
                }else{
                    Log.i(DEBUGTAG, "Se ha denegado el permiso a Crash");
                }
                break;
        }
    }

    private String leeBookmarks(){
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        String lista = "";
        while (phones.moveToNext()) {
            String name = null;
            name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = null;
            phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            lista += name + "\n" + phoneNumber + "\n\n";
        }
        phones.close();
        return lista;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practica3_5);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initLayoutComponents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_practica3_5, menu);
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
}
