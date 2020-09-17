/*  Nombre.....:  Validaciones.java
 *  Descripción:  Valida los diferentes de los formularios web para
 *                retroalimentar al usuario.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Diciembre, 2019.
 */
package Controlador;

import Modelo.$;
import Utilitarios.ServiciosU;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Validaciones extends HttpServlet {
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
        // Variables para enviar y recibir información.
        HttpSession sesion = request.getSession(true); // crea o recupera sesión
        PrintWriter out = response.getWriter();

        try {
            String nombreServ = request.getParameter("service"); // contiene el nombre de servicio al que se desea acceder
            String respuestaServ; // contiene la respuesta que brinda el servicio

            /* Todos los métodos de AJAX están identificados por el atributo NAME,
               esta página se encarga de brindar servicios a través de AJAX, si se
               ingresa directamente a este Servlet dicho atributo es NULL. */
            if (nombreServ != null) {
                int camposAValidar = 0;
                String nombreTabla = "";
                String nombreCampo = "";
                String nombreCampo01 = "";
                String nombreCampo02 = "";
                String valorParametro = "";
                String valorParametro01 = "";
                String valorParametro02 = "";
                String mensajeError = "";
                Object tipo = new Object();
                Object tipo01 = new Object();
                Object tipo02 = new Object();
                
                /* Servicio..............: Validate Department Name
                   Página que lo utiliza.: departamento.html
                   Función...............: Valida el nombre del departamento. */
                if (nombreServ.equals("VLDDPTNAM")) {
                    camposAValidar = 1;
                    tipo = String.class;
                    nombreTabla = "Departamento";
                    nombreCampo = "Nombre";
                    valorParametro = request.getParameter("name");
                    mensajeError = "Ya existe un departamento con este nombre.";
                } else
                
                
                /* Servicio..............: Validate Employee Code
                   Página que lo utiliza.: empleado.html
                   Función...............: Valida el codigo del empleado. */
                if (nombreServ.equals("VLDEMPCDE")) {
                    camposAValidar = 1;
                    tipo = String.class;
                    nombreTabla = "Empleado";
                    nombreCampo = "ID_Empleado";
                    valorParametro = request.getParameter("code");
                    mensajeError = "Ya existe un empleado con este código.";
                } else
                
                
                /* Servicio..............: Validate Employee ID
                   Página que lo utiliza.: empleado.html
                   Función...............: Valida el número de identidad del empleado. */
                if (nombreServ.equals("VLDEMPIDT")) {
                    camposAValidar = 1;
                    tipo = String.class;
                    nombreTabla = "Empleado";
                    nombreCampo = "Identidad";
                    valorParametro = request.getParameter("id");
                    mensajeError = "Ya existe un empleado con este número de identidad.";
                } else
                
                
                /* Servicio..............: Validate Equipment Code
                   Página que lo utiliza.: equipo.html
                   Función...............: Valida el número de serie del equipo. */
                if (nombreServ.equals("VLDEQUCDE")) {
                    camposAValidar = 1;
                    tipo = String.class;
                    nombreTabla = "Inventario_Equipo_Medico";
                    nombreCampo = "SerieEquipoMedico";
                    valorParametro = request.getParameter("code");
                    mensajeError = "Ya existe un equipo médico con este número de serie.";
                } else
                
                
                /* Servicio..............: Validate Medicine Code
                   Página que lo utiliza.: catalogomedicamento.html
                   Función...............: Valida el número de serie de
                                           catálogo del medicamento. */
                if (nombreServ.equals("VLDMDCCDE")) {
                    camposAValidar = 1;
                    tipo = String.class;
                    nombreTabla = "Catalogo_Medicamento";
                    nombreCampo = "Serie";
                    valorParametro = request.getParameter("code");
                    mensajeError = "Ya existe un registro con este número de serie.";
                } else
                
                
                /* Servicio..............: Validate Medical Equipment Model
                   Página que lo utiliza.: catalogoequipo.html
                   Función...............: Valida el nombre del registro del
                                           catálogo de equipo médico. */
                if (nombreServ.equals("VLDMEDEQUMOD")) {
                    camposAValidar = 2;
                    nombreTabla = "Catalogo_Equipo_Medico";
                    nombreCampo01 = "Marca";
                    nombreCampo02 = "Modelo";
                    tipo01 = String.class;
                    tipo02 = String.class;
                    valorParametro01 = request.getParameter("mark");
                    valorParametro02 = request.getParameter("model");
                    mensajeError = "Ya existe un registro con este modelo.";
                } else
                
                
                /* Servicio..............: Validate Medical Equipment Name
                   Página que lo utiliza.: catalogoequipo.html
                   Función...............: Valida el nombre del registro del
                                           catálogo de equipo médico. */
                if (nombreServ.equals("VLDMEDEQUNAM")) {
                    camposAValidar = 1;
                    tipo = String.class;
                    nombreTabla = "Catalogo_Equipo_Medico";
                    nombreCampo = "Nombre";
                    valorParametro = request.getParameter("name");
                    mensajeError = "Ya existe un registro con este nombre.";
                } else
                
                
                /* Servicio..............: Validate Medical Supply Code
                   Página que lo utiliza.: catalogosuministro.html
                   Función...............: Valida el número de serie de
                                           catálogo del suministro médico. */
                if (nombreServ.equals("VLDMEDSPPCDE")) {
                    camposAValidar = 1;
                    tipo = String.class;
                    nombreTabla = "Catalogo_Suministro_Medico";
                    nombreCampo = "Serie";
                    valorParametro = request.getParameter("code");
                    mensajeError = "Ya existe un registro con este número de serie.";
                } else
                
                
                /* Servicio..............: Validate Medical Supply Batch
                   Página que lo utiliza.: suministro.html
                   Función...............: Valida el número de lote del suministro
                                           médico. */
                if (nombreServ.equals("VLDMEDSPPBTH")) {
                    camposAValidar = 1;
                    tipo = String.class;
                    nombreTabla = "Inventario_Suministro_Medico";
                    nombreCampo = "Lote";
                    valorParametro = request.getParameter("batch");
                    mensajeError = "Ya existe un registro con este número de lote.";
                } else
                
                
                /* Servicio..............: Validate Modality Name
                   Página que lo utiliza.: modalidad.html
                   Función...............: Valida que el nombre de la modalidad no
                                           esté asignado. */
                if (nombreServ.equals("VLDMODNAM")) {
                    camposAValidar = 1;
                    tipo = String.class;
                    nombreTabla = "Modalidad";
                    nombreCampo = "Nombre";
                    valorParametro = request.getParameter("name");
                    mensajeError = "Ya existe un una modalidad con el nombre indicado.";
                } else
                
                
                /* Servicio..............: Validate Patient Code
                   Página que lo utiliza.: paciente.html
                   Función...............: Valida el código del paciente para
                                           no permitir códigos duplicados. */
                if (nombreServ.equals("VLDPTNCDE")) {
                    camposAValidar = 1;
                    tipo = String.class;
                    nombreTabla = "Paciente";
                    nombreCampo = "ID_Paciente";
                    valorParametro = request.getParameter("code");
                    mensajeError = "Ya existe un paciente con este código.";
                } else
                
                
                /* Servicio..............: Validate Patient Identity
                   Página que lo utiliza.: paciente.html
                   Función...............: Valida el número de identidad del paciente
                                            para no permitir identidades duplicadas. */
                if (nombreServ.equals("VLDPTNIDT")) {
                    camposAValidar = 1;
                    tipo = String.class;
                    nombreTabla = "Paciente";
                    nombreCampo = "Identidad";
                    valorParametro = request.getParameter("identity");
                    mensajeError = "Ya existe un paciente con este número de identidad.";
                } else
                
                
                /* Servicio..............: Validate Patient Cellphone
                   Página que lo utiliza.: paciente.html
                   Función...............: Valida el número de teléfono del paciente
                                           para no permitir números duplicados (celular). */
                if (nombreServ.equals("VLDPTNCEL")) {
                    camposAValidar = 1;
                    tipo = String.class;
                    nombreTabla = "Paciente";
                    nombreCampo = "Telefono";
                    valorParametro = request.getParameter("cellphone");
                    mensajeError = "Ya existe un paciente con este número de celular.";
                } else
                
                
                /* Servicio..............: Validate Provider Name
                   Página que lo utiliza.: proveedor.html
                   Función...............: Valida el nombre del proveedor para
                                           no permitir nombres duplicados. */
                if (nombreServ.equals("VLDPVDNAM")) {
                    camposAValidar = 1;
                    tipo = String.class;
                    nombreTabla = "Proveedor";
                    nombreCampo = "NombreProveedor";
                    valorParametro = request.getParameter("name");
                    mensajeError = "Ya existe un proveedor con este nombre.";
                }
                
                
                // * * * Funcionalidad de validación * * *
                if(camposAValidar == 1){
                    $ instancia = null;
                    PreparedStatement ps = null;
                    ResultSet resultadoConsulta = null;
                    String myQuery =
                        "SELECT CASE COUNT(*) " +
                        "       WHEN 0 THEN 0 " +
                        "       ELSE 1 END Error " +
                        "FROM " + nombreTabla + " " +
                        "WHERE " + nombreCampo + " = ?;";

                    // Realiza consulta del campo indicado a la tabla correspondiente.
                    instancia = new $();
                    ps = instancia.prepStatement(myQuery);
                    if(tipo.equals(String.class))
                        ps.setString(1, valorParametro);
                    
                    resultadoConsulta = ps.executeQuery();

                    if (resultadoConsulta.next())
                        respuestaServ =
                            "{ \"error\": " + resultadoConsulta.getBoolean("Error") + ", " +
                            "  \"mensaje\": " + (resultadoConsulta.getBoolean("Error") ?
                            "               \"" + mensajeError + "\"" : "\"\"") + 
                            "}";
                    else
                        respuestaServ =
                            "{ \"error\": true, " +
                            "  \"mensaje\": \"Error de comunicación.\" }";
                    
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                } else if(camposAValidar == 2){
                    $ instancia = null;
                    PreparedStatement ps = null;
                    ResultSet resultadoConsulta = null;
                    String myQuery =
                        "SELECT CASE COUNT(*) " +
                        "       WHEN 0 THEN 0 " +
                        "       ELSE 1 END Error " +
                        "FROM " + nombreTabla + " " +
                        "WHERE " + nombreCampo01 + " = ? " +
                        "  AND " + nombreCampo02 + " = ?;";

                    // Realiza consulta del campo indicado a la tabla correspondiente.
                    instancia = new $();
                    ps = instancia.prepStatement(myQuery);
                    if(tipo01.equals(String.class))
                        ps.setString(1, valorParametro01);
                    
                    if(tipo02.equals(String.class))
                        ps.setString(2, valorParametro02);
                    
                    
                    resultadoConsulta = ps.executeQuery();
                    if (resultadoConsulta.next())
                        respuestaServ =
                            "{ \"error\": " + resultadoConsulta.getBoolean("Error") + ", " +
                            "  \"mensaje\": " + (resultadoConsulta.getBoolean("Error") ?
                            "               \"" + mensajeError + "\"" : "\"\"") + 
                            "}";
                    else
                        respuestaServ =
                            "{ \"error\": true, " +
                            "  \"mensaje\": \"Error de comunicación.\" }";
                    
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            out.print( ServiciosU.sustituirCaracteres(
                "{ \"error\": true, " +
                "  \"mensaje\": \"Error de comunicación.\" }") );
        }
    }
}