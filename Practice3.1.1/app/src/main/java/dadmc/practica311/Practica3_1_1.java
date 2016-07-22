package dadmc.practica311;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class Practica3_1_1 extends AppCompatActivity {

    private static final String DEBUGTAG = "Practica3_1_Activity";

    private TextView messageCountText;
    private TextView messageArrayText;
    private Button pushButton;
    private EditText messageEditText;
    private int messageCount;
    private ArrayList<String> messages;

    public void initLayoutComponents(){
        Log.v(DEBUGTAG, "Comienza inicializando");
        messageCountText = (TextView) findViewById(R.id.messageCountText);
        messageArrayText = (TextView) findViewById(R.id.messageArrayText);
        messageEditText = (EditText) findViewById(R.id.messageEditText);
        messageCount = 0;
        messages = new ArrayList<String>();
        Resources res = getResources();

        pushButton = (Button) findViewById(R.id.pushButton);
        pushButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pushButtonClick();
            }
        });

        Log.v(DEBUGTAG, "Termina inicializando");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practica3_1_1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initLayoutComponents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_practica3_1_1, menu);
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
    public void pushButtonClick(){
        String s = messageArrayText.getText().toString() + " hola";
        messageArrayText.setText(s);
    }
    */

    public void pushButtonClick(){
        if(!messageEditText.getText().toString().equals("")){
            Resources res = getResources();
            messageCount++;
            String messageEditString = messageEditText.getText().toString();
            String newText = "";
            messageCountText.setText(res.getQuantityString(R.plurals.messageCountText, messageCount, messageCount));

            for(int i = 0; i < messages.size(); i++){
                newText += String.format(res.getString(R.string.messageArrayTextMens), i+1) +
                        messages.get(i) + "<br />";
            }
            newText += String.format(res.getString(R.string.messageArrayTextMens), messageCount)
                    + messageEditString;
            messages.add(messageEditString);
            CharSequence styledText = Html.fromHtml(newText);
            messageArrayText.setText(styledText);
        }
    }
}
