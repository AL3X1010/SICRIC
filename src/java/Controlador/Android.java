/*  Nombre.....:  Android.java
 *  Descripción:  Atiende las solicitudes realizadas desde la aplicación móvil.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Diciembre, 2019.
 */
package Controlador;

import Modelo.$;
import Modelo.ServiciosM;
import Utilitarios.Consts;
import Utilitarios.ServiciosU;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Android extends HttpServlet {
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
        try (PrintWriter out = response.getWriter()) {
            out.println(
                "{" +
                " \"error\": true, " +
                " \"errorMessage\": \"Esta versión de la aplicación no puede acceder al servidor.\", " +
                " \"fullName\": \"\", " +
                " \"JSONAppointments\": \"\"" +
                "}");
        }
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
        PrintWriter out = response.getWriter();
        $ instanciaBD = null;
        String respuestaServ = "";
        String myQuery;
                
        try {
            // Verifica que se proporcione usuario y contraseña.
            if( request.getParameter("username") == null
                    || request.getParameter("password") == null ){
                respuestaServ = 
                    "{" +
                    " \"error\": true, " +
                    " \"errorMessage\": \"Error al cargar parámetros.\", " +
                    " \"fullName\": \"\", " +
                    " \"JSONAppointments\": \"\"" +
                    "}";
                return;
            } else if( request.getParameter("username").trim().length() == 0
                        && request.getParameter("password").trim().length() == 0 ){
                respuestaServ =
                    "{" +
                    " \"error\": true, " +
                    " \"errorMessage\": \"Error al cargar parámetros.\", " +
                    " \"fullName\": \"\", " +
                    " \"JSONAppointments\": \"\"" +
                    "}";
                return;
            }
            
            
            // Valida credenciales de acceso, además solo los fisioterapeutas
            // pueden acceder a la aplicación móvil.
            instanciaBD = new $();
            myQuery = 
                    "SELECT E.ID_Empleado, E.Nombres, E.Apellidos " +
                    "FROM Usuario U " +
                    "     INNER JOIN Empleado E " +
                    "        ON U.ID_Empleado = E.ID_Empleado " +
                    "WHERE Usuario = ? " +
                    "  AND U.Contrasenia = ? " +
                    "  AND U.Activo = 1 " +
                    "  AND U.ID_Rol = " + Consts.ROL_FISIOTERAPEUTA + ";";
            
            PreparedStatement psUsuario = instanciaBD.prepStatement(myQuery);      
            psUsuario.setString(1, request.getParameter("username"));
            psUsuario.setString(2, request.getParameter("password"));
            
            ResultSet resultado = psUsuario.executeQuery();
            if(resultado.next()){
                ServiciosM modelo = new ServiciosM();
                String numeroEmpleado = resultado.getString("ID_Empleado");
                String nombreEmpleado = ServiciosU.obtenerNombreCompleto(
                        resultado.getString("Nombres"), 
                        resultado.getString("Apellidos"));
                String hoy = String.valueOf(ServiciosU.fechaATimestamp()).substring(0, 8);
                int ahora = Integer.parseInt(String.valueOf(
                                    ServiciosU.fechaATimestamp()).substring(8, 12));
                
                // Procede a recuperar todas las sesiones de rehabilitación
                // agendadas a planes que atiende el usuario de este recurso.
                myQuery =
                        "SELECT PR.ID_PlanRehabilitacion, P.ID_Paciente, Nombres, " +
                        "       Apellidos, Telefono, HoraInicio, HoraFinal " +
                        "FROM Plan_Rehabilitacion PR " +
                        "     INNER JOIN Cita_Plan_Rehabilitacion C " +
                        "        ON PR.ID_PlanRehabilitacion = C.ID_PlanRehabilitacion " +
                        "     INNER JOIN Paciente P " +
                        "        ON PR.ID_Paciente = P.ID_Paciente " +
                        "WHERE ID_Empleado = '" + numeroEmpleado + "' " +
                        "  AND Activo = 1 " +
                        "  AND Fecha = " + hoy + " " +
                        "ORDER BY HoraInicio, HoraFinal;";
                
                ResultSet resultadoCitas = instanciaBD.execQry(myQuery);
                while(resultadoCitas.next()){
                    // Información general del paciente.
                    int idPlanRehabilitacion = resultadoCitas.getInt("ID_PlanRehabilitacion");
                    String expediente = resultadoCitas.getString("ID_Paciente");
                    String nombrePaciente = ServiciosU.obtenerNombreCompleto(
                                resultadoCitas.getString("Nombres"),
                                resultadoCitas.getString("Apellidos"));
                    int telefono = resultadoCitas.getInt("Telefono");
                    int horaInicio = resultadoCitas.getInt("HoraInicio");
                    int horaFinal = resultadoCitas.getInt("HoraFinal");
                    String evento = ahora >= horaInicio && ahora <= horaFinal ? "A" :   // evento actual
                                    (ahora > horaFinal ? "P" : "F");
                    
                    // Llama a método que cuenta las asistencias y ausencias
                    // para colorear barra junto al nombre del paciente.
                    String[] registroAsistencias = {"", "", "", "", ""};
                    int ultimasAusencias = modelo.obtenerAsistenciaSesiones(
                            idPlanRehabilitacion, registroAsistencias);
                
                    // Crea objeto JSON con la información recabada.
                    respuestaServ +=
                            "  {" +
                            "    \\\"name\\\": \\\"" + nombrePaciente + "\\\"," +
                            "    \\\"time\\\": \\\"8:00 am - 9:00 am\\\"," +
                            "    \\\"time\\\": \\\"" + ServiciosU.formatearHora(horaInicio) + " - " +
                                                       ServiciosU.formatearHora(horaFinal) + "\\\"," +
                            "    \\\"absencesInARow\\\": \\\"" + ultimasAusencias + "\\\"," +
                            "    \\\"file\\\": \\\"" + expediente + "\\\"," +
                            "    \\\"cellphone\\\": \\\"" + telefono + "\\\"," +
                            "    \\\"timeline\\\": \\\"" + evento + "\\\"," +
                            "    \\\"record\\\": \\\"" +
                                        (registroAsistencias[0].equals("") ? " " : registroAsistencias[0] ) +
                                        (registroAsistencias[1].equals("") ? " " : registroAsistencias[1] ) +
                                        (registroAsistencias[2].equals("") ? " " : registroAsistencias[2] ) +
                                        (registroAsistencias[3].equals("") ? " " : registroAsistencias[3] ) +
                                        (registroAsistencias[4].equals("") ? " " : registroAsistencias[4] ) + "\\\"" +
                            "  },";
                }
                
                //
                if(respuestaServ.length() == 0)
                    respuestaServ =
                        "{" +
                        " \"error\": false, " +
                        " \"errorMessage\": \"No tiene sesiones agendadas para hoy.\", " +
                        " \"fullName\": \"\", " +
                        " \"JSONAppointments\": \"\"" +
                        "}";
                else
                    respuestaServ =
                        "{" +
                        " \"error\": false, " +
                        " \"errorMessage\": \"\", " +
                        " \"fullName\": \"" + nombreEmpleado + "\", " +
                        " \"JSONAppointments\": \"[" +
                                                    respuestaServ.substring(0,
                                                        respuestaServ.lastIndexOf(",")) +
                                                 "]\"" +
                        "}";
            } else
                respuestaServ =
                    "{" +
                    " \"error\": true, " +
                    " \"errorMessage\": \"Usuario o contraseña inválidos.\", " +
                    " \"fullName\": \"\", " +
                    " \"JSONAppointments\": \"\"" +
                    "}";
        } catch(Exception ex){
            ex.printStackTrace();
            respuestaServ =
                "{" +
                " \"error\": true, " +
                " \"errorMessage\": \"Algo salió mal, por favor intente de nuevo. :(\", " +
                " \"fullName\": \"\", " +
                " \"JSONAppointments\": \"\"" +
                "}";
        } finally {
            out.println(respuestaServ);
            
            if(instanciaBD != null)
                instanciaBD.desconectar();
            
            instanciaBD = null;
            System.gc();
        }
    } // Fin del método doPost();
}