/*  Nombre.....:  Respaldo.java
 *  Descripción:  Llama a procedimiento almacenado encargado de crear o
 *                restaurar el respaldo de la base de datos.
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

public class Respaldo extends HttpServlet {
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
        int idRolAsignado = Integer.parseInt(sesion.getAttribute("idRol").toString());
        String btnRespaldar = request.getParameter("btnRespaldar");
        String btnRestaurar = request.getParameter("btnRestaurar");
        
        try {
            instanciaBD = new $();
            CallableStatement spBaseDatos;
            
            if(btnRespaldar != null){
                spBaseDatos = instanciaBD.storedProc( Consts.SP_RESPALDAR_BD );
            } else if(btnRestaurar != null)
                spBaseDatos = instanciaBD.storedProc( Consts.SP_RESTAURAR_BD );
            else
                return;
            
            // Ejecuta procediimento almacenado para respaldar/restaurar la base de datos.
            registros = spBaseDatos.executeQuery();
            if(registros.next() && registros.getInt(1) == Consts.SQL_OK){
                sesion.setAttribute("mensaje",
                        ServiciosU.sustituirCaracteres("Cambios sometidos satisfactoriamente."));
                response.sendRedirect(Consts.LOBBY_USUARIOS_HTML);
            } else {
                // Redirecciona a la página web de origen y muestra mensaje de
                // retroalimentación al usuario.
                ServiciosU.fb(registros);
                sesion.setAttribute("mensaje",
                        ServiciosU.sustituirCaracteres("Error al actualizar los datos."));
                response.sendRedirect(Consts.RESPALDO_HTML);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if(instanciaBD != null)
                instanciaBD.desconectar();
            
            instanciaBD = null;
            System.gc();
        }
    } // Fin del metodo doPost().
}