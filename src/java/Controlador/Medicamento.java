/*  Nombre.....:  Medicamento.java
 *  Descripción:  Llama al procedimiento almacenado encargado de agregar, devolver
 *                y expedir medicamentos.
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
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Medicamento extends HttpServlet {
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
        
        // Variables para enviar y recibir información.
        HttpSession sesion = request.getSession(false); // crea o recupera sesion
        String btnSubmit = request.getParameter("btnSubmit");
        final String WEB_PAGE = request.getParameter("page").split("\\?")[0].split("\\#")[0];

        if (btnSubmit != null) {
            try {
                String respuesta = "";
                
                if(WEB_PAGE.equals(Consts.DEVOLUCION_MEDICAMENTO_HTML))
                    respuesta = devolverMedicamento(sesion, request, instanciaBD = new $());
                else if(WEB_PAGE.equals(Consts.EXPEDIR_MEDICAMENTO_HTML))
                    respuesta = expedirMedicamento(sesion, request, instanciaBD = new $());
                
                response.sendRedirect(respuesta);
            } catch (Exception ex) {
                ex.printStackTrace();
                
                sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                    "Error al actualizar los datos."));
                sesion.setAttribute("errorSuministroMedico", true);
                
                if(WEB_PAGE.equals(Consts.DEVOLUCION_MEDICAMENTO_HTML)){
                    sesion.setAttribute("errorDevolucionMedicamento", true);
                    sesion.setAttribute("errorSerieDevolucionMedicamento", request.getParameter("serie"));
                    sesion.setAttribute("errorLoteDevolucionMedicamento", request.getParameter("lote"));
                    sesion.setAttribute("errorCantidadDevolucionMedicamento", request.getParameter("cantidad"));
                    sesion.setAttribute("errorMotivoDevolucionMedicamento", request.getParameter("motivo"));
                } else if(WEB_PAGE.equals(Consts.DEVOLUCION_MEDICAMENTO_HTML)){
                    sesion.setAttribute("errorDevolucionMedicamento", true);
                }
                
                
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

    
    // Método que atiende el formulario de devolución de medicamentos.
    private String devolverMedicamento(HttpSession sesion, HttpServletRequest request, $ instanciaBD)
            throws ClassNotFoundException, SQLException {
        ResultSet registros = null; // contiene los registros que devuelve la sentencia SELECT.
        int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
        int rolAsignado = Integer.parseInt(sesion.getAttribute("idRol").toString());

        CallableStatement spDevolucionMedicamento = instanciaBD.storedProc(
                Consts.SP_DEVOLUCION_MEDICAMENTO,
                Consts.CANT_PARMS_SP_DEVOLUCION_MEDICAMENTO);

        // Prepara la infomacion requerida por el procedimento almacenado.
        spDevolucionMedicamento.setLong(1, Long.parseLong(sesion.getAttribute("idSesion").toString()));
        spDevolucionMedicamento.setInt(2, idCentro);
        spDevolucionMedicamento.setInt(3, rolAsignado);
        spDevolucionMedicamento.setString(4, ServiciosU.obtenerParametro(request.getParameter("serie"), 25));
        spDevolucionMedicamento.setString(5, ServiciosU.obtenerParametro(request.getParameter("lote"), 25));
        spDevolucionMedicamento.setInt(6, ServiciosU.formatearFecha(request.getParameter("cantidad")));
        spDevolucionMedicamento.setString(7, ServiciosU.obtenerParametro(request.getParameter("motivo"), 300));
        spDevolucionMedicamento.setInt(8, Integer.parseInt(sesion.getAttribute("idUsuario").toString()));
        spDevolucionMedicamento.setLong(9, ServiciosU.fechaATimestamp());
        
        registros = spDevolucionMedicamento.executeQuery();
        if (registros.next()) {
            if (registros.getInt(1) == Consts.SQL_OK) {
                // Define a cual pagina se refirecciona... al configurar
                // el departamento debe redirigir a configurar el puesto.
                sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                    "Cambios sometidos satisfactoriamente."));
                return Consts.LOBBY_MEDICAMENTOS_HTML;
            } else {
                ServiciosU.fb(registros);
            }
        }
        
        sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                            "Error al actualizar los datos."));
        sesion.setAttribute("errorDevolucionMedicamento", true);
        sesion.setAttribute("errorSerieDevolucionMedicamento", request.getParameter("serie"));
        sesion.setAttribute("errorLoteDevolucionMedicamento", request.getParameter("lote"));
        sesion.setAttribute("errorCantidadDevolucionMedicamento", request.getParameter("cantidad"));
        sesion.setAttribute("errorMotivoDevolucionMedicamento", request.getParameter("motivo"));
        return Consts.LOBBY_MEDICAMENTOS_HTML;
    }

    
    // Método que atiende el formulario de salida de medicamentos.
    private String expedirMedicamento(HttpSession sesion, HttpServletRequest request, $ instanciaBD)
            throws ClassNotFoundException, SQLException {
        ResultSet registros = null; // contiene los registros que devuelve la sentencia SELECT.
        int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());

        CallableStatement spExpedirMedicamento = instanciaBD.storedProc(
                Consts.SP_EXPEDIR_MEDICAMENTO,
                Consts.CANT_PARMS_SP_EXPEDIR_MEDICAMENTO);

        // Prepara la infomacion requerida por el procedimento almacenado.
        spExpedirMedicamento.setLong(1, Long.parseLong(sesion.getAttribute("idSesion").toString()));
        spExpedirMedicamento.setInt(2, idCentro);
        spExpedirMedicamento.setString(3, request.getParameter("output"));
        spExpedirMedicamento.setString(4, request.getParameter("cbxConforme") != null ? "1" : "0");
        spExpedirMedicamento.setString(5, "");
        spExpedirMedicamento.setInt(6, Integer.parseInt(sesion.getAttribute("idUsuario").toString()));
        spExpedirMedicamento.setLong(7, ServiciosU.fechaATimestamp());
        
        registros = spExpedirMedicamento.executeQuery();
        if (registros.next()) {
            if (registros.getInt(1) == Consts.SQL_OK) {
                // Define a cual pagina se refirecciona... al configurar
                // el departamento debe redirigir a configurar el puesto.
                sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                    "Cambios sometidos satisfactoriamente."));
                return Consts.LOBBY_MEDICAMENTOS_HTML;
            } else {
                ServiciosU.fb(registros);
            }
        }
        
        sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                            "Error al actualizar los datos."));
        return Consts.LOBBY_MEDICAMENTOS_HTML;
    }
}