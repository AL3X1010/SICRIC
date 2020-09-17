/*  Nombre.....:  Login.java
 *  Descripción:  Gestiona las credenciales necesarias para el acceso a esta
 *                aplicación, así como establecer las variables de sesión
 *                correspondientes.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Julio, 2019.
 */
package Controlador;

import Modelo.ParametrosBD;
import Modelo.ServiciosM;
import Utilitarios.Consts;
import Utilitarios.ServiciosU;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Login extends HttpServlet {

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
        response.sendRedirect(Consts.INDEX_HTML);    // redirige a página principal.
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
        // Variables para enviar y recibir información.
        HttpSession sesion = request.getSession(false); // crea o recupera sesion
        String btnSubmit = request.getParameter("btnSubmit");
        int[] idCentro = {-1};
        int[] idRol = {-1};
        int[] idUsuario = {-1};
        long[] idSesion = {-1};

        if (btnSubmit != null) {
            try {
                ServiciosM modelo = new ServiciosM();
                String usuario = request.getParameter("usuario");
                String pass = request.getParameter("contrasenia");

                if (modelo.ingresar(usuario, pass, idCentro, idRol, idUsuario, idSesion)) {
                    sesion.setAttribute("idCentro", idCentro[0]);
                    sesion.setAttribute("idRol", idRol[0]);
                    sesion.setAttribute("idUsuario", idUsuario[0]);
                    sesion.setAttribute("destinationPage", Consts.INDEX_HTML);

                    if (idRol[0] == Consts.ROL_FISIOTERAPEUTA) {
                        modelo.crearSesion(idUsuario, idSesion);
                        sesion.setAttribute("idSesion", idSesion[0]);
                    } else if (idRol[0] == Consts.ROL_SA) {
                        if (idSesion[0] < 1) {
                            modelo.crearSesion(idUsuario, idSesion);
                        }

                        sesion.setAttribute("idSesion", idSesion[0]);
                        sesion.setAttribute("codigoCentro", idCentro[0]);
                    } else if (idRol[0] == Consts.ROL_ADMINISTRADOR) {
                        switch (ParametrosBD.getConfiguracionSistema(idCentro[0])) {
                            case Consts.SIN_CONFIGURACION:
                                modelo.crearSesion(idUsuario, idSesion);
                                sesion.setAttribute("idSesion", idSesion[0]);
                                sesion.setAttribute("codigoCentro", idCentro[0]);
                                response.sendRedirect(Consts.CENTRO_HTML);

                                break;
                            case Consts.CENTRO_CONFIGURADO:
                                modelo.crearSesion(idUsuario, idSesion);
                                sesion.setAttribute("idSesion", idSesion[0]);
                                sesion.setAttribute("codigoDepartamento", "0");
                                response.sendRedirect(Consts.DEPARTAMENTO_HTML);

                                break;
                            case Consts.DEPARTAMENTO_CONFIGURADO:
                                modelo.crearSesion(idUsuario, idSesion);
                                sesion.setAttribute("idSesion", idSesion[0]);
                                sesion.setAttribute("codigoPuesto", "0");
                                response.sendRedirect(Consts.PUESTO_HTML);

                                break;
                            case Consts.PUESTO_CONFIGURADO:
                                modelo.crearSesion(idUsuario, idSesion);
                                sesion.setAttribute("idSesion", idSesion[0]);
                                sesion.setAttribute("codigoEmpleado", "");
                                response.sendRedirect(Consts.EMPLEADO_HTML);

                                break;
                            case Consts.APP_CONFIGURADA:
                                modelo.crearSesion(idUsuario, idSesion);
                                sesion.setAttribute("idSesion", idSesion[0]);

                                break;
                            default:
                                // Estado de configuración diferente a los definidos en tabla
                                // de parámetros de la aplicación.
                                sesion.removeAttribute("idCentro");
                                sesion.removeAttribute("idRol");
                                sesion.removeAttribute("idSesion");
                                sesion.removeAttribute("idUsuario");
                                sesion.removeAttribute("codigoCentro");
                                sesion.removeAttribute("codigoDepartamento");
                                sesion.removeAttribute("codigoPuesto");
                                sesion.removeAttribute("codigoEmpleado");
                                sesion.setAttribute("mensaje",
                                        ServiciosU.sustituirCaracteres("¡Usuario o contraseña inválidos!"));
                        }
                    } else if (idRol[0] == Consts.ROL_FARMACIA_APROBADOR
                            || idRol[0] == Consts.ROL_FARMACIA_OPERADOR) {
                        modelo.crearSesion(idUsuario, idSesion);
                        sesion.setAttribute("idSesion", idSesion[0]);
                    } else if (idRol[0] == Consts.ROL_COLABORADOR) {
                        modelo.crearSesion(idUsuario, idSesion);
                        sesion.setAttribute("idSesion", idSesion[0]);
                    }
                    
                    
                    response.sendRedirect(Consts.LOBBY_HTML);
                    
                    /*
                    else {
                        // Usuario no tiene rol parametrizado en tabla.
                        sesion.removeAttribute("idCentro");
                        sesion.removeAttribute("idRol");
                        sesion.removeAttribute("idSesion");
                        sesion.removeAttribute("idUsuario");
                        sesion.removeAttribute("codigoCentro");
                        sesion.removeAttribute("codigoDepartamento");
                        sesion.removeAttribute("codigoPuesto");
                        sesion.removeAttribute("codigoEmpleado");
                        sesion.setAttribute("mensaje",
                                ServiciosU.sustituirCaracteres("¡Usuario o contraseña inválidos!"));

                        response.sendRedirect(Consts.INDEX_HTML);
                    }*/
                } else {
                    sesion.removeAttribute("idCentro");
                    sesion.removeAttribute("idRol");
                    sesion.removeAttribute("idSesion");
                    sesion.removeAttribute("idUsuario");
                    sesion.removeAttribute("codigoCentro");
                    sesion.removeAttribute("codigoDepartamento");
                    sesion.removeAttribute("codigoPuesto");
                    sesion.removeAttribute("codigoEmpleado");
                    sesion.setAttribute("mensaje",
                            ServiciosU.sustituirCaracteres("¡Usuario o contraseña inválidos!"));

                    response.sendRedirect(Consts.INDEX_HTML);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            response.sendRedirect(Consts.INDEX_HTML);
        }
    } // Fin del método doPost().
}
