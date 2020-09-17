/*  Nombre.....:  ParametrosBD.java
 *  Descripción:  Carga el registro de la tabla ParametrosBD.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Julio, 2019.
 */
package Modelo;

import java.sql.ResultSet;

public class ParametrosBD {
    private static String configuracionSistema;
    
    // Metodo constructor de la clase ParametrosBD.
    public ParametrosBD(){
        configuracionSistema = "";
    }
    
    // Metodos SET y GET de ParametrosBD.    
    public static String getConfiguracionSistema(int idCentro){
        cargarRegistro(idCentro);
        return configuracionSistema;
    } // Fin del metodo getConfiguracionSistema().
    
    public static void setConfiguracionSistema(String configuracionSistema){
        ParametrosBD.configuracionSistema = configuracionSistema;
    } // Fin del metodo setConfiguracionSistema().
    
    private static void cargarRegistro(int idCentro) {
        $ instanciaBD = null;
        
        try{
            instanciaBD = new $();
            ResultSet resultado = instanciaBD.execQry(
                    "SELECT ConfiguracionSistema " +
                    "FROM ParametrosBD " +
                    "WHERE ID_Centro = " + idCentro + ";");
            
            if (resultado.next()){
                setConfiguracionSistema(resultado.getString(1));
            }else{
                setConfiguracionSistema("");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally{
            if (instanciaBD != null)
                instanciaBD.desconectar();
            
            instanciaBD = null;
            System.gc();
        }
    } // Fin del metodo cargarRegistro().
} // Fin de la clase ParametrosBD.