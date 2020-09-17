/*  Nombre.....:  CatalogoSuministro.java
 *  Descripción:  Carga el registro de la tabla Catalogo_Suministro_Medico.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Diciembre, 2019.
 */
package Modelo;

import Utilitarios.Consts;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CatalogoSuministro {
    // Constantes de la clase CatalogoSuministro.
    private final String PAGINA_WEB = Consts.CATALOGO_SUMINISTRO_HTML;
    private final String TITULO = Consts.TITULO_LISTA_CATALOGO_SUMINISTROS;
    private final String ETIQUETA_EXISTENTE = Consts.ETIQUETA_EXISTENTE_CATALOGO_SUMINISTRO;
    private final String ETIQUETA_NUEVO = Consts.ETIQUETA_NUEVO_CATALOGO_SUMINISTRO;
    
    // Variables de la clase CatalogoSuministro.
    private String serie;
    private int idProveedor;
    private String nombre;
    private String descripcion;
    private int cantidad;
    private boolean cuantificable;
    private int usuarioModifico;
    private long fechaModificacion;
    private boolean modoVisualizacion;
    private boolean modoEdicion;
    private String selectProveedor;
    

    // Método constructor de la clase CatalogoSuministro.
    public CatalogoSuministro() {
        this.serie = "";
        this.idProveedor = 0;
        this.nombre = "";
        this.descripcion = "";
        this.cantidad = 0;
        this.cuantificable = true;
        this.usuarioModifico = 0;
        this.selectProveedor = "";
        this.selectProveedor = "";
    } // Fin del método constructor.
    
    
    // Metodos GET y SET de la clase CatalogoSuministro.
    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public boolean getCuantificable() {
        return cuantificable;
    }

    public void setCuantificable(boolean cuantificable) {
        this.cuantificable = cuantificable;
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

    public String getSelectProveedor() {
        return selectProveedor;
    }

    public void setSelectProveedor(String selectProveedor) {
        this.selectProveedor = selectProveedor;
    }

    public int getUsuarioModifico() {
        return usuarioModifico;
    }

    public void setUsuarioModifico(int usuarioModifico) {
        this.usuarioModifico = usuarioModifico;
    }

    public long getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(long fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
    
    
    // Métodos utilitarios de la clase CatalogoSuministro.
    // Carga la lista de todos los suministros médicos del catálogo, activos e inactivos.
    public String cargarLista(int idCentro, boolean modoVisualizacion, boolean modoEdicion){
      $ instanciaListaCatalogo = null;
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
        // Realiza consulta de suministros médicos a la base de datos.
        instanciaListaCatalogo = new $();
        ResultSet resultado = instanciaListaCatalogo.execQry(
            "SELECT Serie, Nombre, CantidadExistencial, Cuantificable " +
            "FROM Catalogo_Suministro_Medico " +
            "WHERE ID_Centro = " + idCentro + " " +
            "ORDER BY Serie;");
        
        if( resultado.next() ){
          respuesta =
                // Retroalimentación al usuario del proceso.
                "{ \"error\" : false, " +
                "  \"mensaje\" : \"\", " +
                // Información recuperada de la tabla Catalogo_Suministro_Medico.
                "  \"elementosHTML\": \"" +
                "            <h5 class='center'>" + TITULO +
                "              <a href='" + PAGINA_WEB + "' class='" + ETIQUETA_NUEVO + "'>" +
                "                <i class='material-icons right'>add_circle_outline</i>" +
                "              </a>" +
                "            </h5><hr/>" +
                "            <table id='myTable2'>" +
                "              <thead>" +
                "                <tr>" +
                "                  <th onclick='sortTable(0)' style='cursor:pointer' id='serieCol'>Serie<i></th>" +
                "                  <th onclick='sortTable(1)' style='cursor:pointer' id='nombreCol'>Nombre<i></th>" +
                "                  <th onclick='sortTable(2)' style='cursor:pointer' id='cantidadCol' class='center'>En existencia<i></th>" +
                "                  <th onclick='sortTable(3)' style='cursor:pointer' id='cuantificableCol' class='center'>Cuantificable<i></th>" +
                ( getModoVisualizacion() ? "<th class='center'>Ver</th>" : "" ) +
                ( getModoEdicion() ? "<th class='center'>Editar</th>" : "" ) +
                "                </tr>" +
                "              </thead>" +
                "            <tbody>";
                  
          do{
            // Si existen resultados crea arreglo JSON para mostrar la lista del catálogo de suministros médicos.
            this.setSerie(resultado.getString("Serie"));
            this.setNombre(resultado.getString("Nombre"));
            this.setCantidad(resultado.getInt("CantidadExistencial"));
            this.setCuantificable(resultado.getBoolean("Cuantificable"));
            
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
        if(instanciaListaCatalogo != null)
          instanciaListaCatalogo.desconectar();
          
        instanciaListaCatalogo = null;
        System.gc();
      }

      return respuesta;
    } // Fin del método cargarLista().
    
    
    // Carga el registro del catálogo de suministros médicos con el código proporcionado.
    public void cargarRegistro(String numeroDeSerie) {
        ServiciosM modelo = new ServiciosM();
        setSerie("-1");
        $ instanciaBD = null;
        
        try{
            if(numeroDeSerie.length() > 0){
                instanciaBD = new $();
                String myQuery =
                    "SELECT Serie, ID_Proveedor, Nombre, Descripcion, " +
                    "       CantidadExistencial, Cuantificable, UsuarioModifico " +
                    "FROM Catalogo_Suministro_Medico " +
                    "WHERE Serie = ?;";  

                PreparedStatement psSuministroMedico = instanciaBD.prepStatement(myQuery);      
                psSuministroMedico.setString(1, numeroDeSerie);
                ResultSet resultado = psSuministroMedico.executeQuery(); 

                if(resultado.next()){
                    setSerie(numeroDeSerie); 
                    setIdProveedor(resultado.getInt("ID_Proveedor"));
                    this.setNombre(resultado.getString("Nombre"));
                    this.setDescripcion(resultado.getString("Descripcion"));
                    this.setCantidad(resultado.getInt("CantidadExistencial"));
                    this.setCuantificable(resultado.getBoolean("Cuantificable"));
                    setUsuarioModifico(resultado.getInt("UsuarioModifico"));
                }
            } else {
                setSerie(numeroDeSerie);
                setIdProveedor(0);
                setNombre("");
                setDescripcion("");
                setCantidad(0);
                setCuantificable(true);
                setUsuarioModifico(0);
            }
            
            setSelectProveedor(modelo.elementoSELECTProveedor(getIdProveedor()));
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
        if (getSerie().equals("-1"))
            mensajeError = "Ocurrió un error al cargar el registro del catálogo de suministros médicos.";

        return
            "   \"error\": " + getSerie().equals("-1") + ", " +
            "   \"mensaje\": \"" + mensajeError + "\", " +
            "   \"codigo\": \"" + getSerie() + "\", " +
            "   \"nombre\": \"" + getNombre() + "\", " +
            "   \"cantidad\": " + getCantidad() + ", " +
            "   \"descripcion\": \"" + getDescripcion() + "\", " +
            "   \"cuantificable\": " + getCuantificable() + ", " +
            "   \"usuarioModifico\": \"" + getUsuarioModifico() + "\", " +
            "   \"selectProveedor\": \"" + getSelectProveedor() + "\"";
    } // Fin del metodo toJSON().

    // Devuelve el registro con formato de tabla HTML.
    private String toHTML() {
        return "<tr style='border: none;' class='resaltar'>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + getSerie() + "</td>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + getNombre() + "</td>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;' class='center'>" + getCantidad() + "</td>" +
               "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'>" +
               "    <i class='material-icons'>" + 
                      (getCuantificable() ? "check_box": "check_box_outline_blank") + 
               "    </i></a>" +
               "  </td>" +
               ( getModoVisualizacion() ? 
               "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'><a href='" + PAGINA_WEB + "' class='" + ETIQUETA_EXISTENTE + "' " +
               "      data-codigo='" + getSerie() + "' " +
               "      data-modo='mod-v'>" +
               "    <i class='material-icons'>visibility</i></a>" +
               "  </td>" : "" ) +
               ( getModoEdicion() ? 
               "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'><a href='" + PAGINA_WEB + "' class='" + ETIQUETA_EXISTENTE + "' " +
               "      data-codigo='" + getSerie() + "' " +
               "      data-modo='mod-e'>" +
               "    <i class='material-icons'>edit</i></a>" +
               "  </td>" : "" ) +
               "</tr>";
    } // Fin del método toHTML.
} // Fin de la clase CatalogoSuministro.