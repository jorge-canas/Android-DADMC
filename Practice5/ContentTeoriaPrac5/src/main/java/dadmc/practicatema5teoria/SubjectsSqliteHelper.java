package dadmc.practicatema5teoria;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Inmundus on 10/12/2015.
 */
public class SubjectsSqliteHelper extends SQLiteOpenHelper {

    //Sentencia SQL para crear la tabla de Empleados
    String sqlCreate = "CREATE TABLE Subjects " +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " name TEXT, " +
            " date TEXT, " +
            " marks TEXT )";

    public SubjectsSqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
