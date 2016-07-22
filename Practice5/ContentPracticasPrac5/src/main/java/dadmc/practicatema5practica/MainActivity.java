package dadmc.practicatema5practica;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private final String DEBUGTAG = "Content prov Practica";
    private final String[] markOptions = {"Suspenso", "Aprobado", "Notable", "Sobresaliente",
            "Matrícula"};

    //Referencias a los controles
    private Uri SubjectUri = Uri.parse("content://dadmc.practicatema5practica/asignaturas");
    private View.OnClickListener addListener;
    private View.OnClickListener deleteListener;
    private View.OnClickListener updateListener;
    private View.OnClickListener datePicker;
    private View.OnClickListener marksPicker;
    private EditText edNewSubject;
    private TextView tvNewSubject;

    private String pickerData = "";
    private View pickerDataBtn;

    private LinearLayout llResult;
    private TextView tvSubject;

    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initLayoutComponent();
    }

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

    private void initLayoutComponent() {

        edNewSubject = (EditText) findViewById(R.id.edNewSubject);
        llResult = (LinearLayout) findViewById(R.id.llResult);
        btnAdd = (Button) findViewById(R.id.btnAdd);

        datePicker = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerDataBtn = v;
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog dp = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                pickerData = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dp.setMessage("Seleccione fecha");
                dp.setTitle("Selección de Fecha");
                dp.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        setBtnText();
                        Log.i(DEBUGTAG, "setText button fecha");
                        pickerData = "";
                    }
                });
                dp.setCanceledOnTouchOutside(false);
                dp.show();
            }
        };

        marksPicker = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerDataBtn = v;
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Elija una opción")
                        .setSingleChoiceItems(markOptions, 1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pickerData = markOptions[which];
                            }
                        })
                        .setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                setBtnText();
                                Log.i(DEBUGTAG, "setText button nota");
                                pickerData = "";
                            }
                        })
                        .show();
            }
        };

        addListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(DEBUGTAG, "Boton añadir pulsado");
                if (edNewSubject.getText().toString().trim().length() > 0) {
                    ContentValues values = new ContentValues(); //debemos expresar parejas "nombre", "valor"
                    values.put("name", edNewSubject.getText().toString());

                    Uri lastInsert = getContentResolver().insert(SubjectUri, values);

                    //getData();
                    addLine(lastInsert.getLastPathSegment() ,edNewSubject.getText().toString(),"","");
                    Log.i(DEBUGTAG, "Ultimo id: "+lastInsert.getLastPathSegment());
                }
            }
        };

        btnAdd.setOnClickListener(addListener);

        updateListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues(); //debemos expresar parejas "nombre", "valor"

                LinearLayout ll = (LinearLayout) v.getParent();

                //Log.i(DEBUGTAG, String.valueOf(ll.getChildCount()));
                TextView tvName = (TextView) ll.getChildAt(1);
                values.put("name", tvName.getText().toString());

                Button btnDate = (Button) ll.getChildAt(2);
                values.put("date", btnDate.getText().toString());

                Button btnMarks = (Button) ll.getChildAt(3);
                values.put("marks", btnMarks.getText().toString());

                getContentResolver().update(
                        SubjectUri, values, "_id=?",
                        new String[]{String.valueOf(((TextView) ll.getChildAt(0)).getText().toString())});
                //getData();
            }
        };

        deleteListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout ll = (LinearLayout) v.getParent();
                getContentResolver().delete(SubjectUri, "_id=?",
                        new String[]{String.valueOf(((TextView) ll.getChildAt(0)).getText().toString())});
                ll.setVisibility(LinearLayout.GONE);
                //getData();
            }
        };

        //Mostrar datos almacenados
        getData();
    }

    //mostrar datos almacenados
    private void getData() {
        llResult.removeAllViews();
        ContentResolver cr = getContentResolver();

        //Consulta
        Cursor cur = cr.query(SubjectUri,
                null, //Columnas a devolver (null son todas)
                null, //where de la query
                null, //Argumentos variables de la query (valor de los parámetros ? de la query
                null);//Orden de los resultados

        //Actualizar layout
        while (cur.moveToNext()) {
            addLine(cur.getString(cur.getColumnIndex("_id")),
                    cur.getString(cur.getColumnIndex("name")),
                    cur.getString(cur.getColumnIndex("date")),
                    cur.getString(cur.getColumnIndex("marks"))
            );
        }
    }

    private void addLine(String _id, String _name, String _date, String _marks){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);

        TextView id = new TextView(getBaseContext());
        id.setLayoutParams(params);

        TextView name = new TextView(getBaseContext());
        name.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 3.0f));

        Button date = new Button(getBaseContext());
        date.setLayoutParams(params);
        date.setOnClickListener(datePicker);

        Button marks = new Button(getBaseContext());
        marks.setLayoutParams(params);
        marks.setOnClickListener(marksPicker);

        Button btnUpdate = new Button(getBaseContext());
        btnUpdate.setLayoutParams(params);
        btnUpdate.setText("Guardar");
        btnUpdate.setOnClickListener(updateListener);

        Button btnDelete = new Button(getBaseContext());
        btnDelete.setLayoutParams(params);
        btnDelete.setText("Borrar");
        btnDelete.setOnClickListener(deleteListener);

        LinearLayout llData = new LinearLayout(getApplication());

        id.setText(_id); //igual que poner cur.getString(1);
        name.setText(_name);
        date.setText(_date);
        marks.setText(_marks);

        llData.addView(id);
        llData.addView(name);
        llData.addView(date);
        llData.addView(marks);

        llData.addView(btnUpdate);
        llData.addView(btnDelete);
        llData.setTag(String.valueOf(_id));

        llResult.addView(llData);
    }

    private void setBtnText(){
        ((Button)pickerDataBtn).setText(pickerData);
    }

}
