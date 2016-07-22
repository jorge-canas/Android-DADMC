package dadmc.practica33app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;


public class ActivityLauncher extends AppCompatActivity {

    Button btnExplicitActivity;
    Button btnImplicitActivity;
    TextView TxTvActivityResponse;
    private static final String DEBUGTAG = "Practica3_3_App";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(DEBUGTAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_launcher);
        initLayoutComponents();
    }

    public void initLayoutComponents(){
        Log.i(DEBUGTAG, "Comienza inicialización");
        btnExplicitActivity = (Button) findViewById(R.id.btnExplicitActivity);
        btnImplicitActivity =(Button) findViewById(R.id.btnImplicitActivity);
        TxTvActivityResponse = (TextView) findViewById(R.id.TxTvActivityResponse);

        btnExplicitActivity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnExplicitActivityClick();
            }
        });

        btnImplicitActivity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnImplicitActivityClick();
            }
        });

        Log.i(DEBUGTAG, "Termina inicialización");
    }

    public void btnExplicitActivityClick(){
        Log.i(DEBUGTAG, "Botón actividad explícita");
        //lanzar segunda actividad y esperar resultado
        Intent explicitActivity = new Intent(getBaseContext(), ExplicitActivity.class);
        startActivityForResult(explicitActivity, StaticVar.GET_STRING_RESPONSE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(DEBUGTAG, "Dato devuelto");
        Log.i(DEBUGTAG, "Valor resultCode "+resultCode);
        Log.i(DEBUGTAG, "Valor requestCode "+requestCode);
        Log.i(DEBUGTAG, "");
        if(resultCode == Activity.RESULT_OK && requestCode == StaticVar.GET_STRING_RESPONSE){
            Bundle response = data.getExtras();
            String responseTxT = response.getString("responseTxT");
            Log.i(DEBUGTAG, "Texto devuelto "+responseTxT);
            TxTvActivityResponse.setText(responseTxT);
        }
    }

    public void btnImplicitActivityClick(){
        Log.i(DEBUGTAG, "Botón actividad implícita");
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.uhu.es"));
        String title = getResources().getString(R.string.chooser_title);
        Intent chooser = Intent.createChooser(sendIntent, title);
        //sendIntent.putExtra("URL", webpage);
        if(sendIntent.resolveActivity(getPackageManager()) != null){
            startActivity(chooser);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_launcher, menu);
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
