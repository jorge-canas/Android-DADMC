package dadmc.pract4;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ColorSelector extends AppCompatActivity {

    private EditText etRed;
    private EditText etGreen;
    private EditText etBlue;
    private Button btnChange;
    private View.OnClickListener btnClickEvent;
    private View.OnFocusChangeListener etFocusEvent;
    private final String DEBUGTAG = "ColorSelector_main";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_selector);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initLayoutComponent();
    }

    private void initLayoutComponent(){
        etRed = (EditText) findViewById(R.id.etRed);
        etGreen = (EditText) findViewById(R.id.etGreen);
        etBlue = (EditText) findViewById(R.id.etBlue);
        btnChange = (Button) findViewById(R.id.btnChange);
        btnClickEvent = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                changeColor();
            }
        };
        btnChange.setOnClickListener(btnClickEvent);

        etRed.addTextChangedListener(new TextValidator(etRed));
        etGreen.addTextChangedListener(new TextValidator(etGreen));
        etBlue.addTextChangedListener(new TextValidator(etBlue));
    }

    public void changeColor(){
        int red = Integer.valueOf(etRed.getText().toString());
        int blue = Integer.valueOf(etBlue.getText().toString());
        int green = Integer.valueOf(etGreen.getText().toString());
        if((red >= 0 || red <=255) && (green >= 0 || green <=255) && (blue >= 0 || blue <=255)){
            findViewById(R.id.llMain).setBackgroundColor(Color.rgb(red, green, blue));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_color_selector, menu);
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

    /*
    public void handleClicks(View clickedView){
       if(clickedView.getId() == btn1.getId(){
           ...
       } else if (...){}
    }
    * */
}
