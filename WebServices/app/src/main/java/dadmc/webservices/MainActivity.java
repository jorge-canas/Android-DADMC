package dadmc.webservices;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {
    private static final int SET_TEMP = 10;
    private final String DEBUGTAG = "WebService";
    private final String[] tempValues =
            new String[] {"Celsius", "Fahrenheit", "Rankine", "Reaumur", "Kelvin"};
    private final String[] tempPOSTValue =
            new String[] {"degreeCelsius", "degreeFahrenheit", "degreeRankine", "degreeReaumur",
                    "kelvin"};
    private int tempOriginChoice = 0;
    private int tempDestinyChoice = 0;
    private int tempValue;
    private ArrayAdapter<String> adapter;
    private Spinner spinTempOrigin;
    private Spinner spinTempDestiny;
    //private TextView spinTempOriginLabel;
    //private TextView spinTempDestinyLabel;
    private EditText etValue;
    private TextView tvValueDestinyTxt;
    private TextView tvValueDestiny;

    private static Handler hdnl = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initLayoutComponents();
    }

    public void initLayoutComponents(){
        adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item,
                tempValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTempOrigin = (Spinner) findViewById(R.id.spinTempOrigin);
        spinTempOrigin.setAdapter(adapter);
        spinTempDestiny = (Spinner) findViewById(R.id.spinTempDestiny);
        spinTempDestiny.setAdapter(adapter);
        etValue = (EditText) findViewById(R.id.etValue);
        tvValueDestinyTxt = (TextView) findViewById(R.id.tvValueDestinyTxt);
        tvValueDestiny = (TextView) findViewById(R.id.tvValueDestiny);
        spinTempOrigin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(DEBUGTAG, "Spin origen " + position);
                tempOriginChoice = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinTempDestiny.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(DEBUGTAG, "Spin destiny " + position);
                tempDestinyChoice = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //Petición por HTTP POST
    public void btnConvertHTTPClick(View v){
        Log.i(DEBUGTAG, "btnConvertHTTPClick");
        if (v.getId() == R.id.btnConvertHTTP) {
            //Se coge el valor de la temperatura
            if (etValue.getText().toString().equals("")){
                tempValue = 0;
            }else{
                tempValue = Integer.valueOf(etValue.getText().toString());
            }
            //Se crea un hilo para que no se bloquee
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //Función que realiza la petición POST
                    final String temp = getTemp(tempValue, tempPOSTValue[tempOriginChoice],
                            tempPOSTValue[tempDestinyChoice]);
                    hdnl.post(new Runnable() {
                        @Override
                        public void run() {
                            tvValueDestiny.setText(temp);
                        }
                    });
                }
            }).start();
        }
    }

    //Dirección del servicio web
    private static final String URL_POST =
            "http://www.webserviceX.NET/ConvertTemperature.asmx/ConvertTemp";
    public String getTemp(int Temperature, String FromUnit, String ToUnit){
        Log.i(DEBUGTAG,"getTemp");
        URL url;
        HttpURLConnection connection = null;
        try{
            //Definición de los parámetros
            String urlParameters =
                    "Temperature="+ URLEncoder.encode(String.valueOf(Temperature), "UTF-8")+
                    "&FromUnit="+URLEncoder.encode(FromUnit,"UTF-8")+
                    "&ToUnit="+URLEncoder.encode(ToUnit,"UTF-8");
            Log.i(DEBUGTAG, urlParameters);
            //Se crea la conexión y se define la cabecera
            url = new URL(URL_POST);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));

            //Se define que se envía y recibe información
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Se envía la petición con los parámetros definidos
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.writeBytes (urlParameters);
            wr.flush ();
            wr.close ();

            //Se obtiene la respuesta, como devuelve un XML hay que parsearlo, para ello se crea
            //un lector DOM que lee la respuesta del servidor
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbilder = dbFactory.newDocumentBuilder();
            Document doc = dbilder.parse(is);
            //Como solo hay 1 elemento que es la temperatura se lee directamente
            String response = doc.getDocumentElement().getTextContent();
            rd.close();
            Log.i(DEBUGTAG, response);
            return response;

        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //Petición por SOAP 1.1
    //Definición de direcciones a usar
    private final String NAMESPACE = "http://www.webserviceX.NET/";
    private final String METHOD_NAME = "ConvertTemp";
    private final String URL = "http://www.webservicex.net/ConvertTemperature.asmx";
    private final String SOAP_ACTION = "http://www.webserviceX.NET/ConvertTemp";

    public void btnConvertSOAPClick(View v){
        if (v.getId() == R.id.btnConvertSOAP){
            Log.i(DEBUGTAG, "btnConvertSOAPClick");
            if (etValue.getText().toString().equals("")){
                tempValue = 0;
            }else{
                tempValue = Integer.valueOf(etValue.getText().toString());
            }
            //Preparación de parámetros para el asynctask y SOAP
            Object params[] = {
                    tempValue,
                    tempPOSTValue[tempOriginChoice],
                    tempPOSTValue[tempDestinyChoice]
                    };
            for (int i = 0; i < params.length; i++){
                Log.d(DEBUGTAG, String.valueOf(params[i]));
            }
            TareaAsincronaWS tareaAsinc = new TareaAsincronaWS();
            tareaAsinc.execute(params);
        }
    }

    //////////////////////////// Tarea asincrona
    public class TareaAsincronaWS extends AsyncTask<Object, Void, String> {
        final String DEBUGTAG = "Pract8_2";

        @Override
        protected String doInBackground(Object... params) {
            Log.i(DEBUGTAG, "doInBackground");
            try {
                //Creación del objeto Soap
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                //Definición de propiedades temperatura, Unidad origen y destino
                PropertyInfo Temperature = new PropertyInfo();
                Temperature.setName("Temperature");
                Temperature.setValue(String.valueOf(params[0]));
                Temperature.setType(Double.class);
                request.addProperty(Temperature);

                PropertyInfo FromUnit = new PropertyInfo();
                FromUnit.setName("FromUnit");
                FromUnit.setValue((String) params[1]);
                Log.i(DEBUGTAG, (String) params[1]);
                FromUnit.setType(String.class);
                request.addProperty(FromUnit);

                PropertyInfo ToUnit = new PropertyInfo();
                ToUnit.setName("ToUnit");
                ToUnit.setValue((String) params[2]);
                Log.i(DEBUGTAG, (String) params[2]);
                ToUnit.setType(String.class);
                request.addProperty(ToUnit);

                //Creación del sobre soap con la version 1.1
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                //Creación de la clase transporte y añadido del sobre Soap
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION, envelope);
                //Recoger respuesta del servidor y devolverla al onPostExecute
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                String webResponse = response.toString();
                Log.i(DEBUGTAG, webResponse);
                //tvValueDestiny.setText(webResponse);
                return webResponse;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(DEBUGTAG, "postExecute "+result);
            if (result != "") {
                tvValueDestiny.setText(result);
            } else {
                tvValueDestiny.setText("Error");
            }
        }
    }

    /////////////////////////Auto Generado

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
