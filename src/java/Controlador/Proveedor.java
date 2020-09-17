/*  Nombre.....:  Proveedor.java
 *  Descripción:  Llama al procedimiento almacenado encargado de crear o
 *                modificar el proveedor.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Julio, 2019.
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

public class Proveedor extends HttpServlet {
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
        String modoProveedor = sesion.getAttribute("modoProveedor").toString();
        String btnSubmit = request.getParameter("btnSubmit");

        if (btnSubmit != null && modoProveedor.equals(Consts.EDICION)) {
            try {
                int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                int rolAsignado = Integer.parseInt(sesion.getAttribute("idRol").toString());
                instanciaBD = new $();
                CallableStatement spProveedor = instanciaBD.storedProc(
                        Consts.SP_PROVEEDOR,
                        Consts.CANT_PARMS_SP_PROVEEDOR);

                // Prepara la infomacion requerida por el procedimento almacenado.
                spProveedor.setLong(1, Long.parseLong(sesion.getAttribute("idSesion").toString()));
                spProveedor.setInt(2, idCentro);
                spProveedor.setInt(3, Integer.parseInt(
                        sesion.getAttribute("codigoProveedor").toString()));
                spProveedor.setString(4, ServiciosU.obtenerParametro(
                        request.getParameter("nombreProveedor"), 100));
                spProveedor.setInt(5, Integer.parseInt(
                        request.getParameter("telefonoProveedor")));
                spProveedor.setString(6, ServiciosU.obtenerParametro(
                        request.getParameter("correoElectronico"), 50));
                spProveedor.setString(7, ServiciosU.obtenerParametro(
                        request.getParameter("direccion"), 250));
                spProveedor.setString(8, ServiciosU.obtenerParametro(
                        request.getParameter("nombreContacto"), 50));
                spProveedor.setInt(9, Integer.parseInt(
                        request.getParameter("telefonoContacto")));
                spProveedor.setString(10, ServiciosU.obtenerParametro(
                        request.getParameter("tipo"), 1));
                spProveedor.setString(11, request.getParameter("cbxActivo") != null ? "1" : "0");
                spProveedor.setInt(12, Integer.parseInt(
                        sesion.getAttribute("idUsuario").toString()));
                spProveedor.setLong(13, ServiciosU.fechaATimestamp());
                
                registros = spProveedor.executeQuery();
                if (registros.next()) {
                    if (registros.getInt(1) == Consts.SQL_OK) {
                        // Redirecciona al lobby correspondiente.
                        sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                            "Cambios sometidos satisfactoriamente."));
                        sesion.removeAttribute("codigoProveedor");
                        response.sendRedirect(Consts.LOBBY_PROVEEDORES_HTML);
                    } else {
                        ServiciosU.fb(registros);

                        // Redirecciona a la página web de origen y muestra mensaje de
                        // retroalimentación al usuario.
                        sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                            "Error al actualizar los datos."));
                        sesion.setAttribute("errorProveedor", true);
                        sesion.setAttribute("errorNombreProveedor",
                                request.getParameter("nombreProveedor"));
                        sesion.setAttribute("errorTelefonoProveedor",
                                request.getParameter("telefonoProveedor"));
                        sesion.setAttribute("errorCorreoElectronicoProveedor",
                                request.getParameter("correoElectronico"));
                        sesion.setAttribute("errorDireccionProveedor",
                                request.getParameter("direccion"));
                        sesion.setAttribute("errorNombreContactoProveedor",
                                request.getParameter("nombreContacto"));
                        sesion.setAttribute("errorTelefonoContactoProveedor",
                                request.getParameter("telefonoContacto"));
                        sesion.setAttribute("errorEstadoProveedor",
                                request.getParameter("cbxActivo") != null ? "1" : "0");
                        
                        response.sendRedirect(Consts.PROVEEDOR_HTML);
                    }
                } else {
                    // Redirecciona a la página web de origen y muestra mensaje de
                    // retroalimentación al usuario.
                    sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                        "Error al actualizar los datos."));
                    sesion.setAttribute("errorProveedor", true);
                    sesion.setAttribute("errorNombreProveedor",
                            request.getParameter("nombreProveedor"));
                    sesion.setAttribute("errorTelefonoProveedor",
                            request.getParameter("telefonoProveedor"));
                    sesion.setAttribute("errorCorreoElectronicoProveedor",
                            request.getParameter("correoElectronico"));
                    sesion.setAttribute("errorDireccionProveedor",
                            request.getParameter("direccion"));
                    sesion.setAttribute("errorNombreContactoProveedor",
                            request.getParameter("nombreContacto"));
                    sesion.setAttribute("errorTelefonoContactoProveedor",
                            request.getParameter("telefonoContacto"));
                    sesion.setAttribute("errorEstadoProveedor",
                            request.getParameter("cbxActivo") != null ? "1" : "0");
                        
                    response.sendRedirect(Consts.PROVEEDOR_HTML);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                
                // Redirecciona a la página web de origen y muestra mensaje de
                // retroalimentación al usuario.    
                sesion.setAttribute("errorProveedor", true);
                sesion.setAttribute("errorNombreProveedor",
                        request.getParameter("nombreProveedor"));
                sesion.setAttribute("errorTelefonoProveedor",
                        request.getParameter("telefonoProveedor"));
                sesion.setAttribute("errorCorreoElectronicoProveedor",
                        request.getParameter("correoElectronico"));
                sesion.setAttribute("errorDireccionProveedor",
                        request.getParameter("direccion"));
                sesion.setAttribute("errorNombreContactoProveedor",
                        request.getParameter("nombreContacto"));
                sesion.setAttribute("errorTelefonoContactoProveedor",
                        request.getParameter("telefonoContacto"));
                sesion.setAttribute("errorEstadoProveedor",
                        request.getParameter("cbxActivo") != null ? "1" : "0");
                        
                response.sendRedirect(Consts.PROVEEDOR_HTML);
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
            response.sendRedirect(Consts.PROVEEDOR_HTML);
        }
    } // Fin del metodo doPost().
}