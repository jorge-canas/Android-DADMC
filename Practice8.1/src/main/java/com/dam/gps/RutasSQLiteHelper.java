package com.dam.gps;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
 
public class RutasSQLiteHelper extends SQLiteOpenHelper {
     public RutasSQLiteHelper(Context contexto, String name,
                               CursorFactory factory, int version) {
        super(contexto, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //aqui pondriamos las sentencias sql de LDD para creación de BD
        //Sentencia SQL para crear la tabla de rutas
        String sqlCreate = "CREATE TABLE Ruta (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "lastDistance REAL, totalDistance REAL , finish INTEGER)";
        //Se ejecuta la sentencia SQL de creación de la tabla rutas
        db.execSQL(sqlCreate);

        sqlCreate = "CREATE TABLE Puntos (_id INTEGER PRIMARY KEY AUTOINCREMENT, _idRoute INTEGER," +
                "long REAL, lat REAL, FOREIGN KEY(_idRoute) REFERENCES Ruta(_id) " +
                "ON DELETE CASCADE)";
        //Se ejecuta la sentencia SQL de creación de la tabla puntos
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior,
                          int versionNueva) {

    }
}

