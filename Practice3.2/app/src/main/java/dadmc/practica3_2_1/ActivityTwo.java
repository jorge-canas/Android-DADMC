package dadmc.practica3_2_1;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityTwo extends AppCompatActivity {

    private TextView edTxTonCreate;
    private int onCreateCount = 0;

    private TextView edTxTonStart;
    private int onStartCount = 0;

    private TextView edTxTonResume;
    private int onResumeCount = 0;

    private TextView edTxTonRestart;
    private int onRestartCount = 0;

    private Button btnActivityTwo;

    private static final String DEBUGTAG = "Practica3_2_ActivityTwo";

    public void initLayoutComponents(){
        Log.i(DEBUGTAG, "Comienza inicializando");

        edTxTonCreate = (TextView) findViewById(R.id.edTxTonCreate);
        edTxTonStart = (TextView) findViewById(R.id.edTxTonStart);
        edTxTonResume = (TextView) findViewById(R.id.edTxTonResume);
        edTxTonRestart = (TextView) findViewById(R.id.edTxTonRestart);

        Resources res = getResources();

        btnActivityTwo = (Button) findViewById(R.id.btnActivityTwo);
        btnActivityTwo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnActivityTwoClick();
            }
        });

        Log.i(DEBUGTAG, "Termina inicializando");
    }

    public void reloadMessages(){
        Log.i(DEBUGTAG, "Reload Messages");
        Resources res = getResources();
        edTxTonCreate.setText(String.format(res.getString(R.string.edTxTonCreate), onCreateCount));
        edTxTonStart.setText(String.format(res.getString(R.string.edTxTonStart), onStartCount));
        edTxTonResume.setText(String.format(res.getString(R.string.edTxTonResume), onResumeCount));
        edTxTonRestart.setText(String.format(res.getString(R.string.edTxTonRestart), onRestartCount));
    }

    public void btnActivityTwoClick(){
        Log.i(DEBUGTAG, "Button activity two clicked");
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(DEBUGTAG, "onCreate");
        setContentView(R.layout.activity_activity_two);
        initLayoutComponents();
        if(savedInstanceState != null){
            onRestoreInstanceState(savedInstanceState);
        }

        onCreateCount++;
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
    public void onStart(){
        super.onStart();
        Log.i(DEBUGTAG, "onStart");
        onStartCount++;
        reloadMessages();
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.i(DEBUGTAG, "onResume");
        onResumeCount++;
        reloadMessages();
    }

    @Override
    public void onRestart(){
        super.onRestart();
        Log.i(DEBUGTAG, "onRestart");
        onRestartCount++;
        reloadMessages();
    }

}
