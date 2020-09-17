/*  Nombre.....:  SuministroMedico.java
 *  Descripción:  Carga el registro de la tabla Inventario_Suministro_Medico.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Diciembre, 2019.
 */
package Modelo;

import Utilitarios.Consts;
import Utilitarios.ServiciosU;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SuministroMedico {
    // Constantes de la clase SuministroMedico.
    private final String PAGINA_WEB = Consts.SUMINISTRO_HTML;
    private final String TITULO = Consts.TITULO_LISTA_SUMINISTROS;
    private final String ETIQUETA_EXISTENTE = Consts.ETIQUETA_EXISTENTE_SUMINISTRO;
    private final String ETIQUETA_NUEVO = Consts.ETIQUETA_NUEVO_SUMINISTRO;

    // Variables de la clase SuministroMedico.
    private String serie;
    private String lote;
    private String nombre;
    private float costo;
    private int cantidad;
    private int fechaVencimiento;
    private int usuarioModifico;
    private long fechaModificacion;
    private String selectSerie;
    private String selectEmpleado;
    private boolean modoVisualizacion;
    private boolean modoEdicion;

    // Método constructor de la clase SuministroMedico.
    public SuministroMedico() {
        this.serie = "";
        this.lote = "";
        this.costo = 0;
        this.cantidad = 0;
        this.fechaVencimiento = 0;
        this.usuarioModifico = 0;
        this.selectSerie = "";
        this.selectEmpleado = "";
    } // Fin del método constructor.

    // Metodos GET y SET de la clase SuministroMedico.
    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(int fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
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

    public String getSelectSerie() {
        return selectSerie;
    }

    public void setSelectSerie(String selectSerie) {
        this.selectSerie = selectSerie;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSelectEmpleado() throws ClassNotFoundException, SQLException {
        if(selectEmpleado.trim().length() == 0){
            ServiciosM modelo = new ServiciosM();
            selectEmpleado = modelo.elementoSELECTEmpleado("");
        }
        
        return selectEmpleado;
    }

    // Métodos utilitarios de la clase SuministroMedico.
    // Carga la lista de todos los suministros médicos del inventario de la empresa, activos e inactivos.
    public String cargarLista(int idCentro, boolean modoVisualizacion, boolean modoEdicion) {
        $ instanciaListaSuministros = null;
        String respuesta = "";
        setModoVisualizacion(modoVisualizacion);
        setModoEdicion(modoEdicion);
        // Define la estructura HTML de respuesta.
        final String ESTRUCTURA_ERROR
                = "  \"elementosHTML\": \""
                + "            <h5 class='center'>$#titulo#$"
                + "              <a href='$#paginaWeb#$' class='$#etiquetaNuevo#$'>"
                + "                <i class='material-icons right'>add_circle_outline</i>"
                + "              </a>"
                + "            </h5><hr/>"
                + "            <div class='col s12 center'>"
                + "              <p> *** No se puede mostrar la información solicitada. ***</p>"
                + "            </div>\"";

        try {
            // Realiza consulta de suministros médicos a la base de datos.
            instanciaListaSuministros = new $();
            ResultSet resultado = instanciaListaSuministros.execQry(
                    "SELECT Serie, Lote, Nombre, Cantidad, FechaVencimiento "
                    + "FROM V_Lista_Suministros "
                    + "WHERE ID_Centro = " + idCentro + " "
                    + "ORDER BY Serie, Lote;");

            if (resultado.next()) {
                respuesta
                        = // Retroalimentación al usuario del proceso.
                        "{ \"error\" : false, "
                        + "  \"mensaje\" : \"\", "
                        + // Información recuperada de la tabla Inventario_Suministro_Medico.
                        "  \"elementosHTML\": \""
                        + "            <h5 class='center'>" + TITULO
                        + "              <a href='" + PAGINA_WEB + "' class='" + ETIQUETA_NUEVO + "'>"
                        + "                <i class='material-icons right'>add_circle_outline</i>"
                        + "              </a>"
                        + "            </h5><hr/>"
                        + "            <table id='myTable2'>"
                        + "              <thead>"
                        + "                <tr>"
                        + "                  <th onclick='sortTable(0)' style='cursor:pointer' id='serieCol'>Serie<i></th>"
                        + "                  <th onclick='sortTable(1)' style='cursor:pointer' id='loteCol'>Lote<i></th>"
                        + "                  <th>Nombre</th>"
                        + "                  <th onclick='sortTable(3)' style='cursor:pointer' id='serieCol' class='center'>Cantidad</th>"
                        + (getModoVisualizacion() ? "<th class='center'>Ver</th>" : "")
                        + (getModoEdicion() ? "<th class='center'>Asignar</th>" : "")
                        + (getModoEdicion() ? "<th class='center'>Agregar<br/>Entrada</th>" : "")
                        + "                </tr>"
                        + "              </thead>"
                        + "            <tbody>";

                do {
                    // Si existen resultados crea arreglo JSON para mostrar la lista de suministros medicos.
                    this.setSerie(resultado.getString("Serie"));
                    this.setLote(resultado.getString("Lote"));
                    this.setNombre(resultado.getString("Nombre"));
                    this.setCantidad(resultado.getInt("Cantidad"));
                    this.setFechaVencimiento(resultado.getInt("FechaVencimiento"));

                    respuesta += this.toHTML();
                } while (resultado.next());

                // Completa la estructura del objeto JSON de respuesta.
                respuesta = respuesta + "</tbody></table>\"}";
            } else {
                respuesta = "{ \"error\" : false, "
                        + "  \"mensaje\" : \"No existen registros para mostrar.\" ,"
                        + ESTRUCTURA_ERROR
                        .replace("$#titulo#$", TITULO)
                        .replace("$#paginaWeb#$", PAGINA_WEB)
                        .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO)
                        + "}";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            respuesta = "{ \"error\" : true, "
                    + "  \"mensaje\" : \"Ocurrió un error al intentar cargar los registros.\", "
                    + ESTRUCTURA_ERROR
                    .replace("$#titulo#$", TITULO)
                    .replace("$#paginaWeb#$", PAGINA_WEB)
                    .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO)
                    + "}";
        } finally {
            if (instanciaListaSuministros != null) {
                instanciaListaSuministros.desconectar();
            }

            instanciaListaSuministros = null;
            System.gc();
        }

        return respuesta;
    } // Fin del método cargarLista().

    // Carga el registro de suministro médico con el código proporcionado.
    public void cargarRegistro(String serie, String lote) {
        setSerie("-1");
        $ instanciaBD = null;
        ServiciosM modelo = new ServiciosM();

        try {
            instanciaBD = new $();
            if (serie.length() > 0) {
                instanciaBD = new $();
                String myQuery =
                        "SELECT CostoLote, CantidadIngresada, FechaVencimiento " +
                        "FROM Inventario_Suministro_Medico " +
                        "WHERE Serie = ? " +
                        "  AND Lote = ?";

                PreparedStatement psSuministroMedico = instanciaBD.prepStatement(myQuery);
                psSuministroMedico.setString(1, serie);
                psSuministroMedico.setString(2, lote);
                ResultSet resultado = psSuministroMedico.executeQuery();

                if (resultado.next()) {
                    setSerie(serie);
                    setLote(lote);
                    setCosto(resultado.getFloat("CostoLote"));
                    setCantidad(resultado.getInt("CantidadIngresada"));
                    setFechaVencimiento(resultado.getInt("FechaVencimiento"));
                }
            } else {
                setSerie("");
                setLote("");
                setCosto(0);
                setCantidad(0);
                setFechaVencimiento(0);
            }

            setSelectSerie(modelo.elementoSELECTSerie(getSerie()));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (instanciaBD != null)
                instanciaBD.desconectar();

            instanciaBD = null;
            System.gc();
        }
    } // Fin del metodo cargarRegistro().

    
    // Devuelve el registro con formato de objeto JSON.
    public String toJSON() throws ClassNotFoundException, SQLException {
        String mensajeError = "";
        if (getSerie().equals("-1")) {
            mensajeError = "Ocurrió un error al cargar el registro del empleado.";
        }

        return
            "   \"error\": " + getSerie().equals("-1") + ", " +
            "   \"mensaje\": \"" + mensajeError + "\", " +
            "   \"codigo\": \"" + getLote() + "\", " +
            "   \"codigo2\": \"" + getSerie() + "\", " +
            "   \"cantidad\": " + getCantidad() + ", " +
            "   \"costo\": " + getCosto() + ", " +
            "   \"fechaVencimiento\": \"" + ServiciosU.formatearFecha(getFechaVencimiento()) + "\", " +
            "   \"asignado\": 0, " +
            "   \"notaAdiciona\": \"\", " +
            "   \"selectSerie\": \"" + getSelectSerie() + "\", " +
            "   \"selectEmpleado\": \"" + getSelectEmpleado() + "\", " +
            "   \"usuarioModifico\": \"" + getUsuarioModifico() + "\" ";
    } // Fin del metodo toJSON().

    // Devuelve el registro con formato de tabla HTML.
    private String toHTML() {
        return "<tr style='border: none;' class='resaltar'>"
                + "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + getSerie() + "</td>"
                + "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + getLote() + "</td>"
                + "  <td style='padding-top: 1%; padding-bottom: 1%;'>" + getNombre() + "</td>"
                + "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;' class='center'>" + getCantidad() + "</td>"
                + (getModoVisualizacion()
                ? "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'><a href='" + PAGINA_WEB + "' class='" + ETIQUETA_EXISTENTE + "' "
                + "      data-codigo='" + getSerie() + Consts.SEPARADOR + getLote() + "' "
                + "      data-modo='mod-v'>"
                + "    <i class='material-icons'>visibility</i></a>"
                + "  </td>" : "")
                + (getModoEdicion()
                ? "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'>"
                + "    <a href='" + Consts.ASIGNAR_SUMINISTRO_HTML + "' class='asignarSuministro' "
                + "        data-codigo='" + getSerie() + Consts.SEPARADOR + getLote() + "' "
                + "      data-modo='mod-e'>"
                + "      <i class='material-icons'>person_add</i></a>"
                + "  </td>" : "")
                + (getModoEdicion()
                ? "  <td class='center' style='padding-top: 1%; padding-bottom: 1%; width: 7%; white-space: nowrap;'>"
                + "    <a href='" + Consts.AGREGAR_SUMINISTRO_HTML + "' class='agregarSuministro' "
                + "        data-codigo='" + getSerie() + Consts.SEPARADOR + getLote() + "' "
                + "      data-modo='mod-e'>"
                + "      <i class='material-icons'>exit_to_app</i></a>"
                + "  </td>" : "")
                + "</tr>";
    } // Fin del método toHTML.
} // Fin de la clase SuministroMedico.
