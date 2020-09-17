/*  Nombre.....:  Modalidad.java
 *  Descripción:  Llama al procedimiento almacenado encargado de crear o
 *                modificar la modalidad.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Diciembre, 2019.
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

public class Modalidad extends HttpServlet {
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
        String modoModalidad = sesion.getAttribute("modoModalidad").toString();
        String btnSubmit = request.getParameter("btnSubmit");
        
        if (btnSubmit != null && modoModalidad.equals(Consts.EDICION)) {
            try {
                int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                int rolAsignado = Integer.parseInt(sesion.getAttribute("idRol").toString());
                instanciaBD = new $();
                CallableStatement spModalidad = instanciaBD.storedProc(
                        Consts.SP_MODALIDAD, Consts.CANT_PARMS_SP_MODALIDAD);

                // Prepara la infomacion requerida por el procedimento almacenado.
                System.out.println("1 " + Long.parseLong(sesion.getAttribute("idSesion").toString()));
                System.out.println("2 " + idCentro);
                System.out.println("3 " + 26);
                System.out.println("4 " + ServiciosU.obtenerParametro(
                        request.getParameter("nombre"), 50));
                System.out.println("5 " + request.getParameter("modalidad"));
                System.out.println("6 " + sesion.getAttribute("idUsuario").toString());
                
                spModalidad.setLong(1, Long.parseLong(sesion.getAttribute("idSesion").toString()));
                spModalidad.setInt(2, idCentro);
                spModalidad.setInt(3, Integer.parseInt(
                        sesion.getAttribute("codigoModalidad").toString()));
                spModalidad.setString(4, ServiciosU.obtenerParametro(
                        request.getParameter("nombre"), 50));
                spModalidad.setString(5, request.getParameter("modalidad"));
                spModalidad.setInt(6, Integer.parseInt(
                        sesion.getAttribute("idUsuario").toString()));
                spModalidad.setLong(7, ServiciosU.fechaATimestamp());

                registros = spModalidad.executeQuery();
                if (registros.next()) {
                    if (registros.getInt(1) == Consts.SQL_OK) {
                        // Define a cual pagina se refirecciona... al configurar
                        // el departamento debe redirigir a configurar el puesto.
                        sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                            "Cambios sometidos satisfactoriamente."));
                        sesion.removeAttribute("codigoModalidad");
                        response.sendRedirect(Consts.LOBBY_MODALIDADES_HTML);
                        
                    } else {
                        ServiciosU.fb(registros);

                        // Redirecciona a la página web de origen y muestra mensaje de
                        // retroalimentación al usuario.
                        sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                            "Error al actualizar los datos."));
                        response.sendRedirect(Consts.MODALIDAD_HTML);
                    }
                } else {
                    // Redirecciona a la página web de origen y muestra mensaje de
                    // retroalimentación al usuario.
                    sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                        "Error al actualizar los datos."));
                    response.sendRedirect(Consts.MODALIDAD_HTML);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                // Redirecciona a la página web de origen y muestra mensaje de
                // retroalimentación al usuario.
                sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                    "Error al actualizar los datos."));
                response.sendRedirect(Consts.MODALIDAD_HTML);
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
            response.sendRedirect(Consts.MODALIDAD_HTML);
        }
    } // Fin del método doPost().
}