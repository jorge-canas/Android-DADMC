package dadmc.practica6_1;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;


public class EmpleadosProvider extends ContentProvider {

    //CONTENT_URI
    private static final String myUri =
            "content://dadmc.practica6_1/empleado";  //uri de mi content provider
    public static final Uri CONTENT_URI = Uri.parse(myUri); //objeto estátitico de tipo Uri

    //constantes y variables para Base de datos
    private EmpleadosSQLiteHelper database;
    private static final String DB_NAME = "DBEmpleados";
    private static final int DB_VERSION = 1;
    private static final String EMPLEADOS_TABLE = "Empleados";

    //Necesario para UriMatcher
    private static final int ALL_EMPLEADOS = 1;
    private static final int ID_EMPLEADOS = 2;
    private static final UriMatcher uriMatcher;
    //Inicializamos el UriMatcher
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("dadmc.practica6_1", "empleado", ALL_EMPLEADOS);
        uriMatcher.addURI("dadmc.practica6_1", "empleado/#", ID_EMPLEADOS);
    }

    @Override
    public boolean onCreate() {
        database = new EmpleadosSQLiteHelper(getContext(), DB_NAME, null, DB_VERSION);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //Si es una consulta a un ID concreto construimos el WHERE
        String condicionwhere = selection;
        if(uriMatcher.match(uri) == ID_EMPLEADOS){
            condicionwhere = "_id=" + uri.getLastPathSegment();
        }

        SQLiteDatabase db = database.getWritableDatabase();

        Cursor c = db.query(EMPLEADOS_TABLE, projection, condicionwhere,
                selectionArgs, null, null, sortOrder);
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);
        switch (match)
        {
            case ALL_EMPLEADOS:
                return "vnd.android.cursor.dir/vnd.dadmc.empleado";
            case ID_EMPLEADOS:
                return "vnd.android.cursor.item/vnd.dadmc.empleado";
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long regId = 1;
        SQLiteDatabase db =  database.getWritableDatabase();

        regId = db.insert(EMPLEADOS_TABLE, null, values);
        Uri newUri = ContentUris.withAppendedId(CONTENT_URI, regId);

        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int cont;
        String whereCondition = selection; //Si es una consulta a un ID concreto construimos el WHERE
        if(uriMatcher.match(uri) == ID_EMPLEADOS){
            whereCondition = "_id=" + uri.getLastPathSegment();
        }
        SQLiteDatabase db = database.getWritableDatabase();
        cont = db.delete(EMPLEADOS_TABLE, whereCondition, selectionArgs);
        return cont;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int cont;

        //Si es una consulta a un ID concreto construimos el WHERE
        String whereCondition = selection;
        if(uriMatcher.match(uri) == ID_EMPLEADOS){
            whereCondition = "_id=" + uri.getLastPathSegment();
        }
        SQLiteDatabase db = database.getWritableDatabase();
        cont = db.update(EMPLEADOS_TABLE, values, whereCondition, selectionArgs);

        return cont;
    }
}