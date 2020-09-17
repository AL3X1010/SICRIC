/*  Nombre.....:  Paciente.java
 *  Descripción:  Llama al procedimiento almacenado encargado de crear o
 *                modificar el paciente.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Octubre, 2019.
 */
package Controlador;

import Modelo.$;
import Modelo.ParametrosBD;
import Utilitarios.Consts;
import Utilitarios.ServiciosU;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Paciente extends HttpServlet {
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
        response.sendRedirect("index.html");    // redirige a pagina principal.
    } // Fin del metodo doGet().

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
        ResultSet registros = null; // contiene los registros que devuelve la sentencia SELECT.

        // Variables para enviar y recibir información.
        HttpSession sesion = request.getSession(false); // crea o recupera sesión
        String modoPaciente = sesion.getAttribute("modoPaciente").toString();
        String btnSubmit = request.getParameter("btnSubmit");

        if (btnSubmit != null && modoPaciente.equals(Consts.EDICION)) {
            try {
                int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                int rolAsignado = Integer.parseInt(sesion.getAttribute("idRol").toString());
                instanciaBD = new $();
                CallableStatement sp = instanciaBD.storedProc(Consts.SP_PACIENTE, Consts.CANT_PARMS_SP_PACIENTE);
                
                // Prepara la infomacion requerida por el procedimento almacenado.
                sp.setLong(1, Long.parseLong(sesion.getAttribute("idSesion").toString()));
                sp.setInt(2, idCentro);
                sp.setString(3, sesion.getAttribute("codigoPaciente").toString());
                sp.setString(4, ServiciosU.obtenerParametro(request.getParameter("codigo"), 15));
                sp.setInt(5, Integer.parseInt(request.getParameter("centroRemision")));
                sp.setInt(6, Integer.parseInt(request.getParameter("estadoCivil")));
                sp.setInt(7, Integer.parseInt(request.getParameter("genero")));
                sp.setString(8, ServiciosU.obtenerParametro(request.getParameter("nombre"), 50));
                sp.setString(9, ServiciosU.obtenerParametro(request.getParameter("apellido"), 50));
                sp.setString(10, ServiciosU.obtenerParametro(request.getParameter("identidad"), 15));
                sp.setString(11, ServiciosU.obtenerParametro(request.getParameter("direccion"), 250));
                sp.setInt(12, Integer.parseInt(request.getParameter("telefono")));
                sp.setInt(13, Integer.parseInt(request.getParameter("telefonoEmergencia")));
                sp.setString(14, ServiciosU.obtenerParametro(request.getParameter("correoElectronico"), 50));
                sp.setInt(15, ServiciosU.formatearFecha(request.getParameter("fechaNacimiento")));
                sp.setInt(16, ServiciosU.formatearFecha(request.getParameter("fechaIngreso")));
                sp.setString(17, ServiciosU.obtenerParametro(request.getParameter("medico"), 50));
                sp.setInt(18, Integer.parseInt(sesion.getAttribute("idUsuario").toString()));
                sp.setLong(19, ServiciosU.fechaATimestamp());

                registros = sp.executeQuery();
                if (registros.next()) {
                    if (registros.getInt(1) == Consts.SQL_OK) {
                        // Define a cual pagina se refirecciona... al configurar
                        // el departamento debe redirigir a configurar el puesto.
                        sesion.removeAttribute("codigoPaciente");
                        response.sendRedirect(Consts.LOBBY_PACIENTES_HTML);
                    } else {
                        ServiciosU.fb(registros);
                        sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                "Error al actualizar los datos."));

                        response.sendRedirect(Consts.PACIENTE_HTML);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                        "Error al actualizar los datos."));

                response.sendRedirect(Consts.PACIENTE_HTML);
            } finally {
                if (instanciaBD != null) {
                    instanciaBD.desconectar();
                }

                instanciaBD = null;
                System.gc();
            }
        }
    } // Fin del método doPost().
}
