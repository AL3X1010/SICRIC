/*  Nombre.....:  CentroRemision.java
 *  Descripción:  Carga el registro de la tabla Centro_Remision.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Diciembre, 2019.
 */
package Modelo;

import Utilitarios.Consts;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CentroRemision {
    // Constantes de la clase CentroRemision.
    private final String PAGINA_WEB = Consts.CENTRO_REM_HTML;
    private final String TITULO = Consts.TITULO_LISTA_CENTROS_REM;
    private final String ETIQUETA_EXISTENTE = Consts.ETIQUETA_EXISTENTE_CENTRO_REM;
    private final String ETIQUETA_NUEVO = Consts.ETIQUETA_NUEVO_CENTRO_REM;
    
    // Variables de la clase CentroRemision.
    private int idCentroRemision;
    private String nombre;
    private String direccion;
    private int telefono;
    private String correoElectronico;
    private int tipo;
    private boolean activo;
    private int usuarioModifico;
    private long fechaModificacion;
    private boolean modoVisualizacion;
    private boolean modoEdicion;

    
    // Método constructor de la clase CentroRemision.
    public CentroRemision() {
        this.idCentroRemision = -1;
        this.nombre = "";
        this.direccion = "";
        this.telefono = 0;
        this.correoElectronico = "";
        this.activo = true;
        this.usuarioModifico = 0;
    } // Fin del método constructor.
    
    
    // Metodos GET y SET de la clase CentroRemision.
    public int getIdCentroRemision(){
        return idCentroRemision;
    }

    public void setIdCentroRemision(int idCentroRemision) {
        this.idCentroRemision = idCentroRemision;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getUsuarioModifico() {
        return usuarioModifico;
    }

    public void setUsuarioModifico(int usuarioModifico) {
        this.usuarioModifico = usuarioModifico;
    }
    
    public long getFechaModificacion(){
      return this.fechaModificacion;
    }
    
    public void setFechaModificacion(long fechaModificacion){
      this.fechaModificacion = fechaModificacion;
    }

    public boolean getModoVisualizacion() {
        return modoVisualizacion;
    }

    public void setModoVisualizacion(boolean modoVisualizacion) {
        this.modoVisualizacion = modoVisualizacion;
    }

    public boolean getModoEdicion() {
        return modoEdicion;
    }

    public void setModoEdicion(boolean modoEdicion) {
        this.modoEdicion = modoEdicion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    
    
    // Métodos utilitarios de la clase CentroRemision.
    // Carga la lista de todos los centros de remisión.
    public String cargarLista(int idCentro, boolean modoVisualizacion, boolean modoEdicion){
      $ instanciaListaCentrosRemision = null;
      String respuesta = "";
      setModoVisualizacion(modoVisualizacion);
      setModoEdicion(modoEdicion);
      // Define la estructura HTML de respuesta.
      final String ESTRUCTURA_ERROR =
            "  \"elementosHTML\": \"" +
            "            <h5 class='center'>$#titulo#$" +
            "              <a href='$#paginaWeb#$' class='$#etiquetaNuevo#$'>" +
            "                <i class='material-icons right'>add_circle_outline</i>" +
            "              </a>" +
            "            </h5><hr/>" +
            "            <div class='col s12 center'>" +
            "              <p> *** No se puede mostrar la información solicitada. ***</p>" +
            "            </div>\"";

      try {
        // Realiza consulta de centros de remisión a la base de datos.
        instanciaListaCentrosRemision = new $();
        ResultSet resultado = instanciaListaCentrosRemision.execQry(
            "SELECT ID_CentroRemision, Nombre, Telefono, Direccion " +
            "FROM Centro_Remision " +
            "WHERE ID_Centro = " + idCentro + " " +
            "ORDER BY Nombre;");
        
        if( resultado.next() ){
          respuesta =
                // Retroalimentación al usuario del proceso.
                "{ \"error\" : false, " +
                "  \"mensaje\" : \"\", " +
                // Información recuperada de la tabla CentroRemision.
                "  \"elementosHTML\": \"" +
                "            <h5 class='center'>" + TITULO +
                "              <a href='" + PAGINA_WEB + "' class='" + ETIQUETA_NUEVO + "'>" +
                "                <i class='material-icons right'>add_circle_outline</i>" +
                "              </a>" +
                "            </h5><hr/>" +
                "            <table id='myTable2'>" +
                "              <thead>" +
                "                <tr>" +
                "                  <th onclick='sortTable(0)' style='cursor:pointer' id='nombreCol'>Nombre<i></th>" +
                "                  <th>Dirección</th>" +
                "                  <th>Teléfono</th>" +
                ( getModoVisualizacion() ? "<th class='center'>Ver</th>" : "" ) +
                ( getModoEdicion() ? "<th class='center'>Editar</th>" : "" ) +
                "                </tr>" +
                "              </thead>" +
                "            <tbody>";
                  
          do{
            // Si existen resultados crea arreglo JSON para mostrar la lista de centros de remisión.
            this.setIdCentroRemision(resultado.getInt("ID_CentroRemision"));
            this.setNombre(resultado.getString("Nombre"));
            this.setTelefono(resultado.getInt("Telefono"));
            this.setDireccion(resultado.getString("Direccion"));
            
            respuesta += this.toHTML();
          } while( resultado.next() );
          
          // Completa la estructura del objeto JSON de respuesta.
          respuesta = respuesta + "</tbody></table>\"}";
        } else
          respuesta = "{ \"error\" : false, " +
                      "  \"mensaje\" : \"No existen registros para mostrar.\" ," +
                         ESTRUCTURA_ERROR
                                .replace("$#titulo#$", TITULO)
                                .replace("$#paginaWeb#$", PAGINA_WEB)
                                .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                      "}";
      } catch (Exception ex) {
        ex.printStackTrace();
        respuesta = "{ \"error\" : true, " +
                    "  \"mensaje\" : \"Ocurrió un error al intentar cargar los registros.\", " +
                       ESTRUCTURA_ERROR
                                .replace("$#titulo#$", TITULO)
                                .replace("$#paginaWeb#$", PAGINA_WEB)
                                .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                    "}";
      } finally {
        if(instanciaListaCentrosRemision != null)
          instanciaListaCentrosRemision.desconectar();
          
        instanciaListaCentrosRemision = null;
        System.gc();
      }

      return respuesta;
    } // Fin del método cargarLista().
    
    
    /* Carga el registro de departamento con el código proporcionado.
    public void cargarRegistro(int idDepartamento) {
        setIdDepartamento(-1);  
        $ instanciaBD = null;
        
        try{
            if(idDepartamento > 0){
                instanciaBD = new $();
                String myQuery =
                    "SELECT Nombre, Descripcion, Activo, UsuarioModifico " +
                    "FROM Departamento " +
                    "WHERE ID_Departamento = ?;";  

                PreparedStatement psDepartamento = instanciaBD.prepStatement(myQuery);      
                psDepartamento.setInt(1, idDepartamento);
                ResultSet resultado = psDepartamento.executeQuery(); 

                if(resultado.next()){
                    setIdDepartamento(idDepartamento); 
                    setNombre(resultado.getString("Nombre"));
                    setDescripcion(resultado.getString("Descripcion"));
                    setActivo(resultado.getInt("Activo") == 1);
                    setUsuarioModifico(resultado.getInt("UsuarioModifico"));
                }
            } else {
                setIdDepartamento(idDepartamento);
                setNombre("");
                setDescripcion("");
                setActivo(true);
                setUsuarioModifico(0);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(instanciaBD != null)
                instanciaBD.desconectar();
            
            instanciaBD = null;
            System.gc();
        }
    } // Fin del metodo cargarRegistro().
    
    
    // Devuelve el registro con formato de objeto JSON.
    public String toJSON(){
        String mensajeError = "";
        if(getIdDepartamento() < 0)
            mensajeError = "Ocurrió un error al cargar el registro del departamento.";

        return
            "   \"error\": " + (getIdDepartamento() < 0) + ", " +
            "   \"mensaje\": \"" + mensajeError + "\", " +
            "   \"codigo\": " + getIdDepartamento() + ", " +
            "   \"nombre\": \"" + getNombre() + "\", " +
            "   \"descripcion\": \"" + getDescripcion() + "\", " +
            "   \"activo\": " + getActivo() + ", " +
            "   \"usuarioModifico\": \"" + getUsuarioModifico() + "\"";
    } // Fin del metodo toJSON(). */

    // Devuelve el registro con formato de tabla HTML.
    private String toHTML() {
        return "<tr style='border: none;' class='resaltar'>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + getNombre() + "</td>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + getDireccion() + "</td>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + getTelefono() + "</td>" +
               "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'>" +
               "    <i class='material-icons'>" + 
                      (getActivo() ? "check_box": "check_box_outline_blank") + 
               "    </i></a>" +
               "  </td>" +
               ( getModoVisualizacion() ? 
               "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'><a href='" + PAGINA_WEB + "' class='" + ETIQUETA_EXISTENTE + "' " +
               "      data-codigo='" + getIdCentroRemision() + "' " +
               "      data-modo='mod-v'>" +
               "    <i class='material-icons'>visibility</i></a>" +
               "  </td>" : "" ) +
               ( getModoEdicion() ? 
               "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'><a href='" + PAGINA_WEB + "' class='" + ETIQUETA_EXISTENTE + "' " +
               "      data-codigo='" + getIdCentroRemision() + "' " +
               "      data-modo='mod-e'>" +
               "    <i class='material-icons'>edit</i></a>" +
               "  </td>" : "" ) +
               "</tr>";
    } // Fin del método toHTML.
} // Fin de la clase CentroRemision.