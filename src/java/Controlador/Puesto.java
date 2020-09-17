/*  Nombre.....:  Puesto.java
 *  Descripción:  Llama al procedimiento almacenado encargado de crear o
 *                modificar el puesto de trabajo.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Julio, 2019.
 */
package Controlador;

import Modelo.$;
import Modelo.ParametrosBD;
import Utilitarios.Consts;
import Utilitarios.ServiciosU;
import static Utilitarios.ServiciosU.fechaATimestamp;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Puesto extends HttpServlet {

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
        HttpSession sesion = request.getSession(false); // crea o recupera sesion
        String modoPuesto = sesion.getAttribute("modoPuesto").toString();
        String btnSubmit = request.getParameter("btnSubmit");

        if (btnSubmit != null && modoPuesto.equals(Consts.EDICION)) {
            try {
                int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                int idSesion = Integer.parseInt(sesion.getAttribute("idSesion").toString());
                int rolAsignado = Integer.parseInt(sesion.getAttribute("idRol").toString());
                int idPuestoJefe = request.getParameter("puestoJefe") == null ? 0
                        : Integer.parseInt(request.getParameter("puestoJefe"));
                int idPuestoRespaldo = request.getParameter("puestoRespaldo") == null ? 0
                        : Integer.parseInt(request.getParameter("puestoRespaldo"));
                instanciaBD = new $();
                CallableStatement sp = instanciaBD.storedProc(Consts.SP_PUESTO, Consts.CANT_PARMS_SP_PUESTO);

                // Prepara la información requerida por el procedimiento almacenado.
                sp.setInt(1, idSesion);
                sp.setInt(2, Integer.parseInt(sesion.getAttribute("codigoPuesto").toString()));
                sp.setInt(3, Integer.parseInt(request.getParameter("departamento")));
                sp.setInt(4, idPuestoJefe);
                sp.setInt(5, idPuestoRespaldo);
                sp.setInt(6, Integer.parseInt(request.getParameter("experiencia")));
                sp.setString(7, request.getParameter("tipoSalario"));
                sp.setString(8, ServiciosU.obtenerParametro(
                        request.getParameter("nombre"), 50));
                sp.setString(9, ServiciosU.obtenerParametro(
                        request.getParameter("descripcion"), 250));
                sp.setString(10, request.getParameter("cbxActivo") != null ? "1" : "0");
                sp.setString(11, ServiciosU.obtenerParametro(
                        request.getParameter("objetivo"), 350));
                sp.setString(12, ServiciosU.obtenerParametro(
                        request.getParameter("autoridad"), 150));
                sp.setString(13, request.getParameter("genero"));
                sp.setString(14, request.getParameter("estadoCivil"));
                sp.setInt(15, Integer.parseInt(ServiciosU.obtenerParametro(
                        request.getParameter("edadMinima"), 3)));
                sp.setInt(16, Integer.parseInt(ServiciosU.obtenerParametro(
                        request.getParameter("edadMaxima"), 3)));
                sp.setString(17, ServiciosU.obtenerParametro(
                        request.getParameter("notaExperiencia"), 100));
                sp.setFloat(18, Float.parseFloat(request.getParameter("salarioBase")));
                sp.setString(23, request.getParameter("rbtTiempo"));
                sp.setString(24, request.getParameter("inputARP"));
                sp.setString(25, request.getParameter("inputCB"));
                sp.setString(26, request.getParameter("inputCYH"));
                sp.setString(27, request.getParameter("inputRAC"));
                sp.setInt(28, Integer.parseInt(sesion.getAttribute("idUsuario").toString()));
                sp.setLong(29, fechaATimestamp());

                switch (request.getParameter("tipoSalario")) {
                    case Consts.TIPO_SALARIO_TIEMPO:
                        sp.setFloat(19, (float) 0.0);
                        sp.setFloat(20, (float) 0.0);
                        sp.setFloat(21, (float) 0.0);
                        sp.setFloat(22, (float) 0.0);
                        break;
                    case Consts.TIPO_SALARIO_OBRA:
                        sp.setFloat(19, Float.parseFloat(request.getParameter("salarioUniObr")));
                        sp.setFloat(20, (float) 0.0);
                        sp.setFloat(21, Float.parseFloat(request.getParameter("sueloUniObr")));
                        sp.setFloat(22, Float.parseFloat(request.getParameter("techoUniObr")));
                        break;
                    case Consts.TIPO_SALARIO_PARTICIPACION:
                        sp.setFloat(19, (float) 0.0);
                        sp.setFloat(20, Float.parseFloat(request.getParameter("participacion")));
                        sp.setFloat(21, Float.parseFloat(request.getParameter("sueloParticipacion")));
                        sp.setFloat(22, Float.parseFloat(request.getParameter("techoParticipacion")));
                        break;
                }

                registros = sp.executeQuery();
                if (registros.next()) {
                    if (registros.getInt(1) == Consts.SQL_OK) {
                        // Define a cual pagina se refirecciona... al configurar
                        // el puesto debe redirigir a crear un empleado.
                        if (ParametrosBD.getConfiguracionSistema(idCentro).equals(Consts.PUESTO_CONFIGURADO)) {
                            sesion.removeAttribute("codigoPuesto");
                            sesion.setAttribute("codigoEmpleado", "0");
                            response.sendRedirect(Consts.EMPLEADO_HTML);
                        } else {
                            sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                    "Cambios sometidos satisfactoriamente."));
                            sesion.removeAttribute("codigoPuesto");

                            // Redirige de acuerdo al lobby de puestos de trabajo.
                            response.sendRedirect(Consts.LOBBY_PUESTOS_HTML);
                        }
                    } else {
                        ServiciosU.fb(registros);
                        sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                "Error al actualizar los datos."));

                        // Redirige a la página de origen.
                        response.sendRedirect(Consts.PUESTO_HTML);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                        "Error al actualizar los datos."));

                // Redirige a la página de origen.
                response.sendRedirect(Consts.PUESTO_HTML);
            } finally {
                if (instanciaBD != null) {
                    instanciaBD.desconectar();
                }

                instanciaBD = null;
                System.gc();
            }
        }
    } // Fin del metodo doPost().
}
