package dadmc.practica6_1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class EmpleadosSQLiteHelper extends SQLiteOpenHelper {
     public EmpleadosSQLiteHelper(Context contexto, String nombre,
                               CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("Practicap6", "Tabla empleados");
        //aqui pondriamos las sentencias sql de LDD para creación de BD
        //Sentencia SQL para crear la tabla de empleados
        String sqlCreate = "CREATE TABLE Empleados (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " name TEXT, lastName TEXT, birthday TEXT, salary REAL, fechaMod INTEGER)";
        //Se ejecuta la sentencia SQL de creación de la tabla empleados
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, 
                          int versionNueva) {
        //aqui vendría el código de actualización de la B.D.
        // Este método es llamado cuando la version nueva es distinta de la anterior
        //Supongamos que en la versión 2 deseamos añadir la columna email a empleados

    }
}

