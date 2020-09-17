/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.CatalogoEquipo;
import Modelo.CatalogoMedicamento;
import Modelo.CatalogoSuministro;
import Modelo.CentroRemision;
import Utilitarios.ServiciosU;
import Modelo.Departamento;
import Modelo.Puesto;
import Modelo.Empleado;
import Modelo.EquipoMedico;
import Modelo.Medicamento;
import Modelo.Modalidad;
import Modelo.Paciente;
import Modelo.Proveedor;
import Modelo.ServiciosM;
import Modelo.SuministroMedico;
import Modelo.Usuario;
import Utilitarios.Consts;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CargarLista extends HttpServlet {
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
    } // Fin del método doGet().

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    } // </editor-fold>

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
        final String ESTRUCTURA_ERROR =
                "  \"elementosHTML\": \"" +
                "            <h5 class='center'>$#titulo#$" +
                "              <a href='$#paginaWeb#$' class='$#etiquetaNuevo#$'>" +
                "                <i class='material-icons right'>add_circle_outline</i>" +
                "              </a>" +
                "            </h5><hr/>" +
                "            <div class='col s12 center'>" +
                "              <p> *** No se puede mostrar la información solicitada. ***</p>" +
                "            </div>\"";

        /* Todos los métodos de AJAX están identificados por el atributo NAME,
           esta página se encarga de brindar servicios a través de AJAX, si se
           ingresa directamente a este Servlet dicho atributo es NULL. */
        if (nombreServ != null) {
            /* Servicio..............: Get Remision Center List
               Página que lo utiliza.: lobbycentrosremision.html
               Función...............: Obtiene los registros de la tabla Centro_Remision. */
            if (nombreServ.equals("GETREMCNTL")) {
                final String PAGINA_WEB = Consts.CENTRO_REM_HTML;
                final String TITULO = Consts.TITULO_LISTA_CENTROS_REM;
                final String ETIQUETA_NUEVO = Consts.ETIQUETA_NUEVO_CENTRO_REM;
                
                // Recupera la lista de centros de remisión.
                try {
                    if (sesion.getAttribute("idCentro") != null) {
                        int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                        // Recupera modos permitidos para el usuario, esto para mostrar u ocultar
                        // las columnas VER y EDITAR, según corresponda.
                        boolean modoVisualizacion = Boolean.valueOf(
                                sesion.getAttribute("modoVisualizacion" +
                                    Consts.ETIQUETA_LOBBY_CENTROS_REM).toString());
                        boolean modoEdicion = Boolean.valueOf(
                                sesion.getAttribute("modoEdicion" +
                                    Consts.ETIQUETA_LOBBY_CENTROS_REM).toString());

                        CentroRemision listaCentrosRemision = new CentroRemision();
                        respuestaServ =
                            listaCentrosRemision.cargarLista(idCentro, modoVisualizacion, modoEdicion);
                    } else {
                        respuestaServ =
                            "{ \"error\": true, " +
                            "  \"mensaje\": \"Su sesión ha expirado.\", " +
                                ESTRUCTURA_ERROR
                                    .replace("$#titulo#$", TITULO)
                                    .replace("$#paginaWeb#$", PAGINA_WEB)
                                    .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                            "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{ \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\", " +
                            ESTRUCTURA_ERROR
                                    .replace("$#titulo#$", TITULO)
                                    .replace("$#paginaWeb#$", PAGINA_WEB)
                                    .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                        "}";
                } finally {
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            } else
                
                
            /* Servicio..............: Get Department List
               Página que lo utiliza.: lobbydepartamentos.html
               Función...............: Obtiene los registros de la tabla Departamento. */
            if (nombreServ.equals("GETDPTL")) {
                final String PAGINA_WEB = Consts.DEPARTAMENTO_HTML;
                final String TITULO = Consts.TITULO_LISTA_DEPARTAMENTOS;
                final String ETIQUETA_NUEVO = Consts.ETIQUETA_NUEVO_DEPARTAMENTO;
                
                // Recupera la lista de departamentos de la organización.
                try {
                    if (sesion.getAttribute("idCentro") != null) {
                        int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                        // Recupera modos permitidos para el usuario, esto para mostrar u ocultar
                        // las columnas VER y EDITAR, según corresponda.
                        boolean modoVisualizacion = Boolean.valueOf(
                                sesion.getAttribute("modoVisualizacion" +
                                    Consts.ETIQUETA_LOBBY_DEPARTAMENTOS).toString());
                        boolean modoEdicion = Boolean.valueOf(
                                sesion.getAttribute("modoEdicion" +
                                    Consts.ETIQUETA_LOBBY_DEPARTAMENTOS).toString());

                        Departamento listaDepartamentos = new Departamento();
                        respuestaServ =
                            listaDepartamentos.cargarLista(idCentro, modoVisualizacion, modoEdicion);
                    } else {
                        respuestaServ =
                            "{ \"error\": true, " +
                            "  \"mensaje\": \"Su sesión ha expirado.\", " +
                                ESTRUCTURA_ERROR
                                    .replace("$#titulo#$", TITULO)
                                    .replace("$#paginaWeb#$", PAGINA_WEB)
                                    .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                            "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{ \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\", " +
                            ESTRUCTURA_ERROR
                                    .replace("$#titulo#$", TITULO)
                                    .replace("$#paginaWeb#$", PAGINA_WEB)
                                    .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                        "}";
                } finally {
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            } else
                
                
            /* Servicio..............: Get Employee List
               Página que lo utiliza.: lobbyempleados.html
               Función...............: Obtiene los registros de la tabla Empleado. */
            if (nombreServ.equals("GETEMPL")) {
                // Recupera la lista de empleados de la organización.
                try {
                    if (sesion.getAttribute("idCentro") != null) {
                        int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                        // Recupera modos permitidos para el usuario, esto para mostrar u ocultar
                        // las columnas VER y EDITAR, según corresponda.
                        boolean modoVisualizacion = Boolean.valueOf(
                                sesion.getAttribute("modoVisualizacion" +
                                                    Consts.ETIQUETA_LOBBY_EMPLEADOS).toString());
                        boolean modoEdicion = Boolean.valueOf(
                                sesion.getAttribute("modoEdicion" +
                                                    Consts.ETIQUETA_LOBBY_EMPLEADOS).toString());

                        Empleado listaEmpleados = new Empleado();
                        respuestaServ = 
                            listaEmpleados.cargarLista(idCentro, modoVisualizacion, modoEdicion);
                    } else {
                        respuestaServ =
                            "{ \"error\": true, " +
                            "  \"mensaje\": \"Su sesión ha expirado.\", " +
                            "  \"elementosHTML\": \"" +
                            "            <h5 class='center'>Empleados en la Empresa" +
                            "              <a href='empleado.html' class='empleadoNuevo'>" +
                            "                <i class='material-icons right'>add_circle_outline</i>" +
                            "              </a>" +
                            "            </h5><hr/>" +
                            "            <div class='col s12 center'>" +
                            "              <p> *** No se puede mostrar la información solicitada. ***</p>" +
                            "            </div>\"" +
                            "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{ \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\", " +
                        "  \"elementosHTML\": \"" +
                        "            <h5 class='center'>Empleados en la Empresa" +
                        "              <a href='empleado.html' class='empleadoNuevo'>" +
                        "                <i class='material-icons right'>add_circle_outline</i>" +
                        "              </a>" +
                        "            </h5><hr/>" +
                        "            <div class='col s12 center'>" +
                        "              <p> *** No se puede mostrar la información solicitada. ***</p>" +
                        "            </div>\"" +
                        "}";
                } finally {
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            } else
                
                
            /* Servicio..............: Get Job List
               Página que lo utiliza.: lobbypuestos.html
               Función...............: Obtiene los registros de la tabla Puesto. */
            if (nombreServ.equals("GETJOBL")) {
                // Recupera la lista de puestos de trabajo de la organización.
                final String PAGINA_WEB = Consts.PUESTO_HTML;
                final String TITULO = Consts.TITULO_LISTA_PUESTOS;
                final String ETIQUETA_NUEVO = Consts.ETIQUETA_NUEVO_PUESTO;
                
                // Recupera la lista de puestos de la organización.
                try {
                    if (sesion.getAttribute("idCentro") != null) {
                        int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                        // Recupera modos permitidos para el usuario, esto para mostrar u ocultar
                        // las columnas VER y EDITAR, según corresponda.
                        boolean modoVisualizacion = Boolean.valueOf(
                                sesion.getAttribute("modoVisualizacion" +
                                    Consts.ETIQUETA_LOBBY_PUESTOS).toString());
                        boolean modoEdicion = Boolean.valueOf(
                                sesion.getAttribute("modoEdicion" +
                                    Consts.ETIQUETA_LOBBY_PUESTOS).toString());

                        Puesto listaPuestos = new Puesto();
                        respuestaServ =
                            listaPuestos.cargarLista(idCentro, modoVisualizacion, modoEdicion);
                    } else {
                        respuestaServ =
                            "{ \"error\": true, " +
                            "  \"mensaje\": \"Su sesión ha expirado.\", " +
                                ESTRUCTURA_ERROR
                                    .replace("$#titulo#$", TITULO)
                                    .replace("$#paginaWeb#$", PAGINA_WEB)
                                    .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                            "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{ \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\", " +
                            ESTRUCTURA_ERROR
                                    .replace("$#titulo#$", TITULO)
                                    .replace("$#paginaWeb#$", PAGINA_WEB)
                                    .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                        "}";
                } finally {
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            } else
            
            
            /* Servicio..............: Get Medicament List
               Página que lo utiliza.: lobbycatalogomedicamentos.html
               Función...............: Obtiene los registros de la tabla Catalogo_Medicamento. */
            if (nombreServ.equals("GETMDCL")) {
                final String PAGINA_WEB = Consts.CATALOGO_MEDICAMENTO_HTML;
                final String TITULO = Consts.TITULO_LISTA_CATALOGO_MEDICAMENTOS;
                final String ETIQUETA_NUEVO = Consts.ETIQUETA_NUEVO_CATALOGO_MEDICAMENTO;
                
                // Recupera la lista del catálogo de medicamentos de la organización.
                try {
                    if (sesion.getAttribute("idCentro") != null) {
                        int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                        // Recupera modos permitidos para el usuario, esto para mostrar u ocultar
                        // las columnas VER y EDITAR, según corresponda.
                        boolean modoVisualizacion = Boolean.valueOf(
                                sesion.getAttribute("modoVisualizacion" +
                                    Consts.ETIQUETA_LOBBY_CATALOGO_MEDICAMENTOS).toString());
                        boolean modoEdicion = Boolean.valueOf(
                                sesion.getAttribute("modoEdicion" +
                                    Consts.ETIQUETA_LOBBY_CATALOGO_MEDICAMENTOS).toString());

                        CatalogoMedicamento listaCatalogoMedicamentos = new CatalogoMedicamento();
                        respuestaServ =
                            listaCatalogoMedicamentos.cargarLista(idCentro, modoVisualizacion, modoEdicion);
                    } else {
                        respuestaServ =
                            "{ \"error\": true, " +
                            "  \"mensaje\": \"Su sesión ha expirado.\", " +
                            "  \"elementosHTML\": \"" +
                                ESTRUCTURA_ERROR
                                        .replace("$#titulo#$", TITULO)
                                        .replace("$#paginaWeb#$", PAGINA_WEB)
                                        .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                            "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{ \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\", " +
                            ESTRUCTURA_ERROR
                                    .replace("$#titulo#$", TITULO)
                                    .replace("$#paginaWeb#$", PAGINA_WEB)
                                    .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                        "}";
                } finally {
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            } else
            
            
            /* Servicio..............: Get Medicine Inventory List
               Página que lo utiliza.: lobbymedicamentos.html
               Función...............: Obtiene los registros de la tabla Inventario_Medicicamento. */
            if (nombreServ.equals("GETMDCINVL")) {
                final String PAGINA_WEB = Consts.MEDICAMENTO_HTML;
                final String TITULO = Consts.TITULO_LISTA_MEDICAMENTOS;
                final String ETIQUETA_NUEVO = Consts.ETIQUETA_NUEVO_MEDICAMENTO;
                
                // Recupera la lista de medicamentos de la organización.
                try {
                    if (sesion.getAttribute("idCentro") != null) {
                        int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                        int idUsuario = Integer.parseInt(sesion.getAttribute("idUsuario").toString());
                        int idRol = Integer.parseInt(sesion.getAttribute("idRol").toString());
                        
                        // Recupera modos permitidos para el usuario, esto para mostrar u ocultar
                        // las columnas VER y EDITAR, según corresponda.
                        boolean modoVisualizacion = Boolean.valueOf(
                                sesion.getAttribute("modoVisualizacion" +
                                    Consts.ETIQUETA_LOBBY_MEDICAMENTOS).toString());
                        boolean modoEdicion = Boolean.valueOf(
                                sesion.getAttribute("modoEdicion" +
                                    Consts.ETIQUETA_LOBBY_MEDICAMENTOS).toString());

                        Medicamento listaMedicamentos = new Medicamento();
                        respuestaServ = listaMedicamentos.cargarLista(
                                idCentro, idUsuario, idRol, modoVisualizacion, modoEdicion);
                    } else {
                        respuestaServ =
                            "{ \"error\": true, " +
                            "  \"mensaje\": \"Su sesión ha expirado.\", " +
                            "  \"elementosHTML\": \"" +
                                ESTRUCTURA_ERROR
                                        .replace("$#titulo#$", TITULO)
                                        .replace("$#paginaWeb#$", PAGINA_WEB)
                                        .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                            "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{ \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\", " +
                            ESTRUCTURA_ERROR
                                    .replace("$#titulo#$", TITULO)
                                    .replace("$#paginaWeb#$", PAGINA_WEB)
                                    .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                        "}";
                } finally {
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            } else
                        
                        
            /* Servicio..............: Get Medicine Serial List
               Página que lo utiliza.: expedirmedicamentos.html
               Función...............: Obtiene las series de los medicamentos. */
            if (nombreServ.equals("GETMDCSERL")) {
                // Recupera la lista de series de los medicamentos.
                try {
                    int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                    ServiciosM modelo = new ServiciosM();
                    respuestaServ =
                            "{ \"error\": false, " +
                            "  \"mensaje\": \"\", " +
                            "  \"data\" : " + modelo.elementoCOMPLETESeriesMedicamentos(idCentro) + " }";
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{ \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\", " +
                        "  \"data\": null }";
                } finally {
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            } else
            
            
            /* Servicio..............: Get Medical Equipment List
               Página que lo utiliza.: lobbycatologoequipos.html
               Función...............: Obtiene los registros de la tabla Catalogo_Equipo_Medico. */
            if (nombreServ.equals("GETMEDEQUL")) {
                final String PAGINA_WEB = Consts.CATALOGO_EQUIPO_HTML;
                final String TITULO = Consts.TITULO_LISTA_CATALOGO_EQUIPOS;
                final String ETIQUETA_NUEVO = Consts.ETIQUETA_NUEVO_CATALOGO_EQUIPO;
                
                // Recupera la lista del catálogo de equipos médicos de la organización.
                try {
                    if (sesion.getAttribute("idCentro") != null) {
                        int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                        // Recupera modos permitidos para el usuario, esto para mostrar u ocultar
                        // las columnas VER y EDITAR, según corresponda.
                        boolean modoVisualizacion = Boolean.valueOf(
                                sesion.getAttribute("modoVisualizacion" +
                                    Consts.ETIQUETA_LOBBY_CATALOGO_EQUIPOS).toString());
                        boolean modoEdicion = Boolean.valueOf(
                                sesion.getAttribute("modoEdicion" +
                                    Consts.ETIQUETA_LOBBY_CATALOGO_EQUIPOS).toString());

                        CatalogoEquipo listaCatalogoEquipos = new CatalogoEquipo();
                        respuestaServ =
                            listaCatalogoEquipos.cargarLista(idCentro, modoVisualizacion, modoEdicion);
                    } else {
                        respuestaServ =
                            "{ \"error\": true, " +
                            "  \"mensaje\": \"Su sesión ha expirado.\", " +
                            "  \"elementosHTML\": \"" +
                                ESTRUCTURA_ERROR
                                        .replace("$#titulo#$", TITULO)
                                        .replace("$#paginaWeb#$", PAGINA_WEB)
                                        .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                            "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{ \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\", " +
                            ESTRUCTURA_ERROR
                                    .replace("$#titulo#$", TITULO)
                                    .replace("$#paginaWeb#$", PAGINA_WEB)
                                    .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                        "}";
                } finally {
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            } else
            
            
            /* Servicio..............: Get Medical Equipment Inventory List
               Página que lo utiliza.: lobbyequipos.html
               Función...............: Obtiene los registros de la tabla Inventario_Equipo_Medico. */
            if (nombreServ.equals("GETMEDEQUINVL")) {
                final String PAGINA_WEB = Consts.EQUIPO_HTML;
                final String TITULO = Consts.TITULO_LISTA_EQUIPOS;
                final String ETIQUETA_NUEVO = Consts.ETIQUETA_NUEVO_EQUIPO;
                
                // Recupera la lista de equipos médicos de la organización.
                try {
                    if (sesion.getAttribute("idCentro") != null) {
                        int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                        // Recupera modos permitidos para el usuario, esto para mostrar u ocultar
                        // las columnas VER y EDITAR, según corresponda.
                        boolean modoVisualizacion = Boolean.valueOf(
                                sesion.getAttribute("modoVisualizacion" +
                                    Consts.ETIQUETA_LOBBY_EQUIPOS).toString());
                        boolean modoEdicion = Boolean.valueOf(
                                sesion.getAttribute("modoEdicion" +
                                    Consts.ETIQUETA_LOBBY_EQUIPOS).toString());

                        EquipoMedico listaEquiposMedicos = new EquipoMedico();
                        respuestaServ =
                            listaEquiposMedicos.cargarLista(idCentro, modoVisualizacion, modoEdicion);
                    } else {
                        respuestaServ =
                            "{ \"error\": true, " +
                            "  \"mensaje\": \"Su sesión ha expirado.\", " +
                            "  \"elementosHTML\": \"" +
                                ESTRUCTURA_ERROR
                                        .replace("$#titulo#$", TITULO)
                                        .replace("$#paginaWeb#$", PAGINA_WEB)
                                        .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                            "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{ \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\", " +
                            ESTRUCTURA_ERROR
                                    .replace("$#titulo#$", TITULO)
                                    .replace("$#paginaWeb#$", PAGINA_WEB)
                                    .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                        "}";
                } finally {
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            } else
            
            
            /* Servicio..............: Get Medical Supply List
               Página que lo utiliza.: lobbycatalogosuministros.html
               Función...............: Obtiene los registros de la tabla Catalogo_Suministro_Medico. */
            if (nombreServ.equals("GETMEDSPPL")) {
                final String PAGINA_WEB = Consts.CATALOGO_SUMINISTRO_HTML;
                final String TITULO = Consts.TITULO_LISTA_CATALOGO_SUMINISTROS;
                final String ETIQUETA_NUEVO = Consts.ETIQUETA_NUEVO_CATALOGO_SUMINISTRO;
                
                // Recupera la lista del catálogo de suministros médicos de la organización.
                try {
                    if (sesion.getAttribute("idCentro") != null) {
                        int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                        // Recupera modos permitidos para el usuario, esto para mostrar u ocultar
                        // las columnas VER y EDITAR, según corresponda.
                        boolean modoVisualizacion = Boolean.valueOf(
                                sesion.getAttribute("modoVisualizacion" +
                                    Consts.ETIQUETA_LOBBY_CATALOGO_SUMINISTROS).toString());
                        boolean modoEdicion = Boolean.valueOf(
                                sesion.getAttribute("modoEdicion" +
                                    Consts.ETIQUETA_LOBBY_CATALOGO_SUMINISTROS).toString());

                        CatalogoSuministro listaCatalogoSuministros = new CatalogoSuministro();
                        respuestaServ =
                            listaCatalogoSuministros.cargarLista(idCentro, modoVisualizacion, modoEdicion);
                    } else {
                        respuestaServ =
                            "{ \"error\": true, " +
                            "  \"mensaje\": \"Su sesión ha expirado.\", " +
                            "  \"elementosHTML\": \"" +
                                ESTRUCTURA_ERROR
                                        .replace("$#titulo#$", TITULO)
                                        .replace("$#paginaWeb#$", PAGINA_WEB)
                                        .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                            "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{ \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\", " +
                            ESTRUCTURA_ERROR
                                    .replace("$#titulo#$", TITULO)
                                    .replace("$#paginaWeb#$", PAGINA_WEB)
                                    .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                        "}";
                } finally {
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            } else
            
            
            /* Servicio..............: Get Medical Supply Inventory List
               Página que lo utiliza.: lobbysuministros.html
               Función...............: Obtiene los registros de la tabla Inventario_Suministro_Medico. */
            if (nombreServ.equals("GETMEDSPPINVL")) {
                final String PAGINA_WEB = Consts.SUMINISTRO_HTML;
                final String TITULO = Consts.TITULO_LISTA_SUMINISTROS;
                final String ETIQUETA_NUEVO = Consts.ETIQUETA_NUEVO_SUMINISTRO;
                
                // Recupera la lista de suministros médicos de la organización.
                try {
                    if (sesion.getAttribute("idCentro") != null) {
                        int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                        // Recupera modos permitidos para el usuario, esto para mostrar u ocultar
                        // las columnas VER y EDITAR, según corresponda.
                        boolean modoVisualizacion = Boolean.valueOf(
                                sesion.getAttribute("modoVisualizacion" +
                                    Consts.ETIQUETA_LOBBY_SUMINISTROS).toString());
                        boolean modoEdicion = Boolean.valueOf(
                                sesion.getAttribute("modoEdicion" +
                                    Consts.ETIQUETA_LOBBY_SUMINISTROS).toString());

                        SuministroMedico listaSuministrosMedicos = new SuministroMedico();
                        respuestaServ =
                            listaSuministrosMedicos.cargarLista(idCentro, modoVisualizacion, modoEdicion);
                    } else {
                        respuestaServ =
                            "{ \"error\": true, " +
                            "  \"mensaje\": \"Su sesión ha expirado.\", " +
                            "  \"elementosHTML\": \"" +
                                ESTRUCTURA_ERROR
                                        .replace("$#titulo#$", TITULO)
                                        .replace("$#paginaWeb#$", PAGINA_WEB)
                                        .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                            "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{ \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\", " +
                            ESTRUCTURA_ERROR
                                    .replace("$#titulo#$", TITULO)
                                    .replace("$#paginaWeb#$", PAGINA_WEB)
                                    .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                        "}";
                } finally {
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            } else
            
            
            /* Servicio..............: Get Modality List
               Página que lo utiliza.: lobbymodalidades.html
               Función...............: Obtiene los registros de la tabla Modalidad. */
            if (nombreServ.equals("GETMODL")) {
                final String PAGINA_WEB = Consts.MODALIDAD_HTML;
                final String TITULO = Consts.TITULO_LISTA_MODALIDADES;
                final String ETIQUETA_NUEVO = Consts.ETIQUETA_NUEVO_MODALIDAD;
                
                // Recupera la lista de modalidaes de la organización.
                try {
                    if (sesion.getAttribute("idCentro") != null) {
                        int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                        // Recupera modos permitidos para el usuario, esto para mostrar u ocultar
                        // las columnas VER y EDITAR, según corresponda.
                        boolean modoVisualizacion = Boolean.valueOf(
                                sesion.getAttribute("modoVisualizacion" +
                                    Consts.ETIQUETA_LOBBY_MODALIDADES).toString());
                        boolean modoEdicion = Boolean.valueOf(
                                sesion.getAttribute("modoEdicion" +
                                    Consts.ETIQUETA_LOBBY_MODALIDADES).toString());

                        Modalidad listaModalidades = new Modalidad();
                        respuestaServ =
                            listaModalidades.cargarLista(idCentro, modoVisualizacion, modoEdicion);
                    } else {
                        respuestaServ =
                            "{ \"error\": true, " +
                            "  \"mensaje\": \"Su sesión ha expirado.\", " +
                                ESTRUCTURA_ERROR
                                    .replace("$#titulo#$", TITULO)
                                    .replace("$#paginaWeb#$", PAGINA_WEB)
                                    .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                            "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{ \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\", " +
                            ESTRUCTURA_ERROR
                                    .replace("$#titulo#$", TITULO)
                                    .replace("$#paginaWeb#$", PAGINA_WEB)
                                    .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                        "}";
                } finally {
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            } else
                
            
            /* Servicio..............: Get Patient List
               Página que lo utiliza.: lobbypacientes.html
               Función...............: Obtiene los registros de la tabla Paciente. */
            if (nombreServ.equals("GETPTNL")) {
                final String PAGINA_WEB = Consts.PACIENTE_HTML;
                final String TITULO = Consts.TITULO_LISTA_PACIENTES;
                final String ETIQUETA_NUEVO = Consts.ETIQUETA_NUEVO_PACIENTE;
                
                // Recupera la lista de pacientes de la organización.
                try {
                    if (sesion.getAttribute("idCentro") != null) {
                        int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                        // Recupera modos permitidos para el usuario, esto para mostrar u ocultar
                        // las columnas VER y EDITAR, según corresponda.
                        boolean modoVisualizacion = Boolean.valueOf(
                                sesion.getAttribute("modoVisualizacion" +
                                    Consts.ETIQUETA_LOBBY_PACIENTES).toString());
                        boolean modoEdicion = Boolean.valueOf(
                                sesion.getAttribute("modoEdicion" +
                                    Consts.ETIQUETA_LOBBY_PACIENTES).toString());

                        Paciente listaPacientes = new Paciente();
                        respuestaServ =
                            listaPacientes.cargarLista(idCentro, modoVisualizacion, modoEdicion);
                    } else {
                        respuestaServ =
                            "{ \"error\": true, " +
                            "  \"mensaje\": \"Su sesión ha expirado.\", " +
                                ESTRUCTURA_ERROR
                                    .replace("$#titulo#$", TITULO)
                                    .replace("$#paginaWeb#$", PAGINA_WEB)
                                    .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                            "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{ \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\", " +
                            ESTRUCTURA_ERROR
                                    .replace("$#titulo#$", TITULO)
                                    .replace("$#paginaWeb#$", PAGINA_WEB)
                                    .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                        "}";
                } finally {
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            } else
                        
                        
            /* Servicio..............: Get Provider List
               Página que lo utiliza.: lobbyproveedores.html
               Función...............: Obtiene los registros de la tabla Proveedor. */
            if (nombreServ.equals("GETPVDL")) {
                final String PAGINA_WEB = Consts.PROVEEDOR_HTML;
                final String TITULO = Consts.TITULO_LISTA_PROVEEDORES;
                final String ETIQUETA_NUEVO = Consts.ETIQUETA_NUEVO_PROVEEDOR;
                
                // Recupera la lista de proveedor de la organización.
                try {
                    if (sesion.getAttribute("idCentro") != null) {
                        int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                        // Recupera modos permitidos para el usuario, esto para mostrar u ocultar
                        // las columnas VER y EDITAR, según corresponda.
                        boolean modoVisualizacion = Boolean.valueOf(
                                sesion.getAttribute("modoVisualizacion" +
                                    Consts.ETIQUETA_LOBBY_PROVEEDORES).toString());
                        boolean modoEdicion = Boolean.valueOf(
                                sesion.getAttribute("modoEdicion" +
                                    Consts.ETIQUETA_LOBBY_PROVEEDORES).toString());

                        Proveedor listaProveedor = new Proveedor();
                        respuestaServ =
                            listaProveedor.cargarLista(idCentro, modoVisualizacion, modoEdicion);
                    } else {
                        respuestaServ =
                            "{ \"error\": true, " +
                            "  \"mensaje\": \"Su sesión ha expirado.\", " +
                                ESTRUCTURA_ERROR
                                    .replace("$#titulo#$", TITULO)
                                    .replace("$#paginaWeb#$", PAGINA_WEB)
                                    .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                            "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{ \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\", " +
                            ESTRUCTURA_ERROR
                                    .replace("$#titulo#$", TITULO)
                                    .replace("$#paginaWeb#$", PAGINA_WEB)
                                    .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                        "}";
                } finally {
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            } else
                
                
            /* Servicio..............: Get Patient Information List
               Página que lo utiliza.: planrehabilitacion.html
               Función...............: Obtiene la lista con el código y nombre de los pacientes. */
            if (nombreServ.equals("GETPTNINFL")) {
                // Recupera la lista de pacientes del centro de rehabilitación.
                try {
                    if (sesion.getAttribute("idCentro") != null) {
                        int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                        ServiciosM modelo = new ServiciosM();
                        respuestaServ =
                            "{ \"error\": false, " +
                            "  \"mensaje\": \"\", " +
                            "  \"data\" : " + modelo.elementoCOMPLETEPacientes(idCentro) + " }";
                    } else {
                        respuestaServ =
                            "{ \"error\": true, " +
                            "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\", " +
                            "  \"data\": null }";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                            "{ \"error\": true, " +
                            "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\", " +
                            "  \"data\": null }";
                } finally {
                    out.print(ServiciosU.sustituirCaracteres(respuestaServ));
                }
            } else
                        
                        
            /* Servicio..............: Get Rehabilitation Plans List
               Página que lo utiliza.: citamedica.html
               Función...............: Obtiene el código y nombre de pacientes. */
            if (nombreServ.equals("GETREHPLANL")) {
                // Recupera la lista de códigos de pacientes de la organización.
                try {
                    int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                    ServiciosM modelo = new ServiciosM();
                    respuestaServ =
                            "{ \"error\": false, " +
                            "  \"mensaje\": \"\", " +
                            "  \"data\" : " + modelo.elementoCOMPLETEPlanes(idCentro) + " }";
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{ \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\", " +
                        "  \"data\": null }";
                } finally {
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            } else
                
                
            /* Servicio..............: Get Session List
               Página que lo utiliza.: lobbysesiones.html
               Función...............: Obtiene los registros de las sesiones. */
            if (nombreServ.equals("GETSSNL")) {
                final String PAGINA_WEB = "#";
                final String TITULO = "Sesiones de Usuario";
                
                // Recupera la lista de sesiones de la organización.
                try {
                    if (sesion.getAttribute("idCentro") != null) {
                        // int idUsuario = ServiciosU.formatearFecha(sesion.getAttribute("idUsuarioSel").toString());
                        int idUsuario = 1;
                        
                        // Primera instancia: cargar la lista de sesiones con las fechas proporcionadas.
                        if(request.getParameter("initDate").length() > 0
                                && request.getParameter("endDate").length() > 0){
                            sesion.setAttribute("fechaInicio", request.getParameter("initDate"));
                            sesion.setAttribute("fechaFinal", request.getParameter("endDate"));
                        }
                        
                        // Segunda instancia: recuperar las fechas almacenadas en las variables de sesión.
                        // Sino existen despliega ventana modal para que el usuario seleccione las fechas
                        // que considere oportunas.
                        else if( sesion.getAttribute("fechaInicio") == null
                                || sesion.getAttribute("fechaFinal") == null ){
                            respuestaServ =
                                "{ \"error\": false, " +
                                "  \"fechaRequerida\": true, " +
                                "  \"mensaje\": \"\" " +
                                "}";
                            return;
                        }
                        
                        int fechaInicio = ServiciosU.formatearFecha(sesion.getAttribute("fechaInicio").toString());
                        int fechaFinal = ServiciosU.formatearFecha(sesion.getAttribute("fechaFinal").toString());
                        
                        ServiciosM modelo = new ServiciosM();
                        respuestaServ = modelo.cargarListaSesiones(idUsuario, fechaInicio, fechaFinal);
                    } else {
                        respuestaServ =
                            "{ \"error\": true, " +
                            "  \"mensaje\": \"Su sesión ha expirado.\", " +
                                ESTRUCTURA_ERROR
                                    .replace("$#titulo#$", TITULO)
                                    .replace("$#paginaWeb#$", PAGINA_WEB)
                                    .replace("$#etiquetaNuevo#$", "") +
                            "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{ \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\", " +
                            ESTRUCTURA_ERROR
                                    .replace("$#titulo#$", TITULO)
                                    .replace("$#paginaWeb#$", PAGINA_WEB)
                                    .replace("$#etiquetaNuevo#$", "") +
                        "}";
                } finally {
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            } else
        
            
            
            /* Servicio..............: Get User List
               Página que lo utiliza.: lobbyusuarios.html
               Función...............: Obtiene los registros de la tabla Usuario. */
            if (nombreServ.equals("GETUSRL")) {
                // Recupera la lista de usuarios del sistema.
                final String PAGINA_WEB = Consts.USUARIO_HTML;
                final String TITULO = Consts.TITULO_LISTA_USUARIOS;
                final String ETIQUETA_NUEVO = Consts.ETIQUETA_NUEVO_USUARIO;
                
                // Recupera la lista de usuarios de la organización.
                try {
                    if (sesion.getAttribute("idCentro") != null) {
                        int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                        int idRol = Integer.parseInt(sesion.getAttribute("idRol").toString());
                        
                        Usuario listaUsuarios = new Usuario();
                        respuestaServ = listaUsuarios.cargarLista(idCentro, idRol);
                    } else {
                        respuestaServ =
                            "{ \"error\": true, " +
                            "  \"mensaje\": \"Su sesión ha expirado.\", " +
                                ESTRUCTURA_ERROR
                                    .replace("$#titulo#$", TITULO)
                                    .replace("$#paginaWeb#$", PAGINA_WEB)
                                    .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                            "}";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    respuestaServ =
                        "{ \"error\": true, " +
                        "  \"mensaje\": \"Ocurrió un error al intentar cargar el registro.\", " +
                            ESTRUCTURA_ERROR
                                    .replace("$#titulo#$", TITULO)
                                    .replace("$#paginaWeb#$", PAGINA_WEB)
                                    .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                        "}";
                } finally {
                    out.print( ServiciosU.sustituirCaracteres(respuestaServ) );
                }
            }
        }
                        
    } // Fin del método doPost().
}