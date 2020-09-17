/*  Nombre.....:  CatalogoEquipo.java
 *  Descripción:  Llama al procedimiento almacenado encargado de crear o
 *                modificar el registro del catálogo de equipo médico.
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

public class CatalogoEquipo extends HttpServlet {

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
        String modoCatalogoEquipo = sesion.getAttribute("modoCatalogoEquipo").toString();
        String btnSubmit = request.getParameter("btnSubmit");

        if (btnSubmit != null && modoCatalogoEquipo.equals(Consts.EDICION)) {
            try {
                int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                int rolAsignado = Integer.parseInt(sesion.getAttribute("idRol").toString());
                instanciaBD = new $();
                CallableStatement spCatalogoEquipo = instanciaBD.storedProc(
                        Consts.SP_CATALOGO_EQUIPO_MEDICO,
                        Consts.CANT_PARMS_SP_CATALOGO_EQUIPO_MEDICO);

                // Prepara la infomacion requerida por el procedimento almacenado.
                spCatalogoEquipo.setLong(1, Long.parseLong(sesion.getAttribute("idSesion").toString()));
                spCatalogoEquipo.setInt(2, idCentro);
                spCatalogoEquipo.setInt(3, Integer.parseInt(
                        sesion.getAttribute("codigoCatalogoEquipo").toString()));
                spCatalogoEquipo.setInt(4, Integer.parseInt(
                        request.getParameter("proveedor")));
                spCatalogoEquipo.setString(5, ServiciosU.obtenerParametro(
                        request.getParameter("marca"), 50));
                spCatalogoEquipo.setString(6, ServiciosU.obtenerParametro(
                        request.getParameter("modelo"), 50));
                spCatalogoEquipo.setString(7, ServiciosU.obtenerParametro(
                        request.getParameter("nombre"), 100));
                spCatalogoEquipo.setString(8, ServiciosU.obtenerParametro(
                        request.getParameter("descripcion"), 300));
                spCatalogoEquipo.setFloat(9, Float.parseFloat(request.getParameter("costo")));
                spCatalogoEquipo.setInt(10, Integer.parseInt(
                        sesion.getAttribute("idUsuario").toString()));
                spCatalogoEquipo.setLong(11, ServiciosU.fechaATimestamp());

                registros = spCatalogoEquipo.executeQuery();
                if (registros.next()) {
                    if (registros.getInt(1) == Consts.SQL_OK) {
                        // Define a cual pagina se refirecciona.
                        sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                "Cambios sometidos satisfactoriamente."));
                        sesion.removeAttribute("codigoCatalogoEquipo");
                        response.sendRedirect(Consts.LOBBY_CATALOGO_EQUIPOS_HTML);
                    } else {
                        ServiciosU.fb(registros);
                        
                        // Redirecciona a la página web de origen y muestra mensaje de
                        // retroalimentación al usuario.
                        sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                "Error al actualizar los datos."));
                        sesion.setAttribute("errorCatalogoEquipo", true);
                        sesion.setAttribute("errorMarcaCatalogoEquipo",
                                request.getParameter("marca"));
                        sesion.setAttribute("errorModeloCatalogoEquipo",
                                request.getParameter("modelo"));
                        sesion.setAttribute("errorNombreCatalogoEquipo",
                                request.getParameter("nombre"));
                        sesion.setAttribute("errorDescripcionCatalogoEquipo",
                                request.getParameter("descripcion"));
                        sesion.setAttribute("errorCostoCatalogoEquipo",
                                request.getParameter("costo"));
                        sesion.setAttribute("errorCantidadCatalogoEquipo",
                                request.getParameter("cantidad"));
                        response.sendRedirect(Consts.CATALOGO_EQUIPO_HTML);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();

                // Redirecciona a la página web de origen y muestra mensaje de
                // retroalimentación al usuario.
                sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                    "Error al actualizar los datos."));
                sesion.setAttribute("errorCatalogoEquipo", true);
                sesion.setAttribute("errorMarcaCatalogoEquipo",
                        request.getParameter("marca"));
                sesion.setAttribute("errorModeloCatalogoEquipo",
                        request.getParameter("modelo"));
                sesion.setAttribute("errorNombreCatalogoEquipo",
                        request.getParameter("nombre"));
                sesion.setAttribute("errorDescripcionCatalogoEquipo",
                        request.getParameter("descripcion"));
                sesion.setAttribute("errorCostoCatalogoEquipo",
                        request.getParameter("costo"));
                sesion.setAttribute("errorCantidadCatalogoEquipo",
                        request.getParameter("cantidad"));
                response.sendRedirect(Consts.CATALOGO_EQUIPO_HTML);
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
            response.sendRedirect(Consts.CATALOGO_EQUIPO_HTML);
        }
    } // Fin del metodo doPost().
}