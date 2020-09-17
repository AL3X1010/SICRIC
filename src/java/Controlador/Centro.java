/*  Nombre.....:  Centro.java
 *  Descripción:  Llama al procedimiento almacenado encargado de crear o
 *                modificar el centro.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Julio, 2019.
 */
package Controlador;

import Modelo.$;
import Modelo.ParametrosBD;
import Utilitarios.Consts;
import Utilitarios.ServiciosU;
import java.io.File;
import java.io.IOException;
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

public class Centro extends HttpServlet {

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

  private static final long serialVersionUID = 1L;
  private ServletFileUpload uploader = null;
  private ServiciosU utilitario = null;
  
  @Override
  public void init() throws ServletException{
    String directorioRaiz = System.getProperty("catalina.home");
    DiskFileItemFactory fileFactory = new DiskFileItemFactory();
    File filesDir = new File(directorioRaiz + File.separator + Consts.CARPETA_APP);
    fileFactory.setRepository(filesDir);
    this.uploader = new ServletFileUpload(fileFactory);
  }
  
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
    
    try {
      // Variables para enviar y recibir información.
      utilitario = new ServiciosU(uploader.parseRequest(request));
      HttpSession sesion = request.getSession(false); // crea o recupera sesion
      String btnSubmit = utilitario.getParameter("btnSubmit");
      String nombreArchivoInicial = utilitario.getParameter("nombreArchivoInicial");
      String nombreArchivoSeleccionado = utilitario.getParameter("nombreArchivoSeleccionado");
      
      int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
      int rolAsignado = Integer.parseInt(sesion.getAttribute("idRol").toString());
      String idGenerico = sesion.getAttribute("idCentro").toString();
      List<String> nombreArchivo = new ArrayList<>();
      int[] gravedad = {Consts.GRAVEDAD_ERROR};
      
      if(!nombreArchivoInicial.equals(nombreArchivoSeleccionado))
        utilitario.guardarArchivo(idCentro, idGenerico, "", nombreArchivo,
                                  Consts.REMITE_CENTRO, gravedad);
      else{
          gravedad[0] = Consts.GRAVEDAD_CERO;
          nombreArchivo.add(nombreArchivoSeleccionado);
      }
      
      if(gravedad[0] == Consts.GRAVEDAD_ERROR){
        RequestDispatcher view = request.getRequestDispatcher("centro.html");
        view.forward(request, response);
      } else if(!ServletFileUpload.isMultipartContent(request)){
        sesion.setAttribute("mensaje",
            ServiciosU.sustituirCaracteres("Formulario no permite subir archivos."));
      } else if (btnSubmit != null) {
        instanciaBD = new $();
        CallableStatement spCentro =
            instanciaBD.storedProc(Consts.SPU_CENTRO, Consts.CANT_PARMS_SPU_CENTRO);

        // Prepara la infomacion requerida por el procedimento almacenado.
        spCentro.setLong(1, Long.parseLong(sesion.getAttribute("idSesion").toString()));
        spCentro.setInt(2, idCentro);
        spCentro.setString(3, ServiciosU.obtenerParametro(
                              utilitario.getParameter("nombre"), 100));
        spCentro.setString(4, ServiciosU.obtenerParametro(
                              utilitario.getParameter("direccion"), 250));
        spCentro.setString(5, nombreArchivo.get(0)); // nombre de archivo proporcionado
        spCentro.setInt(6, Integer.parseInt( ServiciosU.obtenerParametro(
                           utilitario.getParameter("telefono"), 8)));
        spCentro.setString(7, ServiciosU.obtenerParametro(
                              utilitario.getParameter("correo"), 50));
        spCentro.setInt(8, ServiciosU.obtenerParametro(
                           utilitario.getParameter("horaInicioLunes").replace(":", ""),
                           0, 2359));
        spCentro.setInt(9, ServiciosU.obtenerParametro(
                           utilitario.getParameter("horaFinalLunes").replace(":", ""),
                           0, 2359));
        spCentro.setInt(10, ServiciosU.obtenerParametro(
                           utilitario.getParameter("horaInicioMartes").replace(":", ""),
                           0, 2359));
        spCentro.setInt(11, ServiciosU.obtenerParametro(
                           utilitario.getParameter("horaFinalMartes").replace(":", ""),
                           0, 2359));
        spCentro.setInt(12, ServiciosU.obtenerParametro(
                           utilitario.getParameter("horaInicioMiercoles").replace(":", ""),
                           0, 2359));
        spCentro.setInt(13, ServiciosU.obtenerParametro(
                           utilitario.getParameter("horaFinalMiercoles").replace(":", ""),
                           0, 2359));
        spCentro.setInt(14, ServiciosU.obtenerParametro(
                           utilitario.getParameter("horaInicioJueves").replace(":", ""),
                           0, 2359));
        spCentro.setInt(15, ServiciosU.obtenerParametro(
                           utilitario.getParameter("horaFinalJueves").replace(":", ""),
                           0, 2359));
        spCentro.setInt(16, ServiciosU.obtenerParametro(
                           utilitario.getParameter("horaInicioViernes").replace(":", ""),
                           0, 2359));
        spCentro.setInt(17, ServiciosU.obtenerParametro(
                           utilitario.getParameter("horaFinalViernes").replace(":", ""),
                           0, 2359));
        spCentro.setInt(18, ServiciosU.obtenerParametro(
                           utilitario.getParameter("horaInicioSabado").replace(":", ""),
                           0, 2359));
        spCentro.setInt(19, ServiciosU.obtenerParametro(
                           utilitario.getParameter("horaFinalSabado").replace(":", ""),
                           0, 2359));
        spCentro.setInt(20, ServiciosU.obtenerParametro(
                           utilitario.getParameter("horaInicioDomingo").replace(":", ""),
                           0, 2359));
        spCentro.setInt(21, ServiciosU.obtenerParametro(
                           utilitario.getParameter("horaFinalDomingo").replace(":", ""),
                           0, 2359));
        spCentro.setInt(22, Integer.parseInt(sesion.getAttribute("idUsuario").toString()));
        spCentro.setLong(23, ServiciosU.fechaATimestamp());

        registros = spCentro.executeQuery();
        if (registros.next()) {
          if (registros.getInt(1) == Consts.SQL_OK) {
            // Define a cuál pagina se refirecciona... al configurar el centro
            // debe redirigir a configurar el departamento.
            if (ParametrosBD.getConfiguracionSistema(idCentro).equals(
                  Consts.CENTRO_CONFIGURADO)) {
              sesion.setAttribute("codigoDepartamento", "0");
              response.sendRedirect(Consts.DEPARTAMENTO_HTML);
            } else {
              sesion.setAttribute("mensaje",
                    ServiciosU.sustituirCaracteres("Cambios sometidos satisfactoriamente."));

              // Redirige de acuerdo a la página web correspondiente.
              response.sendRedirect(Consts.LOBBY_USUARIOS_HTML);
            }
          } else {
            ServiciosU.fb(registros);
            sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                                           "Error al actualizar los datos."));

            // Redirige de nuevo al formulario.
            response.sendRedirect(Consts.CENTRO_HTML);
          }
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      if (instanciaBD != null)
        instanciaBD.desconectar();

      utilitario = null;
      instanciaBD = null;
      System.gc();
    }
  } // Fin del metodo doPost().
}