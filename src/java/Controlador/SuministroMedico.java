/*  Nombre.....:  SuministroMedico.java
 *  Descripción:  Llama al procedimiento almacenado encargado de crear o
 *                modificar el suministro médico.
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

public class SuministroMedico extends HttpServlet {
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
        String modoSuministroMedico = sesion.getAttribute("modoSuministroMedico").toString();
        String btnSubmit = request.getParameter("btnSubmit");
        final String WEB_PAGE = request.getParameter("page");

        if (btnSubmit != null && modoSuministroMedico.equals(Consts.EDICION)) {
            try {
                int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                int rolAsignado = Integer.parseInt(sesion.getAttribute("idRol").toString());
                ServiciosM modelo = new ServiciosM();

                instanciaBD = new $();
                CallableStatement spSuministroMedico
                        = instanciaBD.storedProc(Consts.SP_INV_SUMINISTRO_MEDICO,
                                Consts.CANT_PARMS_SP_INV_SUMINISTRO_MEDICO);

                // Prepara la infomacion requerida por el procedimento almacenado.
                spSuministroMedico.setLong(1, Long.parseLong(sesion.getAttribute("idSesion").toString()));
                spSuministroMedico.setInt(2, idCentro);
                spSuministroMedico.setString(3, ServiciosU.obtenerParametro(
                        request.getParameter("serie"), 25));
                spSuministroMedico.setString(4, 
                        WEB_PAGE.equals(Consts.AGREGAR_SUMINISTRO_HTML) ?
                            ServiciosU.obtenerParametro(request.getParameter("lote"), 25) :
                            ServiciosU.obtenerParametro(request.getParameter("codigo"), 25) );
                spSuministroMedico.setFloat(5, Float.parseFloat(
                        request.getParameter("costo")));
                spSuministroMedico.setInt(6, Integer.parseInt(
                        request.getParameter("cantidad")));
                spSuministroMedico.setInt(7, ServiciosU.formatearFecha(
                        request.getParameter("fechaVencimiento")));
                spSuministroMedico.setInt(8, Integer.parseInt(
                        sesion.getAttribute("idUsuario").toString()));
                spSuministroMedico.setLong(9, ServiciosU.fechaATimestamp());
                
                registros = spSuministroMedico.executeQuery();
                if (registros.next()) {
                    if (registros.getInt(1) == Consts.SQL_OK) {
                        // Define a cual pagina se refirecciona... al configurar
                        // el departamento debe redirigir a configurar el puesto.
                        sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                "Cambios sometidos satisfactoriamente."));
                        sesion.removeAttribute("codigoSuministroMedico");
                        response.sendRedirect(Consts.LOBBY_SUMINISTROS_HTML);
                    } else {
                        ServiciosU.fb(registros);
                        sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                "Error al actualizar los datos."));
                        sesion.setAttribute("errorSuministroMedico", true);
                        sesion.setAttribute("paginaSuministroMedico", WEB_PAGE);
                        sesion.setAttribute("errorSerieSuministroMedico",
                                request.getParameter("serie"));
                        sesion.setAttribute("errorLoteSuministroMedico",
                            WEB_PAGE.equals(Consts.AGREGAR_SUMINISTRO_HTML) ?
                                request.getParameter("lote"):
                                request.getParameter("codigo"));
                        sesion.setAttribute("errorCantidadSuministroMedico",
                                request.getParameter("cantidad"));
                        sesion.setAttribute("errorCostoSuministroMedico",
                                request.getParameter("costo"));
                        sesion.setAttribute("errorFechaVencimientoSuministroMedico",
                                request.getParameter("fechaVencimiento"));
                        
                        response.sendRedirect(WEB_PAGE);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                
                sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                    "Error al actualizar los datos."));
                sesion.setAttribute("errorSuministroMedico", true);
                sesion.setAttribute("errorSerieSuministroMedico",
                        request.getParameter("serie"));
                sesion.setAttribute("errorLoteSuministroMedico",
                            WEB_PAGE.equals(Consts.AGREGAR_SUMINISTRO_HTML) ?
                                request.getParameter("lote"):
                                request.getParameter("codigo"));
                sesion.setAttribute("errorCantidadSuministroMedico",
                        request.getParameter("cantidad"));
                sesion.setAttribute("errorCostoSuministroMedico",
                        request.getParameter("costo"));
                sesion.setAttribute("errorFechaVencimientoSuministroMedico",
                        request.getParameter("fechaVencimiento"));
                response.sendRedirect(WEB_PAGE);
            } finally {
                if (instanciaBD != null)
                    instanciaBD.desconectar();

                instanciaBD = null;
                System.gc();
            }
        } else {
            sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                    "En este momento no puede realizar cambios debido a inconsistencias en su sesión."));
            response.sendRedirect(WEB_PAGE);
        }
    } // Fin del metodo doPost().
}