/*  Nombre.....:  CatalogoSuministro.java
 *  Descripción:  Llama al procedimiento almacenado encargado de crear o
 *                modificar el registro del catálogo de suministro médico.
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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CatalogoSuministro extends HttpServlet {

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
        String modoCatalogoSuministro = sesion.getAttribute("modoCatalogoSuministro").toString();
        String btnSubmit = request.getParameter("btnSubmit");

        if (btnSubmit != null && modoCatalogoSuministro.equals(Consts.EDICION)) {
            try {
                int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                int rolAsignado = Integer.parseInt(sesion.getAttribute("idRol").toString());
                instanciaBD = new $();
                CallableStatement spCatalogoSuministro = instanciaBD.storedProc(
                        Consts.SP_CATALOGO_SUMINISTRO_MEDICO,
                        Consts.CANT_PARMS_SP_CATALOGO_SUMINISTRO_MEDICO);

                // Prepara la infomacion requerida por el procedimento almacenado.
                spCatalogoSuministro.setLong(1, Long.parseLong(sesion.getAttribute("idSesion").toString()));
                spCatalogoSuministro.setInt(2, idCentro);
                spCatalogoSuministro.setString(3,
                        sesion.getAttribute("codigoCatalogoSuministro").toString());
                spCatalogoSuministro.setString(4, ServiciosU.obtenerParametro(
                        request.getParameter("codigo"), 25));
                spCatalogoSuministro.setInt(5, Integer.parseInt(
                        request.getParameter("proveedor")));
                spCatalogoSuministro.setString(6, ServiciosU.obtenerParametro(
                        request.getParameter("nombre"), 100));
                spCatalogoSuministro.setString(7, ServiciosU.obtenerParametro(
                        request.getParameter("descripcion"), 500));
                spCatalogoSuministro.setString(8,
                        request.getParameter("cbxCuantificable") != null ? "1" : "0");
                spCatalogoSuministro.setInt(9, Integer.parseInt(
                        sesion.getAttribute("idUsuario").toString()));
                spCatalogoSuministro.setLong(10, ServiciosU.fechaATimestamp());

                registros = spCatalogoSuministro.executeQuery();
                if (registros.next()) {
                    if (registros.getInt(1) == Consts.SQL_OK) {
                        // Define a cual pagina se refirecciona.
                        sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                            "Cambios sometidos satisfactoriamente."));
                        sesion.removeAttribute("codigoCatalogoSuministro");
                        response.sendRedirect(Consts.LOBBY_CATALOGO_SUMINISTROS_HTML);
                    } else {
                        ServiciosU.fb(registros);
                        
                        // Redirecciona a la página web de origen y muestra mensaje de
                        // retroalimentación al usuario.
                        sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                            "Error al actualizar los datos."));
                        sesion.setAttribute("errorCatalogoSuministro", true);
                        sesion.setAttribute("errorCodigoCatalogoSuministro",
                                request.getParameter("codigo"));
                        sesion.setAttribute("errorNombreCatalogoSuministro",
                                request.getParameter("nombre"));
                        sesion.setAttribute("errorDescripcionCatalogoSuministro",
                                request.getParameter("descripcion"));
                        sesion.setAttribute("errorCuantificableCatalogoSuministro",
                                request.getParameter("cbxCuantificable") != null ? "1" : "0");
                        response.sendRedirect(Consts.CATALOGO_SUMINISTRO_HTML);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                
                // Redirecciona a la página web de origen y muestra mensaje de
                // retroalimentación al usuario.
                sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                    "Error al actualizar los datos."));
                sesion.setAttribute("errorCatalogoSuministro", true);
                sesion.setAttribute("errorCodigoCatalogoSuministro",
                        request.getParameter("codigo"));
                sesion.setAttribute("errorNombreCatalogoSuministro",
                        request.getParameter("nombre"));
                sesion.setAttribute("errorDescripcionCatalogoSuministro",
                        request.getParameter("descripcion"));
                sesion.setAttribute("errorCuantificableCatalogoSuministro",
                        request.getParameter("cbxCuantificable") != null ? "1" : "0");
                response.sendRedirect(Consts.CATALOGO_SUMINISTRO_HTML);
            } finally {
                if (instanciaBD != null)
                    instanciaBD.desconectar();

                instanciaBD = null;
                System.gc();
            }
        } else {
            // Redirecciona a la página web de origen y muestra mensaje de
            // retroalimentación al usuario.
            sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                    "En este momento no puede realizar cambios debido a inconsistencias en su sesión."));
            response.sendRedirect(Consts.CATALOGO_SUMINISTRO_HTML);
        }
    } // Fin del metodo doPost().
}