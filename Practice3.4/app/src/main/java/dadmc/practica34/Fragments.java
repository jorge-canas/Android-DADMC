package dadmc.practica34;

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
import android.widget.Button;
import android.widget.TextView;

public class Fragments extends AppCompatActivity implements Selector.OnFragmentInteractionListener,
        Mostrador.MostradorInteractionListener {

    //private Button btnBecker;
    //private Button btnMachado;
    //private Button btnQuevedo;
    private Selector sel;
    private Mostrador mos;
    private TextView txtPoem;
    private static final String DEBUGTAG = "Practica3_4_Fragment1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments);
        initLayoutComponents();
    }

    public void initLayoutComponents(){
        sel = (Selector) getFragmentManager().findFragmentById(R.id.selector);
        //btnBecker = (Button) sel.getView().findViewById(R.id.btnBecker);
        //btnMachado = (Button) sel.getView().findViewById(R.id.btnMachado);
        //btnQuevedo = (Button) sel.getView().findViewById(R.id.btnQuevedo);

        mos = (Mostrador) getFragmentManager().findFragmentById(R.id.mostrador);
        if(mos != null){
            txtPoem = (TextView) mos.getView().findViewById(R.id.txtPoem);
        }
    }

    @Override
    public void changeSelector(int option){
        Log.i(DEBUGTAG, "Change selector option "+option);
        Intent fragment2 = new Intent(getBaseContext(), Fragment2.class);
        Log.i(DEBUGTAG, "Change selector text " + getString(R.string.txtBecker));
        if (mos == null) {
            switch (option) {
                case 0:
                    fragment2.putExtra("text", getString(R.string.txtBecker));
                    break;
                case 1:
                    fragment2.putExtra("text", getString(R.string.txtMachado));
                    break;
                case 2:
                    fragment2.putExtra("text", getString(R.string.txtQuevedo));
                    break;
            }
            startActivity(fragment2);
        }else{
            switch (option) {
                case 0:
                    setPoem(getString(R.string.txtBecker));
                    break;
                case 1:
                    setPoem(getString(R.string.txtMachado));
                    break;
                case 2:
                    setPoem(getString(R.string.txtQuevedo));
                    break;
            }
        }
    }

    @Override
    public void setPoem(String poem){
        Log.i(DEBUGTAG, "setPoem");
        Log.i(DEBUGTAG, poem);
        txtPoem.setText(poem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fragments, menu);
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
