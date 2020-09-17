/*  Nombre.....:  $.java
 *  Descripción:  Controlador para la coneccion de base de datos de la aplicacion.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Julio, 2019.
 */

package Modelo;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;

public class $ {
    private Connection con = null;
    private Statement qry = null;
    
    // Nombre del Servidor SQL:
    private final String SRV = "14-BK091ST";
    // Nombre de la Base de Datos:
    private final String DAT = "SICRIC_APP";
    // Nombre del Usuario:
    private final String USR = "user_SICRIC";
    // Contraseña para el usuario definido anteriormente:
    private final String PSW = "user_SICRIC";
    
    // Método que inicializa la variable que permitirá ejecutar sentencias de SQL.
    private void conectar() throws ClassNotFoundException, SQLException {
        // Concatena los valores provistos por el usuario para crear el String de conexión.
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String URL = "jdbc:sqlserver://" + SRV + ":1433;" +
                     "databaseName=" + DAT + ";user=" + USR + ";" +
                     "password=" + PSW + ";";

        con = DriverManager.getConnection( URL );    
        qry = con.createStatement();
    } // Fin del método conectar().
    
    
    public ResultSet execQry(String myQuery) throws ClassNotFoundException, SQLException{
        conectar();
        return qry.executeQuery(myQuery);
    }
    
    public int execUpd(String myQuery) throws ClassNotFoundException, SQLException{
        conectar();
        return qry.executeUpdate(myQuery);
    }
    
    // Método que cierra las conexiones creadas.
    public void desconectar(){
        try {
            if(qry != null){
                if(!qry.isClosed())
                    qry.close();

                qry = null;
            }
        } catch(SQLException ex) { ex.printStackTrace(); }
        
        
        try {
            if(con != null){
                if(!con.isClosed())
                    con.close();

                con = null;
            }
        } catch(SQLException ex) { ex.printStackTrace(); }
              
        
        System.gc();
    } // Fin del método desconectar().
    
    
    public CallableStatement storedProc(String nombreProcedimiento) throws ClassNotFoundException, SQLException {
        CallableStatement sp = null;
        String myQuery = "{ CALL " + nombreProcedimiento + "() }";
        
        conectar();
        sp = con.prepareCall(myQuery);
        
        return sp;
    } // Fin del método storedProc().
    
    public CallableStatement storedProc(String nombreProcedimiento, int cantidadParametros) throws ClassNotFoundException, SQLException {
        CallableStatement sp = null;
        String myQuery = "{CALL " + nombreProcedimiento + "(";
        
        for (int i = 0; i < cantidadParametros; i++) {
            myQuery += "?,";
        }
        
        myQuery = myQuery.substring(0, myQuery.length()-1) + ")}";
        
        conectar();
        sp = con.prepareCall(myQuery);
        
        return sp;
    } // Fin del método storedProc().
    
    
    public PreparedStatement prepStatement(String myQuery) throws ClassNotFoundException, SQLException{
        conectar();
        return con.prepareStatement(myQuery);
    } // Fin del método prepStatement().
    
    
    // Método que realiza 'commit' para las transacciones ejecutadas.
    private void commit() throws SQLException{
        con.commit();
    } // Fin del método commit().
} // Fin de la clase $.java