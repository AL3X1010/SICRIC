/*  Nombre.....:  PlanRehabilitacion.java
 *  Descripción:  Llama al procedimiento almacenado encargado de crear el plan
 *                de rehabilitación, anexar citas médica a planes para reposición
 *                y hacer check-out de sesión de rehabilitación.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Diciembre, 2019.
 */
package Controlador;

import Modelo.$;
import Utilitarios.Consts;
import Utilitarios.ServiciosU;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PlanRehabilitacion extends HttpServlet {
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    } // Fin del método processRequest().

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("index.html");    // redirige a página principal.
    } // Fin del método doGet().

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Variables para trabajar con tablas.
        $ instanciaBD = null;
        
        // Variables para enviar y recibir información.
        HttpSession sesion = request.getSession(false); // crea o recupera sesion
        String btnSubmit = request.getParameter("btnSubmit");
        final String WEB_PAGE = request.getParameter("page").split("\\?")[0].split("\\#")[0];

        if (btnSubmit != null) {
            try {
                String respuesta = "";
                
                if(WEB_PAGE.equals(Consts.CITA_MEDICA_HTML))
                    respuesta = crearCitaMedica(sesion, request, instanciaBD = new $());
                else if(WEB_PAGE.equals(Consts.PLAN_REHABILITACION_HTML))
                    respuesta = crearPlanRehabilitacion(sesion, request, instanciaBD = new $());
                else if(WEB_PAGE.equals(Consts.CHECKIN_HTML))
                    respuesta = registrarCitaMedica(sesion, request, instanciaBD = new $());
                
                response.sendRedirect(respuesta);
            } catch (Exception ex) {
                ex.printStackTrace();
                
                sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                    "Error al actualizar los datos."));
                sesion.setAttribute("errorPlanRehabilitacion", true);
                
                response.sendRedirect(WEB_PAGE);
            } finally {
                if (instanciaBD != null)
                    instanciaBD.desconectar();

                instanciaBD = null;
                System.gc();
            }
        } else {
            sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                    "En este momento no puede realizar cambios debido a inconsistencias en su sesión."));
            response.sendRedirect(WEB_PAGE);
        }
    } // Fin del metodo doPost().

    
    // Crea una cita médica para el plan, día y hora especificados.
    private String crearCitaMedica(HttpSession sesion, HttpServletRequest request, $ instanciaBD) throws ClassNotFoundException, SQLException {
        ResultSet registros = null; // contiene los registros que devuelve la sentencia SELECT.
        int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
        int rolAsignado = Integer.parseInt(sesion.getAttribute("idRol").toString());

        CallableStatement spCitaRehabilitacion = instanciaBD.storedProc(
                Consts.SP_CITA_PLAN_REHABILITACION,
                Consts.CANT_PARMS_SP_CITA_PLAN_REHABILITACION);

        // Prepara la infomacion requerida por el procedimento almacenado.
        spCitaRehabilitacion.setLong(1, Long.parseLong(sesion.getAttribute("idSesion").toString()));
        spCitaRehabilitacion.setInt(2, idCentro);
        // spCitaRehabilitacion.setLong(3, Long.parseLong(request.getParameter("numeroPlanRehabilitacion")));
        spCitaRehabilitacion.setLong(3, 1);
        spCitaRehabilitacion.setInt(4, ServiciosU.formatearFecha(request.getParameter("fechaCita")));
        spCitaRehabilitacion.setInt(5, ServiciosU.obtenerHora(request.getParameter("horaInicio")));
        spCitaRehabilitacion.setInt(6, ServiciosU.obtenerHora(request.getParameter("horaFinal")));
        spCitaRehabilitacion.setString(7, ServiciosU.obtenerParametro(request.getParameter("nota"), 500));
        spCitaRehabilitacion.setInt(8, Integer.parseInt(sesion.getAttribute("idUsuario").toString()));
        spCitaRehabilitacion.setLong(9, ServiciosU.fechaATimestamp());
        
        registros = spCitaRehabilitacion.executeQuery();
        if (registros.next()) {
            if (registros.getInt(1) == Consts.SQL_OK) {
                // Define a cual pagina se refirecciona... al configurar
                // el departamento debe redirigir a configurar el puesto.
                sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                    "Cambios sometidos satisfactoriamente."));
                return Consts.AGENDA_HTML;
            } else {
                ServiciosU.fb(registros);
            }
        }
        
        sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                            "Error al actualizar los datos."));
        sesion.setAttribute("errorPlanRehabilitacion", true);
        return Consts.CITA_MEDICA_HTML;
    }
    
    
    // Crea el plan de rehabilitación con los parámetros indicados
    private String crearPlanRehabilitacion(HttpSession sesion, HttpServletRequest request, $ instanciaBD)
            throws ClassNotFoundException, SQLException {
        ResultSet registros = null; // contiene los registros que devuelve la sentencia SELECT.
        int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
        
        // Crea el encabezado (informacion general) del plan de rehabilitación.
        int cantidadSesiones = Integer.parseInt(request.getParameter("cantidadSesiones"));
        CallableStatement spPlanRehabilitacion = instanciaBD.storedProc(
                Consts.SP_PLAN_REHABILITACION,
                Consts.CANT_PARMS_SP_PLAN_REHABILITACION);

        // Prepara la infomacion requerida por el procedimento almacenado.
        spPlanRehabilitacion.setLong(1, Long.parseLong(sesion.getAttribute("idSesion").toString()));
        spPlanRehabilitacion.registerOutParameter(2, java.sql.Types.BIGINT);
        spPlanRehabilitacion.setString(3, ServiciosU.obtenerParametro(
                request.getParameter("pacienteSeleccionado"), 15));
        spPlanRehabilitacion.setString(4, request.getParameter("terapeuta"));
        spPlanRehabilitacion.setInt(5, cantidadSesiones);
        spPlanRehabilitacion.setString(6, request.getParameter("modalidades"));
        spPlanRehabilitacion.setInt(7, Integer.parseInt(sesion.getAttribute("idUsuario").toString()));
        spPlanRehabilitacion.setLong(8, ServiciosU.fechaATimestamp());
        
        registros = spPlanRehabilitacion.executeQuery();
        if (registros.next() && registros.getInt(1) == Consts.SQL_OK) {
            // Recupera el resto de los parámetros provistos por el usuario.
            Long idPlanRehabilitacion = spPlanRehabilitacion.getLong(2);
            String fechaInicial = request.getParameter("fechaInicio");
            int horaInicio = Integer.parseInt(request.getParameter("horaInicio").replace(":", ""));
            int horaFinal = Integer.parseInt(request.getParameter("horaFinal").replace(":", ""));
            String frecuencia = request.getParameter("frecuencia");
            boolean lunes = request.getParameter("cbxLun") != null;
            boolean martes = request.getParameter("cbxMar") != null;
            boolean miercoles = request.getParameter("cbxMie") != null;
            boolean jueves = request.getParameter("cbxJue") != null;
            boolean viernes = request.getParameter("cbxVie") != null;
            boolean sabado = request.getParameter("cbxSab") != null;
            boolean domingo = request.getParameter("cbxDom") != null;

            if(frecuencia.equals("di")){ // frecuencia diaria
                lunes = true;
                martes = true;
                miercoles = true;
                jueves = true;
                viernes = true;
                sabado = true;
                domingo = true;
            }
            
            
            // Procede a crear cada una de las sesiones que componen el plan
            // de rehabilitación según los parámetros recuperados líneas arriba.
            int[] informacionFecha = ServiciosU.getLocalDate(fechaInicial);
            LocalDate date = LocalDate.of(informacionFecha[0], informacionFecha[1], informacionFecha[2]);

            java.time.DayOfWeek startingDay = date.getDayOfWeek();
            date = date.minusDays(1);
            boolean jumpTo = true;
            int consecutivo = 1;
            
            CallableStatement spDetallePlanRehabilitacion = instanciaBD.storedProc(
                    Consts.SP_DET_PLAN_REHABILITACION,
                    Consts.CANT_PARMS_SP_DET_PLAN_REHABILITACION);

            // Prepara la infomacion requerida por el procedimento almacenado.
            spDetallePlanRehabilitacion.setLong(1, idCentro);
            spDetallePlanRehabilitacion.setLong(2, idPlanRehabilitacion);
            spDetallePlanRehabilitacion.setString(3, request.getParameter("terapeuta"));
            spDetallePlanRehabilitacion.setInt(7, horaInicio);
            spDetallePlanRehabilitacion.setInt(8, horaFinal);
            spDetallePlanRehabilitacion.setInt(9, Integer.parseInt(sesion.getAttribute("idUsuario").toString()));
            spDetallePlanRehabilitacion.setLong(10, ServiciosU.fechaATimestamp());
            
            // Recupera la fecha que sigue según la selección de días para
            // llamar al procedimiento almacenado encargado de crear la cita.
            while(cantidadSesiones > 0){
                if(startingDay == DayOfWeek.MONDAY || (!jumpTo && lunes)){
                    date = date.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
                    System.out.println(date.toString());
                    jumpTo = false;
                    spDetallePlanRehabilitacion.setInt(4, consecutivo);
                    spDetallePlanRehabilitacion.setInt(5, Calendar.MONDAY-1);
                    spDetallePlanRehabilitacion.setInt(6,
                            Integer.parseInt(date.toString().replace("-", "")));

                    ResultSet registrosDetalle = spDetallePlanRehabilitacion.executeQuery();
                    if(registrosDetalle.next() && registrosDetalle.getInt(1) == Consts.SQL_OK
                            && --cantidadSesiones == 0)
                        break;
                    else if (registrosDetalle.getInt(1) == Consts.SQL_OK)
                        consecutivo++;
                }

                if(startingDay == DayOfWeek.TUESDAY || (!jumpTo && martes)){
                    date = date.with(TemporalAdjusters.next(DayOfWeek.TUESDAY));
                    System.out.println(date.toString());
                    jumpTo = false;
                    spDetallePlanRehabilitacion.setInt(4, consecutivo);
                    spDetallePlanRehabilitacion.setInt(5, Calendar.TUESDAY-1);
                    spDetallePlanRehabilitacion.setInt(6,
                            Integer.parseInt(date.toString().replace("-", "")));

                    ResultSet registrosDetalle = spDetallePlanRehabilitacion.executeQuery();
                    if(registrosDetalle.next() && registrosDetalle.getInt(1) == Consts.SQL_OK
                            && --cantidadSesiones == 0)
                        break;
                    else if (registrosDetalle.getInt(1) == Consts.SQL_OK)
                        consecutivo++;
                }

                if(startingDay == DayOfWeek.WEDNESDAY || (!jumpTo && miercoles)){
                    date = date.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
                    System.out.println(date.toString());
                    jumpTo = false;
                    spDetallePlanRehabilitacion.setInt(4, consecutivo);
                    spDetallePlanRehabilitacion.setInt(5, Calendar.WEDNESDAY-1);
                    spDetallePlanRehabilitacion.setInt(6,
                            Integer.parseInt(date.toString().replace("-", "")));

                    ResultSet registrosDetalle = spDetallePlanRehabilitacion.executeQuery();
                    if(registrosDetalle.next() && registrosDetalle.getInt(1) == Consts.SQL_OK
                            && --cantidadSesiones == 0)
                        break;
                    else if (registrosDetalle.getInt(1) == Consts.SQL_OK)
                        consecutivo++;
                }

                if(startingDay == DayOfWeek.THURSDAY || (!jumpTo && jueves)){
                    date = date.with(TemporalAdjusters.next(DayOfWeek.THURSDAY));
                    System.out.println(date.toString());
                    jumpTo = false;
                    spDetallePlanRehabilitacion.setInt(4, consecutivo);
                    spDetallePlanRehabilitacion.setInt(5, Calendar.THURSDAY-1);
                    spDetallePlanRehabilitacion.setInt(6,
                            Integer.parseInt(date.toString().replace("-", "")));

                    ResultSet registrosDetalle = spDetallePlanRehabilitacion.executeQuery();
                    if(registrosDetalle.next() && registrosDetalle.getInt(1) == Consts.SQL_OK
                            && --cantidadSesiones == 0)
                        break;
                    else if (registrosDetalle.getInt(1) == Consts.SQL_OK)
                        consecutivo++;
                }

                if(startingDay == DayOfWeek.FRIDAY || (!jumpTo && viernes)){
                    date = date.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
                    System.out.println(date.toString());
                    jumpTo = false;
                    spDetallePlanRehabilitacion.setInt(4, consecutivo);
                    spDetallePlanRehabilitacion.setInt(5, Calendar.FRIDAY-1);
                    spDetallePlanRehabilitacion.setInt(6,
                            Integer.parseInt(date.toString().replace("-", "")));

                    ResultSet registrosDetalle = spDetallePlanRehabilitacion.executeQuery();
                    if(registrosDetalle.next() && registrosDetalle.getInt(1) == Consts.SQL_OK
                            && --cantidadSesiones == 0)
                        break;
                    else if (registrosDetalle.getInt(1) == Consts.SQL_OK)
                        consecutivo++;
                }

                if(startingDay == DayOfWeek.SATURDAY || (!jumpTo && sabado)){
                    date = date.with(TemporalAdjusters.next(DayOfWeek.SATURDAY));
                    System.out.println(date.toString());
                    jumpTo = false;
                    spDetallePlanRehabilitacion.setInt(4, consecutivo);
                    spDetallePlanRehabilitacion.setInt(5, Calendar.SATURDAY-1);
                    spDetallePlanRehabilitacion.setInt(6,
                            Integer.parseInt(date.toString().replace("-", "")));

                    ResultSet registrosDetalle = spDetallePlanRehabilitacion.executeQuery();
                    if(registrosDetalle.next() && registrosDetalle.getInt(1) == Consts.SQL_OK
                            && --cantidadSesiones == 0)
                        break;
                    else if (registrosDetalle.getInt(1) == Consts.SQL_OK)
                        consecutivo++;
                }

                if(startingDay == DayOfWeek.SUNDAY || (!jumpTo && domingo)){
                    date = date.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
                    System.out.println(date.toString());
                    jumpTo = false;
                    spDetallePlanRehabilitacion.setInt(4, consecutivo);
                    spDetallePlanRehabilitacion.setInt(5, 7); // día de la semana
                    spDetallePlanRehabilitacion.setInt(6,
                            Integer.parseInt(date.toString().replace("-", "")));

                    ResultSet registrosDetalle = spDetallePlanRehabilitacion.executeQuery();
                    if(registrosDetalle.next() && registrosDetalle.getInt(1) == Consts.SQL_OK
                            && --cantidadSesiones == 0)
                        break;
                    else if (registrosDetalle.getInt(1) == Consts.SQL_OK)
                        consecutivo++;
                }
            }
            
            // Define a cual pagina se refirecciona... al configurar
            // el departamento debe redirigir a configurar el puesto.
            sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                "Se ha creado el plan de rehabilitación satisfactoriamente."));
            sesion.setAttribute("codigoPlanRehabilitacion", idPlanRehabilitacion);
            return Consts.ASIGNAR_MODALIDAD_HTML;
        } else {
            ServiciosU.fb(registros);
        }
        
        sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                            "Error al actualizar los datos."));
        sesion.setAttribute("errorPlanRehabilitacion", true);
        return Consts.PLAN_REHABILITACION_HTML;
    }
    
    
    // Registra la sesión de rehabilitación como completada.
    private String registrarCitaMedica(HttpSession sesion, HttpServletRequest request, $ instanciaBD) throws ClassNotFoundException, SQLException {
        ResultSet registros = null; // contiene los registros que devuelve la sentencia SELECT.
        int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
        int rolAsignado = Integer.parseInt(sesion.getAttribute("idRol").toString());

        CallableStatement spRegistroCitaRehabilitacion = instanciaBD.storedProc(
                Consts.SP_CHECK_IN_PLAN_REHABILITACION,
                Consts.CANT_PARMS_SP_CHECK_IN_PLAN_REHABILITACION);

        // Prepara la infomacion requerida por el procedimento almacenado.
        spRegistroCitaRehabilitacion.setLong(1, Long.parseLong(sesion.getAttribute("idSesion").toString()));
        spRegistroCitaRehabilitacion.setInt(2, idCentro);
        // spCitaRehabilitacion.setLong(3, Long.parseLong(request.getParameter("numeroPlanRehabilitacion")));
        spRegistroCitaRehabilitacion.setLong(3, Long.parseLong(request.getParameter("planRehabilitacion")));
        spRegistroCitaRehabilitacion.setInt(4, Integer.parseInt(request.getParameter("fechaAtencion")));
        spRegistroCitaRehabilitacion.setInt(5, Integer.parseInt(request.getParameter("horaInicio")));
        spRegistroCitaRehabilitacion.setInt(6, Integer.parseInt(request.getParameter("horaFinal")));
        spRegistroCitaRehabilitacion.setString(7, request.getParameter("evaluacionFinal"));
        spRegistroCitaRehabilitacion.setString(8, request.getParameter("nuevasIndicaciones"));
        spRegistroCitaRehabilitacion.setString(9, request.getParameter("cbxAltaMedica") != null ? "1" : "0");
        spRegistroCitaRehabilitacion.setInt(10, Integer.parseInt(
                    String.valueOf(ServiciosU.fechaATimestamp()).substring(0, 8)));
        
        
        
        // spCitaRehabilitacion.setInt(5, ServiciosU.obtenerHora(request.getParameter("horaInicio")));
        // spCitaRehabilitacion.setInt(6, ServiciosU.obtenerHora(request.getParameter("horaFinal")));
       //  spCitaRehabilitacion.setString(7, ServiciosU.obtenerParametro(request.getParameter("nota"), 500));
        // spCitaRehabilitacion.setInt(8, Integer.parseInt(sesion.getAttribute("idUsuario").toString()));
        // spCitaRehabilitacion.setLong(9, ServiciosU.fechaATimestamp());
        
        registros = spRegistroCitaRehabilitacion.executeQuery();
        if (registros.next()) {
            if (registros.getInt(1) == Consts.SQL_OK) {
                // Define a cual pagina se refirecciona... al configurar
                // el departamento debe redirigir a configurar el puesto.
                sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                    "Cambios sometidos satisfactoriamente."));
                return Consts.AGENDA_HTML;
            } else {
                ServiciosU.fb(registros);
            }
        }
        
        sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                            "Error al actualizar los datos."));
        sesion.setAttribute("errorPlanRehabilitacion", true);
        return Consts.CITA_MEDICA_HTML;
    }
}