package com.dam.gps;


import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DBConnectionSql {

    public ResultSet resultado;
    StrictMode.ThreadPolicy old;
    private Exception exception;
    public Connection Conexion = null;
    private Statement stmt;
    private String servidor;
    private String instancia;
    private String base;
    private String usuario;
    private String clave;
    private String url;

    public boolean connect(String servidor, String instancia, String base, String usuario, String clave) {
        this.servidor = servidor;
        this.instancia = instancia;
        this.base = base;
        this.usuario = usuario;
        this.clave = clave;

        this.url = "jdbc:jtds:sqlserver://" + servidor + "/" + base +
                ";encrypt=false;user=" + usuario + ";password=" + clave + ";";

        if (!instancia.equals("")) {
            this.url += "instance=" + instancia + ";";
        }

        // Deshabilitamos el modo estricto para conexiones externas temporalmente
        old = StrictMode.getThreadPolicy();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
                .permitNetwork()
                .build());

        // Nos conectamos a la base de datos
        try {
            String driver = "net.sourceforge.jtds.jdbc.Driver"; //driver de la conexión
            Class.forName(driver).newInstance(); //añadimos dicho driver para su uso
            DriverManager.setLoginTimeout(5); //timeout para la conexión
            Conexion = DriverManager.getConnection(url, usuario, clave);

        } catch (Exception e) {
            Log.w("Error connection", "" + e.getMessage());
            return false;
        }
        return (Conexion != null);
    }

    public boolean iniciartransaccion()
    {
        try {
            Conexion.setAutoCommit(false);
            return true;
        }
        catch (Exception e) {
            try {
                Conexion.rollback();
                Conexion.setAutoCommit(true);
            }
            catch (Exception e2)
            {
                Log.v("No puedo deshacer",e.getMessage());
            }
            return false;
        }
    }
    public boolean cerrartransaccionok()
    {
        try {
            Conexion.commit();
            Conexion.setAutoCommit(true);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    public boolean deshacertransaccion()
    {
        try {
            Conexion.rollback();
            Conexion.setAutoCommit(true);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }
    public Connection ObtenerConexion(String servidor, String instancia, String base, String usuario, String clave) {
        this.servidor = servidor;
        this.instancia = instancia;
        this.base = base;
        this.usuario = usuario;
        this.clave = clave;

        this.url = "jdbc:jtds:sqlserver://" + servidor + "/" + base + ";encrypt=false;user=" + usuario + ";password=" + clave + ";";

        if (!instancia.equals("")) {
            this.url += "instance=" + instancia + ";";
        }

        // Deshabilitamos el modo estricto para conexiones externas temporalmente
        old = StrictMode.getThreadPolicy();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
                .permitNetwork()
                .build());

        // Nos conectamos a la base de datos
        try {
            String driver = "net.sourceforge.jtds.jdbc.Driver";
            Class.forName(driver).newInstance();
            Conexion = DriverManager.getConnection(url, usuario, clave);
            return Conexion;
        } catch (Exception e) {
            Log.w("Error connection", "" + e.getMessage());
            return null;
        }
    }

    public ResultSet select(String sqlQuery) {
        if (Conexion != null) {

            PreparedStatement prest;
            ResultSet resultado=null;
            try {
                prest = Conexion.prepareStatement(sqlQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                resultado = prest.executeQuery();
            } catch (SQLException e) {
                Log.w("Error select", "" + e.getMessage());
            }


            return resultado;
        } else {
            return null;
        }
    }

    /*
    Para realizar UPDATE, INSERT y DELETE.
    Devuelve el número de filas insertadas, actualizadas o borradas.
     */
    public int update(String sqlQuery) {
        PreparedStatement prest;
        int rows = -1;
        if (Conexion != null) {
            try {

                prest = Conexion.prepareStatement(sqlQuery);
                rows = prest.executeUpdate();

            } catch (SQLException e) {
                Log.w("Error update", "" + e.getMessage());
            }
        }
        return rows;
    }


    public void disconnect() {
        try {
            if (Conexion != null) {
                Conexion.close();
                Conexion=null;
            }
        } catch (SQLException e) {
            Log.w("Error disconnection", "" + e.getMessage());
        }
        // Restauramos el modo estricto
        StrictMode.setThreadPolicy(old);
    }

}