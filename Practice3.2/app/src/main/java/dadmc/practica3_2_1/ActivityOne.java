package dadmc.practica3_2_1;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class ActivityOne extends AppCompatActivity {

    private TextView edTxTonCreate;
    private int onCreateCount = 0;

    private TextView edTxTonStart;
    private int onStartCount = 0;

    private TextView edTxTonResume;
    private int onResumeCount = 0;

    private TextView edTxTonRestart;
    private int onRestartCount = 0;


    private Button btnActivityOne;

    private static final String DEBUGTAG = "Practica3_2_ActivityOne";

    public void initLayoutComponents(){
        Log.i(DEBUGTAG, "Comienza inicializando");

        edTxTonCreate = (TextView) findViewById(R.id.edTxTonCreate);
        edTxTonStart = (TextView) findViewById(R.id.edTxTonStart);
        edTxTonResume = (TextView) findViewById(R.id.edTxTonResume);
        edTxTonRestart = (TextView) findViewById(R.id.edTxTonRestart);

        Resources res = getResources();

        btnActivityOne = (Button) findViewById(R.id.btnActivityOne);
        btnActivityOne.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnActivityOneClick();
            }
        });

        Log.i(DEBUGTAG, "Termina inicializando");
    }

    public void btnActivityOneClick(){
        Log.i(DEBUGTAG, "Button one click");
        Intent a2 = new Intent(getBaseContext(), ActivityTwo.class);
        startActivity(a2);
    }

    public void reloadMessages(){
        Log.i(DEBUGTAG, "Reload messages");
        Resources res = getResources();
        edTxTonCreate.setText(String.format(res.getString(R.string.edTxTonCreate), onCreateCount));
        edTxTonStart.setText(String.format(res.getString(R.string.edTxTonStart), onStartCount));
        edTxTonResume.setText(String.format(res.getString(R.string.edTxTonResume), onResumeCount));
        edTxTonRestart.setText(String.format(res.getString(R.string.edTxTonRestart), onRestartCount));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(DEBUGTAG, "onCrete");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_one);
        initLayoutComponents();

        if(savedInstanceState != null){
            onRestoreInstanceState(savedInstanceState);
        }

        onCreateCount++;
        reloadMessages();
    }

    @Override
    public void onStart(){
        Log.i(DEBUGTAG, "onStart");
        super.onStart();
        onStartCount++;
        reloadMessages();
    }

    @Override
    public void onResume(){
        Log.i(DEBUGTAG, "onResume");
        super.onResume();
        onResumeCount++;
        reloadMessages();
    }

    @Override
    public void onRestart(){
        Log.i(DEBUGTAG, "onRestart");
        super.onRestart();
        onRestartCount++;
        reloadMessages();
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        Log.i(DEBUGTAG, "onSaveInstanceState");
        state.putInt("onCreateCount", onCreateCount);
        state.putInt("onStartCount", onStartCount);
        state.putInt("onResumeCount", onResumeCount);
        state.putInt("onRestartCount", onRestartCount);
    }

    @Override
    public void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        Log.i(DEBUGTAG, "onRestoreInstanceState");
        onCreateCount = state.getInt("onCreateCount");
        onStartCount = state.getInt("onStartCount");
        onResumeCount = state.getInt("onResumeCount");
        onRestartCount = state.getInt("onRestartCount");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_one, menu);
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
