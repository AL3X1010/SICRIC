/*  Nombre.....:  CargarRegistro.java
 *  Descripción:  Llama a la clase correspondiente para cargar un registro
 *                especifico de acuerdo al parametro proporcionado.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Julio, 2019.
 */
package Controlador;

import Modelo.CatalogoEquipo;
import Modelo.CatalogoMedicamento;
import Modelo.CatalogoSuministro;
import Modelo.Centro;
import Modelo.Departamento;
import Modelo.Empleado;
import Modelo.EquipoMedico;
import Modelo.HistorialMedico;
import Modelo.Medicamento;
import Modelo.Modalidad;
import Modelo.PlanMedico;
import Modelo.Paciente;
import Modelo.Proveedor;
import Modelo.Puesto;
import Modelo.ServiciosM;
import Modelo.SuministroMedico;
import Modelo.Usuario;
import Utilitarios.Consts;
import Utilitarios.ServiciosU;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CargarRegistro extends HttpServlet {
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
        processRequest(request, response);// Variables para enviar y recibir información.
        HttpSession sesion = request.getSession(false); // crea o recupera sesión
        PrintWriter out = response.getWriter();
        String nombreServ = request.getParameter("name"); // contiene el nombre de servicio al que se desea acceder
        String respuestaServ = ""; // contiene la respuesta que brinda el servicio

        /* Todos los métodos de AJAX están identificados por el atributo NAME,
           esta página se encarga de brindar servicios a través de AJAX, si se
           ingresa directamente a este Servlet dicho atributo es NULL. */
        if (nombreServ != null) {
            /* Servicio..............: Get Center Information
               Página que lo utiliza.: centro.html
               Función...............: Obtiene el registro con la información del
                                       centro de rehabilitación. */
            if (nombreServ.equals("GETCNTINF")){
                // Recupera el registro del centro seleccionado.
                try {
                    if (sesion.getAttribute("idCentro") != null) {
                        String codigo = sesion.getAttribute("idCentro").toString();
                        Centro centro = new Centro();
                        centro.cargarRegistro(Integer.parseInt(codigo));
                        respuestaServ = "[{\"modo\": \"" + sesion.getAttribute("modoCentro") + "\"},"
                                + centro.toJSON() + "]";
                    } else {
                        respuestaServ = "[{\"modo\": \"" + Consts.VISUALIZACION + "\"},"
                                + " { \"error\": true, "
                                + "   \"mensaje\": \"" + ServiciosU.sustituirCaracteres(
                                        "Su sesión ha expirado.") + "\" "
                                + " }]";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ = "[{\"modo\": \"" + Consts.VISUALIZACION + "\"},"
                            + " {\"error\": true, "
                            + "  \"mensaje\": \"" + ServiciosU.sustituirCaracteres(
                                    "Ocurrió un error al intentar cargar el registro.") + "\" "
                            + " }]";
                } finally {
                    out.print(respuestaServ);
                }
            } else
                
                
            /* Servicio..............: Get Department Information
               Página que lo utiliza.: departamento.html
               Función...............: Obtiene el registro con la información del
                                       departamento. */
            if (nombreServ.equals("GETDPTINF")) {
                try {
                    boolean errorDepartamento = false;
                    
                    // Recupera el registro del departamento seleccionado.
                    if (sesion.getAttribute("codigoDepartamento") != null) {
                        String codigo = sesion.getAttribute("codigoDepartamento").toString();

                        if (sesion.getAttribute("errorDepartamento") != null)
                            errorDepartamento = Boolean.parseBoolean(sesion.getAttribute("errorDepartamento").toString());

                        if(errorDepartamento){
                            respuestaServ =
                                "{  \"modo\": \"" + sesion.getAttribute("modoDepartamento") + "\", " +
                                "   \"nombre\": \"" + sesion.getAttribute("errorNombreDepartamento") + "\", " +
                                "   \"descripcion\": \"" + sesion.getAttribute("errorDescripcionDepartamento") +"\", " +
                                "   \"activo\": " + sesion.getAttribute("errorEstadoDepartamento") + " " +
                                "}";

                            // Elimina valores almacenados temporalmente.
                            sesion.removeAttribute("errorDepartamento");
                            sesion.removeAttribute("errorNombreDepartamento");
                            sesion.removeAttribute("errorDescripcionDepartamento");
                            sesion.removeAttribute("errorEstadoDepartamento");
                        } else {
                            Departamento departamento = new Departamento();
                            departamento.cargarRegistro(Integer.parseInt(codigo));
                            respuestaServ =
                                "{  \"modo\": \"" + sesion.getAttribute("modoDepartamento") + "\", " +
                                    departamento.toJSON() + "}";
                        }
                    } else {
                        respuestaServ =
                            "{" +
                            "  \"modo\": \"" + Consts.VISUALIZACION + "\", " +
                            "  \"error\": true, " +
                            "  \"mensaje\": \"Se han encontrado inconsistencias en su sesión.\" " +
                            "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{" +
                        "  \"modo\": \"" + Consts.VISUALIZACION + "\", " +
                        "  \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\" " +
                        "}";
                } finally {
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            } else
                    
                    
            /* Servicio..............: Get Employee Information
               Página que lo utiliza.: empleado.html
               Función...............: Obtiene el registro con la información del
                                       empleado. */
            if (nombreServ.equals("GETEMPINF")){
                // Recupera el registro del empleado seleccionado.
                try {
                    boolean errorEmpleado = false;
                    
                    // Recupera el registro del empleado seleccionado.
                    if (sesion.getAttribute("codigoEmpleado") != null) {
                        int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString().trim());
                        String codigo = sesion.getAttribute("codigoEmpleado").toString();
                        
                        if (sesion.getAttribute("errorEmpleado") != null)
                            errorEmpleado = Boolean.parseBoolean(sesion.getAttribute("errorEmpleado").toString());
                        
                        if(errorEmpleado){
                            respuestaServ =
                                "{  \"modo\": \"" + sesion.getAttribute("modoEmpleado") + "\", " +
                                "   \"codigo\": \"" + sesion.getAttribute("errorCodigo") + "\", " +
                                "   \"codigo3\": \"" + sesion.getAttribute("errorTipoSalario") + "\", " +
                                "   \"nombres\": \"" + sesion.getAttribute("errorNombre") + "\", " +
                                "   \"apellidos\": \"" + sesion.getAttribute("errorApellido") + "\", " +
                                "   \"identidad\": \"" + sesion.getAttribute("errorIdentidad") + "\", " +
                                "   \"direccion\": \"" + sesion.getAttribute("errorDireccion") + "\", " +
                                "   \"telefono\": \"" + sesion.getAttribute("errorTelefono") + "\", " +
                                "   \"correoElectronico\": \"" + sesion.getAttribute("errorCorreoElectronico") + "\", " +
                                "   \"fechaNacimiento\": \"" + sesion.getAttribute("errorFechaNacimiento") + "\", " +
                                "   \"fechaIngreso\": \"" + sesion.getAttribute("errorFechaIngreso") + "\", " +
                                "   \"hojaDeVida\": \"sddf\", " +
                                "   \"activo\": " + sesion.getAttribute("errorEstado") + ", " +
                                "   \"salarioBase\": " + sesion.getAttribute("errorSalarioBase") + ", " +
                                "   \"salarioObra\": " + sesion.getAttribute("errorSalarioUnitarioObra") + ", " +
                                "   \"participacion\": " + sesion.getAttribute("errorParticipacion") + ", " +
                                "   \"suelo\": " + sesion.getAttribute("errorSuelo") + ", " +
                                "   \"techo\": " + sesion.getAttribute("errorTecho") + ", " +
                                "   \"frecuencia\": \"" + sesion.getAttribute("errorFrecuencia") + "\", " +
                                "   \"selectDepartamento\": \"" + sesion.getAttribute("errorSelectDepartamento") + "\", " +
                                "   \"selectEstadoCivil\": \"" + sesion.getAttribute("errorSelectEstadoCivil") + "\", " +
                                "   \"selectGenero\": \"" + sesion.getAttribute("errorSelectGenero") + "\", " +
                                "   \"selectPuesto\": \"" + sesion.getAttribute("errorSelectPuesto") + "\"" +
                                "}";
                            
                            // Elimina valores almacenados temporalmente.
                            sesion.removeAttribute("errorEmpleado");
                            sesion.removeAttribute("errorCodigo");
                            sesion.removeAttribute("errorTipoSalario");
                            sesion.removeAttribute("errorNombre");
                            sesion.removeAttribute("errorApellido");
                            sesion.removeAttribute("errorIdentidad");
                            sesion.removeAttribute("errorDireccion");
                            sesion.removeAttribute("errorTelefono");
                            sesion.removeAttribute("errorCorreoElectronico");
                            sesion.removeAttribute("errorFechaNacimiento");
                            sesion.removeAttribute("errorFechaIngreso");
                            sesion.removeAttribute("errorEstado");
                            sesion.removeAttribute("errorSalarioBase");
                            sesion.removeAttribute("errorSalarioUnitarioObra");
                            sesion.removeAttribute("errorParticipacion");
                            sesion.removeAttribute("errorSuelo");
                            sesion.removeAttribute("errorTecho");
                            sesion.removeAttribute("errorFrecuencia");
                            sesion.removeAttribute("errorSelectDepartamento");
                            sesion.removeAttribute("errorSelectEstadoCivil");
                            sesion.removeAttribute("errorSelectGenero");
                            sesion.removeAttribute("errorSelectPuesto");
                        } else {
                            Empleado empleado = new Empleado();
                            empleado.cargarRegistro(idCentro, codigo);
                            respuestaServ =
                                "{  \"modo\": \"" + sesion.getAttribute("modoEmpleado") + "\", " +
                                    empleado.toJSON() + "}";
                        }
                    } else {
                        respuestaServ =
                            "{" +
                            "  \"modo\": \"" + Consts.VISUALIZACION + "\", " +
                            "  \"error\": true, " +
                            "  \"mensaje\": \"Se han encontrado inconsistencias en su sesión.\" " +
                            "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{" +
                        "  \"modo\": \"" + Consts.VISUALIZACION + "\", " +
                        "  \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\" " +
                        "}";
                } finally {
                    out.print(ServiciosU.sustituirCaracteres(respuestaServ));
                }
            } else
                
            
            /* Servicio..............: Get Job Information
         Página que lo utiliza.: puesto.html
         Función...............: Obtiene el registro con la información del
                                 puesto de trabajo. */
                        if (nombreServ.equals("GETJOBINF")) {
                            try {
                                // Recupera el registro del puesto de trabajo seleccionado.
                                if (sesion.getAttribute("codigoPuesto") != null) {
                                    String codigo = sesion.getAttribute("codigoPuesto").toString();
                                    Puesto puesto = new Puesto();
                                    puesto.cargarRegistro(Integer.parseInt(codigo));
                                    respuestaServ = "[{\"modo\": \"" + sesion.getAttribute("modoPuesto") + "\"},"
                                            + puesto.toJSON() + "]";
                                } else {
                                    respuestaServ = "[{\"modo\": \"" + Consts.VISUALIZACION + "\"},"
                                            + " { \"error\": true, "
                                            + "   \"mensaje\": \"" + ServiciosU.sustituirCaracteres(
                                                    "Su sesión ha expirado.") + "\" "
                                            + " }]";
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                respuestaServ = "[{\"modo\": \"" + Consts.VISUALIZACION + "\"},"
                                        + " { \"error\": true, "
                                        + "   \"mensaje\": \"" + ServiciosU.sustituirCaracteres(
                                                "Ocurrió un error al intentar cargar el registro.") + "\" "
                                        + " }]";
                            } finally {
                                out.print(respuestaServ);
                            }
                        } else
            
            
            /* Servicio..............: Get Medical Appointment
         Página que lo utiliza.: citamedica.html
         Función...............: Obtiene el registro con la información de la
                                 cita médica del paciente. */
                            if (nombreServ.equals("GETMEDAPN")){
                                // Recupera el registro de la cita médica seleccionada.
                                try {
                                    /*
          if( sesion.getAttribute("codigoPlanRehabilitacion") == null
                || sesion.getAttribute("codigoFecha") == null
                || sesion.getAttribute("codigoHoraInicio") == null
                || sesion.getAttribute("codigoHoraFinal") == null ){
            sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                        "No puede acceder a esta página de forma directa."));
            // Redirige de acuerdo al rol que tenga el usuario.
            // response.sendRedirect(Consts.CALENDARIO_HTML);
            respuestaServ = "{ \"error\": true, " +
                            "  \"mensaje\": \"" +
                                    ServiciosU.sustituirCaracteres( 
                                    "Ocurrió un error al intentar cargar el registro.") + "\" " +
                            "}";
          } else { 
            int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString().trim());
            Long codigoPlanRehabilitacion =
                  sesion.getAttribute("codigoPlanRehabilitacion").toString().trim().equals("") ?
                  0 : Long.parseLong(sesion.getAttribute("codigoPlanRehabilitacion").toString());
            int codigoFecha =
                  sesion.getAttribute("codigoFecha").toString().trim().equals("") ?
                  0 : Integer.parseInt(sesion.getAttribute("codigoFecha").toString());
            int codigoHoraInicio =
                  sesion.getAttribute("codigoHoraInicio").toString().trim().equals("") ?
                  0 : Integer.parseInt(sesion.getAttribute("codigoHoraInicio").toString());
            int codigoHoraFinal =
                  sesion.getAttribute("codigoHoraFinal").toString().trim().equals("") ?
                  0 : Integer.parseInt(sesion.getAttribute("codigoHoraFinal").toString() );
          
           if( codigoPlanRehabilitacion < 1 || codigoFecha < 20190901
                  || codigoHoraInicio < 1 || codigoHoraFinal < 1 ){
              sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                          "Valores no permitidos para la cita médica."));
              // Redirige de acuerdo al rol que tenga el usuario.
              response.sendRedirect(Consts.CALENDARIO_HTML);
            }

                                     */
                                    int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString().trim());
                                    Long codigoPlanRehabilitacion = (long) 1;
                                    int codigoFecha = 20190811;
                                    int codigoHoraInicio = 800;
                                    int codigoHoraFinal = 900;

                                    PlanMedico citaMedica = new PlanMedico();
                                    citaMedica.cargarRegistro(codigoPlanRehabilitacion, codigoFecha,
                                            codigoHoraInicio, codigoHoraFinal);
                                    respuestaServ = citaMedica.toJSON();
                                    // }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    respuestaServ = "{ \"error\": true, "
                                            + "  \"mensaje\": \""
                                            + ServiciosU.sustituirCaracteres(
                                                    "Ocurrió un error al intentar cargar el registro.") + "\" "
                                            + "}";
                                } finally {
                                    out.print(respuestaServ);
                                }
                            } else
                
                
            /* Servicio..............: Get Medical History Information
               Página que lo utiliza.: historialmedico.html
               Función...............: Obtiene el historial médico del paciente. */
            if (nombreServ.equals("GETMEDHSTINF")) {
                try {
                    // Recupera el historial médico del paciente seleccionado.
                    if (sesion.getAttribute("codigoPaciente") != null || 1 == 1) {
                        // String codigo = request.getParameter("code");
                        String[] respuesta = {"", ""};

                        HistorialMedico historialMedico = new HistorialMedico();
                        historialMedico.cargarHistorialMedico("0801199512345", respuesta);
                        respuestaServ =
                            "{  \"modo\": \"" + sesion.getAttribute("modoDepartamento") + "\", " +
                            "   \"elementoLI\": \"" + respuesta[0] + "\", " +
                            "   \"elementoDIV\": \"" + respuesta[1] + "\" }";
                        
                    } else {
                        respuestaServ =
                            "{" +
                            "  \"modo\": \"" + Consts.VISUALIZACION + "\", " +
                            "  \"error\": true, " +
                            "  \"mensaje\": \"Se han encontrado inconsistencias en su sesión.\" " +
                            "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{" +
                        "  \"modo\": \"" + Consts.VISUALIZACION + "\", " +
                        "  \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\" " +
                        "}";
                } finally {
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            }
                
                
            /* Servicio..............: Get Medicine Information
               Página que lo utiliza.: catalogomedicamento.html
               Función...............: Obtiene el registro del catálogo con la
                                       información del medicamento. */
            if (nombreServ.equals("GETMDCINF")) {
                try {
                    boolean errorMedicamento = false;
                    
                    // Recupera el registro del medicamento seleccionado.
                    if (sesion.getAttribute("codigoCatalogoMedicamento") != null) {
                        String codigo = sesion.getAttribute("codigoCatalogoMedicamento").toString();

                        if (sesion.getAttribute("errorCatalogoMedicamento") != null)
                            errorMedicamento = Boolean.parseBoolean(
                                    sesion.getAttribute("errorCatalogoMedicamento").toString());

                        if(errorMedicamento){
                            respuestaServ =
                                "{  \"modo\": \"" + sesion.getAttribute("modoCatalogoMedicamento") + "\", " +
                                "   \"codigo\": \"" + sesion.getAttribute("errorCodigoCatalogoMedicamento") + "\", " +
                                "   \"nombre\": \"" + sesion.getAttribute("errorNombreCatalogoMedicamento") + "\", " +
                                "   \"descripcion\": \"" + sesion.getAttribute("errorDescripcionCatalogoMedicamento") +"\", " +
                                "   \"cantidad\": \"" + sesion.getAttribute("errorCantidadCatalogoMedicamento") + "\", " +
                                "   \"cuantificable\": \"" + sesion.getAttribute("errorCuantificableCatalogoMedicamento") + "\" " +
                                "}";

                            // Elimina valores almacenados temporalmente.
                            sesion.removeAttribute("modoCatalogoMedicamento");
                            sesion.removeAttribute("errorCodigoCatalogoMedicamento");
                            sesion.removeAttribute("errorNombreCatalogoMedicamento");
                            sesion.removeAttribute("errorDescripcionCatalogoMedicamento");
                            sesion.removeAttribute("errorCantidadCatalogoMedicamento");
                            sesion.removeAttribute("errorCuantificableCatalogoMedicamento");
                        } else {
                            CatalogoMedicamento registroCatalogoMedicamento = new CatalogoMedicamento();
                            registroCatalogoMedicamento.cargarRegistro(codigo);
                            respuestaServ =
                                "{  \"modo\": \"" + sesion.getAttribute("modoCatalogoMedicamento") + "\", " +
                                    registroCatalogoMedicamento.toJSON() + "}";
                        }
                    } else {
                        respuestaServ =
                            "{" +
                            "  \"modo\": \"" + Consts.VISUALIZACION + "\", " +
                            "  \"error\": true, " +
                            "  \"mensaje\": \"Se han encontrado inconsistencias en su sesión.\" " +
                            "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{" +
                        "  \"modo\": \"" + Consts.VISUALIZACION + "\", " +
                        "  \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\" " +
                        "}";
                } finally {
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            } else
                
                
            /* Servicio..............: Get Medicine Return Information
               Página que lo utiliza.: devolvermedicamento.html
               Función...............: Obtiene el registro con la información 
                                       del medicamento a devolver. */
            if (nombreServ.equals("GETMDCRTNINF")) {
                try {
                    boolean errorMedicamento = false;
                    
                    // Recupera el registro del medicamento seleccionado.
                    if (sesion.getAttribute("codigoMedicamento") != null) {
                        String codigo = sesion.getAttribute("codigoMedicamento").toString();
                        String serie = codigo.split(Consts.SEPARADOR_REGEX)[0];
                        String lote = codigo.split(Consts.SEPARADOR_REGEX)[1];
                        int idUsuario = Integer.parseInt(sesion.getAttribute("idUsuario").toString());
                        

                        if (sesion.getAttribute("errorDevolucionMedicamento") != null)
                            errorMedicamento = Boolean.parseBoolean(
                                    sesion.getAttribute("errorDevolucionMedicamento").toString());
                        
                        Medicamento registroMedicamento = new Medicamento();
                        registroMedicamento.cargarRegistro(idUsuario, serie, lote);
                            
                        if(errorMedicamento){
                            respuestaServ =
                                "{  \"modo\": \"" + sesion.getAttribute("modoMedicamento") + "\", " +
                                "   \"error\": \"" + errorMedicamento + "\", " +
                                "   \"mensaje\": \"" + sesion.getAttribute("mensaje") + "\", " +
                                "   \"codigo\": \"" + sesion.getAttribute("errorLoteDevolucionMedicamento") + "\", " +
                                "   \"codigo2\": \"" + sesion.getAttribute("errorSerieDevolucionMedicamento") + "\", " +
                                "   \"costo\": \"" + registroMedicamento.getCosto() + "\", " +
                                "   \"cantidad\": \"" + registroMedicamento.getCantidad() + "\", " +
                                "   \"cantidadDevuelta\": \"" + sesion.getAttribute("errorCantidadDevolucionMedicamento") + "\", " +
                                "   \"motivo\": \"" + sesion.getAttribute("errorMotivoDevolucionMedicamento") + "\", " +
                                "   \"fechaVencimiento\": \"" + registroMedicamento.getFechaVencimiento() + "\" " +
                                "}";

                            // Elimina valores almacenados temporalmente.
                            sesion.removeAttribute("modoMedicamento");
                            sesion.removeAttribute("mensaje");
                            sesion.removeAttribute("errorDevolucionMedicamento");
                            sesion.removeAttribute("errorSerieDevolucionMedicamento");
                            sesion.removeAttribute("errorLoteDevolucionMedicamento");
                            sesion.removeAttribute("errorCantidadDevolucionMedicamento");
                            sesion.removeAttribute("errorMotivoDevolucionMedicamento");
                        } else {
                            respuestaServ =
                                "{  \"modo\": \"" + sesion.getAttribute("modoCatalogoMedicamento") + "\", " +
                                    registroMedicamento.toJSON() + "}";
                        }
                    } else {
                        respuestaServ =
                            "{" +
                            "  \"modo\": \"" + Consts.VISUALIZACION + "\", " +
                            "  \"error\": true, " +
                            "  \"mensaje\": \"Se han encontrado inconsistencias en su sesión.\" " +
                            "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{" +
                        "  \"modo\": \"" + Consts.VISUALIZACION + "\", " +
                        "  \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\" " +
                        "}";
                } finally {
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            } else
                
                
            /* Servicio..............: Get Medical Equipment Detail
               Página que lo utiliza.: equipo.html
               Función...............: Obtiene el registro con la información
                                       del equipo médico. */
            if (nombreServ.equals("GETMEDEQUDET")) {
                try {
                    boolean errorEquipoMedico = false;
                    
                    // Recupera el registro del equipo médico seleccionado.
                    if (sesion.getAttribute("codigoEquipoMedico") != null) {
                        String codigo = sesion.getAttribute("codigoEquipoMedico").toString();

                        if (sesion.getAttribute("errorEquipoMedico") != null)
                            errorEquipoMedico = Boolean.parseBoolean(
                                    sesion.getAttribute("errorEquipoMedico").toString());

                        if(errorEquipoMedico){
                            respuestaServ =
                                "{  \"modo\": \"" + sesion.getAttribute("modoEquipoMedico") + "\", " +
                                "   \"codigo\": \"" + sesion.getAttribute("errorCodigoEquipoMedico") + "\", " +
                                "   \"fechaIngreso\": \"" + sesion.getAttribute("errorFechaIngresoEquipoMedico") + "\", " +
                                "   \"fechaMantenimiento\": \"" + sesion.getAttribute("errorFechaMantenimientoEquipoMedico") + "\", " +
                                "   \"activo\": \"" + sesion.getAttribute("errorEstadoEquipoMedico") + "\" " +
                                "}";

                            // Elimina valores almacenados temporalmente.
                            sesion.removeAttribute("errorEquipoMedico");
                            sesion.removeAttribute("errorCodigoEquipoMedico");
                            sesion.removeAttribute("errorFechaIngresoEquipoMedico");
                            sesion.removeAttribute("errorFechaMantenimientoEquipoMedico");
                            sesion.removeAttribute("errorEstadoEquipoMedico");
                        } else {
                            EquipoMedico registroEquipoMedico = new EquipoMedico();
                            registroEquipoMedico.cargarRegistro(codigo);
                            sesion.setAttribute("codigoCatalogoAnterior", registroEquipoMedico.getIdCatalogo());
                            respuestaServ =
                                "{  \"modo\": \"" + sesion.getAttribute("modoEquipoMedico") + "\", " +
                                    registroEquipoMedico.toJSON() + "}";
                        }
                    } else {
                        respuestaServ =
                            "{" +
                            "  \"modo\": \"" + Consts.VISUALIZACION + "\", " +
                            "  \"error\": true, " +
                            "  \"mensaje\": \"Se han encontrado inconsistencias en su sesión.\" " +
                            "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{" +
                        "  \"modo\": \"" + Consts.VISUALIZACION + "\", " +
                        "  \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\" " +
                        "}";
                } finally {
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            } else
                
                
            /* Servicio..............: Get Medical Equipment Information
               Página que lo utiliza.: catalogoequipo.html
               Función...............: Obtiene el registro del catálogo con la
                                       información del equipo médico. */
            if (nombreServ.equals("GETMEDEQUINF")) {
                try {
                    boolean errorEquipoMedico = false;
                    
                    // Recupera el registro del equipo médico seleccionado.
                    if (sesion.getAttribute("codigoCatalogoEquipo") != null) {
                        String codigo = sesion.getAttribute("codigoCatalogoEquipo").toString();

                        if (sesion.getAttribute("errorCatalogoEquipo") != null)
                            errorEquipoMedico = Boolean.parseBoolean(
                                    sesion.getAttribute("errorCatalogoEquipo").toString());

                        if(errorEquipoMedico){
                            respuestaServ =
                                "{  \"modo\": \"" + sesion.getAttribute("modoCatalogoEquipo") + "\", " +
                                "   \"marca\": \"" + sesion.getAttribute("errorMarcaCatalogoEquipo") + "\", " +
                                "   \"modelo\": \"" + sesion.getAttribute("errorModeloCatalogoEquipo") + "\", " +
                                "   \"nombre\": \"" + sesion.getAttribute("errorNombreCatalogoEquipo") + "\", " +
                                "   \"costo\": " + sesion.getAttribute("errorCostoCatalogoEquipo") + ", " +
                                "   \"cantidad\": \"" + sesion.getAttribute("errorCantidadCatalogoEquipo") + "\", " +
                                "   \"descripcion\": \"" + sesion.getAttribute("errorDescripcionCatalogoEquipo") +"\" " +
                                "}";

                            // Elimina valores almacenados temporalmente.
                            sesion.removeAttribute("errorCatalogoEquipo");
                            sesion.removeAttribute("errorMarcaCatalogoEquipo");
                            sesion.removeAttribute("errorModeloCatalogoEquipo");
                            sesion.removeAttribute("errorNombreCatalogoEquipo");
                            sesion.removeAttribute("errorDescripcionCatalogoEquipo");
                            sesion.removeAttribute("errorCostoCatalogoEquipo");
                            sesion.removeAttribute("errorCantidadCatalogoEquipo");
                        } else {
                            CatalogoEquipo registroCatalogoEquipo = new CatalogoEquipo();
                            registroCatalogoEquipo.cargarRegistro(Integer.parseInt(codigo));
                            respuestaServ =
                                "{  \"modo\": \"" + sesion.getAttribute("modoCatalogoEquipo") + "\", " +
                                    registroCatalogoEquipo.toJSON() + "}";
                        }
                    } else {
                        respuestaServ =
                            "{" +
                            "  \"modo\": \"" + Consts.VISUALIZACION + "\", " +
                            "  \"error\": true, " +
                            "  \"mensaje\": \"Se han encontrado inconsistencias en su sesión.\" " +
                            "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{" +
                        "  \"modo\": \"" + Consts.VISUALIZACION + "\", " +
                        "  \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\" " +
                        "}";
                } finally {
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            } else
                
                
            /* Servicio..............: Get Medical Supply Assignation
               Página que lo utiliza.: asignarsuministro.html
               Función...............: Obtiene el registro del inventario con la
                                       información del suministro médico para
                                       mostrarla como referencia al usuario en la
                                       pantalla de asignación de suministros médicos. */
            if (nombreServ.equals("GETMEDSPPASS")) {
                try {
                    boolean errorSuministroMedico = false;
                    
                    // Recupera el registrAsginarSuministroMedicoo del suministro médico seleccionado.
                    if (sesion.getAttribute("codigoSuministroMedico") != null) {
                        String codigo = sesion.getAttribute("codigoSuministroMedico").toString();
                        String serie = codigo.split(Consts.SEPARADOR_REGEX)[0];
                        String lote = codigo.split(Consts.SEPARADOR_REGEX)[1];

                        if (sesion.getAttribute("errorSuministroMedico") != null)
                            errorSuministroMedico = Boolean.parseBoolean(
                                    sesion.getAttribute("errorSuministroMedico").toString());
                        
                        SuministroMedico registroSuministroMedico = new SuministroMedico();
                        registroSuministroMedico.cargarRegistro(serie, lote);
                        respuestaServ =
                            "{  \"modo\": \"" + sesion.getAttribute("modoSuministroMedico") + "\", " +
                                registroSuministroMedico.toJSON() + "}";
                        
                        if(errorSuministroMedico){
                            respuestaServ =
                                "{  \"modo\": \"" + sesion.getAttribute("modoSuministroMedico") + "\", " +
                                "   \"codigo2\": \"" + sesion.getAttribute("errorSerieSuministroMedico") + "\", " +
                                "   \"codigo\": \"" + sesion.getAttribute("errorLoteSuministroMedico") + "\", " +
                                "   \"asignado\": \"" + sesion.getAttribute("errorAsignadoSuministroMedico") + "\", " +
                                "   \"cantidad\": \"" + sesion.getAttribute("errorExistenciaSuministroMedico") + "\", " +
                                "   \"fechaVencimiento\": \"" + sesion.getAttribute("errorFechaVencimientoSuministroMedico") + "\", " +
                                "   \"notaAdicional\": \"" + sesion.getAttribute("errorNotaSuministroMedico") +"\", " +
                                "   \"selectEmpleado\": \"" + registroSuministroMedico.getSelectEmpleado() +"\" " +
                                "}";

                            // Elimina valores almacenados temporalmente.
                            sesion.removeAttribute("errorSuministroMedico");
                            sesion.removeAttribute("errorSerieSuministroMedico");
                            sesion.removeAttribute("errorLoteSuministroMedico");
                            sesion.removeAttribute("errorAsignadoSuministroMedico");
                            sesion.removeAttribute("errorExistenciaSuministroMedico");
                            sesion.removeAttribute("errorCosSuministroMedico");
                            sesion.removeAttribute("errorFechaVencimientoSuministroMedico");
                            sesion.removeAttribute("errorNotaSuministroMedico");
                        }
                    } else {
                        respuestaServ =
                            "{" +
                            "  \"modo\": \"" + Consts.VISUALIZACION + "\", " +
                            "  \"error\": true, " +
                            "  \"mensaje\": \"Se han encontrado inconsistencias en su sesión.\" " +
                            "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{" +
                        "  \"modo\": \"" + Consts.VISUALIZACION + "\", " +
                        "  \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\" " +
                        "}";
                } finally {
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            } else
            
            
                
                
            /* Servicio..............: Get Medical Supply Detail
               Página que lo utiliza.: suministro.html, agregarsuministro.html
               Función...............: Obtiene el registro del inventario con la
                                       información del suministro médico. */
            if (nombreServ.equals("GETMEDSPPDET")) {
                try {
                    boolean errorSuministroMedico = false;
                    
                    if (sesion.getAttribute("codigoSuministroMedico") != null) {
                        String serie = "";
                        String lote = "";
                        
                        // Recupera el código del suministro médico seleccionado.
                        String codigo = sesion.getAttribute("codigoSuministroMedico").toString();
                        if(codigo.contains(Consts.SEPARADOR)){
                            serie = codigo.split(Consts.SEPARADOR_REGEX)[0];
                            lote = codigo.split(Consts.SEPARADOR_REGEX)[1];
                        }
                        
                        // Verifica si hubo error de un intento previo.
                        if (sesion.getAttribute("errorSuministroMedico") != null)
                            errorSuministroMedico = Boolean.parseBoolean(
                                    sesion.getAttribute("errorSuministroMedico").toString());
                        
                        // De forma predefinida carga el registro de la BD.
                        SuministroMedico registroSuministroMedico = new SuministroMedico();
                        registroSuministroMedico.cargarRegistro(serie, lote);
                        respuestaServ =
                            "{  \"modo\": \"" + sesion.getAttribute("modoSuministroMedico") + "\", " +
                                registroSuministroMedico.toJSON() + "}";
                        
                        // Si existió intento previo que ocasionó error devuelve los valores
                        // ingresados por el usuario
                        if(errorSuministroMedico){
                            final String WEB_PAGE = sesion.getAttribute("paginaSuministroMedico").toString();
                            ServiciosM modelo = new ServiciosM();
                            respuestaServ =
                                "{  \"modo\": \"" + sesion.getAttribute("modoSuministroMedico") + "\", " +
                                "   \"codigo\": \"" + sesion.getAttribute("errorLoteSuministroMedico") + "\", " +
                                "   \"codigo2\": \"" + sesion.getAttribute("errorSerieSuministroMedico") + "\", " +
                                ( WEB_PAGE.equals(Consts.AGREGAR_SUMINISTRO_HTML) ?
                                    "   \"cantidadIngresada\": \"" + sesion.getAttribute("errorCantidadSuministroMedico") + "\", " +
                                    "   \"costoLote\": " + sesion.getAttribute("errorCostoSuministroMedico") + ", " :
                                    "   \"costo\": " + sesion.getAttribute("errorCostoSuministroMedico") + ", " +
                                    "   \"cantidad\": \"" + sesion.getAttribute("errorCantidadSuministroMedico") + "\", " +
                                    "   \"selectSerie\": \"" + modelo.elementoSELECTSerie(
                                                         sesion.getAttribute("errorSerieSuministroMedico").toString()) + "\", " ) +
                                "   \"fechaVencimiento\": \"" + sesion.getAttribute("errorFechaVencimientoSuministroMedico") +"\" " +
                                "}";

                            // Elimina valores almacenados temporalmente.
                            sesion.removeAttribute("errorSuministroMedico");
                            sesion.removeAttribute("errorLoteSuministroMedico");
                            sesion.removeAttribute("errorSerieSuministroMedico");
                            sesion.removeAttribute("errorCantidadSuministroMedico");
                            sesion.removeAttribute("errorCostoSuministroMedico");
                            sesion.removeAttribute("errorFechaVencimientoSuministroMedico");
                        }
                    } else {
                        respuestaServ =
                            "{" +
                            "  \"modo\": \"" + Consts.VISUALIZACION + "\", " +
                            "  \"error\": true, " +
                            "  \"mensaje\": \"Se han encontrado inconsistencias en su sesión.\" " +
                            "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{" +
                        "  \"modo\": \"" + Consts.VISUALIZACION + "\", " +
                        "  \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\" " +
                        "}";
                } finally {
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            } else
                
                
            /* Servicio..............: Get Medical Supply Information
               Página que lo utiliza.: catalogosuministro.html
               Función...............: Obtiene el registro del catálogo con la
                                       información del suministro médico. */
            if (nombreServ.equals("GETMEDSPPINF")) {
                try {
                    boolean errorSuministroMedico = false;
                    
                    // Recupera el registro del suministro médico seleccionado.
                    if (sesion.getAttribute("codigoCatalogoSuministro") != null) {
                        String codigo = sesion.getAttribute("codigoCatalogoSuministro").toString();

                        if (sesion.getAttribute("errorCatalogoSuministro") != null)
                            errorSuministroMedico = Boolean.parseBoolean(
                                    sesion.getAttribute("errorCatalogoSuministro").toString());

                        if(errorSuministroMedico){
                            respuestaServ =
                                "{  \"modo\": \"" + sesion.getAttribute("modoCatalogoSuministro") + "\", " +
                                "   \"codigo\": \"" + sesion.getAttribute("errorCodigoCatalogoSuministro") + "\", " +
                                "   \"nombre\": \"" + sesion.getAttribute("errorNombreCatalogoSuministro") + "\", " +
                                "   \"descripcion\": \"" + sesion.getAttribute("errorDescripcionCatalogoSuministro") +"\", " +
                                "   \"cantidad\": \"" + sesion.getAttribute("errorCantidadCatalogoSuministro") + "\", " +
                                "   \"cuantificable\": \"" + sesion.getAttribute("errorCuantificableCatalogoSuministro") + "\" " +
                                "}";

                            // Elimina valores almacenados temporalmente.
                            sesion.removeAttribute("errorCatalogoSuministro");
                            sesion.removeAttribute("errorCodigoCatalogoSuministro");
                            sesion.removeAttribute("errorNombreCatalogoSuministro");
                            sesion.removeAttribute("errorDescripcionCatalogoSuministro");
                            sesion.removeAttribute("errorCantidadCatalogoSuministro");
                            sesion.removeAttribute("errorCuantificableCatalogoSuministro");
                        } else {
                            CatalogoSuministro registroCatalogoSuministro = new CatalogoSuministro();
                            registroCatalogoSuministro.cargarRegistro(codigo);
                            respuestaServ =
                                "{  \"modo\": \"" + sesion.getAttribute("modoCatalogoSuministro") + "\", " +
                                    registroCatalogoSuministro.toJSON() + "}";
                        }
                    } else {
                        respuestaServ =
                            "{" +
                            "  \"modo\": \"" + Consts.VISUALIZACION + "\", " +
                            "  \"error\": true, " +
                            "  \"mensaje\": \"Se han encontrado inconsistencias en su sesión.\" " +
                            "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{" +
                        "  \"modo\": \"" + Consts.VISUALIZACION + "\", " +
                        "  \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\" " +
                        "}";
                } finally {
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            } else
                
                
            /* Servicio..............: Get Modality Information
               Página que lo utiliza.: modalidad.html
               Función...............: Obtiene el registro con la información de
                                       la modalidad. */
            if (nombreServ.equals("GETMODINF")) {
                try {
                    // Recupera el registro del departamento seleccionado.
                    if (sesion.getAttribute("codigoModalidad") != null) {
                        String codigo = sesion.getAttribute("codigoModalidad").toString();
                        
                        Modalidad modalidad = new Modalidad();
                        modalidad.cargarRegistro(Integer.parseInt(codigo));
                        respuestaServ =
                            "{  \"modo\": \"" + sesion.getAttribute("modoModalidad") + "\", " +
                                modalidad.toJSON() + "}";
                    } else {
                        respuestaServ =
                            "{" +
                            "  \"modo\": \"" + Consts.VISUALIZACION + "\", " +
                            "  \"error\": true, " +
                            "  \"mensaje\": \"Se han encontrado inconsistencias en su sesión.\" " +
                            "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{" +
                        "  \"modo\": \"" + Consts.VISUALIZACION + "\", " +
                        "  \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\" " +
                        "}";
                } finally {
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            } else
                
                
            /* Servicio..............: Get Patient Information
               Página que lo utiliza.: paciente.html
               Función...............: Obtiene el registro con la información del
                                       paciente. */
                if (nombreServ.equals("GETPTNINF")) // Recupera el registro del paciente seleccionado.
                {
                    try {
                        // Recupera el registro del paciente seleccionado.
                        if (sesion.getAttribute("codigoPaciente") != null) {
                            int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString().trim());
                            String codigo = sesion.getAttribute("codigoPaciente").toString();
                            Paciente paciente = new Paciente();
                            paciente.cargarRegistro(idCentro, codigo);
                            respuestaServ = "[{\"modo\": \"" + sesion.getAttribute("modoPaciente") + "\"},"
                                    + paciente.toJSON() + "]";
                        } else {
                            respuestaServ = "[{\"modo\": \"" + Consts.VISUALIZACION + "\"},"
                                    + " { \"error\": true, "
                                    + "   \"mensaje\": \"" + ServiciosU.sustituirCaracteres(
                                            "Su sesión ha expirado.") + "\" "
                                    + " }]";
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        respuestaServ = "[{\"modo\": \"" + Consts.VISUALIZACION + "\"},"
                                + " {\"error\": true, "
                                + "  \"mensaje\": \"" + ServiciosU.sustituirCaracteres(
                                        "Ocurrió un error al intentar cargar el registro.") + "\" "
                                + " }]";
                    } finally {
                        out.print(respuestaServ);
                    }
                } else
                                    
            
            /* Servicio..............: Get Provider Information
               Página que lo utiliza.: proveedor.html
               Función...............: Obtiene el registro del proveedor. */
            if (nombreServ.equals("GETPVDINF")) {
                try {
                    boolean errorProveedor = false;
                    
                    // Recupera el registro del proveedor seleccionado.
                    if (sesion.getAttribute("codigoProveedor") != null) {
                        String codigo = sesion.getAttribute("codigoProveedor").toString();

                        if (sesion.getAttribute("errorProveedor") != null)
                            errorProveedor = Boolean.parseBoolean(
                                    sesion.getAttribute("errorProveedor").toString());
                        
                        Proveedor registroProveedor = new Proveedor();
                        registroProveedor.cargarRegistro(Integer.parseInt(codigo));
                        
                        if(errorProveedor){
                            respuestaServ =
                                "{  \"modo\": \"" + sesion.getAttribute("modoProveedor") + "\", " +
                                "   \"error\": " + errorProveedor + ", " +
                                "   \"mensaje\": \"" + sesion.getAttribute("mensaje") + "\", " +
                                "   \"codigo\": \"" + sesion.getAttribute("codigoProveedor") + "\", " +
                                "   \"nombreProveedor\": \"" + sesion.getAttribute("errorNombreProveedor") + "\", " +
                                "   \"telefonoProveedor\": 1" + sesion.getAttribute("errorTelefonoProveedor") +", " +
                                "   \"correoElectronico\": \"" + sesion.getAttribute("errorCorreoElectronicoProveedor") +"\", " +
                                "   \"direccion\": \"" + sesion.getAttribute("errorDireccionProveedor") +"\", " +
                                "   \"nombreContacto\": \"" + sesion.getAttribute("errorNombreContactoProveedor") + "\", " +
                                "   \"telefonoContacto\": " + sesion.getAttribute("errorTelefonoContactoProveedor") +", " +
                                "   \"activo\": " + sesion.getAttribute("errorEstadoProveedor") + ", " +
                                "   \"selectTipo\": \"" + registroProveedor.getSelectTipo("") + "\" " +
                                "}";

                            // Elimina valores almacenados temporalmente.
                            sesion.removeAttribute("errorProveedor");
                            sesion.removeAttribute("mensaje");
                            sesion.removeAttribute("errorNombreProveedor");
                            sesion.removeAttribute("errorTelefonoProveedor");
                            sesion.removeAttribute("errorCorreoElectronicoProveedor");
                            sesion.removeAttribute("errorDireccionProveedor");
                            sesion.removeAttribute("errorNombreContactoProveedor");
                            sesion.removeAttribute("errorTelefonoContactoProveedor");
                            sesion.removeAttribute("errorEstadoProveedor");
                        } else {
                            respuestaServ =
                            "{  \"modo\": \"" + sesion.getAttribute("modoProveedor") + "\", " +
                                registroProveedor.toJSON() + "}";
                        }
                    } else {
                        respuestaServ =
                            "{" +
                            "  \"modo\": \"" + Consts.VISUALIZACION + "\", " +
                            "  \"error\": true, " +
                            "  \"mensaje\": \"Se han encontrado inconsistencias en su sesión.\" " +
                            "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{" +
                        "  \"modo\": \"" + Consts.VISUALIZACION + "\", " +
                        "  \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\" " +
                        "}";
                } finally {
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            } else
                                    
                
            
            /* Servicio..............: Get User Information
               Página que lo utiliza.: usuario.html
               Función...............: Obtiene el registro con la información del
                                       usuario. */
            if (nombreServ.equals("GETUSRINF")) {
                try {
                    // Recupera el registro del usuario seleccionado.
                    if (sesion.getAttribute("idCentro") != null) {
                        String codigo = sesion.getAttribute("codigoUsuario").toString();
                        Usuario Usuario = new Usuario();
                        
                        Usuario.cargarRegistro(Integer.parseInt(codigo));
                        respuestaServ =
                            "{  \"modo\": \"" + sesion.getAttribute("modoUsuario") + "\", " +
                                Usuario.toJSON() + "}";
                    } else {
                        respuestaServ =
                                "{" +
                                "  \"modo\": \"" + Consts.VISUALIZACION + "\", " +
                                "  \"error\": true, " +
                                "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\" " +
                                "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{" +
                        "  \"modo\": \"" + Consts.VISUALIZACION + "\", " +
                        "  \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\" " +
                        "}";
                } finally {
                    System.out.println(respuestaServ);
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            }
        }
    }
}
