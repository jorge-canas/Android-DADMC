package dadmc.practica6_1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class ConsultActivity extends AppCompatActivity {
    private final String DEBUGTAG = "Prac6p1";
    private final String[] searchOptions = {"<", "<=", "=", ">=", ">"};

    private Button btnSearchSalaryOption;
    private EditText etSearchSalaryOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initLayoutComponents();
    }

    public void initLayoutComponents(){

        etSearchSalaryOption = (EditText) findViewById(R.id.etSearchSalaryOption);
        btnSearchSalaryOption = (Button) findViewById(R.id.btnSearchSalaryOption);
        btnSearchSalaryOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ConsultActivity.this)
                        .setTitle("Elija una opción")
                        .setSingleChoiceItems(searchOptions, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                btnSearchSalaryOption.setText(searchOptions[which]);
                            }
                        })
                        .show();
            }
        });

    }

    public void btnSearchBackClick(View v){
        Log.i(DEBUGTAG, "btn response click");
        if (v.getId() == R.id.btnSearchBack){
            Intent resultIntent = new Intent();
            String response = "salary"+btnSearchSalaryOption.getText().toString() +"'"+
                    etSearchSalaryOption.getText().toString()+"'";
            resultIntent.putExtra("responseTxT", response);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }

    public void btnSearchResetClick(View v){
        Log.i(DEBUGTAG, "btn reset click");
        if (v.getId() == R.id.btnSearchReset){
            Intent resultIntent = new Intent();
            resultIntent.putExtra("responseTxT", "ALL");
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }

}
