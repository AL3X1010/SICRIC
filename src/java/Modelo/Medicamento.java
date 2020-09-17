/*  Nombre.....:  Medicamento.java
 *  Descripción:  Carga el registro de la tabla Inventario_Medicamento.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Diciembre, 2019.
 */
package Modelo;

import Utilitarios.Consts;
import Utilitarios.ServiciosU;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Medicamento {
    // Constantes de la clase Medicamento.
    private final String PAGINA_WEB = Consts.MEDICAMENTO_HTML;
    private final String TITULO = Consts.TITULO_LISTA_MEDICAMENTOS;
    private final String ETIQUETA_EXISTENTE = Consts.ETIQUETA_EXISTENTE_MEDICAMENTO;
    private final String ETIQUETA_NUEVO = Consts.ETIQUETA_NUEVO_MEDICAMENTO;

    // Variables de la clase Medicamento.
    private String serie;
    private String lote;
    private int idUsuario;
    private int idRol;
    private String nombre;
    private float costo;
    private int cantidad;
    private int cantidadDevuelta;
    private int fechaVencimiento;
    private int usuarioModifico;
    private long fechaModificacion;
    private String selectSerie;
    private String selectEmpleado;
    private boolean pendienteAprobacion;
    private boolean modoVisualizacion;
    private boolean modoEdicion;

    // Método constructor de la clase Medicamento.
    public Medicamento() {
        this.serie = "";
        this.lote = "";
        this.idUsuario = 0;
        this.idRol = 0;
        this.costo = 0;
        this.cantidad = 0;
        this.cantidadDevuelta = 0;
        this.fechaVencimiento = 0;
        this.usuarioModifico = 0;
        this.pendienteAprobacion = false;
    } // Fin del método constructor.

    // Metodos GET y SET de la clase Medicamento.
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

    public int getCantidadDevuelta() {
        return cantidadDevuelta;
    }

    public void setCantidadDevuelta(int cantidadDevuelta) {
        this.cantidadDevuelta = cantidadDevuelta;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public boolean getPendienteAprobacion() {
        return pendienteAprobacion;
    }

    public void setPendienteAprobacion(boolean pendienteAprobacion) {
        this.pendienteAprobacion = pendienteAprobacion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    // Métodos utilitarios de la clase Medicamento.
    // Carga la lista de todos los medicamentos médicos del inventario de la empresa, activos e inactivos.
    public String cargarLista(int idCentro, int idUsuario, int idRol, boolean modoVisualizacion, boolean modoEdicion) {
        $ instanciaListaMedicamentos = null;
        String respuesta = "";
        setIdUsuario(idUsuario);
        setIdRol(idRol);
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
            // Realiza consulta de medicamentos médicos a la base de datos.
            instanciaListaMedicamentos = new $();
            ResultSet resultado = instanciaListaMedicamentos.execQry(
                    "SELECT Serie, Lote, Nombre, Cantidad, FechaVencimiento, "
                    + "     PENDIENTE_APROBACION "
                    + "FROM V_Lista_Medicamentos "
                    + "WHERE ID_Centro = " + idCentro + " "
                    + "ORDER BY Serie, Lote;");

            if (resultado.next()) {
                respuesta
                        = // Retroalimentación al usuario del proceso.
                        "{ \"error\" : false, "
                        + "  \"mensaje\" : \"\", "
                        + // Información recuperada de la tabla Inventario_Medicamento.
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
                        + (getModoEdicion() ? "<th class='center'>Expedir</th>" : "")
                        + (getIdRol() == Consts.ROL_FARMACIA_OPERADOR ? "<th class='center'>Descartar<br/>Medicamento</th>" : "")
                        + (getIdRol() == Consts.ROL_FARMACIA_APROBADOR ? "<th class='center'>Aprobar<br/>Descarte</th>" : "")
                        + "                </tr>"
                        + "              </thead>"
                        + "            <tbody>";

                do {
                    // Si existen resultados crea arreglo JSON para mostrar la lista de medicamentos.
                    this.setSerie(resultado.getString("Serie"));
                    this.setLote(resultado.getString("Lote"));
                    this.setNombre(resultado.getString("Nombre"));
                    this.setCantidad(resultado.getInt("Cantidad"));
                    this.setFechaVencimiento(resultado.getInt("FechaVencimiento"));
                    this.setPendienteAprobacion(resultado.getBoolean("PENDIENTE_APROBACION"));

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
            if (instanciaListaMedicamentos != null) {
                instanciaListaMedicamentos.desconectar();
            }

            instanciaListaMedicamentos = null;
            System.gc();
        }

        return respuesta;
    } // Fin del método cargarLista().

    // Carga el registro de medicamento con el código proporcionado.
    public void cargarRegistro(int idUsuario, String serie, String lote) {
        setSerie("-1");
        setIdUsuario(idUsuario);
        $ instanciaBD = null;

        try {
            instanciaBD = new $();
            if (serie.length() > 0) {
                instanciaBD = new $();
                String myQuery =
                        "SELECT I.Serie, I.Lote, CostoLote, CantidadIngresada, " +
                        "       I.CantidadDevuelta, FechaVencimiento, " +
                        "       CASE WHEN D.Serie IS NULL THEN 0 " +
                        "       ELSE 1 END AS PENDIENTE_APROBACION " +
                        "FROM Inventario_Medicamento I " +
                        "     LEFT JOIN Devolucion_Medicamento D " +
                        "       ON (I.Serie = D.Serie AND I.Lote = D.Lote) " +
                        "WHERE I.Serie = ? " +
                        "  AND I.Lote = ?;";

                PreparedStatement psMedicamento = instanciaBD.prepStatement(myQuery);
                psMedicamento.setString(1, serie);
                psMedicamento.setString(2, lote);
                ResultSet resultado = psMedicamento.executeQuery();

                if (resultado.next()) {
                    setSerie(serie);
                    setLote(lote);
                    setCosto(resultado.getFloat("CostoLote"));
                    setCantidad(resultado.getInt("CantidadIngresada"));
                    setCantidadDevuelta(resultado.getInt("CantidadDevuelta"));
                    setFechaVencimiento(resultado.getInt("FechaVencimiento"));
                    setPendienteAprobacion(resultado.getBoolean("PENDIENTE_APROBACION"));
                }
            } else {
                setSerie("");
                setLote("");
                setCosto(0);
                setCantidad(0);
                setCantidadDevuelta(0);
                setFechaVencimiento(0);
                setPendienteAprobacion(false);
            }
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
        ServiciosM modelo = new ServiciosM();
        String mensajeError;
        
        if (getSerie().equals("-1")) {
            mensajeError = "Ocurrió un error al cargar el registro del medicamento.";
        } else {
            mensajeError = getPendienteAprobacion() ? "" :
                    modelo.obtenerRetroalimentacionOperador(getIdUsuario(), getSerie(), getLote());
        }

        return
            "   \"error\": " + getSerie().equals("-1") + ", " +
            "   \"mensaje\": \"" + mensajeError + "\", " +
            "   \"codigo\": \"" + getLote() + "\", " +
            "   \"codigo2\": \"" + getSerie() + "\", " +
            "   \"costo\": " + getCosto() + ", " +
            "   \"cantidad\": " + getCantidad() + ", " +
            "   \"cantidadDevuelta\": " + getCantidadDevuelta() + ", " +
            "   \"fechaVencimiento\": \"" + ServiciosU.formatearFecha(getFechaVencimiento()) + "\", " +
            "   \"motivoDevolucion\": \"\" ";
    } // Fin del metodo toJSON().

    // Devuelve el registro con formato de tabla HTML.
    private String toHTML() {
        ServiciosM modelo = new ServiciosM();
        String mensajeRetroalimentacion =
                getPendienteAprobacion() ? "" :
                modelo.obtenerRetroalimentacionOperador(getIdUsuario(), getSerie(), getLote());
        
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
                + "    <a href='" + Consts.EXPEDIR_MEDICAMENTO_HTML + "' class='expedirMedicamento' "
                + "        data-codigo='" + getSerie() + Consts.SEPARADOR + getLote() + "' "
                + "      data-modo='mod-e'>"
                + "      <i class='material-icons'>playlist_play</i></a>"
                + "  </td>" : "")
                + (getIdRol() == Consts.ROL_FARMACIA_OPERADOR
                ? "  <td class='center' style='padding-top: 1%; padding-bottom: 1%; width: 7%; white-space: nowrap;'>"
                + ( getPendienteAprobacion() ?
                "      <i class='material-icons'>remove_circle_outline</i>" :
                "      <a href='" + Consts.DEVOLUCION_MEDICAMENTO_HTML + "' class='devolucionMedicamento' "
                + "        data-codigo='" + getSerie() + Consts.SEPARADOR + getLote() + "' "
                + "        data-modo='mod-e' " +
                           (mensajeRetroalimentacion.length() > 0 ? "style='color: red;'>" : ">")
                + "      <i class='material-icons'>remove_circle_outline</i></a>")
                + "  </td>" : "")
                + (getIdRol() == Consts.ROL_FARMACIA_APROBADOR
                ? "  <td class='center' style='padding-top: 1%; padding-bottom: 1%; width: 7%; white-space: nowrap;'>"
                + ( getPendienteAprobacion() ?
                "    <a href='" + Consts.APROBAR_DEVOLUCION_MEDICAMENTO_HTML + "' class='devolucionMedicamento' "
                + "        data-codigo='" + getSerie() + Consts.SEPARADOR + getLote() + "' "
                + "      data-modo='mod-e' style='color: red;'>"
                + "      <i class='material-icons'>error_outline</i></a>" :
                "      <i class='material-icons'>error_outline</i>")
                + "  </td>" : "")
                + "</tr>";
    } // Fin del método toHTML.
} // Fin de la clase Medicamento.