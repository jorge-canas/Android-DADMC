package dadmc.practica6_1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String DEBUGTAG = "Prac6p1";
    private Uri empleadosUri = Uri.parse("content://dadmc.practica6_1/empleado");

    private TextView tvCodeEmp;
    private EditText etName;
    private EditText etLastname;
    private Button btnBirthday;
    private EditText etSalary;

    private Button btnNumberOf;

    private List<DatosEmpleado> datosEmpleados;
    private List<EditText> etList;
    private int index;
    private String option = "";
    private LinearLayout llChoice;
    private LinearLayout llNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initLayoutComponents();
    }

    public void initLayoutComponents(){
        tvCodeEmp = (TextView) findViewById(R.id.tvCodeEmp);
        etName  = (EditText) findViewById(R.id.etName);
        etLastname = (EditText) findViewById(R.id.etLastname);
        btnBirthday = (Button) findViewById(R.id.btnBirthday);
        etSalary  = (EditText) findViewById(R.id.etSalary);
        btnNumberOf = (Button) findViewById(R.id.btnNumberOf);
        llChoice = (LinearLayout) findViewById(R.id.llChoice);
        llNav = (LinearLayout) findViewById(R.id.llNav);

        datosEmpleados = new ArrayList<DatosEmpleado>();
        etList = new ArrayList<EditText>();
        etList.add(etName);
        etList.add(etLastname);
        etList.add(etSalary);

        btnBirthday.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog dp = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                btnBirthday.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dp.setMessage("Seleccione fecha");
                dp.setTitle("Selección de Fecha");
                dp.setCanceledOnTouchOutside(false);
                dp.show();
            }
        }
        );

        changeStatus(false);

        searchOption("ALL");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(DEBUGTAG, "Dato devuelto");
        Log.i(DEBUGTAG, "Valor resultCode " + resultCode);
        Log.i(DEBUGTAG, "Valor requestCode " + requestCode);
        Log.i(DEBUGTAG, "");
        if(resultCode == Activity.RESULT_OK && requestCode == StaticVar.GET_STRING_RESPONSE){
            Bundle response = data.getExtras();
            String responseTxT = response.getString("responseTxT");
            Log.i(DEBUGTAG, "Texto devuelto " + responseTxT);
            searchOption(responseTxT);
        }
    }

    public void searchOption(String responseTxT){
        datosEmpleados.clear();
        clearEditText();
        Cursor cur;
        switch(responseTxT){
            case "ALL":
            cur = getContentResolver().query(empleadosUri,
                    null, //Columnas a devolver (null son todas)
                    null, //where de la query
                    null, //Argumentos variables de la query (valor de los parámetros ? de la query
                    null);//Orden de los resultados
                break;

            default:
                cur = getContentResolver().query(empleadosUri,
                        null, //Columnas a devolver (null son todas)
                        responseTxT, //where de la query
                        null, //Argumentos variables de la query (valor de los parámetros ? de la query
                        null);//Orden de los resultados
                break;
        }

        try{
            while (cur.moveToNext()) {
                datosEmpleados.add(
                        new DatosEmpleado(
                                cur.getInt(cur.getColumnIndex("_id")),
                                cur.getString(cur.getColumnIndex("name")),
                                cur.getString(cur.getColumnIndex("lastName")),
                                cur.getString(cur.getColumnIndex("birthday")),
                                cur.getString(cur.getColumnIndex("salary"))
                        )
                );
            }
            cur.close();
        }catch (Exception e){
            Log.i(DEBUGTAG, "error"+e.getMessage());
        }

        if (datosEmpleados.size() == 0){
            updateNumber(0);
            index = 0;
        }else{
            updateNumber(1);
            updateEmployee(0);
            index = 1;
        }
    }

    public void changeStatus(boolean status){
        for (int i = 0; i < etList.size(); i++){
            etList.get(i).setFocusable(status);
            etList.get(i).setFocusableInTouchMode(status);
            etList.get(i).setClickable(status);
            etList.get(i).setEnabled(status);
            btnBirthday.setClickable(status);
            if (status){
                llNav.setVisibility(View.INVISIBLE);
                llChoice.setVisibility(View.VISIBLE);
            }else{
                llNav.setVisibility(View.VISIBLE);
                llChoice.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void updateNumber(int n){btnNumberOf.setText(n + " de " + datosEmpleados.size());}

    public void updateEmployee(int n){
        Log.i(DEBUGTAG, "Index "+n+" Code "+datosEmpleados.get(n).code);
        tvCodeEmp.setText(String.valueOf(datosEmpleados.get(n).code));
        etName.setText(datosEmpleados.get(n).name);
        etLastname.setText(datosEmpleados.get(n).lastName);
        btnBirthday.setText(datosEmpleados.get(n).birthday);
        etSalary.setText(datosEmpleados.get(n).salary);
    }

    public void btnFirstClick(View v){
        if (v.getId() == R.id.btnFirst){
            if (datosEmpleados.size() > 0) {
                updateNumber(1);
                updateEmployee(0);
                index = 0;
            }
        }
    }

    public void btnBackClick(View v){
        if (v.getId() == R.id.btnBack){
            if(index > 1){
                index--;
                updateNumber(index);
                updateEmployee(index - 1);
            }
        }
    }

    public void btnNextClick(View v){
        if (v.getId() == R.id.btnNext){
            if(index < datosEmpleados.size()){
                index++;
                updateNumber(index);
                updateEmployee(index - 1);
            }
        }
    }

    public void btnLastClick(View v){
        if (v.getId() == R.id.btnLast){
            if (datosEmpleados.size() > 0) {
                index = datosEmpleados.size();
                updateNumber(index);
                updateEmployee(index - 1);
            }
        }
    }

    public void btnAddClick(View v){
        if (v.getId() == R.id.btnAdd){
            changeStatus(true);
            clearEditText();
            option = "ADD";
        }
    }

    private void clearEditText(){
        for (int i = 0; i < etList.size(); i++) {
            etList.get(i).setText("");
        }
        btnBirthday.setText("");
    }

    public void btnUpdateClick(View v){
        if (v.getId() == R.id.btnUpdate){
            changeStatus(true);
            option = "EDIT";
        }
    }

    public void btnDeleteClick(View v){
        if (v.getId() == R.id.btnDelete){
            if (datosEmpleados.size() > 0){
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Dialogo de confirmación")
                        .setMessage("Seguro que desea eliminarlo?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getContentResolver().delete(empleadosUri, "_id=?",
                                        new String[]{String.valueOf(tvCodeEmp.getText().toString())});
                                datosEmpleados.remove(index--);
                                dialog.dismiss();
                                if (datosEmpleados.size() > 0) {
                                    if (index > 0) {
                                        updateEmployee(index - 1);
                                    } else {
                                        updateEmployee(0);
                                        index = 1;
                                    }
                                }
                                updateNumber(index);
                            }
                        })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        }
    }

    public void btnSelectClick(View v){
        if (v.getId() == R.id.btnSelect){
            Intent consultActivity = new Intent(getBaseContext(), ConsultActivity.class);
            startActivityForResult(consultActivity, StaticVar.GET_STRING_RESPONSE);
        }
    }

    public void btnAcceptClick(View v){
        if (v.getId() == R.id.btnAccept){
            ContentValues values = new ContentValues();
            switch (option){
                case "ADD":
                    boolean hasValues = false;
                    if (!etName.getText().toString().equals("")){
                        values.put("name", etName.getText().toString());
                        hasValues = true;
                    }
                    if (!etLastname.getText().toString().equals("")){
                        values.put("lastName", etLastname.getText().toString());
                        hasValues = true;
                    }
                    if (!btnBirthday.getText().toString().equals("")){
                        values.put("birthday", btnBirthday.getText().toString());
                        hasValues = true;
                    }
                    if (!etSalary.getText().toString().equals("")){
                        values.put("salary", etSalary.getText().toString());
                        hasValues = true;
                    }
                    if (hasValues){
                        Uri lastInsert = getContentResolver().insert(empleadosUri, values);
                        Log.i(DEBUGTAG, "Codigo " + lastInsert.getLastPathSegment());
                        datosEmpleados.add(
                                new DatosEmpleado(
                                        Integer.valueOf(lastInsert.getLastPathSegment()),
                                        etName.getText().toString(),
                                        etLastname.getText().toString(),
                                        btnBirthday.getText().toString(),
                                        etSalary.getText().toString()
                                )
                        );
                        index = datosEmpleados.size();
                        Log.i(DEBUGTAG, "Numero de empleados "+index);
                        updateNumber(index);
                        updateEmployee(index - 1);
                    }

                    break;

                case "EDIT":
                    values.put("name", etName.getText().toString());
                    values.put("lastName", etLastname.getText().toString());
                    values.put("birthday", btnBirthday.getText().toString());
                    values.put("salary", etSalary.getText().toString());

                    datosEmpleados.get(index - 1).name = etName.getText().toString();
                    datosEmpleados.get(index - 1).lastName = etLastname.getText().toString();
                    datosEmpleados.get(index - 1).birthday = btnBirthday.getText().toString();
                    datosEmpleados.get(index - 1).salary = etSalary.getText().toString();

                    getContentResolver().update(
                            empleadosUri, values, "_id=?",
                            new String[]{String.valueOf(tvCodeEmp.getText().toString())});
                    break;
            }
            changeStatus(false);
        }
    }

    public void btnCancelClick(View v){
        if (v.getId() == R.id.btnCancel){
            if(datosEmpleados.size() > 0){
                switch (option){
                    case "ADD":
                        updateNumber(1);
                        updateEmployee(0);
                        break;
                    case "EDIT":
                        updateEmployee(index-1);
                        break;
                }
            }
            changeStatus(false);
        }
    }

    /////////////////// Auto creado ///////////////////////////////////////

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

    ///////////////////////////////////////////////// basura /////////////////////////////

    public void updateEmpleadoView(String _id, String nombre, String apellidos, String fechaNac,
                                   String salario){

    }

    //mostrar datos almacenados
    private void getData(int _id) {
        ContentResolver cr = getContentResolver();

        //Consulta
        Cursor cur = cr.query(empleadosUri,
                null, //Columnas a devolver (null son todas)
                "_id = " + _id, //where de la query
                null, //Argumentos variables de la query (valor de los parámetros ? de la query
                null);//Orden de los resultados

        while (cur.moveToNext()) {
            updateEmpleadoView(
                    cur.getString(cur.getColumnIndex("_id")),
                    cur.getString(cur.getColumnIndex("name")),
                    cur.getString(cur.getColumnIndex("lastName")),
                    cur.getString(cur.getColumnIndex("birthday")),
                    cur.getString(cur.getColumnIndex("salary"))
            );
        }
        cur.close();
    }


}
