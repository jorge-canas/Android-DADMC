package dadmc.practica33app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ExplicitActivity extends AppCompatActivity {

    private TextView TxTvMessage;
    private EditText edTxTMessage;
    private Button btnResponse;
    private static final String DEBUGTAG = "Practica3_3_ExplicitAct";

    public void initLayoutComponents(){
        Log.i(DEBUGTAG, "Comienza inicialización");
        TxTvMessage = (TextView) findViewById(R.id.TxTvMessage);
        edTxTMessage = (EditText) findViewById(R.id.edTxTMessage);
        btnResponse = (Button) findViewById(R.id.btnResponse);

        btnResponse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnResponseClick();
            }
        });
        Log.i(DEBUGTAG, "Termina inicialización");
    }

    public void btnResponseClick(){
        Log.i(DEBUGTAG, "btn response click");
        Intent resultIntent = new Intent();
        Log.i(DEBUGTAG, "Valor editText "+edTxTMessage.getText().toString());
        resultIntent.putExtra("responseTxT", edTxTMessage.getText().toString());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(DEBUGTAG, "onCreate");
        setContentView(R.layout.explicit_activity);

        initLayoutComponents();

    }

}
