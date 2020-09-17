/*  Nombre.....:  EquipoMedico.java
 *  Descripción:  Carga el registro de la tabla Inventario_Equipo_Medico.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Diciembre, 2019.
 */
package Modelo;

import Utilitarios.Consts;
import Utilitarios.ServiciosU;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EquipoMedico {
    // Constantes de la clase EquipoMedico.
    private final String PAGINA_WEB = Consts.EQUIPO_HTML;
    private final String TITULO = Consts.TITULO_LISTA_EQUIPOS;
    private final String ETIQUETA_EXISTENTE = Consts.ETIQUETA_EXISTENTE_EQUIPO;
    private final String ETIQUETA_NUEVO = Consts.ETIQUETA_NUEVO_EQUIPO;
    
    // Variables de la clase EquipoMedico.
    private int idCatalogo;
    private String marca;
    private String modelo;
    private String serie;
    private String nombre;
    private int fechaIngreso;
    private int fechaMantenimiento;
    private boolean activo;
    private int usuarioModifico;
    private long fechaModificacion;
    private String selectMarca;
    private String selectModelo;
    private boolean modoVisualizacion;
    private boolean modoEdicion;

    
    // Método constructor de la clase EquipoMedico.
    public EquipoMedico() {
        this.idCatalogo = -1;
        this.serie = "";
        this.fechaIngreso = 0;
        this.fechaMantenimiento = 0;
        this.activo = true;
        this.usuarioModifico = 0;
        this.selectMarca = "";
        this.selectModelo = "";
    } // Fin del método constructor.
    
    
    // Metodos GET y SET de la clase EquipoMedico.
    public int getIdCatalogo() {
        return idCatalogo;
    }

    public void setIdCatalogo(int idCatalogo) {
        this.idCatalogo = idCatalogo;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public int getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(int fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public int getFechaMantenimiento() {
        return fechaMantenimiento;
    }

    public void setFechaMantenimiento(int fechaMantenimiento) {
        this.fechaMantenimiento = fechaMantenimiento;
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

    public long getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(long fechaModificacion) {
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

    public String getSelectMarca() {
        return selectMarca;
    }

    public void setSelectMarca(String selectMarca) {
        this.selectMarca = selectMarca;
    }

    public String getSelectModelo() {
        return selectModelo;
    }

    public void setSelectModelo(String selectModelo) {
        this.selectModelo = selectModelo;
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
    
    
    // Métodos utilitarios de la clase EquipoMedico.
    // Carga la lista de todos los equipos médicos del inventario de la empresa, activos e inactivos.
    public String cargarLista(int idCentro, boolean modoVisualizacion, boolean modoEdicion){
      $ instanciaListaEquipos = null;
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
        instanciaListaEquipos = new $();
        ResultSet resultado = instanciaListaEquipos.execQry(
            "SELECT Marca, Modelo, Nombre, SerieEquipo, Activo " +
            "FROM V_Lista_Equipos " +
            "WHERE ID_Centro = " + idCentro + " " +
            "ORDER BY Marca, Modelo;");
        
        if( resultado.next() ){
          respuesta =
                // Retroalimentación al usuario del proceso.
                "{ \"error\" : false, " +
                "  \"mensaje\" : \"\", " +
                // Información recuperada de la tabla Inventario_Equipo_Medico.
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
                "                  <th>Nombre</th>" +
                "                  <th onclick='sortTable(3)' style='cursor:pointer' id='serieCol'>Serie</th>" +
                "                  <th onclick='sortTable(4)' style='cursor:pointer' id='activoCol' class='center'>Activo</th>" +
                ( getModoVisualizacion() ? "<th class='center'>Ver</th>" : "" ) +
                ( getModoEdicion() ? "<th class='center'>Editar</th>" : "" ) +
                "                </tr>" +
                "              </thead>" +
                "            <tbody>";
                  
          do{
            // Si existen resultados crea arreglo JSON para mostrar la lista de equipos medicos.
            this.setMarca(resultado.getString("Marca"));
            this.setModelo(resultado.getString("Modelo"));
            this.setNombre(resultado.getString("Nombre"));
            this.setSerie(resultado.getString("SerieEquipo"));
            this.setActivo(resultado.getBoolean("Activo"));
            
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
        if(instanciaListaEquipos != null)
          instanciaListaEquipos.desconectar();
          
        instanciaListaEquipos = null;
        System.gc();
      }

      return respuesta;
    } // Fin del método cargarLista().
    
    
    // Carga el registro de equipo médico con el código proporcionado.
    public void cargarRegistro(String numeroSerie) {
        setSerie("-1");
        $ instanciaBD = null;
        ServiciosM modelo = new ServiciosM();
        
        try{
            instanciaBD = new $();
            if(numeroSerie.length() > 0){
                instanciaBD = new $();
                String myQuery =
                    "SELECT ID_Catalogo, SerieEquipoMedico, FechaIngreso, " +
                    "       FechaMantenimiento, Activo " +
                    "FROM Inventario_Equipo_Medico " +
                    "WHERE SerieEquipoMedico = ?;";  

                PreparedStatement psEquipoMedico = instanciaBD.prepStatement(myQuery);      
                psEquipoMedico.setString(1, numeroSerie);
                ResultSet resultado = psEquipoMedico.executeQuery(); 

                if(resultado.next()){
                    setSerie(numeroSerie);
                    setIdCatalogo(resultado.getInt("ID_Catalogo"));
                    setFechaIngreso(resultado.getInt("FechaIngreso"));
                    setFechaMantenimiento(resultado.getInt("FechaMantenimiento"));
                    setActivo(resultado.getInt("Activo") == 1);
                }
            } else {
                setSerie("");
                setIdCatalogo(0);
                setFechaIngreso(0);
                setFechaMantenimiento(0);
                setActivo(true);
            }
            
            
            String[] elementosSelect = {String.valueOf(getIdCatalogo()), "", ""};
            modelo.elementoSELECTMarcaModelo(elementosSelect);
            setIdCatalogo(Integer.parseInt(elementosSelect[0]));
            setSelectMarca(elementosSelect[1]);
            setSelectModelo(elementosSelect[2]);
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
            mensajeError = "Ocurrió un error al cargar el registro del empleado.";
        
        return
            "   \"error\": " + getSerie().equals("-1") + ", " +
            "   \"mensaje\": \"" + mensajeError + "\", " +
            "   \"codigo\": \"" + getSerie() + "\", " +
            "   \"codigo2\": " + getIdCatalogo() + ", " +
            "   \"fechaIngreso\": \"" + ServiciosU.formatearFecha(getFechaIngreso()) + "\", " +
            "   \"fechaMantenimiento\": \"" + ServiciosU.formatearFecha(getFechaMantenimiento()) + "\", " +
            "   \"activo\": " + getActivo() + ", " +
            "   \"selectMarca\": \"" + getSelectMarca() + "\", " +
            "   \"selectModelo\": \"" + getSelectModelo() + "\", " +
            "   \"usuarioModifico\": \"" + getUsuarioModifico() + "\" ";
    } // Fin del metodo toJSON().

    // Devuelve el registro con formato de tabla HTML.
    private String toHTML() {
        return "<tr style='border: none;' class='resaltar'>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%;'>" + getMarca() + "</td>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%;'>" + getModelo() + "</td>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + getNombre() + "</td>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%;'>" + getSerie() + "</td>" +
               "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'>" +
               "    <i class='material-icons'>" + 
                      (getActivo() ? "check_box": "check_box_outline_blank") + 
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
} // Fin de la clase EquipoMedico.