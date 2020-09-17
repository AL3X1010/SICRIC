/*  Nombre.....:  HistorialMedico.java
 *  Descripción:  Carga los registros de los planes de rehabilitación.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Diciembre, 2019.
 */
package Modelo;

import Utilitarios.Consts;
import Utilitarios.ServiciosU;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HistorialMedico {

    // Constantes de la clase HistorialMedico.
    private final String PAGINA_WEB = Consts.DEPARTAMENTO_HTML;
    private final String TITULO = Consts.TITULO_LISTA_DEPARTAMENTOS;
    private final String ETIQUETA_EXISTENTE = Consts.ETIQUETA_EXISTENTE_DEPARTAMENTO;
    private final String ETIQUETA_NUEVO = Consts.ETIQUETA_NUEVO_DEPARTAMENTO;

    // Variables de la clase HistorialMedico.
    private String idPaciente;

    // Método constructor de la clase HistorialMedico.
    public HistorialMedico() {
        this.idPaciente = "";
    } // Fin del método constructor.

    // Metodos GET y SET de la clase HistorialMedico.
    public String getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(String idPaciente) {
        this.idPaciente = idPaciente;
    }

    // Métodos utilitarios de la clase HistorialMedico.
    // Carga la lista de todos los departamentos de la empresa, activos e inactivos.
    public void cargarHistorialMedico(String idPaciente, String[] respuesta) {
        $ instanciaBD = null;
        String respuestaLI = "";
        String respuestaDivPlan = "";
        String respuestaDivContent = "";

        try {
            // Realiza consulta de todos los planes a la BD a la base de datos.
            instanciaBD = new $();
            int numeroPlanRehabilitacion;
            int cantidadSesiones;
            int fechaInicioPlan = 0;
            int fechaFinalizacionPlan = 0;
            
            String myQuery
                    = "SELECT ID_PlanRehabilitacion, CantidadSesiones "
                    + "FROM Plan_Rehabilitacion "
                    + "WHERE ID_Paciente = ? "
                    + "ORDER BY ID_PlanRehabilitacion;";

            PreparedStatement psPlanesRehabilitacion = instanciaBD.prepStatement(myQuery);
            psPlanesRehabilitacion.setString(1, idPaciente);
            ResultSet resultadoPlanesRehabilitacion = psPlanesRehabilitacion.executeQuery();
            while (resultadoPlanesRehabilitacion.next()) {
                numeroPlanRehabilitacion = resultadoPlanesRehabilitacion.getInt("ID_PlanRehabilitacion");
                cantidadSesiones = resultadoPlanesRehabilitacion.getInt("CantidadSesiones");

                // Agrega número del plan de rehabilitación a la lista de LI (encabezado).
                respuestaLI +=
                        "<li class='tab'>"
                        + "  <a href='#plan" + numeroPlanRehabilitacion + "'>"
                        + "    Plan " + numeroPlanRehabilitacion + "</a></li>";

                // Busca información de resumen del plan de rehabilitación.
                ResultSet resultadoFechasPlan = instanciaBD.execQry(
                        "SELECT COUNT(*) AS CantidadSesiones, " +
                        "       MIN(C.Fecha) AS FechaInicio, " +
                        "       MAX(C.Fecha) AS FechaFinal "
                        + "FROM Cita_Plan_Rehabilitacion C "
                        + "WHERE ID_PlanRehabilitacion = " + numeroPlanRehabilitacion + ";");
                if (resultadoFechasPlan.next()) {
                    cantidadSesiones = resultadoFechasPlan.getInt("CantidadSesiones");
                    fechaInicioPlan = resultadoFechasPlan.getInt("FechaInicio");
                    fechaFinalizacionPlan = resultadoFechasPlan.getInt("FechaFinal");
                }
                
                // Costruye la cadena de texto de respuesta conteniendo el HTML
                // que dara forma a la pantalla que visualiza el usuario.
                respuestaDivContent = "<div class='myContent' id='content" + numeroPlanRehabilitacion + "'>";
                respuestaDivPlan +=
                        "<div id='plan" + numeroPlanRehabilitacion + "' class='row' style='margin-top: -1%;'>"
                        + "  <div class='input-field col s12'>"
                        + "    <table style='border-collapse: collapse;'>"
                        + "      <tbody>"
                        + "        <tr style='border-bottom: none;'>"
                        + "          <td colspan='3' style='padding-left:0; padding-top:0; padding-bottom:0.4%;'>"
                        + "            Número de plan: <a>" + numeroPlanRehabilitacion + "</a></td></tr>"
                        + "        <tr style='border-bottom: none;'>"
                        + "          <td style='width:40%; padding-left:0; padding-top:0.7%; padding-bottom:0.7%;'>"
                        + "            Asistencias/sesiones: $#asistencias#$/" + cantidadSesiones + "</td>"
                        + "          <td style='width:30%; padding-left:0; padding-top:0.7%; padding-bottom:0.7%;'>"
                        + "            Fecha de inicio: " + ServiciosU.formatearFecha(fechaInicioPlan) + "</td>"
                        + "          <td style='width:36%; padding-left:0; padding-top:0.7%; padding-bottom:0.7%;'>"
                        + "            Fecha de finalización: " + ServiciosU.formatearFecha(fechaFinalizacionPlan) + "</td></tr>"
                        + "        <tr style='border-bottom: none;'>"
                        + "          <td colspan='3' style='padding-left:0; padding-top:0; padding-bottom:0.7%;' class='valing-wrapper'>"
                        + "            Registro de citas: &nbsp;";

                // Recupera el registro de todas las sesiones médicas que componen el
                // plan de rehabilitación.
                int numeroSesion = 1;
                int numeroAsistencias = 0;
                ResultSet resultadoCitasMedicas = instanciaBD.execQry(
                        "SELECT Consecutivo, Fecha, HoraInicio, HoraFinal, Asistio, "
                        + "       MotivoAusencia, Observaciones, FechaModificacion "
                        + "FROM Cita_Plan_Rehabilitacion C "
                        + "WHERE ID_PlanRehabilitacion = " + numeroPlanRehabilitacion + " "
                        + "ORDER BY Fecha, HoraInicio, HoraFinal;");
                
                while (resultadoCitasMedicas.next()) {
                    int fechaSesion = resultadoCitasMedicas.getInt("Fecha");
                    int horaInicio = resultadoCitasMedicas.getInt("HoraInicio");
                    int horaFinal = resultadoCitasMedicas.getInt("HoraFinal");
                    int asistio = resultadoCitasMedicas.getInt("Asistio");
                    String motivoAusencia = resultadoCitasMedicas.getString("MotivoAusencia");
                    String observaciones = resultadoCitasMedicas.getString("Observaciones");
                    Long fechaModificacion = resultadoCitasMedicas.getLong("FechaModificacion");
                    
                    // 0: ausencia sin excusa, 1: ausencia con excusa, 2: asistio
                    numeroAsistencias = asistio == 2 ? ++numeroAsistencias : numeroAsistencias;
                    String textoAsistencias = (numeroSesion < 10 ? "0" + numeroSesion : "" + numeroSesion);
                    String colorBoton = asistio == 2 ? "green" :
                                       (asistio == 1 ? "grey" : 
                                       (fechaModificacion > 0 ? "red" : "disabled"));
                    
                    // Anexa información recuperada de cada una de las citas médicas
                    // a respuesta con formato HTML.
                    respuestaDivContent +=
                    "<div id='cita" + numeroPlanRehabilitacion + "_" + textoAsistencias + "'" +
                    "     class='row detalle-cita' style='margin-top: -3.9%; margin-bottom: -2%;'>" +
                    "  <div class='input-field col s2'>" +
                    "    <label>Sesión " + numeroSesion + "/" + cantidadSesiones + "</label></div>" +
                    "  <div class='input-field col s10' style='margin-bottom: -2%;'>" +
                    "    <input type='text' class='campoVacio' readonly style='border-bottom-color: white;'/></div>" +
                    "  <div class='input-field col s4'>" +
                    "    <input type='text' class='campoVacio' readonly " +
                    "           value='" + ServiciosU.formatearFecha(fechaSesion) + "'/>" +
                    "    <label class='active'>Fecha de atención</label></div>" +
                    "  <div class='input-field col s4'>" +
                    "    <input type='text' class='campoVacio' readonly" +
                    "           value='" + ServiciosU.formatearHora(horaInicio) + "'/>" +
                    "    <label class='active'>Hora inicio</label></div>" +
                    "  <div class='input-field col s4'>" +
                    "    <input type='text' class='campoVacio' readonly " +
                    "           value='" + ServiciosU.formatearHora(horaFinal) + "'/>" +
                    "    <label class='active'>Hora final</label></div>" +
                    "  <div class='switch input-field col s4'>" +
                    "    <label>" +
                    "      El paciente: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Faltó" +
                    "      <input type='checkbox'" + (asistio == 2 ? " checked" : "") + "/>" +
                    "      <span class='lever'></span>Asistió</label></div>" +
                    "  <div class='input-field col s8'>" +
                    "    <textarea class='materialize-textarea' readonly" +
                    "             >" + (asistio == 2 ? "N/A" : motivoAusencia) + "</textarea>" +
                    "    <label class='active'>Motivo de la ausencia</label></div>" +
                    "  <div class='input-field col s12'>" +
                    "    <textarea class='materialize-textarea' readonly" +
                    "             >" + observaciones + "</textarea>" +
                    // "    <label class='active'>Notas</label></div></div>" +
                    "    <label class='active'>Notas</label></div>" +
                    "</div>";
                    
                    respuestaDivPlan += 
                        "<a data-appointment='" + textoAsistencias + "'"
                        + "    data-plan='" + numeroPlanRehabilitacion + "'"
                        + "    data-related='content" + numeroPlanRehabilitacion + "'"
                        + "    class='" + colorBoton + " btn-floating btn-small waves-effect"
                        + "    waves-light btn-flat appointment-detail'"
                        + "    style='color: white; font-weight: bold; margin-left: 0.5%;'" +
                               (colorBoton.equals("disabled") ? " disabled": "") +
                            ">" + numeroSesion++ + "</a>";
                }
                
                // Completa estructura del HTML que contiene la información del plan de rehabilitación.
                respuestaDivPlan = respuestaDivPlan.replace("$#asistencias#$", "" + numeroAsistencias);
                respuestaDivPlan += "</td></tr></tbody></table></div>" + respuestaDivContent + "</div></div>";
                
                
                
                // ResultSet resultado = instanciaBD.execQry(myQuery);
            }

            System.out.println(respuestaDivPlan);
            respuesta[0] = respuestaLI;
            respuesta[1] = respuestaDivPlan;
        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            if (instanciaBD != null)
                instanciaBD.desconectar();

            instanciaBD = null;
            System.gc();
        }
    } // Fin del método cargarHistorialMedico().

    /*
    // Carga el registro de departamento con el código proporcionado.
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
    } // Fin del metodo toJSON().

    // Devuelve el registro con formato de tabla HTML.
    private String toHTML() {
        return "<tr style='border: none;' class='resaltar'>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + getNombre() + "</td>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + getDescripcion() + "</td>" +
               "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'>" +
               "    <i class='material-icons'>" + 
                      (getActivo() ? "check_box": "check_box_outline_blank") + 
               "    </i></a>" +
               "  </td>" +
               ( getModoVisualizacion() ? 
               "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'><a href='" + PAGINA_WEB + "' class='" + ETIQUETA_EXISTENTE + "' " +
               "      data-codigo='" + getIdDepartamento() + "' " +
               "      data-modo='mod-v'>" +
               "    <i class='material-icons'>visibility</i></a>" +
               "  </td>" : "" ) +
               ( getModoEdicion() ? 
               "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'><a href='" + PAGINA_WEB + "' class='" + ETIQUETA_EXISTENTE + "' " +
               "      data-codigo='" + getIdDepartamento() + "' " +
               "      data-modo='mod-e'>" +
               "    <i class='material-icons'>edit</i></a>" +
               "  </td>" : "" ) +
               "</tr>";
    } // Fin del método toHTML.*/
} // Fin de la clase Departamento.
