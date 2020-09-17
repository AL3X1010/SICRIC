/*  Nombre.....:  ServiciosC.java
 *  Descripción:  Ofrecer lógica de diferentes servicios a los servlets que lo
 *                requieran.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Julio, 2019.
 */
package Controlador;

import Modelo.Modalidad;
import Modelo.ServiciosM;
import Utilitarios.Consts;
import Utilitarios.ServiciosU;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Locale;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ServiciosC extends HttpServlet {

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
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    
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
        String servicioSolicitado = request.getParameter("name");
        PrintWriter out = response.getWriter();
        HttpSession sesion = request.getSession(false); // crea o recupera sesión
        String respuestaServ = "";
        
        
        /* Todos los métodos de AJAX están identificados por el atributo NAME,
           esta página se encarga de brindar servicios a través de AJAX, si se
           ingresa directamente a este Servlet dicho atributo es NULL. */
        if (servicioSolicitado != null) {
            /* Servicio..............: Get Message
               Página que lo utiliza.: index.html, lobby.html
               Función...............: Obtiene cualquier mensaje de retroalimentación
                                       para el usuario, si lo hay. */
            if (servicioSolicitado.equals("LGCNT")) {
                String directorioRaiz = System.getProperty("catalina.home");
                String idCentro = String.format("%05d",
                        Integer.parseInt(sesion.getAttribute("idCentro").toString()));
                String nombreArchivoGuardado = Consts.PREFIJO_CENTRO + idCentro
                        + Consts.EXTENSION_PERMITIDA_CENTRO;
                File file = new File(
                        directorioRaiz + File.separator + Consts.CARPETA_APP
                        + File.separator + Consts.SUBCARPETA_APP + idCentro
                        + File.separator + Consts.CARPETA_CENTRO
                        + File.separator + nombreArchivoGuardado);

                if (file.exists()) {
                    InputStream fileInputStream = new FileInputStream(file);
                    String mymeType = getServletContext().getMimeType(file.getAbsolutePath());
                    response.setContentType(mymeType != null ? mymeType : "application/octet-stream");
                    response.setContentLength((int) file.length());
                    response.setHeader("Content-Disposition", "attachment; name=\"" + nombreArchivoGuardado + "\"");
                }
            } else


            /* Servicio..............: Get Events Informatiom
               Página que lo utiliza.: agenda.html
               Función...............: Recurpera todas las citas de rehabilitación
                                       para desplegarlas en la agenda de sesiones. */
            if (servicioSolicitado.equals("GETEVTINF")) {
                // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
                try {
                    // Valida si las variables de sesión aún están activas. En caso de haber pasado
                    // el tiempo establecido sin hacer ninguna acción redirige al index.html.
                    if (sesion.getAttribute("idCentro") == null) {
                        sesion.setAttribute("mensaje", "Su sesión ha expirado.");
                        respuestaServ =
                                "{ \"error\": true, " +
                                "  \"mensaje\": \"Error con la información su sesión.\" }";
                    } else {
                        int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                        ServiciosM modelo = new ServiciosM();
                        respuestaServ =
                                "{ \"error\": false, " +
                                "  \"mensaje\": \"\", " +
                                "  \"eventsData\":  {" +
                                                        modelo.obtenerSesionesAgendadas(idCentro) +
                                        "           } " +
                                "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                                "{ \"error\": true, " +
                                "  \"mensaje\": \"Error al cargar las sesiones de rehabilitación.\" }";
                }
                
                out.print(ServiciosU.eliminarCaracteresEspeciales(respuestaServ));
                // </editor-fold>
            } else
                
                    
            /* Servicio..............: Get Message
               Página que lo utiliza.: *all.html
               Función...............: Obtiene cualquier mensaje de retroalimentación
                                       para el usuario, si lo hay. */
            if (servicioSolicitado.equals("GETMSG")) {
                // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
                respuestaServ = sesion.getAttribute("mensaje") != null ?
                                ServiciosU.sustituirCaracteres(String.valueOf(
                                sesion.getAttribute("mensaje")).trim()) :
                                "";
                sesion.setAttribute("mensaje", "");

                if (request.getParameter("page") != null)
                    if (request.getParameter("page").equals(Consts.INDEX_HTML)) {
                        sesion.removeAttribute("idCentro");
                        sesion.removeAttribute("idSesion");
                        sesion.removeAttribute("idUsuario");
                        sesion.removeAttribute("idRol");
                    }

                out.print(respuestaServ);
                // </editor-fold>
            } else
            
            
            /* Servicio..............: Get Session Information
                Página que lo utiliza.: agenda.html
                Función...............: Obtiene información relacionada a la
                                        sesión: usuario, rol, entre otros. */
             if (servicioSolicitado.equals("GETSSNI")) {
                // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
                boolean[] accesos = {false, false};

                String paginaWeb = request.getParameter("page").split("#")[0];
                if (paginaWeb.lastIndexOf(".") > 0)
                    if (paginaWeb.substring(paginaWeb.lastIndexOf(".")).equals(".do"))
                        paginaWeb = paginaWeb.substring(0, paginaWeb.lastIndexOf(".")) + ".html";

                // Valida si las variables de sesión aún están activas. En caso de haber pasado
                // el tiempo establecido sin hacer ninguna acción redirige al index.html.
                if (sesion.getAttribute("idSesion") == null || sesion.getAttribute("idUsuario") == null) {
                    sesion.setAttribute("mensaje", "Su sesión ha expirado.");
                    out.print("{ \"error\": true }");
                } else if (sesion.getAttribute("idSesion") != null
                        && sesion.getAttribute("idSesion").toString().trim().length() == 0) {
                    sesion.removeAttribute("idSesion");
                    sesion.setAttribute("mensaje", "Acceso denegado.");
                    out.print("{ \"error\": true }");
                } else if (sesion.getAttribute("idUsuario") != null
                        && sesion.getAttribute("idUsuario").toString().trim().length() == 0) {
                    sesion.setAttribute("mensaje", "Error al cargar su sesión.");
                    sesion.removeAttribute("idUsuario");
                    out.print("{ \"error\": true }");
                } else if (ServiciosM.tieneAcceso(
                        Integer.parseInt(sesion.getAttribute("idCentro").toString()),
                        Integer.parseInt(sesion.getAttribute("idUsuario").toString()),
                        paginaWeb, accesos)) {
                    if( (!accesos[0] && !accesos[1])
                            // Estas páginas solo permiten edición
                            || (paginaWeb.equals(Consts.AGENDA_HTML) && !accesos[1])
                            || (paginaWeb.equals(Consts.APROBAR_DEVOLUCION_MEDICAMENTO_HTML) && !accesos[1])
                            || (paginaWeb.equals(Consts.CITA_MEDICA_HTML) && !accesos[1])
                            || (paginaWeb.equals(Consts.DEVOLUCION_MEDICAMENTO_HTML) && !accesos[1])
                            || (paginaWeb.equals(Consts.PLAN_REHABILITACION_HTML) && !accesos[1])


                            ){
                        sesion.setAttribute("mensaje", "No tiene acceso a la página web solicitada.");
                        out.print("{ \"error\": true }");
                        return;
                    }

                    ServiciosM modelo = new ServiciosM();
                    modelo.registrarAccion(
                            Integer.parseInt(sesion.getAttribute("idSesion").toString()),
                            paginaWeb, "", Consts.ACCION_VISUALIZACION);

                    out.print("{ \"error\": false }");
                } else {
                    sesion.setAttribute("mensaje", "Acceso denegado.");
                    out.print("{ \"error\": true }");
                }
                // </editor-fold>
             } else


            /* Servicio..............: Set Rehabilitation Session Information
               Página que lo utiliza.: agenda.html
               Función...............: Establece los valores de expediente,
                                       hora de inicio y final de la sesión
                                       de rehabilitación para mostrarle al usuario
                                       la información de las modalidades 
                                       que se deben llevar a cabo para dicho plan. */
            if (servicioSolicitado.equals("SETREHSSNINF")) {
                // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
                try {
                    if(request.getParameter("file") != null
                            && request.getParameter("start") != null
                            && request.getParameter("end") != null){
                        sesion.setAttribute("expedienteAgenda", request.getParameter("file"));
                        sesion.setAttribute("fechaAgenda", ServiciosU.formatearFecha(
                                request.getParameter("start"), true));
                        sesion.setAttribute("horaInicioAgenda", ServiciosU.formatearFecha(
                                request.getParameter("start"), false));
                        sesion.setAttribute("horaFinalAgenda", ServiciosU.formatearFecha(
                                request.getParameter("end"), false));
                        
                        respuestaServ =
                                "{ \"error\": false, " +
                                "  \"mensaje\": \"\" }";
                    } else 
                        respuestaServ =
                                "{ \"error\": true, " +
                                "  \"mensaje\": \"Error al cargar parámetros.\" }";
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                                "{ \"error\": true, " +
                                "  \"mensaje\": \"Error al cargar parámetros.\" }";
                }
                
                out.print(respuestaServ);
                // </editor-fold>
            }
        } else {
            response.sendRedirect(Consts.INDEX_HTML);    // redirige a página principal.
        }
    } // Fin del método doGet().
    
    
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
        // Variables para enviar y recibir información.
        HttpSession sesion = request.getSession(true); // crea o recupera sesión
        PrintWriter out = response.getWriter();

        try {
            String nombreServ = request.getParameter("name"); // contiene el nombre de servicio al que se desea acceder
            String respuestaServ; // contiene la respuesta que brinda el servicio

            /* Todos los métodos de AJAX están identificados por el atributo NAME,
               esta página se encarga de brindar servicios a través de AJAX, si se
               ingresa directamente a este Servlet dicho atributo es NULL. */
            if (nombreServ != null) {
                /* Servicio..............: Close session
                   Página que lo utiliza.: *all.html
                   Función...............: Cierra la sesión eliminado todas las
                                           variables relacionadas. */
                if (nombreServ.equals("CLOSSN")) {
                    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
                    respuestaServ = "true";
                    
                    sesion.removeAttribute("idCentro");
                    sesion.removeAttribute("idSesion");
                    sesion.removeAttribute("idUsuario");
                    sesion.removeAttribute("idRol");

                    out.print(respuestaServ);
                    // </editor-fold>
                } else
                
                    
                /* Servicio..............: Get Access Map
                   Página que lo utiliza.: usuario.html
                   Función...............: Obtiene la lista de todas las p{aginas
                                           web a las que puede acceder el rol
                                           indicado. */
                if (nombreServ.equals("GETACCMAP")) {
                    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
                    respuestaServ = "";
                    
                    if (sesion.getAttribute("idUsuario") != null) {
                        ServiciosM modelo = new ServiciosM();
                        int idUsuario = Integer.parseInt(sesion.getAttribute("idUsuario").toString());
                        int idRolAsignado = Integer.parseInt(sesion.getAttribute("idRol").toString());
                        int idRolProporcionado = Integer.parseInt(request.getParameter("code"));
                        
                        if(idRolAsignado == idRolProporcionado)
                            respuestaServ = modelo.cargarMapaAccesos(idUsuario, idRolAsignado);
                        else
                            respuestaServ = modelo.cargarMapaAccesos(0, idRolProporcionado);
                        
                        respuestaServ = 
                                "{" +
                                "   \"error\": false, " +
                                "   \"mapaAccesos\": \"" + respuestaServ + "\" }";
                    }
                    
                    out.print(ServiciosU.sustituirCaracteres(respuestaServ));
                    // </editor-fold>
                } else
                
                
                /* Servicio..............: Get Job List
                   Página que lo utiliza.: empleado.html
                   Función...............: Obtiene la lista de puestos de trabajo que puede
                                           ocupar un empleado. */
                if (nombreServ.equals("GETJOBL")) {
                    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
                    respuestaServ = "{ \"error\": true }";
                    
                    if (request.getParameter("code") != null) {
                        int[] idDepartamento = {Integer.parseInt(request.getParameter("code"))};
                        ServiciosM modelo = new ServiciosM();
                        respuestaServ =
                            "{ \"error\": false, " +
                            "  \"respuesta\": \"" +
                                modelo.elementoSELECTPuesto(0, idDepartamento, 0) + "\"" +
                            "}";
                    }

                    out.print(respuestaServ);    
                    // </editor-fold>
                } else
                
                    
                /* Servicio..............: Get Logo
                   Página que lo utiliza.: index.html
                   Función...............: Recupera la ubicacion donde esta
                                           almacenado el logotipo de la empresa
                                           y devuelve dicha ubicacion codificada en base64. */
                if (nombreServ.equals("GETLOGO")) {
                    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
                    respuestaServ = "";
                    
                    if (request.getParameter("page") != null){
                        ServiciosM modelo = new ServiciosM();
                        respuestaServ =
                                " { \"error\": false, " +
                                "   \"url\": \"" + ServiciosU.encryptBase64(
                                                   modelo.obtenerLogo() ) + "\" }";
                    
                    } else {
                        respuestaServ = " { \"error\": true, " +
                                        "   \"mensaje\": \"\" } ";
                    }
                    
                    out.print(respuestaServ);
                    // </editor-fold>
                } else 
                
                    
                /* Servicio..............: Get Medicine Detail
                   Página que lo utiliza.: expedirmedicamento.html
                   Función...............: Obtiene la información del medicamento
                                           buscado por el usuario: nombre, serie,
                                           lote, vencimiento y unidades en existencia. */
                if (nombreServ.equals("GETMDCDET")) {
                    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
                    respuestaServ = "";
                    
                    if (sesion.getAttribute("idUsuario") != null) {
                        ServiciosM modelo = new ServiciosM();
                        String serie = request.getParameter("code").split("  -  ")[0];
                        respuestaServ = modelo.obtenerExistenciaMedicamento(serie);
                    }
                    
                    out.print(ServiciosU.sustituirCaracteres(respuestaServ));
                    // </editor-fold>
                } else
                
                
                /* Servicio..............: Get Medical Equipment List
                   Página que lo utiliza.: equipo.html
                   Función...............: Obtiene la lista de modelos para la marca
                                           seleccionada en el formulario de equipo médico. */
                if (nombreServ.equals("GETMEDEQUL")) {
                    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
                    respuestaServ = "{ \"error\": true }";
                    
                    if (request.getParameter("code") != null) {
                        String marca = request.getParameter("code");
                        final String SELECT = "Modelo";
                        final String FROM = "Catalogo_Equipo_Medico";
                        final String WHERE = "Marca = ?";
                        final String ORDER = "ORDER BY Modelo";
                        
                        ServiciosM modelo = new ServiciosM();
                        respuestaServ =
                            "{ \"error\": false, " +
                            "  \"respuesta\": \"" +
                                modelo.elementoSELECTGenerico(
                                        SELECT, FROM, WHERE, ORDER, marca) + "\"" +
                            "}";
                    }
                    
                    out.print(respuestaServ);
                    // </editor-fold>
                } else
                
                    
                /* Servicio..............: Get Message
                   Página que lo utiliza.: *all.html
                   Función...............: Obtiene cualquier mensaje de retroalimentación
                                           para el usuario, si lo hay. */
                if (nombreServ.equals("GETMSG")) {
                    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
                    respuestaServ = sesion.getAttribute("mensaje") != null ?
                                    ServiciosU.sustituirCaracteres(String.valueOf(
                                    sesion.getAttribute("mensaje")).trim()) :
                                    "";
                    sesion.setAttribute("mensaje", "");

                    if (request.getParameter("page") != null)
                        if (request.getParameter("page").equals(Consts.INDEX_HTML)) {
                            sesion.removeAttribute("idCentro");
                            sesion.removeAttribute("idSesion");
                            sesion.removeAttribute("idUsuario");
                            sesion.removeAttribute("idRol");
                        }
                    
                    out.print(respuestaServ);
                    // </editor-fold>
                } else
                
                    
                /* Servicio..............: Get Navigation Bar
                   Página que lo utiliza.: *ALL.html
                   Función...............: Obtiene ls barra de navegación
                                           para el usuario de acuerdo a los 
                                           permisos de acceso que este tenga. */
                if (nombreServ.equals("GETNAVBAR")) {
                    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
                    respuestaServ = "";
                    
                    if (sesion.getAttribute("idUsuario") != null) {
                        ServiciosM modelo = new ServiciosM();
                        respuestaServ = modelo.cargarBarraNavegacion(
                                Integer.parseInt(sesion.getAttribute("idUsuario").toString()), 
                                Integer.parseInt(sesion.getAttribute("idRol").toString()));
                    }
                    
                    out.print(ServiciosU.sustituirCaracteres(respuestaServ));
                    // </editor-fold>
                } else
                
                    
                /* Servicio..............: Get Rehabilitaion Plan Level 01
                   Página que lo utiliza.: citamedica.html
                   Función...............: Obtiene la información del plan de
                                           rehabilitacion de acuerdo al nivel más
                                           general. */
                if (nombreServ.equals("GETREHPLANL1")) {
                    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
                    respuestaServ = "";
                    
                    if (sesion.getAttribute("idUsuario") != null) {
                        ServiciosM modelo = new ServiciosM();
                        String codigo = request.getParameter("code").split("  -  ")[0];
                        respuestaServ = modelo.obtenerPlanRehabilitacionL1(codigo);
                    }
                    
                    out.print(ServiciosU.sustituirCaracteres(respuestaServ));
                    // </editor-fold>
                } else
                
                    
                /* Servicio..............: Get Rehabilitaion Plan Level 02
                   Página que lo utiliza.: indicarmodalidaes.html
                   Función...............: Obtiene la información del plan de
                                           rehabilitacion de acuerdo al nivel más
                                           general. */
                if (nombreServ.equals("GETREHPLANL2")) {
                    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
                    respuestaServ = "";
                    
                    if (sesion.getAttribute("idUsuario") != null) {
                        ServiciosM modelo = new ServiciosM();
                        int codigo = Integer.parseInt(
                               sesion.getAttribute("codigoPlanRehabilitacion").toString());
                        respuestaServ = modelo.obtenerPlanRehabilitacionL2(codigo);
                    } else
                        respuestaServ =
                                "{" +
                                "  \"error\": true, " +
                                "  \"mensaje\": \"Ocurrió un error al solicitar la información del plan de rehabilitación.\"" +
                                "}";
                    
                    out.print(ServiciosU.sustituirCaracteres(respuestaServ));
                    // </editor-fold>
                } else
                
                    
                /* Servicio..............: Get Rehabilitaion Plan Level 03
                   Página que lo utiliza.: checkin.html
                   Función...............: Obtiene la información del plan de
                                           rehabilitacion de acuerdo al nivel más
                                           general. */
                if (nombreServ.equals("GETREHPLANL3")) {
                    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
                    respuestaServ = "";
                    
                    if (sesion.getAttribute("idUsuario") != null) {
                        ServiciosM modelo = new ServiciosM();
                        String expediente = sesion.getAttribute("expedienteAgenda").toString();
                        int fecha = Integer.parseInt(sesion.getAttribute("fechaAgenda").toString());
                        int horaInicio = Integer.parseInt(sesion.getAttribute("horaInicioAgenda").toString());
                        int horaFinal = Integer.parseInt(sesion.getAttribute("horaFinalAgenda").toString());
                        respuestaServ = modelo.obtenerPlanRehabilitacionL3(
                                            expediente, fecha, horaInicio, horaFinal);
                    } else
                        respuestaServ =
                                "{" +
                                "  \"error\": true, " +
                                "  \"mensaje\": \"Ocurrió un error al solicitar la información del plan de rehabilitación.\"" +
                                "}";
                    
                    out.print(ServiciosU.sustituirCaracteres(respuestaServ));
                } else
                
                    
                /* Servicio..............: Get Rehabilitaion Plan Modality List
                   Página que lo utiliza.: indicarmodalidaes.html
                   Función...............: Obtiene la lista detallada de las
                                           modalidades que se pueden aplicar en
                                           el plan de rehabilitación. */
                if (nombreServ.equals("GETREHPLANMODL")) {
                    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
                    respuestaServ = "";
                    
                    if (sesion.getAttribute("idUsuario") != null) {
                        int idCentro =
                                Integer.parseInt(sesion.getAttribute("idCentro").toString());                       
                     
                        int codigo = 0;
                        if(request.getParameter("code") != null)
                            codigo = Integer.parseInt(request.getParameter("code"));
                        
                        Modalidad modalidad = new Modalidad();
                        
                        respuestaServ =
                                "{" +
                                "  \"error\": false, " +
                                "  \"mensaje\": \"\", " +
                                "  \"listaModalidades\": \"" +
                                        modalidad.listaDetalleModalidades(idCentro, codigo) + "\"" +
                                "}";
                    } else
                        respuestaServ =
                                "{" +
                                "  \"error\": true, " +
                                "  \"mensaje\": \"Error al cargar la información solicitada.\"" +
                                "}";
                    System.out.println(respuestaServ);
                    out.print(ServiciosU.sustituirCaracteres(respuestaServ));
                    // </editor-fold>
                } else
                
                    
                /* Servicio..............: Get Report Access
                   Página que lo utiliza.: reporte.html
                   Función...............: Obtiene la lista de reportes que puede
                                           generar el usuario de la aplicación de
                                           acuerdo a su rol. */
                if (nombreServ.equals("GETRPTACC")) {
                    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
                    respuestaServ = "";
                    
                    if (sesion.getAttribute("idUsuario") != null) {
                        ServiciosM modelo = new ServiciosM();
                        int idRolAsignado = Integer.parseInt(sesion.getAttribute("idRol").toString());
                        int idUsuario = Integer.parseInt(sesion.getAttribute("idUsuario").toString());
                        
                        
                        if(idRolAsignado == Consts.ROL_SA
                                || idRolAsignado == Consts.ROL_ADMINISTRADOR)
                            respuestaServ = 
                                    "{ \"error\": false, " +
                                    "  \"equipo\": true," +
                                    "  \"insumo\": true," +
                                    "  \"medicamento\": true," +
                                    "  \"costos\": true," +
                                    "  \"terapeuta\": true," +
                                    "  \"asistencia\": true," +
                                    "  \"sesion\": true," +
                                    "  \"evento\": true }";
                        else
                            respuestaServ = modelo.cargarReporteAccesos(idUsuario);
                    }
                    
                    out.print(ServiciosU.sustituirCaracteres(respuestaServ));
                    // </editor-fold>
                } else
                
                
                /* Servicio..............: Get Therapist List
                   Página que lo utiliza.: planrehabilitacion.html
                   Función...............: Obtiene la lista de terapeutas que pueden
                                           atender a un paciente. */
                if (nombreServ.equals("GETTRPL")) {
                    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
                    respuestaServ =
                            "{ \"error\": true," +
                            "  \"mensaje\":\"Ocurrió un error al intentar cargar la lista de empleados.\" }";
                    
                    if (sesion.getAttribute("idUsuario") != null) {
                        ServiciosM modelo = new ServiciosM();
                        int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                        respuestaServ =
                            "{ \"error\": false," +
                            "  \"mensaje\": \"\"," +
                            "  \"elemento\": \"" + modelo.elementoSELECTMedico(idCentro, "") + "\" }";
                    }
                    
                    out.print(ServiciosU.sustituirCaracteres(respuestaServ));
                    // </editor-fold>
                } else
                
                    
                /* Servicio..............: Get Session Information
                   Página que lo utiliza.: *.html
                   Función...............: Obtiene información relacionada a la
                                           sesión: usuario, rol, entre otros. */
                if (nombreServ.equals("GETSSNI")) {
                    // <editor-fold defaultstate="collapsed" desc="Verifica si el usuario tiene acceso a la página web a la que intenta acceder. Establece código del registro en cero o vacío y modo edición editar en caso que el usuario haya accedido directamente a un formulario.">
                    boolean[] accesos = {false, false};
                    int idRol = Integer.parseInt(sesion.getAttribute("idRol").toString());
                    
                    String paginaWeb = request.getParameter("page").split("#")[0];
                    if (paginaWeb.lastIndexOf(".") > 0)
                        if (paginaWeb.substring(paginaWeb.lastIndexOf(".")).equals(".do"))
                            paginaWeb = paginaWeb.substring(0, paginaWeb.lastIndexOf(".")) + ".html";
                    
                    // Valida si las variables de sesión aún están activas. En caso de haber pasado
                    // el tiempo establecido sin hacer ninguna acción redirige al index.html.
                    if (sesion.getAttribute("idSesion") == null || sesion.getAttribute("idUsuario") == null) {
                        sesion.setAttribute("mensaje", "Su sesión ha expirado.");
                        out.print("{ \"error\": true }");
                    } else if (sesion.getAttribute("idSesion") != null
                            && sesion.getAttribute("idSesion").toString().trim().length() == 0) {
                        sesion.removeAttribute("idSesion");
                        sesion.setAttribute("mensaje", "Acceso denegado.");
                        out.print("{ \"error\": true }");
                    } else if (sesion.getAttribute("idUsuario") != null
                            && sesion.getAttribute("idUsuario").toString().trim().length() == 0) {
                        sesion.setAttribute("mensaje", "Error al cargar su sesión.");
                        sesion.removeAttribute("idUsuario");
                        out.print("{ \"error\": true }");
                    } else if (ServiciosM.tieneAcceso(
                            Integer.parseInt(sesion.getAttribute("idCentro").toString()),
                            Integer.parseInt(sesion.getAttribute("idUsuario").toString()),
                            paginaWeb, accesos) || idRol == Consts.ROL_SA) {
                        if( (!accesos[0] && !accesos[1])
                                // Estas páginas solo permiten edición
                                || (paginaWeb.equals(Consts.AGENDA_HTML) && !accesos[1])
                                || (paginaWeb.equals(Consts.APROBAR_DEVOLUCION_MEDICAMENTO_HTML) && !accesos[1])
                                || (paginaWeb.equals(Consts.CITA_MEDICA_HTML) && !accesos[1])
                                || (paginaWeb.equals(Consts.DEVOLUCION_MEDICAMENTO_HTML) && !accesos[1])
                                || (paginaWeb.equals(Consts.PLAN_REHABILITACION_HTML) && !accesos[1])){
                            sesion.setAttribute("mensaje", "No tiene acceso a la página web solicitada.");
                            out.print("{ \"error\": true }");
                            return;
                        }
                        
                        String idRegistro = "", destino = "", valorPorDefecto = "";
                        switch (paginaWeb) {
                            // De acuerdo a la página web define el código de registro
                            // predeterminado en caso de no existir, esto con el propósito
                            // que si el usuario tecleo la URL y tiene acceso a ella lo
                            // registre como código "NUEVO".

                            // Para los Lobbys: idRegistro = Consts.SU_PROPIA_ETIQUETA + BREAK;
                            case Consts.LOBBY_ADMIN_HTML:

                            case Consts.LOBBY_DEPARTAMENTOS_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_LOBBY_DEPARTAMENTOS;
                                }
                            case Consts.LOBBY_PUESTOS_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_LOBBY_PUESTOS;
                                }
                            case Consts.LOBBY_EMPLEADOS_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_LOBBY_EMPLEADOS;
                                }
                            case Consts.LOBBY_PROVEEDORES_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_LOBBY_PROVEEDORES;
                                }
                            case Consts.LOBBY_MODALIDADES_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_LOBBY_MODALIDADES;
                                }
                            case Consts.LOBBY_CENTROS_REM_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_LOBBY_CENTROS_REM;
                                }
                            case Consts.LOBBY_CATALOGO_EQUIPOS_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_LOBBY_CATALOGO_EQUIPOS;
                                }
                            case Consts.LOBBY_EQUIPOS_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_LOBBY_EQUIPOS;
                                }
                            case Consts.LOBBY_CATALOGO_SUMINISTROS_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_LOBBY_CATALOGO_SUMINISTROS;
                                }
                            case Consts.LOBBY_SUMINISTROS_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_LOBBY_SUMINISTROS;
                                }
                            case Consts.LOBBY_CATALOGO_MEDICAMENTOS_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_LOBBY_CATALOGO_MEDICAMENTOS;
                                }
                            case Consts.LOBBY_MEDICAMENTOS_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_LOBBY_MEDICAMENTOS;
                                }
                            case Consts.LOBBY_SESIONES_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_LOBBY_SESIONES;
                                }
                            case Consts.LOBBY_PACIENTES_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_LOBBY_PACIENTES;
                                }
                                
                                idRegistro = Consts.ETIQUETA_LOBBY;
                                sesion.setAttribute("modoVisualizacion" + destino, accesos[0]);
                                sesion.setAttribute("modoEdicion" + destino, accesos[1]);

                                break;
                            case Consts.CENTRO_HTML:
                                idRegistro = sesion.getAttribute("idCentro").toString();
                                sesion.setAttribute("modoCentro",
                                        sesion.getAttribute("modoCentro") == null
                                        ? Consts.VISUALIZACION
                                        : sesion.getAttribute("modoCentro"));
                                break;
                            case Consts.DEPARTAMENTO_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_DEPARTAMENTO;
                                    valorPorDefecto = "0";
                                }
                            case Consts.PUESTO_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_PUESTO;
                                    valorPorDefecto = "0";
                                }
                            case Consts.EMPLEADO_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_EMPLEADO;
                                    valorPorDefecto = "";
                                }
                            case Consts.PROVEEDOR_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_PROVEEDOR;
                                    valorPorDefecto = "0";
                                }
                            case Consts.MODALIDAD_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_MODALIDAD;
                                    valorPorDefecto = "0";
                                }
                            case Consts.CENTRO_REM_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_CENTRO_REM;
                                    valorPorDefecto = "0";
                                }
                            case Consts.PACIENTE_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_PACIENTE;
                                    valorPorDefecto = "";
                                }
                            case Consts.USUARIO_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_USUARIO;
                                    valorPorDefecto = "0";
                                }
                            case Consts.DETALLE_SESION_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_SESION;
                                    valorPorDefecto = "0";
                                }
                            case Consts.CATALOGO_EQUIPO_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_CATALOGO_EQUIPO;
                                    valorPorDefecto = "0";
                                }
                            case Consts.EQUIPO_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_EQUIPO;
                                    valorPorDefecto = "";
                                }
                            case Consts.CATALOGO_SUMINISTRO_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_CATALOGO_SUMINISTRO;
                                    valorPorDefecto = "";
                                }
                            case Consts.SUMINISTRO_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_SUMINISTRO;
                                    valorPorDefecto = "";
                                }
                            case Consts.ASIGNAR_SUMINISTRO_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_SUMINISTRO;
                                    valorPorDefecto = "";
                                }
                            case Consts.AGREGAR_SUMINISTRO_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_SUMINISTRO;
                                    valorPorDefecto = "";
                                }
                            case Consts.CATALOGO_MEDICAMENTO_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_CATALOGO_MEDICAMENTO;
                                    valorPorDefecto = "";
                                }
                            case Consts.MEDICAMENTO_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_MEDICAMENTO;
                                    valorPorDefecto = "";
                                }
                            case Consts.AGREGAR_MEDICAMENTO_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_MEDICAMENTO;
                                    valorPorDefecto = "";
                                }
                            case Consts.DEVOLUCION_MEDICAMENTO_HTML:
                                if (destino.trim().length() == 0) {
                                    destino = Consts.ETIQUETA_MEDICAMENTO;
                                    valorPorDefecto = "";
                                }
                            
                            default:
                                if (destino.trim().length() == 0)
                                    break;

                                // Establece la variable de sesión "codigo + Destino" para no
                                // confundir los códigos establecidos por otras páginas web.
                                if (sesion.getAttribute("codigo" + destino) != null) {
                                    idRegistro = sesion.getAttribute("codigo" + destino).toString();

                                    if (idRegistro.trim().equals("")) {
                                        sesion.setAttribute("codigo" + destino, valorPorDefecto);
                                        idRegistro = Consts.ETIQUETA_NUEVO;
                                    } else if (idRegistro.equals(valorPorDefecto)) {
                                        idRegistro = Consts.ETIQUETA_NUEVO;
                                    }

                                    if (sesion.getAttribute("modo" + destino) == null) {
                                        sesion.setAttribute("modo" + destino, Consts.EDICION);
                                    }
                                } else {
                                    sesion.setAttribute("codigo" + destino, valorPorDefecto);
                                    sesion.setAttribute("modo" + destino, Consts.EDICION);
                                    idRegistro = Consts.ETIQUETA_NUEVO;
                                }
                        }

                        ServiciosM modelo = new ServiciosM();
                        modelo.registrarAccion(
                                Integer.parseInt(sesion.getAttribute("idSesion").toString()),
                                paginaWeb,
                                idRegistro,
                                Consts.ACCION_VISUALIZACION);

                        // String temp = ServiciosM.obtenerNavegacion(sesion.getAttribute("idRol").toString());
                        // out.print( temp );
                        out.print("{ \"error\": false }");
                    } else {
                        sesion.setAttribute("mensaje", "Acceso denegado.");
                        out.print("{ \"error\": true }");
                    }
                    // </editor-fold>
                } else
                    
                    
                /* Servicio..............: Get User Actions List
                   Página que lo utiliza.: detallesesion.html
                   Función...............: Obtiene la lista de acciones que realizó
                                           un usuario para la sesión indicada. */
                if (nombreServ.equals("GETUSRACTL")) {
                    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
                    respuestaServ =
                            "{ \"error\": true," +
                            "  \"mensaje\":\"Ocurrió un error al intentar cargar la lista de acciones.\" }";
                    
                    if (sesion.getAttribute("idUsuario") != null) {
                        ServiciosM modelo = new ServiciosM();
                        int idSesion = Integer.parseInt(sesion.getAttribute("codigo" + Consts.ETIQUETA_SESION).toString());
                        System.out.println("*** " + idSesion);
                        respuestaServ = modelo.cargarAccionesUsuario(idSesion);
                    }
                    
                    out.print(ServiciosU.sustituirCaracteres(respuestaServ));
                    // </editor-fold>
                } else
                    
                            
                /* Servicio..............: Save Trace Information
                   Página que lo utiliza.: *all.html
                   Función...............: Salva la información de rastreo de la sesión. */
                if (nombreServ.equals("SAVTRCINF")) {
                    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
                    int idSesion = Integer.parseInt(
                            sesion.getAttribute("idSesion") == null ? "0" :
                            sesion.getAttribute("idSesion").toString());
                    int idUsuario = Integer.parseInt(
                            sesion.getAttribute("idUsuario") == null ? "0" :
                            sesion.getAttribute("idUsuario").toString());

                    ServiciosM modelo = new ServiciosM();
                    modelo.cerrarSesion(idSesion, idUsuario);
                    
                    String destinationPage =
                            sesion.getAttribute("destinationPage") != null ?
                            sesion.getAttribute("destinationPage").toString() : "";
                    String leavingPage = request.getParameter("page");
                    // Limpia el código seleccionado cuando el usuario sale del formulario,
                    // esto para que si ingresa de nuevo mediante el URL no cargue el
                    // registro previo.
                    if( !destinationPage.equals(leavingPage) &&
                            !leavingPage.equals(Consts.DEPARTAMENTO_HTML) &&
                            !leavingPage.equals(Consts.LOBBY_DEPARTAMENTOS_HTML)){
                        sesion.removeAttribute("codigo" + Consts.ETIQUETA_DEPARTAMENTO);
                    } else if( !destinationPage.equals(leavingPage) &&
                            !leavingPage.equals(Consts.PUESTO_HTML) &&
                            !leavingPage.equals(Consts.LOBBY_PUESTOS_HTML) ){
                        sesion.removeAttribute("codigo" + Consts.ETIQUETA_PUESTO);
                    } else if( !destinationPage.equals(leavingPage) &&
                            !leavingPage.equals(Consts.EMPLEADO_HTML) &&
                            !leavingPage.equals(Consts.LOBBY_EMPLEADOS_HTML) ){
                        sesion.removeAttribute("codigo" + Consts.ETIQUETA_EMPLEADO);
                    } else if( !destinationPage.equals(leavingPage) &&
                            !leavingPage.equals(Consts.PROVEEDOR_HTML) &&
                            !leavingPage.equals(Consts.LOBBY_PROVEEDORES_HTML) ){
                        sesion.removeAttribute("codigo" + Consts.ETIQUETA_PROVEEDOR);
                    } else if( !destinationPage.equals(leavingPage) &&
                            !leavingPage.equals(Consts.MODALIDAD_HTML) &&
                            !leavingPage.equals(Consts.LOBBY_MODALIDADES_HTML) ){
                        sesion.removeAttribute("codigo" + Consts.ETIQUETA_MODALIDAD);
                    } else if( !destinationPage.equals(leavingPage) &&
                            !leavingPage.equals(Consts.CENTRO_REM_HTML) &&
                            !leavingPage.equals(Consts.LOBBY_CENTROS_REM_HTML) ){
                        sesion.removeAttribute("codigo" + Consts.ETIQUETA_CENTRO_REM);
                    } else if( !destinationPage.equals(leavingPage) &&
                            !leavingPage.equals(Consts.PACIENTE_HTML) &&
                            !leavingPage.equals(Consts.LOBBY_PACIENTES_HTML) ){
                        sesion.removeAttribute("codigo" + Consts.ETIQUETA_PACIENTE);
                    } else if( !destinationPage.equals(leavingPage) &&
                            !leavingPage.equals(Consts.CATALOGO_EQUIPO_HTML) &&
                            !leavingPage.equals(Consts.LOBBY_CATALOGO_EQUIPOS_HTML) ){
                        sesion.removeAttribute("codigo" + Consts.ETIQUETA_CATALOGO_EQUIPO);
                    } else if( !destinationPage.equals(leavingPage) &&
                            !leavingPage.equals(Consts.EQUIPO_HTML) &&
                            !leavingPage.equals(Consts.LOBBY_EQUIPOS_HTML) ){
                        sesion.removeAttribute("codigo" + Consts.ETIQUETA_EQUIPO);
                    } else if( !destinationPage.equals(leavingPage) &&
                            !leavingPage.equals(Consts.CATALOGO_SUMINISTRO_HTML) &&
                            !leavingPage.equals(Consts.LOBBY_CATALOGO_SUMINISTROS_HTML) ){
                        sesion.removeAttribute("codigo" + Consts.ETIQUETA_CATALOGO_SUMINISTRO);
                    } else if( !destinationPage.equals(leavingPage) &&
                            !leavingPage.equals(Consts.SUMINISTRO_HTML) &&
                            !leavingPage.equals(Consts.AGREGAR_SUMINISTRO_HTML) &&
                            !leavingPage.equals(Consts.ASIGNAR_SUMINISTRO_HTML) &&
                            !leavingPage.equals(Consts.LOBBY_SUMINISTROS_HTML) ){
                        sesion.removeAttribute("codigo" + Consts.ETIQUETA_SUMINISTRO);
                    } else if( !destinationPage.equals(leavingPage) &&
                            !leavingPage.equals(Consts.PROVEEDOR_HTML) &&
                            !leavingPage.equals(Consts.LOBBY_PROVEEDORES_HTML) ){
                        sesion.removeAttribute("codigo" + Consts.ETIQUETA_PROVEEDOR);
                    } else if( !destinationPage.equals(leavingPage) &&
                            !leavingPage.equals(Consts.USUARIO_HTML) &&
                            !leavingPage.equals(Consts.LOBBY_USUARIOS_HTML) ){
                        sesion.removeAttribute("codigo" + Consts.ETIQUETA_USUARIO);
                    } else if( !destinationPage.equals(leavingPage) &&
                            !leavingPage.equals(Consts.DETALLE_SESION_HTML) &&
                            !leavingPage.equals(Consts.LOBBY_SESIONES_HTML) ){
                        sesion.removeAttribute("codigo" + Consts.ETIQUETA_SESION);
                    }
                    // </editor-fold>
                } else
                

                /* Servicio..............: Set Code
                   Página que lo utiliza.: lobby.html, lobbypuestos.html, lobbyempleados.html
                   Función...............: Establece el ID del elemento que se presionó. */
                if (nombreServ.equals("SETCDE")) {
                    // <editor-fold defaultstate="collapsed" desc="Establece el código del registro seleccionado.">
                    String paginaWeb = request.getParameter("page").split(".html")[0] + ".html";
                    String codigo = request.getParameter("code");
                    String modo = request.getParameter("mode");
                    String etiqueta = "";

                    switch (paginaWeb) {
                        // De acuerdo a la página web define el código de registro
                        // predeterminado en caso de no existir, esto con el propósito
                        // que el usuario haya modificado manualmente los atributos de los
                        // elementos HTML.

                        case Consts.LOBBY_DEPARTAMENTOS_HTML:
                            if (etiqueta.trim().length() == 0) {
                                etiqueta = Consts.ETIQUETA_DEPARTAMENTO;
                                sesion.setAttribute("destinationPage", Consts.DEPARTAMENTO_HTML);
                            }
                        case Consts.LOBBY_PUESTOS_HTML:
                            if (etiqueta.trim().length() == 0) {
                                etiqueta = Consts.ETIQUETA_PUESTO;
                                sesion.setAttribute("destinationPage", Consts.PUESTO_HTML);
                            }
                        case Consts.LOBBY_EMPLEADOS_HTML:
                            if (etiqueta.trim().length() == 0) {
                                etiqueta = Consts.ETIQUETA_EMPLEADO;
                                sesion.setAttribute("destinationPage", Consts.EMPLEADO_HTML);
                            }
                        case Consts.LOBBY_PROVEEDORES_HTML:
                            if (etiqueta.trim().length() == 0) {
                                etiqueta = Consts.ETIQUETA_PROVEEDOR;
                                sesion.setAttribute("destinationPage", Consts.PROVEEDOR_HTML);
                            }
                        case Consts.LOBBY_MODALIDADES_HTML:
                            if (etiqueta.trim().length() == 0) {
                                etiqueta = Consts.ETIQUETA_MODALIDAD;
                                sesion.setAttribute("destinationPage", Consts.MODALIDAD_HTML);
                            }
                        case Consts.LOBBY_CENTROS_REM_HTML:
                            if (etiqueta.trim().length() == 0) {
                                etiqueta = Consts.ETIQUETA_CENTRO_REM;
                                sesion.setAttribute("destinationPage", Consts.CENTRO_REM_HTML);
                            }
                        case Consts.LOBBY_PACIENTES_HTML:
                            if (etiqueta.trim().length() == 0) {
                                etiqueta = Consts.ETIQUETA_PACIENTE;
                                sesion.setAttribute("destinationPage", Consts.PACIENTE_HTML);
                            }
                        case Consts.LOBBY_USUARIOS_HTML:
                            if (etiqueta.trim().length() == 0) {
                                etiqueta = Consts.ETIQUETA_USUARIO;
                                sesion.setAttribute("destinationPage", Consts.USUARIO_HTML);
                            }
                        case Consts.LOBBY_CATALOGO_EQUIPOS_HTML:
                            if (etiqueta.trim().length() == 0) {
                                etiqueta = Consts.ETIQUETA_CATALOGO_EQUIPO;
                                sesion.setAttribute("destinationPage", Consts.CATALOGO_EQUIPO_HTML);
                            }
                        case Consts.LOBBY_EQUIPOS_HTML:
                            if (etiqueta.trim().length() == 0) {
                                etiqueta = Consts.ETIQUETA_EQUIPO;
                                sesion.setAttribute("destinationPage", Consts.EQUIPO_HTML);
                            }
                        case Consts.LOBBY_CATALOGO_SUMINISTROS_HTML:
                            if (etiqueta.trim().length() == 0) {
                                etiqueta = Consts.ETIQUETA_CATALOGO_SUMINISTRO;
                                sesion.setAttribute("destinationPage", Consts.CATALOGO_SUMINISTRO_HTML);
                            }
                        case Consts.LOBBY_SUMINISTROS_HTML:
                            if (etiqueta.trim().length() == 0) {
                                etiqueta = Consts.ETIQUETA_SUMINISTRO;
                                sesion.setAttribute("destinationPage", Consts.SUMINISTRO_HTML);
                            }
                        case Consts.LOBBY_CATALOGO_MEDICAMENTOS_HTML:
                            if (etiqueta.trim().length() == 0) {
                                etiqueta = Consts.ETIQUETA_CATALOGO_MEDICAMENTO;
                                sesion.setAttribute("destinationPage", Consts.CATALOGO_MEDICAMENTO_HTML);
                            }
                        case Consts.LOBBY_MEDICAMENTOS_HTML:
                            if (etiqueta.trim().length() == 0) {
                                etiqueta = Consts.ETIQUETA_MEDICAMENTO;
                                sesion.setAttribute("destinationPage", Consts.MEDICAMENTO_HTML);
                            }
                        case Consts.LOBBY_SESIONES_HTML:
                            if (etiqueta.trim().length() == 0) {
                                etiqueta = Consts.ETIQUETA_SESION;
                                sesion.setAttribute("destinationPage", Consts.DETALLE_SESION_HTML);
                            }
                        

                        default:
                            if (etiqueta.trim().length() == 0) {
                                break;
                            }

                            if (!codigo.equals(Consts.CODIGO_ERRONEO)) {
                                sesion.setAttribute("codigo" + etiqueta, codigo);
                                sesion.setAttribute("modo" + etiqueta,
                                        modo.equals(Consts.EDICION) ? modo
                                        : modo.equals(Consts.VISUALIZACION) ? modo
                                        : Consts.VISUALIZACION);
                            } else {
                                sesion.removeAttribute("codigo" + etiqueta);
                                sesion.removeAttribute("modo" + etiqueta);
                            }
                    }
                    // </editor-fold>
                } else
                
                    
                /* Servicio..............: Set Rehabilitation Session Information
                   Página que lo utiliza.: agenda.html
                   Función...............: Establece los valores de expediente,
                                           hora de inicio y final de la sesión
                                           de rehabilitación para mostrarle al usuario
                                           la información de las modalidades 
                                           que se deben llevar a cabo para dicho plan. */
                if (nombreServ.equals("SETREHSSNINF")) {
                    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
                    if(request.getParameter("file") != null
                            && request.getParameter("start") != null
                            && request.getParameter("end") != null){
                        sesion.setAttribute("expedienteAgenda", request.getParameter("file"));
                        sesion.setAttribute("fechaAgenda", ServiciosU.formatearFecha(
                                request.getParameter("start"), true));
                        sesion.setAttribute("horaInicioAgenda", ServiciosU.formatearFecha(
                                request.getParameter("start"), false));
                        sesion.setAttribute("horaFinalAgenda", ServiciosU.formatearFecha(
                                request.getParameter("end"), false));
                        
                        respuestaServ =
                                "{ \"error\": false, " +
                                "  \"mensaje\": \"\" }";
                    } else 
                        respuestaServ =
                                "{ \"error\": true, " +
                                "  \"mensaje\": \"Error al cargar parámetros.\" }";
                
                    out.print(respuestaServ);
                    // </editor-fold>
                }
                
                
                
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            out.print("{ \"error\": true }");
        }
    } // Fin del método doPost().
}