/*  Nombre.....:  Departamento.java
 *  Descripción:  Llama al procedimiento almacenado encargado de crear o
 *                modificar el departamento.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Julio, 2019.
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

public class Departamento extends HttpServlet {
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
        String modoDepartamento = sesion.getAttribute("modoDepartamento").toString();
        String btnSubmit = request.getParameter("btnSubmit");

        if (btnSubmit != null && modoDepartamento.equals(Consts.EDICION)) {
            try {
                int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                int rolAsignado = Integer.parseInt(sesion.getAttribute("idRol").toString());
                instanciaBD = new $();
                CallableStatement spDepartamento = instanciaBD.storedProc(
                        Consts.SP_DEPARTAMENTO,
                        Consts.CANT_PARMS_SP_DEPARTAMENTO);

                // Prepara la infomacion requerida por el procedimento almacenado.
                spDepartamento.setLong(1, Long.parseLong(sesion.getAttribute("idSesion").toString()));
                spDepartamento.setInt(2, idCentro);
                spDepartamento.setInt(3, Integer.parseInt(
                        sesion.getAttribute("codigoDepartamento").toString()));
                spDepartamento.setString(4, ServiciosU.obtenerParametro(
                        request.getParameter("nombre"), 100));
                spDepartamento.setString(5, ServiciosU.obtenerParametro(
                        request.getParameter("descripcion"), 300));
                spDepartamento.setString(6, request.getParameter("cbxActivo") != null ? "1" : "0");
                spDepartamento.setInt(7, Integer.parseInt(
                        sesion.getAttribute("idUsuario").toString()));
                spDepartamento.setLong(8, ServiciosU.fechaATimestamp());

                registros = spDepartamento.executeQuery();
                if (registros.next()) {
                    if (registros.getInt(1) == Consts.SQL_OK) {
                        // Define a cual pagina se refirecciona... al configurar
                        // el departamento debe redirigir a configurar el puesto.
                        if (ParametrosBD.getConfiguracionSistema(idCentro).equals(
                                Consts.DEPARTAMENTO_CONFIGURADO)) {
                            sesion.removeAttribute("codigoDepartamento");
                            sesion.setAttribute("codigoPuesto", "0");
                            response.sendRedirect(Consts.PUESTO_HTML);
                        } else {
                            sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                                "Cambios sometidos satisfactoriamente."));
                            sesion.removeAttribute("codigoDepartamento");
                            response.sendRedirect(Consts.LOBBY_DEPARTAMENTOS_HTML);
                        }
                    } else {
                        ServiciosU.fb(registros);

                        // Redirecciona a la página web de origen y muestra mensaje de
                        // retroalimentación al usuario.
                        sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                            "Error al actualizar los datos."));
                        sesion.setAttribute("errorDepartamento", true);
                        sesion.setAttribute("errorNombreDepartamento",
                                request.getParameter("nombre"));
                        sesion.setAttribute("errorEstadoDepartamento",
                                request.getParameter("cbxActivo") != null ? "1" : "0");
                        sesion.setAttribute("errorDescripcionDepartamento",
                                request.getParameter("descripcion"));
                        response.sendRedirect(Consts.DEPARTAMENTO_HTML);
                    }
                } else {
                    // Redirecciona a la página web de origen y muestra mensaje de
                    // retroalimentación al usuario.
                    sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                        "Error al actualizar los datos."));
                    sesion.setAttribute("errorDepartamento", true);
                    sesion.setAttribute("errorNombreDepartamento",
                            request.getParameter("nombre"));
                    sesion.setAttribute("errorEstadoDepartamento",
                            request.getParameter("cbxActivo") != null ? "1" : "0");
                    sesion.setAttribute("errorDescripcionDepartamento",
                            request.getParameter("descripcion"));
                    response.sendRedirect(Consts.DEPARTAMENTO_HTML);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                // Redirecciona a la página web de origen y muestra mensaje de
                // retroalimentación al usuario.
                sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                    "Error al actualizar los datos."));
                response.sendRedirect(Consts.DEPARTAMENTO_HTML);
            } finally {
                if (instanciaBD != null) {
                    instanciaBD.desconectar();
                }

                instanciaBD = null;
                System.gc();
            }
        } else {
            // Redirecciona a la página web de origen y muestra mensaje de
            // retroalimentación al usuario.
            sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                    "En este momento no puede realizar cambios debido a inconsistencias en su sesión."));
            response.sendRedirect(Consts.DEPARTAMENTO_HTML);
        }
    } // Fin del metodo doPost().
}