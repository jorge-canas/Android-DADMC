package dadmc.practica34;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class Fragment2 extends AppCompatActivity implements Mostrador.MostradorInteractionListener {
    private Mostrador mos;
    private TextView txtPoem;
    private static final String DEBUGTAG = "Practica3_4_Fragment2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment2);
        initLayoutComponents();
    }

    public void initLayoutComponents(){
        mos = (Mostrador) getFragmentManager().findFragmentById(R.id.mostrador);
        txtPoem = (TextView) mos.getView().findViewById(R.id.txtPoem);
    }

    @Override
    public void setPoem(String poem){
        Log.i(DEBUGTAG, "setPoem");
        String text = getIntent().getStringExtra("text");
        Log.i(DEBUGTAG, text);
        txtPoem.setText(text);
    }

}
