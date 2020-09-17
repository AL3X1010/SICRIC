/*  Nombre.....:  CatalogoEquipo.java
 *  Descripción:  Carga el registro de la tabla Catalogo_Equipo_Medico.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Diciembre, 2019.
 */
package Modelo;

import Utilitarios.Consts;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CatalogoEquipo {
    // Constantes de la clase CatalogoEquipo.
    private final String PAGINA_WEB = Consts.CATALOGO_EQUIPO_HTML;
    private final String TITULO = Consts.TITULO_LISTA_CATALOGO_EQUIPOS;
    private final String ETIQUETA_EXISTENTE = Consts.ETIQUETA_EXISTENTE_CATALOGO_EQUIPO;
    private final String ETIQUETA_NUEVO = Consts.ETIQUETA_NUEVO_CATALOGO_EQUIPO;
    
    // Variables de la clase CatalogoEquipo.
    private int idCatalogo;
    private int idProveedor;
    private String marca;
    private String modelo;
    private String nombre;
    private String descripcion;
    private float costo;
    private int cantidad;
    private int usuarioModifico;
    private long fechaModificacion;
    private boolean modoVisualizacion;
    private boolean modoEdicion;
    private String selectProveedor;
    

    // Método constructor de la clase CatalogoEquipo.
    public CatalogoEquipo() {
        this.idCatalogo = -1;
        this.idProveedor = 0;
        this.marca = "";
        this.modelo = "";
        this.nombre = "";
        this.descripcion = "";
        this.costo = 0;
        this.cantidad = 0;
        this.usuarioModifico = 0;
        this.selectProveedor = "";
    } // Fin del método constructor.
    
    
    // Metodos GET y SET de la clase CatalogoEquipo.
    public int getIdCatalogo(){
        return idCatalogo;
    }

    public void setIdCatalogo(int idCatalogo) {
        this.idCatalogo = idCatalogo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public String getCantidad() {
        return cantidad > 0 ? String.valueOf(cantidad): "";
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public String getSelectProveedor() {
        return selectProveedor;
    }

    public void setSelectProveedor(String selectProveedor) {
        this.selectProveedor = selectProveedor;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }
    
    
    // Métodos utilitarios de la clase CatalogoEquipo.
    // Carga la lista de todos los equipos médicos del catálogo, activos e inactivos.
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
        // Realiza consulta de equipos médicos a la base de datos.
        instanciaListaCatalogo = new $();
        ResultSet resultado = instanciaListaCatalogo.execQry(
            "SELECT ID_Catalogo, Marca, Modelo, Nombre, CostoUnitario, " +
            "       CantidadExistencial " +
            "FROM Catalogo_Equipo_Medico " +
            "WHERE ID_Centro = " + idCentro + " " +
            "ORDER BY Marca, Modelo, Nombre;");
        
        if( resultado.next() ){
          respuesta =
                // Retroalimentación al usuario del proceso.
                "{ \"error\" : false, " +
                "  \"mensaje\" : \"\", " +
                // Información recuperada de la tabla Catalogo_Equipo_Medico.
                "  \"elementosHTML\": \"" +
                "            <h5 class='center'>" + TITULO +
                "              <a href='" + PAGINA_WEB + "' class='" + ETIQUETA_NUEVO + "'>" +
                "                <i class='material-icons right'>add_circle_outline</i>" +
                "              </a>" +
                "            </h5><hr/>" +
                "            <table id='myTable2'>" +
                "              <thead>" +
                "                <tr>" +
                "                  <th onclick='sortTable(0)' style='cursor:pointer' id='marcaCol'>Marca<i></th>" +
                "                  <th onclick='sortTable(1)' style='cursor:pointer' id='modeloCol'>Modelo<i></th>" +
                "                  <th onclick='sortTable(2)' style='cursor:pointer' id='nombreCol'>Nombre<i></th>" +
                "                  <th onclick='sortTable(3)' style='cursor:pointer' id='costoCol'>Costo unitario<i></th>" +
                "                  <th onclick='sortTable(4)' style='cursor:pointer' id='cantidadCol'>Cantidad<i></th>" +
                ( getModoVisualizacion() ? "<th class='center'>Ver</th>" : "" ) +
                ( getModoEdicion() ? "<th class='center'>Editar</th>" : "" ) +
                "                </tr>" +
                "              </thead>" +
                "            <tbody>";
                  
          do{
            // Si existen resultados crea arreglo JSON para mostrar la lista del catálogo de equipos médicos.
            this.setIdCatalogo(resultado.getInt("ID_Catalogo"));
            this.setMarca(resultado.getString("Marca"));
            this.setModelo(resultado.getString("Modelo"));
            this.setNombre(resultado.getString("Nombre"));
            this.setCosto(resultado.getFloat("CostoUnitario"));
            this.setCantidad(resultado.getInt("CantidadExistencial"));
            
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
    
    
    // Carga el registro del catálogo de equipos médicos con el código proporcionado.
    public void cargarRegistro(int idCatalogo) {
        ServiciosM modelo = new ServiciosM();
        setIdCatalogo(-1);  
        $ instanciaBD = null;
        
        try{
            if(idCatalogo > 0){
                instanciaBD = new $();
                String myQuery =
                    "SELECT ID_Catalogo, ID_Proveedor, Marca, Modelo, Nombre, " +
                    "       Descripcion, CostoUnitario, CantidadExistencial, " +
                    "       UsuarioModifico " +
                    "FROM Catalogo_Equipo_Medico " +
                    "WHERE ID_Catalogo = ?;";  

                PreparedStatement psEquipoMedico = instanciaBD.prepStatement(myQuery);      
                psEquipoMedico.setInt(1, idCatalogo);
                ResultSet resultado = psEquipoMedico.executeQuery(); 

                if(resultado.next()){
                    setIdCatalogo(idCatalogo); 
                    setIdProveedor(resultado.getInt("ID_Proveedor"));
                    this.setMarca(resultado.getString("Marca"));
                    this.setModelo(resultado.getString("Modelo"));
                    this.setNombre(resultado.getString("Nombre"));
                    this.setDescripcion(resultado.getString("Descripcion"));
                    this.setCosto(resultado.getFloat("CostoUnitario"));
                    this.setCantidad(resultado.getInt("CantidadExistencial"));
                    setUsuarioModifico(resultado.getInt("UsuarioModifico"));
                }
            } else {
                setIdCatalogo(idCatalogo);
                setIdProveedor(0);
                setMarca("");
                setModelo("");
                setNombre("");
                setDescripcion("");
                setCosto(0);
                setCantidad(0);
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
        if(getIdCatalogo() < 0)
            mensajeError = "Ocurrió un error al cargar el registro del catálogo de equipos médicos.";

        return
            "   \"error\": " + (getIdCatalogo() < 0) + ", " +
            "   \"mensaje\": \"" + mensajeError + "\", " +
            "   \"codigo\": " + getIdCatalogo() + ", " +
            "   \"marca\": \"" + getMarca() + "\", " +
            "   \"modelo\": \"" + getModelo() + "\", " +
            "   \"nombre\": \"" + getNombre() + "\", " +
            "   \"descripcion\": \"" + getDescripcion() + "\", " +
            "   \"costo\": " + getCosto() + ", " +
            "   \"cantidad\": \"" + getCantidad() + "\", " +
            "   \"usuarioModifico\": \"" + getUsuarioModifico() + "\", " +
            "   \"selectProveedor\": \"" + getSelectProveedor() + "\"";
    } // Fin del metodo toJSON().

    // Devuelve el registro con formato de tabla HTML.
    private String toHTML() {
        return "<tr style='border: none;' class='resaltar'>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + getMarca() + "</td>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + getModelo() + "</td>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + getNombre() + "</td>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%;'>" + getCosto() + "</td>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%;'>" + getCantidad() + "</td>" +
               ( getModoVisualizacion() ? 
               "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'><a href='" + PAGINA_WEB + "' class='" + ETIQUETA_EXISTENTE + "' " +
               "      data-codigo='" + getIdCatalogo() + "' " +
               "      data-modo='mod-v'>" +
               "    <i class='material-icons'>visibility</i></a>" +
               "  </td>" : "" ) +
               ( getModoEdicion() ? 
               "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'><a href='" + PAGINA_WEB + "' class='" + ETIQUETA_EXISTENTE + "' " +
               "      data-codigo='" + getIdCatalogo() + "' " +
               "      data-modo='mod-e'>" +
               "    <i class='material-icons'>edit</i></a>" +
               "  </td>" : "" ) +
               "</tr>";
    } // Fin del método toHTML.
} // Fin de la clase CatalogoEquipo.