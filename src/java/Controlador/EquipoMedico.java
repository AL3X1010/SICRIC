/*  Nombre.....:  EquipoMedico.java
 *  Descripción:  Llama al procedimiento almacenado encargado de crear o
 *                modificar el equipo médico.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Diciembre, 2019.
 */
package Controlador;

import Modelo.$;
import Modelo.ServiciosM;
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

public class EquipoMedico extends HttpServlet {
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
        ResultSet registros = null; // contiene los registros que devuelve la sentencia SELECT.

        // Variables para enviar y recibir información.
        HttpSession sesion = request.getSession(false); // crea o recupera sesion
        String modoEquipoMedico = sesion.getAttribute("modoEquipoMedico").toString();
        String btnSubmit = request.getParameter("btnSubmit");

        if (btnSubmit != null && modoEquipoMedico.equals(Consts.EDICION)) {
            try {
                int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                int rolAsignado = Integer.parseInt(sesion.getAttribute("idRol").toString());
                ServiciosM modelo = new ServiciosM();
                int codigoCatalagoNuevo = modelo.obtenerCodigoCatalogo(
                        request.getParameter("marca"), request.getParameter("modelo"));

                instanciaBD = new $();
                CallableStatement spEquipoMedico
                        = instanciaBD.storedProc(Consts.SP_INV_EQUIPO_MEDICO,
                                Consts.CANT_PARMS_SP_INV_EQUIPO_MEDICO);

                // Prepara la infomacion requerida por el procedimento almacenado.
                spEquipoMedico.setLong(1, Long.parseLong(sesion.getAttribute("idSesion").toString()));
                spEquipoMedico.setInt(2, idCentro);
                spEquipoMedico.setInt(3, Integer.parseInt(
                        sesion.getAttribute("codigoCatalogoAnterior").toString()));
                spEquipoMedico.setInt(4, codigoCatalagoNuevo);
                spEquipoMedico.setString(5, sesion.getAttribute("codigoEquipoMedico").toString());
                spEquipoMedico.setString(6, ServiciosU.obtenerParametro(
                        request.getParameter("codigo"), 20));
                spEquipoMedico.setInt(7, ServiciosU.formatearFecha(request.getParameter("fechaIngreso")));
                spEquipoMedico.setInt(8, ServiciosU.formatearFecha(request.getParameter("fechaMantenimiento")));
                spEquipoMedico.setString(9, request.getParameter("cbxActivo") != null ? "1" : "0");
                spEquipoMedico.setInt(10, Integer.parseInt(
                        sesion.getAttribute("idUsuario").toString()));
                spEquipoMedico.setLong(11, ServiciosU.fechaATimestamp());

                registros = spEquipoMedico.executeQuery();
                if (registros.next()) {
                    if (registros.getInt(1) == Consts.SQL_OK) {
                        // Define a cual pagina se refirecciona... al configurar
                        // el departamento debe redirigir a configurar el puesto.
                        sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                "Cambios sometidos satisfactoriamente."));
                        sesion.removeAttribute("codigoEquipoMedico");
                        response.sendRedirect(Consts.LOBBY_EQUIPOS_HTML);
                    } else {
                        ServiciosU.fb(registros);
                        sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                "Error al actualizar los datos."));
                        sesion.setAttribute("errorEquipoMedico", true);
                        sesion.setAttribute("errorCodigoEquipoMedico", request.getParameter("codigo"));
                        sesion.setAttribute("errorFechaIngresoEquipoMedico", request.getParameter("fechaIngreso"));
                        sesion.setAttribute("errorFechaMantenimientoEquipoMedico", request.getParameter("fechaMantenimiento"));
                        sesion.setAttribute("errorEstadoEquipoMedico", request.getParameter("cbxActivo") != null ? "1" : "0");
                        response.sendRedirect(Consts.EQUIPO_HTML);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (instanciaBD != null) {
                    instanciaBD.desconectar();
                }

                instanciaBD = null;
                System.gc();
            }
        } else {
            sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                    "En este momento no puede realizar cambios debido a inconsistencias en su sesión."));
            response.sendRedirect(Consts.EQUIPO_HTML);
        }
    } // Fin del metodo doPost().
}