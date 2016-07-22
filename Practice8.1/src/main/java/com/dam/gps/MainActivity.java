package com.dam.gps;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity{
    private final static String DEBUGTAG="Prac8";

    private LocationManager locManager;
    private LocationListener locListener;

    private TextView lblLatitud;
    private TextView lblLongitud;
    private TextView lblPrecision;
    private TextView lblEstado;
    private TextView lblProv;
    private TextView lblProveedores;
    private TextView tvRoutePoints;

    private TextView tvLastDistanceText;
    private TextView tvTotalDistanceText;
    private double totalDistance;

    private List<TextView> listTv;

    private Location lastLoc = null;

    private RutasSQLiteHelper dbRutas;
    private SQLiteDatabase db;

    private static final String RUTAS_TABLE = "Ruta";
    private static final String PUNTOS_TABLE = "Puntos";

    private long lastRutaID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayoutComponents();
    }

    private void initLayoutComponents(){

        lblLatitud = (TextView)findViewById(R.id.LblPosLatitud);
        lblLongitud = (TextView)findViewById(R.id.LblPosLongitud);
        lblPrecision = (TextView)findViewById(R.id.LblPosPrecision);
        lblEstado = (TextView)findViewById(R.id.LblEstado);
        lblProv=(TextView)findViewById(R.id.LblProv);
        lblProveedores=(TextView)findViewById(R.id.LblProveedores);
        tvLastDistanceText = (TextView) findViewById(R.id.tvLastDistanceText);
        tvTotalDistanceText = (TextView) findViewById(R.id.tvTotalDistanceText);
        tvRoutePoints = (TextView) findViewById(R.id.tvRoutePoints);

        listTv = new ArrayList<TextView>();
        listTv.add(lblLatitud);
        listTv.add(lblLongitud);
        listTv.add(lblPrecision);
        listTv.add(lblEstado);
        listTv.add(lblProv);
        listTv.add(lblProveedores);
        listTv.add(tvLastDistanceText);
        listTv.add(tvTotalDistanceText);
        listTv.add(tvRoutePoints);

        dbRutas = new RutasSQLiteHelper(this, "RutasBD", null, 1); //el 1 indica la versión de la BD
        db = dbRutas.getWritableDatabase();

        //printPuntos();
    }

    //Comprobar los puntos de la ultima ruta
    private void printPuntos(){
        String sql="SELECT * FROM Puntos";
        Cursor c = db.rawQuery(sql, null);
        if (c!=null) {
            while(c.moveToNext()){
                Log.i(DEBUGTAG,
                        "_id "+c.getString(c.getColumnIndex("_id"))+
                        "_idRoute "+c.getString(c.getColumnIndex("_idRoute"))+
                        "long "+c.getString(c.getColumnIndex("long"))+
                        "lat "+c.getString(c.getColumnIndex("lat"))+"\n"
                );
            }
        }
    }

    private void printLastLocation(){
        lastRutaID = -1;
        String sql="SELECT _id, lastDistance, totalDistance, finish FROM Ruta";
        Cursor c = db.rawQuery(sql,null);
        if ((c!=null) && (c.moveToLast())) {
            lastRutaID  = c.getInt(0);
        }
        Log.i(DEBUGTAG, "Ultima localizacion: "+lastRutaID);
        if (lastRutaID != -1 && c.getInt(3) != 0){
            tvLastDistanceText.setText(String.valueOf(c.getDouble(1)));
            tvTotalDistanceText.setText(String.valueOf(c.getDouble(2)));

            //rellenar con los puntos de la ruta
            printPoints();
        }
        c.close();
    }

    public void printPoints(){
        String sql = "SELECT long, lat FROM Puntos WHERE _idRoute='"+lastRutaID+"'";
        Log.i(DEBUGTAG, "SQL: "+sql);
        Cursor c=db.rawQuery(sql,null);
        if (c!=null){
            tvRoutePoints.setText("");
            while(c.moveToNext()){
                tvRoutePoints.setText(
                        tvRoutePoints.getText().toString()+
                                "Longitud: "+c.getString(c.getColumnIndex("long"))+
                                ", Latitud: "+c.getString(c.getColumnIndex("lat"))+"\n"
                );
            }
        }
        c.close();
    }

    public void startLocation(View v) {
        if(v.getId() == R.id.BtnActualizar) {
            printLastLocation();
            ContentValues values = new ContentValues();
            values.put("lastDistance", 0);
            values.put("totalDistance", 0);
            values.put("finish", 1);
            lastRutaID = db.insert(RUTAS_TABLE, null, values);
            //Obtenemos una referencia al LocationManager
            locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            List<String> listaproveedores = locManager.getAllProviders();
            lblProveedores.setText("");
            for (int i = 0; i < listaproveedores.size(); i++) {
                LocationProvider provider = locManager.getProvider(listaproveedores.get(i));
                //Toast.makeText(MainActivity.this,provider.getName(),Toast.LENGTH_LONG);
                lblProveedores.append(provider.getName() + " ");
            }

            //Obtenemos la ultima posicion conocida
            Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            //Mostramos la ultima posicion conocida
            mostrarPosicion(loc);

            //Nos registramos para recibir actualizaciones de la posicion
            locListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    mostrarPosicion(location);
                }

                public void onProviderDisabled(String provider) {
                    lblEstado.setText("Provider OFF");
                }

                public void onProviderEnabled(String provider) {
                    lblEstado.setText("Provider ON ");
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                    Log.i("", "Provider Status: " + status);
                    lblEstado.setText("Provider Status: " + status);
                }
            };

            //solicitamos al GPS actualizaciones de posición cada 5 segundos
            locManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 5000, 0.5f, locListener);
        }
    }

    public void stopLocation(View v){
        if(v.getId() == R.id.BtnDesactivar){
            //actualizar ruta
            String sql = "UPDATE Ruta SET finish='0' WHERE _id='"+lastRutaID+"'";
            db.rawQuery(sql, null);
            locManager.removeUpdates(locListener);
            for(int i = 0; i < listTv.size(); i++){
                listTv.get(i).setText("");
            }
        }
    }

    private void mostrarPosicion(Location loc) {
        if(loc != null) {
            if(lastLoc != null){
                lblLatitud.setText("Latitud: " + String.valueOf(loc.getLatitude()));
                lblLongitud.setText("Longitud: " + String.valueOf(loc.getLongitude()));
                lblPrecision.setText("Precision: " + String.valueOf(loc.getAccuracy()));
                lblEstado.setText("Provider Status: Provider ON ");
                Log.i(DEBUGTAG, String.valueOf(loc.getLatitude() + " - " + String.valueOf(loc.getLongitude())));
                if(lastDistance(lastLoc, loc)){
                    if(lastLoc.getLongitude() != loc.getLongitude() ||
                            lastLoc.getLatitude() != loc.getLatitude()){
                        //actualizar ruta en DB
                        Log.i(DEBUGTAG, "Agregar localizacion a la DB Puntos");
                        insertLocation(loc);
                        tvRoutePoints.setText(
                                tvRoutePoints.getText().toString()+
                                        "Longitud: "+loc.getLongitude()+
                                        ",  Latitud: "+loc.getLatitude()+"\n"
                        );
                    }

                }
                lastLoc = loc;
            }else{
                lastLoc = loc;
                lblLatitud.setText("Latitud: " + String.valueOf(loc.getLatitude()));
                lblLongitud.setText("Longitud: " + String.valueOf(loc.getLongitude()));
                lblPrecision.setText("Precision: " + String.valueOf(loc.getAccuracy()));
                insertLocation(loc);

                tvRoutePoints.setText(
                        tvRoutePoints.getText() + "Longitud: " +
                                loc.getLongitude() + ",  Latitud: " +
                                loc.getLatitude()
                );

                lblEstado.setText("Provider Status: Provider ON ");
                tvLastDistanceText.setText("0.00");
                tvTotalDistanceText.setText("0.00");
                Log.i("", String.valueOf(loc.getLatitude() + " - " + String.valueOf(loc.getLongitude())));
            }
        }
        else
        {
            lblLatitud.setText("Latitud: (sin_datos)");
            lblLongitud.setText("Longitud: (sin_datos)");
            lblPrecision.setText("Precision: (sin_datos)");
            lblEstado.setText("Provider OFF");
        }
    }

    private boolean lastDistance(Location lastLoc, Location actLoc){
        float loc = actLoc.distanceTo(lastLoc);
        if (loc < 100.0f){
            tvLastDistanceText.setText(String.valueOf(loc));
            totalDistance += loc;
            tvTotalDistanceText.setText(String.valueOf(totalDistance));
            return true;
        }else{
            tvLastDistanceText.setText("Posible fallo");
            return false;
        }
    }

    private void insertLocation(Location loc){
        ContentValues values = new ContentValues();
        values.put("_idRoute", lastRutaID);
        values.put("long", loc.getLongitude());
        values.put("lat", loc.getLatitude());
        db.insert(PUNTOS_TABLE, null, values);
    }

    @Override
    public void finish() {
        db.close();
        super.finish();
    }
}
