/*  Nombre.....:  Empleado.java
 *  Descripción:  Llama al procedimiento almacenado encargado de crear o
 *                modificar el empleado.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Octubre, 2019.
 */
package Controlador;

import Modelo.$;
import Modelo.ServiciosM;
import Utilitarios.Consts;
import Utilitarios.ServiciosU;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author Moises Snow
 */
public class Empleado extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ServletFileUpload uploader = null;
    private ServiciosU utilitario = null;

    @Override
    public void init() throws ServletException {
        String directorioRaiz = System.getProperty("catalina.home");
        DiskFileItemFactory fileFactory = new DiskFileItemFactory();
        File filesDir = new File(directorioRaiz + File.separator + Consts.CARPETA_APP);
        fileFactory.setRepository(filesDir);
        this.uploader = new ServletFileUpload(fileFactory);
    }

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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Empleado</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Empleado at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

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
        processRequest(request, response);
    }

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
        ServiciosM modelo = new ServiciosM();
        HttpSession sesion = request.getSession(false); // crea o recupera sesion

        try {
            // Variables para enviar y recibir información.
            String modoEmpleado = sesion.getAttribute("modoEmpleado").toString();
            String btnSubmit = utilitario.getParameter("btnSubmit");
            String nombreArchivoInicial = utilitario.getParameter("nombreArchivoInicial");
            String nombreArchivoSeleccionado = utilitario.getParameter("nombreArchivoSeleccionado");
            utilitario = new ServiciosU(uploader.parseRequest(request));

            int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
            int rolAsignado = Integer.parseInt(sesion.getAttribute("idRol").toString());
            String suelo = "";
            String techo = "";
            int[] idDepartamento = {0};

            String idGenerico = utilitario.getParameter("codigo");
            String nombreArchivoBorrar = sesion.getAttribute("codigoEmpleado").toString();
            List<String> nombreArchivo = new ArrayList<>();
            int[] gravedad = {Consts.GRAVEDAD_ERROR};
            
            if(!nombreArchivoInicial.equals(nombreArchivoSeleccionado))
                utilitario.guardarArchivo(idCentro, idGenerico, nombreArchivoBorrar,
                                          nombreArchivo, Consts.REMITE_EMPLEADO, gravedad);
            else{
                gravedad[0] = Consts.GRAVEDAD_CERO;
                nombreArchivo.add(nombreArchivoSeleccionado);
            }

            if (gravedad[0] == Consts.GRAVEDAD_ERROR) {
                RequestDispatcher view = request.getRequestDispatcher(Consts.EMPLEADO_HTML);
                view.forward(request, response);
            } else if (!ServletFileUpload.isMultipartContent(request)) {
                sesion.setAttribute("mensaje",
                        ServiciosU.sustituirCaracteres("Formulario no permite subir archivos."));
            } else if (btnSubmit != null && modoEmpleado.equals(Consts.EDICION)) {
                instanciaBD = new $();
                CallableStatement spEmpleado = instanciaBD.storedProc(
                        Consts.SP_EMPLEADO,
                        Consts.CANT_PARMS_SP_EMPLEADO);

                // Prepara la infomacion requerida por el procedimento almacenado.
                spEmpleado.setLong(1, Long.parseLong(sesion.getAttribute("idSesion").toString()));
                spEmpleado.setInt(2, idCentro);
                spEmpleado.setString(3, ServiciosU.obtenerParametro(
                        sesion.getAttribute("codigoEmpleado").toString(), 15));
                spEmpleado.setString(4, ServiciosU.obtenerParametro(
                        utilitario.getParameter("codigo"), 15));
                spEmpleado.setInt(5, Integer.parseInt(utilitario.getParameter("puesto")));
                spEmpleado.setInt(6, Integer.parseInt(ServiciosU.obtenerParametro(
                        utilitario.getParameter("estadoCivil"), 1)));
                spEmpleado.setInt(7, Integer.parseInt(ServiciosU.obtenerParametro(
                        utilitario.getParameter("genero"), 1)));
                spEmpleado.setString(8, ServiciosU.obtenerParametro(
                        utilitario.getParameter("tipoSalario"), 1));
                spEmpleado.setString(9, ServiciosU.obtenerParametro(
                        utilitario.getParameter("nombre"), 50));
                spEmpleado.setString(10, ServiciosU.obtenerParametro(
                        utilitario.getParameter("apellido"), 50));
                spEmpleado.setString(11, ServiciosU.obtenerParametro(
                        utilitario.getParameter("identidad"), 15));
                spEmpleado.setString(12, ServiciosU.obtenerParametro(
                        utilitario.getParameter("direccion"), 250));
                spEmpleado.setInt(13, Integer.parseInt(ServiciosU.obtenerParametro(
                        utilitario.getParameter("telefono"), 8)));
                spEmpleado.setString(14, ServiciosU.obtenerParametro(
                        utilitario.getParameter("correoElectronico"), 50));
                spEmpleado.setInt(15, ServiciosU.formatearFecha(
                        utilitario.getParameter("fechaNacimiento")));
                spEmpleado.setInt(16, ServiciosU.formatearFecha(
                        utilitario.getParameter("fechaIngreso")));
                spEmpleado.setString(17, nombreArchivo.get(0)); // nombre de archivo proporcionado
                spEmpleado.setString(18,
                        utilitario.getParameter("cbxActivo") != null ? "1" : "0");
                spEmpleado.setFloat(19, Float.parseFloat(ServiciosU.obtenerParametro(
                        utilitario.getParameter("salarioBase"), 9)));
                spEmpleado.setString(24, ServiciosU.obtenerParametro(
                        utilitario.getParameter("rbtTiempo"), 1));
                spEmpleado.setInt(25, Integer.parseInt(
                        sesion.getAttribute("idUsuario").toString()));
                spEmpleado.setLong(26, ServiciosU.fechaATimestamp());

                switch (utilitario.getParameter("tipoSalario")) {
                    case Consts.TIPO_SALARIO_TIEMPO:
                        spEmpleado.setFloat(20, (float) 0.0);
                        spEmpleado.setFloat(21, (float) 0.0);
                        spEmpleado.setFloat(22, (float) 0.0);
                        spEmpleado.setFloat(23, (float) 0.0);
                        suelo = "0.0";
                        techo = "0.0";
                        break;
                    case Consts.TIPO_SALARIO_OBRA:
                        spEmpleado.setFloat(20, Float.parseFloat(
                                utilitario.getParameter("salarioUniObr")));
                        spEmpleado.setFloat(21, (float) 0.0);
                        spEmpleado.setFloat(22, Float.parseFloat(
                                utilitario.getParameter("sueloUniObr")));
                        suelo = utilitario.getParameter("sueloUniObr");
                        spEmpleado.setFloat(23, Float.parseFloat(
                                utilitario.getParameter("techoUniObr")));
                        techo = utilitario.getParameter("techoUniObr");
                        break;
                    case Consts.TIPO_SALARIO_PARTICIPACION:
                        spEmpleado.setFloat(20, (float) 0.0);
                        spEmpleado.setFloat(21, Float.parseFloat(
                                utilitario.getParameter("participacion")));
                        spEmpleado.setFloat(22, Float.parseFloat(
                                utilitario.getParameter("sueloParticipacion")));
                        suelo = utilitario.getParameter("sueloParticipacion");
                        spEmpleado.setFloat(23, Float.parseFloat(
                                utilitario.getParameter("techoParticipacion")));
                        techo = utilitario.getParameter("techoParticipacion");
                        break;
                }

                registros = spEmpleado.executeQuery();
                if (registros.next()) {
                    if (registros.getInt(1) == Consts.SQL_OK) {
                        // Define a cual pagina se refirecciona... al configurar el
                        // empleado se da por terminado el proceso de configuración.
                        sesion.removeAttribute("codigoEmpleado");
                        sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                "Cambios sometidos satisfactoriamente."));

                        // Redirige de acuerdo al rol que tenga el usuario.
                        if (rolAsignado == Consts.ROL_ADMINISTRADOR)
                            response.sendRedirect(Consts.LOBBY_USUARIOS_HTML);
                        else
                            response.sendRedirect(Consts.LOBBY_EMPLEADOS_HTML);
                    } else {
                        ServiciosU.fb(registros);
                        
                        // Redirecciona a la página web de origen y muestra mensaje de
                        // retroalimentación al usuario.
                        sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                "Error al actualizar los datos."));
                        sesion.setAttribute("errorEmpleado", true);
                        sesion.setAttribute("errorCodigo",
                                utilitario.getParameter("codigo"));
                        sesion.setAttribute("errorTipoSalario",
                                utilitario.getParameter("tipoSalario"));
                        sesion.setAttribute("errorNombre",
                                utilitario.getParameter("nombre"));
                        sesion.setAttribute("errorApellido",
                                utilitario.getParameter("apellido"));
                        sesion.setAttribute("errorIdentidad",
                                utilitario.getParameter("identidad"));
                        sesion.setAttribute("errorDireccion",
                                utilitario.getParameter("direccion"));
                        sesion.setAttribute("errorTelefono",
                                utilitario.getParameter("telefono"));
                        sesion.setAttribute("errorCorreoElectronico",
                                utilitario.getParameter("correoElectronico"));
                        sesion.setAttribute("errorFechaNacimiento",
                                utilitario.getParameter("fechaNacimiento"));
                        sesion.setAttribute("errorFechaIngreso",
                                utilitario.getParameter("fechaIngreso"));
                        sesion.setAttribute("errorEstado",
                                utilitario.getParameter("cbxActivo") != null);
                        sesion.setAttribute("errorFrecuencia",
                                utilitario.getParameter("rbtTiempo"));
                        sesion.setAttribute("errorSalarioBase",
                                utilitario.getParameter("salarioBase"));
                        sesion.setAttribute("errorSalarioUnitarioObra",
                                utilitario.getParameter("salarioUniObr"));
                        sesion.setAttribute("errorParticipacion",
                                utilitario.getParameter("participacion"));
                        sesion.setAttribute("errorSuelo", suelo);
                        sesion.setAttribute("errorTecho", techo);
                        sesion.setAttribute("errorSelectPuesto",
                                modelo.elementoSELECTPuesto(
                                        idCentro, idDepartamento,
                                        Integer.parseInt(utilitario.getParameter("puesto"))));
                        sesion.setAttribute("errorSelectDepartamento",
                                modelo.elementoSELECTDepartamento(
                                        idDepartamento[0]));
                        sesion.setAttribute("errorSelectEstadoCivil",
                                modelo.elementoSELECTEstadoCivil(
                                        Integer.parseInt(utilitario.getParameter("estadoCivil"))));
                        sesion.setAttribute("errorSelectGenero",
                                modelo.elementoSELECTGenero(
                                        Integer.parseInt(utilitario.getParameter("genero"))));
                        response.sendRedirect(Consts.EMPLEADO_HTML);
                    }
                }
            } else {
                // Redirecciona a la página web de origen y muestra mensaje de
                // retroalimentación al usuario.
                sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                        "En este momento no puede realizar cambios debido a inconsistencias en su sesión."));
                response.sendRedirect(Consts.EMPLEADO_HTML);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            // Redirecciona a la página web de origen y muestra mensaje de
            // retroalimentación al usuario.
            sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                    "Ocurrió un error al intentar guardar el registro."));
            response.sendRedirect(Consts.EMPLEADO_HTML);
        } finally {
            if (instanciaBD != null)
                instanciaBD.desconectar();

            instanciaBD = null;
            System.gc();
        }
    }
}
