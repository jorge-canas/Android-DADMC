package dadmc.pract4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private final String DEBUGTAG = "Pract4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void btnColorSelectorClick(View v){
        if (v.getId() == R.id.btnColorSelector){
            Intent i = new Intent(getBaseContext(), ColorSelector.class);
            startActivity(i);
        }
    }

    public void btnFormClick(View v){
        if (v.getId() == R.id.btnForm){
            Intent i = new Intent(getBaseContext(), Form.class);
            startActivity(i);
        }
    }

    public void btnSudokuClick(View v){
        if (v.getId() == R.id.btnSudoku){
            Log.d(DEBUGTAG, "clicked on ");
            Intent intent = new Intent(getBaseContext(), Game.class);
            startActivity(intent);
        }
    }

    /////////////////////////////////Auto generado
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
}
