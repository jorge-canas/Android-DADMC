package dadmc.practica33;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class VerURL extends AppCompatActivity {

    private static final String DEBUGTAG = "Practica3_3_VerURL";

    TextView TxTvURL;

    public void initLayoutComponents(){
        TxTvURL = (TextView) findViewById(R.id.TxTvURL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(DEBUGTAG, "onCreate");
        setContentView(R.layout.activity_ver_url);

        initLayoutComponents();

        setURL();
    }

    public void setURL(){
        Log.i(DEBUGTAG, "set URL");

        Uri data;
        if((data = getIntent().getData()) != null) {
            Log.i(DEBUGTAG, "Dato recogido "+data);
            TxTvURL.setText(data.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ver_url, menu);
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
