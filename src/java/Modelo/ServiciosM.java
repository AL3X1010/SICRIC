/*  Nombre.....:  ServiciosM.java
 *  Descripción:  Permite ejecutar diferentes servicios relacionados a la base de datos.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Julio, 2019.
 */
package Modelo;

import Utilitarios.Consts;
import Utilitarios.ServiciosU;
import static Utilitarios.ServiciosU.*;
import java.io.File;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ServiciosM {
    // Métodos utilitarios de la clase ServiciosM.

    /*
      Nombre......: cargarBarraNavegacion.
      Parámetros..: entero - idUsuario
      Llamado por.: ServiciosC.java.
      Propósito...: Cargar la barra de navegación con las páginas web a las que
                    el usuario tiene acceso según su rol.
     */
    public String cargarBarraNavegacion(int idUsuario, int idRol) {
        $ instanciaNavegacion = null;
        String respuesta = "";
        String myQuery;
        try {
            instanciaNavegacion = new $();
            String nombreModuloAnterior = "";
            String dropModulo;
            String dropMovil = "";
            String dropPaginas = "";
            
            if(idRol == Consts.ROL_SA)
                // Proporciona acceso total al ser usuario super administrador.
                myQuery =
                        "SELECT M.ID_Modulo, M.Modulo, P.Nombre, P.Pagina " +
                        "FROM Modulo M INNER JOIN Pagina_Web P " +
                        "         ON M.ID_Modulo = P.ID_Modulo " +
                        "WHERE Nombre <> '' " +
                        "ORDER BY ID_Modulo, ID_Pagina;";
            else{
                String unionRol =
                idRol == Consts.ROL_FARMACIA_APROBADOR || idRol == Consts.ROL_FARMACIA_OPERADOR ?
                    "(U.ID_Rol = P.ID_Rol OR  " + Consts.ROL_FARMACIA_MIXTO + " = P.ID_Rol)" :
                    "U.ID_Rol = P.ID_Rol";
                
                // Recupera únicamente las páginas web, y los módulos, a los que tiene
                // acceso el usuario que está ejecutando la aplicación.
                myQuery =
                        "SELECT A.ID_Modulo, M.Modulo, P.Nombre, P.Pagina " +
                        "FROM Acceso_Usuario A " +
                        "   INNER JOIN Usuario U " +
                        "   ON A.ID_Usuario = U.ID_Usuario " +
                        "   INNER JOIN Pagina_Web P " +
                        "   ON (A.ID_Pagina = P.ID_Pagina AND " + unionRol + ") " +
                        "   INNER JOIN Modulo M " +
                        "   ON P.ID_Modulo = M.ID_Modulo " +
                        "WHERE A.ID_Usuario = " + idUsuario +
                        "  AND Nombre <> '' " +
                        "  AND (AccesoVer = 1 OR AccesoEditar = 1) " +
                        "ORDER BY ID_Modulo, Pagina;";
            }

            ResultSet resultadoNavegacion = instanciaNavegacion.execQry(myQuery);
            if (resultadoNavegacion.next()) {
                // Crea navbar vacía para ser llenada con la información
                // recuperada de la base de datos.
                respuesta
                        = "$#webpage-detail#$"
                        + "<nav style='background-color: #33A5FF;'>"
                        + "  <div class='nav-wrapper'>"
                        + "    <a href='#' data-target='mobile-demo' class='sidenav-trigger'>"
                        + "        <i class='material-icons'>menu</i></a>"
                        + "    <ul class='hide-on-med-and-down'>$#module#$</ul>"
                        + "  </div>"
                        + "</nav>"
                        + "<ul class='sidenav' id='mobile-demo'>"
                        + "  $#newRootElement#$"
                        + "  <li style='margin-left: -10%;'><a style='font-weight: normal;' href='#' class='btnSalir'>SALIR</a></li>"
                        + "</ul>";

                do {
                    // Verifica si módulo es diferente para crear un nuevo elemento
                    // en el navbar.
                    if (!resultadoNavegacion.getString("Modulo").equals(nombreModuloAnterior)) {
                        // Agrega las páginas web almacenadas del módulo anterior.
                        respuesta = respuesta.replace("$#webpage-detail#$", dropPaginas + "$#webpage-detail#$");
                        nombreModuloAnterior = resultadoNavegacion.getString("Modulo");

                        // Nuevo elemento en la barra de navegación con el nombre del módulo.
                        dropModulo
                                = "<li>"
                                + "<a class='dropdown-trigger' href='#!'"
                                + "data-target='drpdwn" + resultadoNavegacion.getString("ID_Modulo") + "'>"
                                + resultadoNavegacion.getString("Modulo")
                                + "<i class='material-icons right'>arrow_drop_down</i>"
                                + "</a>"
                                + "</li>$#module#$";
                        respuesta = respuesta.replace("$#module#$", dropModulo);

                        // Nuevo elemento a la barra de navegación para dispositivos con pantalla pequeña.
                        respuesta = respuesta.replace("$#newRootElement#$", dropMovil + "$#newRootElement#$");
                        dropMovil
                                = "<li>"
                                + resultadoNavegacion.getString("Modulo")
                                + "<ul>$#newChildElement#$</ul>"
                                + "</li>";

                        // Reinicia valor de la variable que contiene las páginas
                        // web que pertenecen a un módulo específico.
                        dropPaginas
                                = "<ul id='drpdwn" + resultadoNavegacion.getString("ID_Modulo") + "' "
                                + "class='dropdown-content'>$#currentPage#$</ul>";
                    }

                    // Agrega a la variable la página web devuelta por la base de datos.
                    dropPaginas = dropPaginas.replace(
                            "$#currentPage#$",
                            "<li>"
                            + "<a href='" + resultadoNavegacion.getString("Pagina") + "'>"
                            + resultadoNavegacion.getString("Nombre") + "</a>"
                            + "</li>$#currentPage#$");

                    dropMovil = dropMovil.replace(
                            "$#newChildElement#$",
                            "<li>"
                            + "<a href='" + resultadoNavegacion.getString("Pagina") + "'>"
                            + resultadoNavegacion.getString("Nombre") + "</a>"
                            + "</li>$#newChildElement#$");
                } while (resultadoNavegacion.next());

                // Elimina etiquetas de reemplazo sobrantes y agrega el botón Salir
                // a la navbar creada.
                respuesta = respuesta
                        .replace("$#webpage-detail#$", dropPaginas)
                        .replace("$#newRootElement#$", dropMovil)
                        .replace("$#module#$", "<li class='right'>"
                                + "<a href='#' class='btnSalir'>"
                                + "<i class='material-icons right'>power_settings_new</i>"
                                + "Salir"
                                + "</a>"
                                + "</li>");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            respuesta = "";
        } finally {
            if (instanciaNavegacion != null) {
                instanciaNavegacion.desconectar();
            }

            instanciaNavegacion = null;
            System.gc();
        }

        return respuesta
                .replace("$#webpage-detail#$", "")
                .replace("$#module#$", "")
                .replace("$#currentPage#$", "")
                .replace("$#newRootElement#$", "")
                .replace("$#newChildElement#$", "");
    } // Fin del método cargarBarraNavegacion().

    
    // Método llamado por Usuario.java
    public String cargarMapaAccesos(int idUsuario, int idRol) {
        $ instanciaListaModulos = null;
        String respuesta = "";

        try {
            instanciaListaModulos = new $();
            String myQuery
                    = "SELECT ID_Modulo, Modulo "
                    + "FROM Modulo;";

            ResultSet resultadoListaModulos = instanciaListaModulos.execQry(myQuery);
            while (resultadoListaModulos.next()) {
                $ instanciaListaPaginasWeb = new $();
                myQuery
                        = "WITH Pagina_Web_CTE AS ("
                        + "   SELECT ID_Pagina, Pagina "
                        + "   FROM Pagina_Web "
                        + "   WHERE ID_Modulo = " + resultadoListaModulos.getInt("ID_Modulo") + " "
                        +( idRol == Consts.ROL_FARMACIA_APROBADOR ?
                          "     AND (ID_Rol = " + idRol + " OR ID_Rol = " + Consts.ROL_FARMACIA_MIXTO + " )), " :
                          idRol == Consts.ROL_FARMACIA_OPERADOR ?
                          "     AND (ID_Rol = " + idRol + " OR ID_Rol = " + Consts.ROL_FARMACIA_MIXTO + " )), " :
                          "     AND ID_Rol = " + idRol + "),")
                        + "Acceso_Usuario_CTE AS ("
                        + "   SELECT ID_Pagina, ID_Usuario, AccesoVer, AccesoEditar "
                        + "   FROM Acceso_Usuario "
                        + "   WHERE ID_Modulo = " + resultadoListaModulos.getInt("ID_Modulo") + " "
                        + "     AND ID_Usuario = " + idUsuario + " ) "
                        + "SELECT P.ID_Pagina, P.Pagina, A.AccesoVer, AccesoEditar "
                        + "FROM Pagina_Web_CTE P "
                        + "LEFT JOIN Acceso_Usuario_CTE A "
                        + "  ON P.ID_Pagina = A.ID_Pagina";

                ResultSet resultadoListaPaginas = instanciaListaPaginasWeb.execQry(myQuery);
                if (resultadoListaPaginas.next()) {
                    respuesta
                            += "<div class='col s12'>"
                            + "  <table class='tablaPaginas'>"
                            + "    <tr><th colspan='4'>" + resultadoListaModulos.getString("Modulo") + "</th></tr>";

                    do {
                        respuesta
                                += "<tr>"
                                + "   <td data-codigo='" + resultadoListaPaginas.getString("ID_Pagina") + "'></td>"
                                + "   <td style='width: 67%;'>" + resultadoListaPaginas.getString("Pagina") + "</td>"
                                + "   <td style='width: 20%; cursor: pointer;' class='colVisualizar center "
                                + (resultadoListaPaginas.getBoolean("AccesoVer") ? "habilitado" : "deshabilitado") + "' "
                                + "       id='colVis" + resultadoListaPaginas.getString("ID_Pagina") + "' "
                                + "       data-estado='" + resultadoListaPaginas.getBoolean("AccesoVer") + "'>Visualizar</td>"
                                + "   <td style='width: 15%; cursor: pointer;' class='colEditar center "
                                + (resultadoListaPaginas.getBoolean("AccesoEditar") ? "habilitado" : "deshabilitado") + "' "
                                + "       id='colEdt" + resultadoListaPaginas.getString("ID_Pagina") + "' "
                                + "       data-estado='" + resultadoListaPaginas.getBoolean("AccesoEditar") + "'>Editar</td>"
                                + "<tr>";
                    } while (resultadoListaPaginas.next());

                    instanciaListaPaginasWeb.desconectar();
                    instanciaListaPaginasWeb = null;
                    resultadoListaPaginas = null;
                    System.gc();

                    respuesta
                            += "  </table>"
                            + "</div>";
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (instanciaListaModulos != null) {
                instanciaListaModulos.desconectar();
            }

            instanciaListaModulos = null;
            System.gc();
        }

        return respuesta;
    } // Fin del método cargarMapaAccesos().
    
    
    // Método llamado por:  *all.html.
    public String cargarReporteAccesos(int idUsuario) {
        $ instanciaBD = null;
        String respuesta =
                "{ \"error\": false, " +
                "  \"equipo\": $#todefine#$, " +
                "  \"insumo\": $#todefine#$, " +
                "  \"medicamento\": $#todefine#$, " +
                "  \"terapeuta\": $#todefine#$, " +
                "  \"asistencia\": $#todefine#$, " +
                "  \"costos\": false, " +
                "  \"sesion\": false, " +
                "  \"evento\": false }";

        try {
            instanciaBD = new $();
            String myQuery =
                    "SELECT CASE WHEN AccesoVer = 1 OR AccesoEditar = 1 " +
                    "            THEN 1 ELSE 0 END AS TieneAcceso, " +
                    "       Pagina " +
                    "FROM Acceso_Usuario A " +
                    "     INNER JOIN Pagina_Web P " +
                    "        ON A.ID_Pagina = P.ID_Pagina " +
                    "WHERE ID_Usuario = " + idUsuario +
                    "  AND Pagina IN(" +
                    "      '" + Consts.LOBBY_CATALOGO_MEDICAMENTOS_HTML + "', " +
                    "      '" + Consts.LOBBY_MEDICAMENTOS_HTML + "', " +
                    "      '" + Consts.LOBBY_CATALOGO_EQUIPOS_HTML + "', " +
                    "      '" + Consts.LOBBY_EQUIPOS_HTML + "', " +
                    "      '" + Consts.LOBBY_CATALOGO_SUMINISTROS_HTML + "', " +
                    "      '" + Consts.LOBBY_SUMINISTROS_HTML + "');";
            
            ResultSet resultadoListaPaginas = instanciaBD.execQry(myQuery);
            while(resultadoListaPaginas.next()){
                String paginaWeb = resultadoListaPaginas.getString("Pagina");
                boolean tieneAcceso = resultadoListaPaginas.getBoolean("TieneAcceso");
                
                
                if(paginaWeb.equals(Consts.LOBBY_CATALOGO_MEDICAMENTOS_HTML)
                        || paginaWeb.equals(Consts.LOBBY_MEDICAMENTOS_HTML))
                    respuesta = respuesta
                                    .replace("\"medicamento\": $#todefine#$",
                                             "\"medicamento\": " + tieneAcceso)
                                    .replace("\"medicamento\": false",
                                             "\"medicamento\": " + tieneAcceso);
                else if(paginaWeb.equals(Consts.LOBBY_CATALOGO_SUMINISTROS_HTML)
                        || paginaWeb.equals(Consts.LOBBY_SUMINISTROS_HTML))
                    respuesta = respuesta
                                    .replace("\"insumo\": $#todefine#$",
                                             "\"insumo\": " + tieneAcceso)
                                    .replace("\"insumo\": false",
                                             "\"insumo\": " + tieneAcceso);
                else if(paginaWeb.equals(Consts.LOBBY_CATALOGO_EQUIPOS_HTML)
                        || paginaWeb.equals(Consts.LOBBY_EQUIPOS_HTML))
                    respuesta = respuesta
                                    .replace("\"equipo\": $#todefine#$",
                                             "\"equipo\": " + tieneAcceso)
                                    .replace("\"equipo\": false",
                                             "\"equipo\": " + tieneAcceso);
            }
            
            respuesta = respuesta.replace("$#todefine#$", "false");
        } catch (Exception ex) {
            ex.printStackTrace();
            respuesta = "{" +
                        "   \"error\": true, " +
                        "   \"mensaje\": \"Error al intentar cargar la lista de reportes permitidos.\" }";
        } finally {
            if (instanciaBD != null)
                instanciaBD.desconectar();

            instanciaBD = null;
            System.gc();
        }
        
        return respuesta;
    } // Fin del método cerrarSesion().
    
    
    // Carga la lista de las acciones que realizó el usuario.
    public String cargarAccionesUsuario(int idSesion){
        $ instanciaBD = null;
        String respuesta = "";

        // Define la estructura HTML de respuesta.
        final String ESTRUCTURA_ERROR =
            "  \"elementosHTML\": \"" +
            "            <h5 class='center'>$#titulo#$</h5><hr/>" +
            "            <div class='col s12 center'>" +
            "              <p> *** No se puede mostrar la información solicitada. ***</p>" +
            "            </div>\"";
        final String PAGINA_WEB = "#";
        final String TITULO = "Sesiones de Usuario";
        final String ETIQUETA_EXISTENTE = "sesionExistente";    

        try {
            // Realiza consulta de sesiones a la base de datos.
            instanciaBD = new $();
            ResultSet resultadoEncabezado = instanciaBD.execQry(
                "SELECT * FROM " +
                "(SELECT *, ROW_NUMBER() OVER( " +
                "          PARTITION BY ID_Sesion, ID_Registro " +
                "          ORDER BY FechaAccion DESC ) AS row_num " +
                "FROM Acciones_Usuario " +
                "WHERE ID_Registro NOT IN ('INGRESO', 'NUEVO', '') " +
                "  AND Pantalla NOT LIKE 'lobby%' " +
                ") X " +
                "WHERE ID_Sesion = " + idSesion +
                "  AND X.row_num = 1 " +
                "ORDER BY FechaAccion ASC;");

            if( resultadoEncabezado.next() ){
                String idRegistro;
                Long fechaAccion;
                String pantalla;
                String evento;
                
                respuesta =
                    // Retroalimentación al usuario del proceso.
                    "{ \"error\" : false, " +
                    "  \"mensaje\" : \"\", " +
                    // Información recuperada de la tabla Departamento.
                    "  \"elementoHTML\": \"";

                do{
                    // Si existen resultados crea arreglo JSON para mostrar la lista de acciones.
                    idRegistro = resultadoEncabezado.getString("ID_Registro");
                    fechaAccion = resultadoEncabezado.getLong("FechaAccion");
                    System.out.println(fechaAccion);
                    pantalla = resultadoEncabezado.getString("Pantalla");
                    evento = resultadoEncabezado.getString("Accion");
                    
                    String myQuery = "";
                    String codigoRegistro = "";
                    String descripcionRegistro = "";
                    
                    if(evento.equals("I")){
                        codigoRegistro = "IN";
                        descripcionRegistro = "Inserción";
                        
                        if(pantalla.equals(Consts.DEPARTAMENTO_HTML)){
                            myQuery =
                                "SELECT Nombre, Descripcion, Activo " +
                                "FROM Bitacora_Departamento " +
                                "WHERE ID_Departamento = " + idRegistro +
                                "  AND CodigoRegistro = '" + codigoRegistro + "'" +
                                "  AND FechaAccion = " + fechaAccion + ";";
                        }
                    } else if(evento.equals("V")){
                        descripcionRegistro = "Visualización";
                        
                        if(pantalla.equals(Consts.PACIENTE_HTML)){
                            myQuery =
                                "SELECT ID_Paciente, EstadoCivil, Genero, Nombre, " +
                                "       MedicoRemitio, Nombres, Apellidos, Identidad, " +
                                "       P.Direccion, P.Telefono, TelefonoEmergencia, " +
                                "       P.CorreoElectronico, FechaNacimiento, FechaIngreso " +
                                "FROM Paciente P " +
                                "     INNER JOIN Estado_Civil E " +
                                "        ON P.ID_EstadoCivil = E.ID_EstadoCivil " +
                                "     INNER JOIN Genero G " +
                                "        ON P.ID_Genero = G.ID_Genero " +
                                "     INNER JOIN Centro_Remision R " +
                                "        ON P.RemitidoPor = R.ID_CentroRemision " +
                                "WHERE ID_Paciente = '" + idRegistro + "';";
                        } else
                        
                        if(pantalla.equals(Consts.CENTRO_HTML)){
                            myQuery =
                                "SELECT Nombre, Direccion, Logo, Telefono, " +
                                "       Correo, Activo, " +
                                "       HoraInicioLunes, HoraFinalLunes, " +
                                "       HoraInicioMartes, HoraFinalMartes, " +
                                "       HoraInicioMiercoles, HoraFinalMiercoles, " +
                                "       HoraInicioJueves, HoraFinalJueves, " +
                                "       HoraInicioViernes, HoraFinalViernes, " +
                                "       HoraInicioSabado, HoraFinalSabado, " +
                                "       HoraInicioDomingo, HoraFinalDomingo " +
                                "FROM Centro C " +
                                "     INNER JOIN ParametrosBD P " +
                                "        ON C.ID_Centro = P.ID_Centro " +
                                "WHERE C.ID_Centro = 1;";
                        }
                    } else if(evento.equals("M")){
                        descripcionRegistro = "Modificación";
                        
                        if(pantalla.equals(Consts.PUESTO_HTML)){
                            myQuery =
                                "SELECT CodigoRegistro, ID_Puesto, D.Nombre Departamento, " +
                                "       S.Nombre TipoSalario, P.Nombre, P.Descripcion, " +
                                "       EdadMinima, EdadMaxima, G.Genero, EC.EstadoCivil, " +
                                "       EX.ExperienciaRequerida, NotaExperiencia, " +
                                "       Objetivo, Autoridad, SalarioBase, P.Activo " +
                                "FROM Bitacora_Puesto P " +
                                "     INNER JOIN Departamento D " +
                                "        ON P.ID_Departamento = D.ID_Departamento " +
                                "     INNER JOIN Salario S " +
                                "        ON P.ID_Salario = S.ID_Salario " +
                                "     INNER JOIN Genero G " +
                                "        ON P.Genero = G.ID_Genero " +
                                "     INNER JOIN Estado_Civil EC " +
                                "        ON P.EstadoCivil = EC.ID_EstadoCivil " +
                                "     INNER JOIN Experiencia EX " +
                                "        ON P.ExperienciaRequerida = EX.ID_Experiencia " +
                                "WHERE ID_Puesto = " + idRegistro + " " +
                                "  AND FechaAccion = " + fechaAccion + " " +
                                "ORDER BY CodigoRegistro DESC;";
                        } else if(pantalla.equals(Consts.PACIENTE_HTML)){
                            myQuery =
                                "SELECT CodigoRegistro, " +
                                "       ID_Paciente, EstadoCivil, Genero, " +
                                "       C.Nombre, MedicoRemitio, Nombres, " +
                                "       Apellidos, Identidad, P.Direccion, P.Telefono, " +
                                "       P.CorreoElectronico, FechaNacimiento, FechaIngreso " +
                                "FROM Bitacora_Paciente P " +
                                "     INNER JOIN Genero G " +
                                "        ON P.ID_Genero = G.ID_Genero " +
                                "     INNER JOIN Estado_Civil EC " +
                                "        ON P.ID_EstadoCivil = EC.ID_EstadoCivil " +
                                "     INNER JOIN Centro_Remision C " +
                                "        ON RemitidoPor = ID_CentroRemision " +
                                "WHERE ID_Paciente = '" + idRegistro + "' " +
                                "  AND FechaAccion = " + fechaAccion + ";";
                        }
                    }
                    
                    
                    
                    // Recupera las fechas de inicio y final de cada sesión.
                    ResultSet resultadoAccion = instanciaBD.execQry(myQuery);
                    while(resultadoAccion.next()){
                        if(pantalla.equals(Consts.DEPARTAMENTO_HTML) && evento.equals("I")){
                            respuesta +=
                            "<div class='input-field col s12 div-table'>" +
                            "  <table style='border-collapse: collapse;'>" +
                            "      <tbody>" +
                            "          <tr style='border-bottom: none; line-height: 70%;'>" +
                            "              <th style='text-align:' class='generic'>Pantalla</th>" +
                            "              <th style='text-align:' class='generic'>Evento</th>" +
                            "              <th style='text-align:' class='generic'>Fecha y Hora</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Nombre</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Descripción</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Activo</th>" +
                            "          </tr>" +
                            "          <tr style='border-bottom: none;'>" +
                            "              <td style='padding-top:0; padding-bottom:0.7%;' class='generic'>" + pantalla + "</td>" +
                            "              <td style='padding-top:0; padding-bottom:0.7%;' class='generic'>" + descripcionRegistro + "</td>" +
                            "              <td style='padding-top:0; padding-bottom:0.7%;' class='generic'>" + ServiciosU.formatearFecha(fechaAccion) + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("Nombre") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("Descripcion") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic center'>" +
                            "                <i class='material-icons'>" + (resultadoAccion.getBoolean("Activo") ? "check_box" : "check_box_outline_blank" ) + "</i></td>" +
                            "          </tr>" +
                            "      </tbody>" +
                            "  </table>" +
                            "</div>";
                        } else
                        
                        
                        if(pantalla.equals(Consts.PACIENTE_HTML) && evento.equals("V")){
                            respuesta +=
                            "<div class='input-field col s12 div-table'>" +
                            "  <table style='border-collapse: collapse;'>" +
                            "      <tbody>" +
                            "          <tr style='border-bottom: none; line-height: 70%;'>" +
                            "              <th style='text-align:' class='generic'>Pantalla</th>" +
                            "              <th style='text-align:' class='generic'>Evento</th>" +
                            "              <th style='text-align:' class='generic'>Fecha y Hora</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Codigo Paciente</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Estado Civil</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Genero</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Centro de Remisión</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Medico que Remitió</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Nombres</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Apellidos</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Identidad</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Dirección</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Telefono</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Correo Electrónico</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Fecha de Nacimiento</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Fecha de Ingreso</th>" +
                            "          </tr>" +
                            "          <tr style='border-bottom: none;'>" +
                            "              <td style='padding-top:0; padding-bottom:0.7%;' class='generic'>" + pantalla + "</td>" +
                            "              <td style='padding-top:0; padding-bottom:0.7%;' class='generic'>" + descripcionRegistro + "</td>" +
                            "              <td style='padding-top:0; padding-bottom:0.7%;' class='generic'>" + ServiciosU.formatearFecha(fechaAccion) + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("ID_Paciente") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("EstadoCivil") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("Genero") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("Nombre") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("MedicoRemitio") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("Nombres") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("Apellidos") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("Identidad") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("Direccion") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("Telefono") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("CorreoElectronico") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("FechaNacimiento") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("FechaIngreso") + "</td>" +
                            "          </tr>" +
                            "      </tbody>" +
                            "  </table>" +
                            "</div>";
                        } else
                        
                        
                        if(pantalla.equals(Consts.PACIENTE_HTML) && evento.equals("M")){
                            if(resultadoAccion.getString("CodigoRegistro").equals("PR"))                            
                                respuesta +=
                                "<div class='input-field col s12 div-table'>" +
                                "  <table style='border-collapse: collapse;'>" +
                                "      <tbody>" +
                                "          <tr style='border-bottom: none; line-height: 70%;'>" +
                                "              <th style='text-align:' class='generic'>Pantalla</th>" +
                                "              <th style='text-align:' class='generic'>Evento</th>" +
                                "              <th style='text-align:' class='generic'>Fecha y Hora</th>" +
                                "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Codigo Paciente</th>" +
                                "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Estado Civil</th>" +
                                "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Género</th>" +
                                "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Centro de Remisión</th>" +
                                "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Medico que Remitió</th>" +
                                "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Nombres</th>" +
                                "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Apellidos</th>" +
                                "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Identidad</th>" +
                                "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Dirección</th>" +
                                "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Teléfono</th>" +
                                "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Correo Electrónico</th>" +
                                "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Fecha Nacimiento</th>" +
                                "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Fecha Ingreso</th>" +
                                "          </tr>" +
                                "          <tr style='border-bottom: none;'>" +
                                "              <td style='padding-top:0; padding-bottom:0.7%;' class='generic'>" + pantalla + "</td>" +
                                "              <td style='padding-top:0; padding-bottom:0.7%;' class='generic'>Modificación-Previo</td>" +
                                "              <td style='padding-top:0; padding-bottom:0.7%;' class='generic'>" + ServiciosU.formatearFecha(fechaAccion) + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("ID_Paciente") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("EstadoCivil") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("Genero") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("Nombre") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("MedicoRemitio") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("Nombres") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("Apellidos") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("Identidad") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("Direccion") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("Telefono") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("CorreoElectronico") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("FechaNacimiento") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("FechaIngreso") + "</td>" +
                                "          </tr>";
                            else
                                respuesta +=
                                "          <tr style='border-bottom: none;'>" +
                                "              <td style='padding-top:0; padding-bottom:0.7%;' class='generic'>" + pantalla + "</td>" +
                                "              <td style='padding-top:0; padding-bottom:0.7%;' class='generic'>Modificación-Post</td>" +
                                "              <td style='padding-top:0; padding-bottom:0.7%;' class='generic'>" + ServiciosU.formatearFecha(fechaAccion) + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("ID_Paciente") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("EstadoCivil") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("Genero") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("Nombre") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("MedicoRemitio") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("Nombres") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("Apellidos") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("Identidad") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("Direccion") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("Telefono") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("CorreoElectronico") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("FechaNacimiento") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("FechaIngreso") + "</td>" +
                                "          </tr>" +
                                "      </tbody>" +
                                "  </table>" +
                                "</div>";
                        } else
                        
                        
                        if(pantalla.equals(Consts.CENTRO_HTML) && evento.equals("V")){
                            respuesta +=
                            "<div class='input-field col s12 div-table'>" +
                            "  <table style='border-collapse: collapse;'>" +
                            "      <tbody>" +
                            "          <tr style='border-bottom: none; line-height: 70%;'>" +
                            "              <th style='text-align:' class='generic'>Pantalla</th>" +
                            "              <th style='text-align:' class='generic'>Evento</th>" +
                            "              <th style='text-align:' class='generic'>Fecha y Hora</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Nombre</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Dirección</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Logo</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Telefóno</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Correo Electrónico</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Activo</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Inicio Lunes</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Final Lunes</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Inicio Martes</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Final Martes</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Inicio Miercoles</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Final Miercoles</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Inicio Jueves</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Final Jueves</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Inicio Viernes</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Final Viernes</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Inicio Sabado</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Final Sabado</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Inicio Domingo</th>" +
                            "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Final Domingo</th>" +
                            "          </tr>" +
                            "          <tr style='border-bottom: none;'>" +
                            "              <td style='padding-top:0; padding-bottom:0.7%;' class='generic'>" + pantalla + "</td>" +
                            "              <td style='padding-top:0; padding-bottom:0.7%;' class='generic'>" + descripcionRegistro + "</td>" +
                            "              <td style='padding-top:0; padding-bottom:0.7%;' class='generic'>" + ServiciosU.formatearFecha(fechaAccion) + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("Nombre") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("Direccion") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("Logo") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("Telefono") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("Correo") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic center'>" +
                            "                <i class='material-icons'>" + (resultadoAccion.getBoolean("Activo") ? "check_box" : "check_box_outline_blank" ) + "</i></td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("HoraInicioLunes") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("HoraFinalLunes") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("HoraInicioMartes") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("HoraFinalMartes") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("HoraInicioMiercoles") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("HoraFinalMiercoles") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("HoraInicioJueves") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("HoraFinalJueves") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("HoraInicioViernes") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("HoraFinalViernes") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("HoraInicioSabado") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("HoraFinalSabado") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("HoraInicioDomingo") + "</td>" +
                            "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                resultadoAccion.getString("HoraFinalDomingo") + "</td>" +
                            "          </tr>" +
                            "      </tbody>" +
                            "  </table>" +
                            "</div>";
                        } else
                        
                        
                        if(pantalla.equals(Consts.PUESTO_HTML) && evento.equals("M")){
                            if(resultadoAccion.getString("CodigoRegistro").equals("PR"))                            
                                respuesta +=
                                "<div class='input-field col s12 div-table'>" +
                                "  <table style='border-collapse: collapse;'>" +
                                "      <tbody>" +
                                "          <tr style='border-bottom: none; line-height: 70%;'>" +
                                "              <th style='text-align:' class='generic'>Pantalla</th>" +
                                "              <th style='text-align:' class='generic'>Evento</th>" +
                                "              <th style='text-align:' class='generic'>Fecha y Hora</th>" +
                                "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Codigo Puesto</th>" +
                                "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Departamento</th>" +
                                "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Tipo de Salario</th>" +
                                "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Nombre del Puesto</th>" +
                                "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Descripción del Puesto</th>" +
                                "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Edad Mínima</th>" +
                                "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Edad Máxima</th>" +
                                "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Genero</th>" +
                                "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Estado Civil</th>" +
                                "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Experiencia Requerida</th>" +
                                "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Nota Experiencia</th>" +
                                "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Objetivo</th>" +
                                "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Autoridad</th>" +
                                "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Salario</th>" +
                                "              <th style='border-left: 1px solid black; text-align: center;' class='generic'>Activo</th>" +
                                "          </tr>" +
                                "          <tr style='border-bottom: none;'>" +
                                "              <td style='padding-top:0; padding-bottom:0.7%;' class='generic'>" + pantalla + "</td>" +
                                "              <td style='padding-top:0; padding-bottom:0.7%;' class='generic'>Modificación-Previo</td>" +
                                "              <td style='padding-top:0; padding-bottom:0.7%;' class='generic'>" + ServiciosU.formatearFecha(fechaAccion) + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("ID_Puesto") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("Departamento") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("TipoSalario") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("Nombre") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("Descripcion") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("EdadMinima") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("EdadMaxima") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("Genero") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("EstadoCivil") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("ExperienciaRequerida") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("NotaExperiencia") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("Objetivo") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("Autoridad") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("SalarioBase") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic center'>" +
                                "                <i class='material-icons'>" + (resultadoAccion.getBoolean("Activo") ? "check_box" : "check_box_outline_blank" ) + "</i></td>" +
                                "          </tr>";
                            else
                                respuesta +=
                                "          <tr style='border-bottom: none;'>" +
                                "              <td style='padding-top:0; padding-bottom:0.7%;' class='generic'>" + pantalla + "</td>" +
                                "              <td style='padding-top:0; padding-bottom:0.7%;' class='generic'>Modificación-Post</td>" +
                                "              <td style='padding-top:0; padding-bottom:0.7%;' class='generic'>" + ServiciosU.formatearFecha(fechaAccion) + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("ID_Puesto") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("Departamento") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("TipoSalario") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("Nombre") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("Descripcion") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("EdadMinima") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("EdadMaxima") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("Genero") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("EstadoCivil") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("ExperienciaRequerida") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("NotaExperiencia") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("Objetivo") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("Autoridad") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic'>" +
                                                    resultadoAccion.getString("SalarioBase") + "</td>" +
                                "              <td style='border-left: 1px solid black; padding-top:0; padding-bottom:0.7%;' class='generic center'>" +
                                "                <i class='material-icons'>" + (resultadoAccion.getBoolean("Activo") ? "check_box" : "check_box_outline_blank" ) + "</i></td>" +
                                "          </tr>" +
                                "      </tbody>" +
                                "  </table>" +
                                "</div>";
                        }
                    }
                } while( resultadoEncabezado.next() );

                // Completa la estructura del objeto JSON de respuesta.
                respuesta = respuesta + "\" }";
            } else
                respuesta = "{ \"error\" : false, " +
                            "  \"mensaje\" : \"No existen registros para mostrar.\" ," +
                               ESTRUCTURA_ERROR
                                      .replace("$#titulo#$", TITULO)
                                      .replace("$#paginaWeb#$", PAGINA_WEB) +
                            "}";
        } catch (Exception ex) {
            ex.printStackTrace();
            respuesta = "{ \"error\" : true, " +
                        "  \"mensaje\" : \"Ocurrió un error al intentar cargar los registros.\", " +
                           ESTRUCTURA_ERROR
                                    .replace("$#titulo#$", TITULO)
                                    .replace("$#paginaWeb#$", PAGINA_WEB) +
                        "}";
        } finally {
            if(instanciaBD != null)
              instanciaBD.desconectar();

            instanciaBD = null;
            System.gc();
        }

        return respuesta;
    } // Fin del método cargarLista().
    
    
    // Carga la lista de todos los departamentos de la empresa, activos e inactivos.
    public String cargarListaSesiones(int idUsuario, int fechaInicio, int fechaFinal){
        $ instanciaBD = null;
        String respuesta = "";

        // Define la estructura HTML de respuesta.
        final String ESTRUCTURA_ERROR =
            "  \"elementosHTML\": \"" +
            "            <h5 class='center'>$#titulo#$</h5><hr/>" +
            "            <div class='col s12 center'>" +
            "              <p> *** No se puede mostrar la información solicitada. ***</p>" +
            "            </div>\"";
        final String PAGINA_WEB = "#";
        final String TITULO = "Sesiones de Usuario";
        final String ETIQUETA_EXISTENTE = "sesionExistente";    

        try {
            // Realiza consulta de sesiones a la base de datos.
            instanciaBD = new $();
            ResultSet resultado = instanciaBD.execQry(
                "SELECT ID_Sesion, U.Usuario, E.Nombres, E.Apellidos " +
                "FROM LogBD L INNER JOIN Usuario U " +
                "        ON L.ID_Usuario = U.ID_Usuario " +
                "     INNER JOIN Empleado E " +
                "        ON U.ID_Empleado = E.ID_Empleado " +
                "WHERE U.ID_Usuario = " + idUsuario + " " +
                "  AND ID_Sesion IN( " +
                "      SELECT DISTINCT(ID_Sesion) " +
                "      FROM Acciones_Usuario " +
                "      WHERE FechaAccion BETWEEN " + fechaInicio + "000000000 AND " + fechaFinal + "999999999 " +
                "        AND ID_Sesion <= 281 )" +
                "ORDER BY ID_Sesion DESC;");

            if( resultado.next() ){
                String sesion;
                String usuario;
                String nombreCompleto;
                
                respuesta =
                    // Retroalimentación al usuario del proceso.
                    "{ \"error\" : false, " +
                    "  \"mensaje\" : \"\", " +
                    "  \"fechaRequerida\": false, " +
                    // Información recuperada de la tabla Departamento.
                    "  \"elementosHTML\": \"" +
                    "            <h5 class='center'>" + TITULO + 
                    "              <a href='#modal1' class='modal-trigger'>" +
                    "                <i class='material-icons right' style='color: red;'>event</i>" +
                    "              </a>" +
                    "            </h5><hr/>" +
                    "            <table id='myTable2'>" +
                    "              <thead>" +
                    "                <tr>" +
                    "                  <th onclick='sortTable(0)' style='cursor:pointer' id='sesionCol' class='center'>Sesión</th>" +
                    "                  <th>Usuario</th>" +
                    "                  <th>Nombre del Empleado</th>" +
                    "                  <th onclick='sortTable(0)' style='cursor:pointer' id='ingresoCol'>Ingreso</th>" +
                    "                  <th>Salida</th>" +
                    "                  <th class='center'>Visualizar</th>" +
                    "                </tr>" +
                    "              </thead>" +
                    "            <tbody>";

                do{
                    // Si existen resultados crea arreglo JSON para mostrar la lista de sesiones.
                    sesion = resultado.getString("ID_Sesion");
                    usuario = resultado.getString("Usuario");
                    nombreCompleto = ServiciosU.obtenerNombreCompleto(
                                        resultado.getString("Nombres"),
                                        resultado.getString("Apellidos"));
                    
                    // Recupera las fechas de inicio y final de cada sesión.
                    ResultSet resultadoHoras = instanciaBD.execQry(
                        "SELECT MIN(FechaAccion), MAX(FechaAccion) " +
                        "FROM Acciones_Usuario " +
                        "WHERE ID_Sesion = " + sesion + " " +
                        "  AND FechaAccion >= 20200101000000000");
                    while(resultadoHoras.next()){
                        respuesta +=
                                "<tr style='border: none;' class='resaltar'>" +
                                "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;' class='center'>" + sesion + "</td>" +
                                "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + usuario + "</td>" +
                                "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + nombreCompleto + "</td>" +
                                "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" +
                                        ServiciosU.formatearFecha(resultadoHoras.getLong(1)) + "</td>" +
                                "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" +
                                        ServiciosU.formatearFecha(resultadoHoras.getLong(2)) + "</td>" +
                                "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;' class='center'>" +
                                "    <a href='detallesesion.html' data-codigo='" + sesion + "' class='sesionExistente'><i class='material-icons'>visibility</i></a>" +
                                "  </td>" +
                                "</tr>";
                    }
                } while( resultado.next() );

                // Completa la estructura del objeto JSON de respuesta.
                respuesta = respuesta + "</tbody></table>\"}";
            } else
                respuesta = "{ \"error\" : false, " +
                            "  \"fechaRequerida\": false, " +
                            "  \"mensaje\" : \"No existen registros para mostrar.\" ," +
                               ESTRUCTURA_ERROR
                                      .replace("$#titulo#$", TITULO)
                                      .replace("$#paginaWeb#$", PAGINA_WEB) +
                            "}";
        } catch (Exception ex) {
            ex.printStackTrace();
            respuesta = "{ \"error\" : true, " +
                        "  \"fechaRequerida\": false, " +
                        "  \"mensaje\" : \"Ocurrió un error al intentar cargar los registros.\", " +
                           ESTRUCTURA_ERROR
                                    .replace("$#titulo#$", TITULO)
                                    .replace("$#paginaWeb#$", PAGINA_WEB) +
                        "}";
        } finally {
            if(instanciaBD != null)
              instanciaBD.desconectar();

            instanciaBD = null;
            System.gc();
        }

        return respuesta;
    } // Fin del método cargarListaSesiones().
    
    
    // Método llamado por:  *all.html.
    public void cerrarSesion(int idSesion, int idUsuario) {
        long fechaActual = fechaATimestamp();
        $ instanciaBD = null;

        try {
            String myQuery
                    = "UPDATE LogBD "
                    + "SET FechaFinalizacion = " + fechaActual + " "
                    + "WHERE ID_Sesion = " + idSesion + " "
                    + "  AND ID_Usuario = " + idUsuario + ";";
            instanciaBD = new $();
            instanciaBD.execUpd(myQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (instanciaBD != null) {
                instanciaBD.desconectar();
            }

            instanciaBD = null;
            System.gc();
        }
    } // Fin del método cerrarSesion().
    
    
    // Método llamado por:  Login.java.
    public void crearSesion(int[] idUsuario, long[] idSesion) throws ClassNotFoundException, SQLException {
        long fechaActual = fechaATimestamp();
        $ instanciaBD = null;
        $ instanciaBD2 = null;
        String myQuery
                = "INSERT INTO LogBD( ID_Usuario, FechaIngreso, FechaFinalizacion ) "
                + "VALUES(" + idUsuario[0] + ", " + fechaActual + ", " + fechaActual + ");";

        try {
            instanciaBD = new $();
            if (instanciaBD.execUpd(myQuery) > 0) {
                instanciaBD2 = new $();
                ResultSet resultado = null;
                // Recupera el id asignado a la sesión.
                myQuery
                        = "SELECT ID_Sesion FROM LogBD "
                        + "WHERE ID_Usuario = " + idUsuario[0] + " "
                        + "  AND FechaIngreso = " + fechaActual + ";";

                resultado = instanciaBD2.execQry(myQuery);
                if (resultado.next()) {
                    idSesion[0] = resultado.getLong("ID_Sesion");
                    registrarAccion(idSesion[0], Consts.INDEX_HTML,
                            Consts.ETIQUETA_INGRESO, Consts.ACCION_INGRESO);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (instanciaBD != null) {
                instanciaBD.desconectar();
            }

            if (instanciaBD2 != null) {
                instanciaBD2.desconectar();
            }

            instanciaBD = null;
            instanciaBD2 = null;
            System.gc();
        }
    } // Fin del método crearSesion().

    // Método llamado por CargarLista.java.
    public String elementoCOMPLETEPacientes(int idCentro) throws ClassNotFoundException, SQLException {
        // Carga la lista de códigos de paciente y planes en un elemento INPUT
        // para sugerirle posibles valores al usuario a medida que ingresa información.
        String respuesta = "";
        $ instanciaPaciente = new $();
        String myQuery
                = "SELECT ID_Paciente, Nombres + ' ' + Apellidos AS NombreCompleto "
                + "FROM Paciente "
                + "WHERE ID_Paciente NOT IN( "
                + "         SELECT DISTINCT(ID_Paciente)"
                + "         FROM Plan_Rehabilitacion"
                + "         WHERE Activo = 1 );";

        ResultSet resultadoListaPacientes = instanciaPaciente.execQry(myQuery);
        while (resultadoListaPacientes.next()) {
            respuesta +=
                "\"" + resultadoListaPacientes.getString("ID_Paciente") +
                       "  -  " + resultadoListaPacientes.getString("NombreCompleto") +
                "\": null, ";
        }

        instanciaPaciente.desconectar();
        instanciaPaciente = null;
        
        if(respuesta.trim().length() > 0)
            return "{" + respuesta.substring(0, respuesta.lastIndexOf(",")) + "}";
        else return "null";
    }

    // Método llamado por CargarLista.java.
    public String elementoCOMPLETEPlanes(int idCentro) throws ClassNotFoundException, SQLException {
        // Carga la lista de códigos de paciente y planes en un elemento INPUT
        // para sugerirle posibles valores al usuario a medida que ingresa información.
        String respuesta = "";
        $ instanciaPlanRehabilitacion = new $();
        String myQuery
                = "SELECT P.ID_Paciente, P.Nombres + ' ' + P.Apellidos AS NombreCompleto "
                + "FROM Plan_Rehabilitacion R "
                + "     INNER JOIN Paciente P "
                + "        ON R.ID_Paciente = P.ID_Paciente "
                + "WHERE R.Activo = 1 "
                + "  AND ID_Centro = " + idCentro + ";";

        ResultSet resultadoListaPlanesRehabilitacion = instanciaPlanRehabilitacion.execQry(myQuery);
        while (resultadoListaPlanesRehabilitacion.next()) {
            respuesta +=
                "\"" + resultadoListaPlanesRehabilitacion.getString("ID_Paciente") +
                       "  -  " + resultadoListaPlanesRehabilitacion.getString("NombreCompleto") +
                "\": null, ";
        }

        instanciaPlanRehabilitacion.desconectar();
        instanciaPlanRehabilitacion = null;
        
        if(respuesta.trim().length() > 0)
            return "{" + respuesta.substring(0, respuesta.lastIndexOf(",")) + "}";
        else return "null";
    }

    // Método llamado por CargarLista.java.
    public String elementoCOMPLETESeriesMedicamentos(int idCentro) throws ClassNotFoundException, SQLException {
        // Carga la lista de series de medicamentos en un elemento INPUT para
        // ugerirle posibles valores al usuario a medida que ingresa información.
        String respuesta = "";
        $ instanciaSerieMedicamento = new $();
        String myQuery
                = "SELECT Serie + '  -  ' + Nombre AS Dato "
                + "FROM Catalogo_Medicamento "
                + "WHERE ID_Centro = " + idCentro + " "
                + "ORDER BY Serie;";

        ResultSet resultadoListaSeriesMedicamentos = instanciaSerieMedicamento.execQry(myQuery);
        while (resultadoListaSeriesMedicamentos.next()) {
            respuesta +=
                "\"" + resultadoListaSeriesMedicamentos.getString("Dato") +
                "\": null, ";
        }

        instanciaSerieMedicamento.desconectar();
        instanciaSerieMedicamento = null;
        
        if(respuesta.trim().length() > 0)
            return "{" + respuesta.substring(0, respuesta.lastIndexOf(",")) + "}";
        else return "null";
    }

    // Método llamado por: Empleado.java
    public String elementoSELECTDepartamento(int idDepartamento) throws ClassNotFoundException, SQLException {
        // Carga la lista de departamentos en un elemento SELECT-html para
        // seleccionar el departamento que el usuario requiera.
        String respuesta = "";
        $ instanciaDepartamento = new $();
        String myQuery
                = "SELECT ID_Departamento, Nombre "
                + "FROM Departamento "
                + "WHERE Activo = 1 "
                + "ORDER BY Nombre;";

        ResultSet resultadoListaDepartamentos = instanciaDepartamento.execQry(myQuery);
        while (resultadoListaDepartamentos.next()) {
            respuesta
                    += "<option value='" + resultadoListaDepartamentos.getInt("ID_Departamento") + (idDepartamento == resultadoListaDepartamentos.getInt("ID_Departamento")
                    ? "' selected>" : "'>")
                    + resultadoListaDepartamentos.getString("Nombre")
                    + "</option>";
        }

        instanciaDepartamento.desconectar();
        instanciaDepartamento = null;
        return respuesta;
    } // Fin del método elementoSELECTDepartamento().

    // Método llamado por: SuministroMedico.java
    public String elementoSELECTEmpleado(String idEmpleado) throws ClassNotFoundException, SQLException {
        // Carga la lista de empleados en un elemento SELECT-html para
        // seleccionar el empleado que el usuario requiera.
        String respuesta = "";
        $ instanciaEmpleado = new $();
        String myQuery
                = "SELECT ID_Empleado, Nombres + ' ' + Apellidos AS NombreCompleto "
                + "FROM Empleado "
                + "WHERE Activo = 1 "
                + "ORDER BY ID_Empleado;";

        ResultSet resultadoListaEmpleado = instanciaEmpleado.execQry(myQuery);
        while (resultadoListaEmpleado.next()) {
            respuesta
                    += "<option value='" + resultadoListaEmpleado.getString("ID_Empleado") + (idEmpleado.equals(resultadoListaEmpleado.getString("ID_Empleado"))
                    ? "' selected>" : "'>")
                    + resultadoListaEmpleado.getString("ID_Empleado") + "  -  "
                    + resultadoListaEmpleado.getString("NombreCompleto")
                    + "</option>";
        }

        instanciaEmpleado.desconectar();
        instanciaEmpleado = null;
        return respuesta;
    } // Fin del método elementoSELECTEmpleado().

    // Método llamado por: Empleado.java
    public String elementoSELECTEstadoCivil(int idEstadoCivil) throws ClassNotFoundException, SQLException {
        // Carga la lista de estados civiles en un elemento SELECT-html para 
        // seleccionar el estado civil que el usuario requiera.
        String respuesta = "";
        $ instanciaEstadoCivil = new $();
        String myQuery
                = "SELECT ID_EstadoCivil, EstadoCivil "
                + "FROM Estado_Civil "
                + "ORDER BY EstadoCivil;";

        ResultSet resultadoListaEstadoCivil = instanciaEstadoCivil.execQry(myQuery);
        while (resultadoListaEstadoCivil.next()) {
            respuesta
                    += "<option value='" + resultadoListaEstadoCivil.getInt("ID_EstadoCivil") + (idEstadoCivil == resultadoListaEstadoCivil.getInt("ID_EstadoCivil")
                    ? "' selected>" : "'>")
                    + resultadoListaEstadoCivil.getString("EstadoCivil")
                    + "</option>";
        }

        instanciaEstadoCivil.desconectar();
        instanciaEstadoCivil = null;
        return respuesta;
    } // Fin del método elementoSELECTEstadoCivil().

    // Método llamado por: Empleado.java
    public String elementoSELECTExperiencia(int idExperiencia) throws ClassNotFoundException, SQLException {
        // Carga la lista de experiencia en un elemento SELECT-html para seleccionar
        // el el tiempo de experiencia que el usuario requiera.
        String respuesta = "";
        $ instanciaExperiencia = new $();
        String myQuery
                = "SELECT ID_Experiencia, ExperienciaRequerida "
                + "FROM Experiencia "
                + "ORDER BY ID_Experiencia;";

        ResultSet resultadoListaExperiencia = instanciaExperiencia.execQry(myQuery);
        while (resultadoListaExperiencia.next()) {
            respuesta
                    += "<option value='" + resultadoListaExperiencia.getInt("ID_Experiencia") + (idExperiencia == resultadoListaExperiencia.getInt("ID_Experiencia")
                    ? "' selected>" : "'>")
                    + resultadoListaExperiencia.getString("ExperienciaRequerida")
                    + "</option>";
        }

        instanciaExperiencia.desconectar();
        instanciaExperiencia = null;
        return respuesta;
    } // Fin del método elementoSELECTExperiencia().

    // Método llamado por: ServiciosC.java
    public String elementoSELECTGenerico(
            String camposSelect, String tabla, String condicion, String orden, String valorCondicion) throws ClassNotFoundException, SQLException {
        String respuesta = "";
        $ instanciaGenerica = new $();
        String myQuery
                = " SELECT " + camposSelect
                + " FROM " + tabla
                + " WHERE " + condicion
                + " " + orden + ";";

        PreparedStatement psGenerico = instanciaGenerica.prepStatement(myQuery);
        psGenerico.setString(1, valorCondicion);
        ResultSet resultadoListaGenerica = psGenerico.executeQuery();
        while (resultadoListaGenerica.next()) {
            respuesta
                    += "<option value='" + resultadoListaGenerica.getString(camposSelect) + "'>"
                    + resultadoListaGenerica.getString(camposSelect)
                    + "</option>";
        }

        instanciaGenerica.desconectar();
        instanciaGenerica = null;
        return respuesta;
    } // Fin del método elementoSELECTGenerico().

    // Método llamado por: Empleado.java
    public String elementoSELECTGenero(int idGenero) throws ClassNotFoundException, SQLException {
        // Carga la lista de géneros en un elemento SELECT-html para seleccionar
        // el género que el usuario requiera.
        String respuesta = "";
        $ instanciaGenero = new $();
        String myQuery
                = "SELECT ID_Genero, Genero "
                + "FROM Genero "
                + "ORDER BY Genero;";

        ResultSet resultadoListaGenero = instanciaGenero.execQry(myQuery);
        while (resultadoListaGenero.next()) {
            respuesta
                    += "<option value='" + resultadoListaGenero.getInt("ID_Genero") + (idGenero == resultadoListaGenero.getInt("ID_Genero")
                    ? "' selected>" : "'>")
                    + resultadoListaGenero.getString("Genero")
                    + "</option>";
        }

        instanciaGenero.desconectar();
        instanciaGenero = null;
        return respuesta;
    } // FIn del método elementoSELECTGenero().

    // Método llamado por: Paciente.java
    public String elementoSELECTHospital(int idHospital) throws ClassNotFoundException, SQLException {
        // Carga la lista de centros de remisión en un elemento SELECT-html para
        // que el usuario pueda seleccionar el hospital que remite al paciente.
        String respuesta = "";
        $ instanciaSinRemision = new $();
        // Muestra como primera opción "SIN REMISION".
        String myQuery
                = "SELECT ID_CentroRemision, Nombre "
                + "FROM Centro_Remision "
                + "WHERE ID_CentroRemision = 1;";

        ResultSet resultadoListaHospital = instanciaSinRemision.execQry(myQuery);
        if (resultadoListaHospital.next()) {
            respuesta
                    = "<option value='" + resultadoListaHospital.getInt("ID_CentroRemision") + (idHospital <= resultadoListaHospital.getInt("ID_CentroRemision")
                    ? "' selected>" : "'>")
                    + resultadoListaHospital.getString("Nombre")
                    + "</option>";
        }

        instanciaSinRemision.desconectar();
        instanciaSinRemision = null;

        // Carga los demás centros de remisión a la lista de opciones.
        myQuery
                = "SELECT ID_CentroRemision, Nombre "
                + "FROM Centro_Remision "
                + "WHERE ID_CentroRemision > 1 "
                + "ORDER BY Nombre;";
        $ instanciaHospital = new $();
        resultadoListaHospital = instanciaHospital.execQry(myQuery);

        while (resultadoListaHospital.next()) {
            respuesta
                    += "<option value='" + resultadoListaHospital.getInt("ID_CentroRemision") + (idHospital == resultadoListaHospital.getInt("ID_CentroRemision")
                    ? "' selected>" : "'>")
                    + resultadoListaHospital.getString("Nombre")
                    + "</option>";
        }

        instanciaHospital.desconectar();
        instanciaHospital = null;
        return respuesta;
    } // Fin del método elementoSELECTHospital().
    
    
    // Método llamado por: EquipoMedico.java
    public void elementoSELECTMarcaModelo(String[] elementosSelect) throws ClassNotFoundException, SQLException {
        // Carga la lista de marcas y modelos en elementos SELECT-html para que el
        // usuario pueda seleccionarlas en el formulario de equipo médico.
        $ instanciaPreseleccion = new $();
        $ instanciaCatalogo = new $();
        String marcaPreseleccionada = "";
        String modeloPreseleccionado = "";
        String elementoSelectMarca = "";
        String elementoSelectModelo = "";
        String myQuery
                = "SELECT Marca, Modelo "
                + "FROM Catalogo_Equipo_Medico "
                + "WHERE ID_Catalogo = " + elementosSelect[0] + ";";

        ResultSet resultadoPreseleccion = instanciaPreseleccion.execQry(myQuery);
        if (resultadoPreseleccion.next()) {
            marcaPreseleccionada = resultadoPreseleccion.getString("Marca");
            modeloPreseleccionado = resultadoPreseleccion.getString("Modelo");
        }

        myQuery
                = "SELECT DISTINCT(Marca), '---' AS Modelo, 0  AS Catalogo, 'MARCA' AS Tipo "
                + "FROM Catalogo_Equipo_Medico  "
                + "UNION ALL "
                + "SELECT Marca, Modelo, ID_Catalogo AS Catalogo, 'MODELO' AS Tipo "
                + "FROM Catalogo_Equipo_Medico;";
        ResultSet resultadoElementosSelect = instanciaCatalogo.execQry(myQuery);
        while (resultadoElementosSelect.next()) {
            if (marcaPreseleccionada.trim().length() == 0) {
                marcaPreseleccionada = resultadoElementosSelect.getString("Marca");
            }

            if (resultadoElementosSelect.getString("Tipo").equals("MARCA")) {
                elementoSelectMarca
                        += "<option value='" + resultadoElementosSelect.getString("Marca") + (marcaPreseleccionada.equals(resultadoElementosSelect.getString("Marca"))
                        ? "' selected>" : "'>")
                        + resultadoElementosSelect.getString("Marca")
                        + "</option>";
            }

            if (modeloPreseleccionado.trim().length() == 0
                    && !resultadoElementosSelect.getString("Modelo").equals("---")
                    && resultadoElementosSelect.getString("Marca").equals(marcaPreseleccionada)) {
                modeloPreseleccionado = resultadoElementosSelect.getString("Modelo");
                elementosSelect[0] = resultadoElementosSelect.getString("Catalogo");
            }

            if (marcaPreseleccionada.equals(resultadoElementosSelect.getString("Marca"))
                    && resultadoElementosSelect.getString("Tipo").equals("MODELO")) {
                elementoSelectModelo
                        += "<option value='" + resultadoElementosSelect.getString("Modelo") + (modeloPreseleccionado.equals(resultadoElementosSelect.getString("Modelo"))
                        ? "' selected>" : "'>")
                        + resultadoElementosSelect.getString("Modelo")
                        + "</option>";
            }
        }

        instanciaPreseleccion.desconectar();
        instanciaCatalogo.desconectar();
        instanciaPreseleccion = null;
        instanciaCatalogo = null;
        elementosSelect[1] = elementoSelectMarca;
        elementosSelect[2] = elementoSelectModelo;
    } // Fin del método elementoSELECTMarcaModelo().
    
    
    // Método llamado por: PlanMedico.java
    public String elementoSELECTMedico(int idCentro, String idEmpleado)
            throws ClassNotFoundException, SQLException {
        // Carga la lista de médicos en un elemento SELECT-html para que el
        // usuario pueda seleccionar el médico que atiende al paciente.
        String respuesta = "";
        $ instanciaMedico = new $();
        String myQuery
                = "SELECT E.ID_Empleado, "
                + "       CASE ID_Genero "
                + "          WHEN " + Consts.GENERO_FEMENINO + " "
                + "          THEN 'Dra. ' + E.Nombres + ' ' + E.Apellidos "
                + "          ELSE 'Dr. ' + E.Nombres + ' ' + E.Apellidos "
                + "       END AS Titulo_Nombre_Apellido "
                + "FROM Empleado E "
                + "   INNER JOIN Usuario U    ON E.ID_Empleado = U.ID_Empleado "
                + "   INNER JOIN Puesto P     ON E.ID_Puesto = P.ID_Puesto "
                + "   INNER JOIN Departamento D  ON P.ID_Departamento = D.ID_Departamento "
                + "WHERE E.Activo = 1 "
                + "  AND P.Activo = 1 "
                + "  AND D.Activo = 1 "
                + "  AND U.ID_Rol = " + Consts.ROL_FISIOTERAPEUTA + " "
                + "  AND D.ID_Centro = " + idCentro + ";";

        ResultSet resultadoListaMedicos = instanciaMedico.execQry(myQuery);
        while (resultadoListaMedicos.next()) {
            respuesta
                    += "<option value='" + resultadoListaMedicos.getString("ID_Empleado") + (idEmpleado.equals(resultadoListaMedicos.getString("ID_Empleado"))
                    ? "' selected>" : "'>")
                    + resultadoListaMedicos.getString("Titulo_Nombre_Apellido")
                    + "</option>";
        }

        instanciaMedico.desconectar();
        instanciaMedico = null;
        return respuesta;
    } // Fin del método elementoSELECTMedico().
    
    
    // Método llamado por: Empleado.java
    public String elementoSELECTProveedor(int idProveedor) throws ClassNotFoundException, SQLException {
        // Carga la lista de proveedores en un elemento SELECT-html para seleccionar
        // el proveedor de equipos o suministros médicos.
        String respuesta = "";
        $ instanciaProveedor = new $();
        String myQuery
                = "SELECT ID_Proveedor, NombreProveedor "
                + "FROM Proveedor "
                + "ORDER BY NombreProveedor;";

        ResultSet resultadoListaProveedor = instanciaProveedor.execQry(myQuery);
        while (resultadoListaProveedor.next()) {
            respuesta
                    += "<option value='" + resultadoListaProveedor.getInt("ID_Proveedor") + (idProveedor == resultadoListaProveedor.getInt("ID_Proveedor")
                    ? "' selected>" : "'>")
                    + resultadoListaProveedor.getString("NombreProveedor")
                    + "</option>";
        }

        instanciaProveedor.desconectar();
        instanciaProveedor = null;
        return respuesta;
    } // Fin del método elementoSELECTProveedor().
    
    
    // Método llamado por: Empleado.java
    public String elementoSELECTPuesto(int idCentro, int[] idDepartamento, int idPuesto) throws ClassNotFoundException, SQLException {
        // Carga la lista de puestos de trabajo en un elemento SELECT-html para
        // seleccionar el puesto de trabajo que el usuario requiera.
        String respuesta = "";
        $ instanciaPuesto = new $();
        String myQuery = null;

        if (idPuesto > 0) {
            myQuery
                    = "SELECT ID_Puesto, ID_Departamento, Nombre "
                    + "FROM Puesto "
                    + "WHERE Activo = 1 "
                    + "  AND ID_Departamento = ( "
                    + "SELECT ID_Departamento "
                    + "FROM Puesto "
                    + "WHERE ID_Puesto = " + idPuesto + ") "
                    + "ORDER BY Nombre;";
        } else if (idDepartamento[0] > 0) {
            myQuery
                    = "SELECT ID_Puesto, ID_Departamento, Nombre "
                    + "FROM Puesto "
                    + "WHERE Activo = 1 "
                    + "  AND ID_Departamento = " + idDepartamento[0] + " "
                    + "ORDER BY Nombre;";
        } else {
            myQuery
                    = "SELECT ID_Puesto, ID_Departamento, Nombre "
                    + "FROM Puesto "
                    + "WHERE Activo = 1 "
                    + "  AND ID_Departamento = ( "
                    + "SELECT TOP(1) ID_Departamento "
                    + "FROM Departamento "
                    + "WHERE ID_Centro = " + idCentro + " "
                    + "  AND Activo = 1 "
                    + "ORDER BY Nombre ) "
                    + "ORDER BY Nombre;";
        }

        ResultSet resultadoListaPuesto = instanciaPuesto.execQry(myQuery);
        while (resultadoListaPuesto.next()) {
            idDepartamento[0] = resultadoListaPuesto.getInt("ID_Departamento");
            respuesta
                    += "<option value='" + resultadoListaPuesto.getInt("ID_Puesto") + (idPuesto == resultadoListaPuesto.getInt("ID_Puesto")
                    ? "' selected>" : "'>")
                    + resultadoListaPuesto.getString("Nombre")
                    + "</option>";
        }

        instanciaPuesto.desconectar();
        instanciaPuesto = null;
        return respuesta;
    } // FIn del método elementoSELECTPuesto().
    
    
    // Método llamado por: Usuario.java
    public String elementoSELECTRol(int[] idRol) throws ClassNotFoundException, SQLException {
        // Carga la lista de roles en un elemento SELECT-html para seleccionar
        // el rol que el usuario requiera.
        String respuesta = "";
        $ instanciaUsuario = new $();
        String myQuery
                = "SELECT ID_Rol, Rol "
                + "FROM Rol "
                + "ORDER BY Rol;";

        ResultSet resultadoListaRoles = instanciaUsuario.execQry(myQuery);
        while (resultadoListaRoles.next()) {
            if(idRol[0] == 0)
                idRol[0] = resultadoListaRoles.getInt("ID_Rol");
            
            respuesta
                    += "<option value='" + resultadoListaRoles.getInt("ID_Rol") +
                    (idRol[0] == resultadoListaRoles.getInt("ID_Rol")
                    ? "' selected>" : "'>")
                    + resultadoListaRoles.getString("Rol")
                    + "</option>";
        }

        instanciaUsuario.desconectar();
        instanciaUsuario = null;
        return respuesta;
    } // Fin del método elementoSELECTRol().
    
    
    // Método llamado por: SuministroMedico.java
    public String elementoSELECTSerie(String serie) throws ClassNotFoundException, SQLException {
        // Carga la lista de series en un elemento SELECT-html para seleccionar
        // el la serie del suministro médico que se está recibiendo/modificando.
        String respuesta = "";
        $ instanciaSerie = new $();
        String myQuery
                = "SELECT Serie "
                + "FROM Catalogo_Suministro_Medico "
                + "ORDER BY Serie;";

        ResultSet resultadoListaSeries = instanciaSerie.execQry(myQuery);
        while (resultadoListaSeries.next()) {
            respuesta
                    += "<option value='" + resultadoListaSeries.getString("Serie") + (serie.equals(resultadoListaSeries.getString("Serie"))
                    ? "' selected>" : "'>")
                    + resultadoListaSeries.getString("Serie")
                    + "</option>";
        }

        instanciaSerie.desconectar();
        instanciaSerie = null;
        return respuesta;
    } // Fin del método elementoSELECTSerie().
    
    
    // Método llamado por:  Login.java.
    public boolean ingresar(
            String usuario, String pass, int[] idCentro, int[] idRol, int[] idUsuario, long[] idSesion) {
        $ instanciaBD = null;
        $ instanciaBD2 = null;
        $ instanciaBD3 = null;
        ResultSet resultado = null;
        PreparedStatement psUsuario = null;

        String myQuery
                = "SELECT ID_Usuario, ID_Centro, ID_Rol "
                + "FROM Usuario "
                + "WHERE Usuario = ? "
                + "  AND Contrasenia = ?;";

        try {
            // Verifica si existe el usuario con la contraseña proporcionada.
            instanciaBD = new $();
            psUsuario = instanciaBD.prepStatement(myQuery);
            psUsuario.setString(1, usuario);
            psUsuario.setString(2, pass);
            resultado = psUsuario.executeQuery();

            if (resultado.next()) {
                idUsuario[0] = resultado.getInt("ID_Usuario");
                idCentro[0] = resultado.getInt("ID_Centro");
                idRol[0] = resultado.getInt("ID_Rol");

                return true;
            } else {
                // Verifica si existen más usuarios en la BD, de no existir
                // se procede a crear uno ya que es el primer ingreso al sistema.
                myQuery = "SELECT COUNT(*) AS CANTIDAD_USUARIOS FROM Usuario;";
                instanciaBD2 = new $();
                resultado = instanciaBD2.execQry(myQuery);

                if (resultado.next()) {
                    if (resultado.getInt("CANTIDAD_USUARIOS") == 0) {
                        // Crea registro del primer centro de rehabilitación.
                        long fechaActual = fechaATimestamp();
                        instanciaBD3 = new $();
                        CallableStatement spiCentro
                                = instanciaBD3.storedProc(Consts.SPI_CENTRO, Consts.CANT_PARMS_SPI_CENTRO);

                        // Prepara la infomación requerida por el procedimento almacenado.
                        spiCentro.setLong(1, 0);
                        spiCentro.registerOutParameter(1, java.sql.Types.BIGINT);
                        spiCentro.setInt(2, 0);
                        spiCentro.registerOutParameter(2, java.sql.Types.SMALLINT);
                        spiCentro.setString(3, usuario);
                        spiCentro.setString(4, pass);
                        spiCentro.setInt(5, 0);
                        spiCentro.registerOutParameter(5, java.sql.Types.SMALLINT);
                        spiCentro.setLong(6, fechaActual);

                        ResultSet rsCentro = spiCentro.executeQuery();
                        rsCentro.next();
                        if (rsCentro.getInt(1) == Consts.SQL_OK) {
                            idUsuario[0] = spiCentro.getInt(5);
                            idCentro[0] = spiCentro.getInt(2);
                            idRol[0] = Consts.ROL_ADMINISTRADOR;
                            idSesion[0] = spiCentro.getInt(1);

                            return true;
                        }

                        ServiciosU.fb(rsCentro);
                        return false;
                    } else // Existen otros usuarios por lo que ya se ha superado
                    // la etapa de configuración inicial de la aplicación.
                    {
                        return false;
                    }
                } else // Ocurrió un error al intentar recuperar la cantidad de usuarios.
                {
                    return false;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (instanciaBD != null) {
                instanciaBD.desconectar();
            }

            if (instanciaBD2 != null) {
                instanciaBD2.desconectar();
            }

            if (instanciaBD3 != null) {
                instanciaBD3.desconectar();
            }

            instanciaBD = null;
            instanciaBD2 = null;
            instanciaBD3 = null;
            System.gc();
        }

        return false;
    } //  Fin del método ingresar().

    // Método llamado por:  EquipoMedico.java.
    public int obtenerCodigoCatalogo(String marca, String modelo) throws ClassNotFoundException, SQLException {
        $ instanciaBD = new $();
        String myQuery
                = "SELECT ID_Catalogo "
                + "FROM Catalogo_Equipo_Medico "
                + "WHERE Marca = ? "
                + "  AND Modelo = ?;";

        PreparedStatement psCatalogo = instanciaBD.prepStatement(myQuery);
        psCatalogo.setString(1, marca);
        psCatalogo.setString(2, modelo);
        ResultSet resultado = psCatalogo.executeQuery();
        if (resultado.next()) {
            return resultado.getInt("ID_Catalogo");
        }

        return 0;
    } // Fin del método obtenerCodigoCatalogo().
    
    
    // Método llamado por:  ServiciosC.java.
    public String obtenerSesionesAgendadas(int idCentro) throws ClassNotFoundException, SQLException {
        $ instanciaOpciones = new $();
        $ instanciaEventos = new $();
        String respuesta ="";
        // Recupera los parámetros de hora de inicio y final para desplegar la agenda.
        String myQuery =
                "SELECT HoraInicioLunes, HoraInicioMartes, HoraInicioMiercoles, " +
                "       HoraInicioJueves, HoraInicioViernes, HoraInicioSabado, " +
                "       HoraInicioDomingo, " +
                "       HoraFinalLunes, HoraFinalMartes, HoraFinalMiercoles, " +
                "       HoraFinalJueves, HoraFinalViernes, HoraFinalSabado, " +
                "       HoraFinalDomingo " +
                "FROM ParametrosBD " +
                "WHERE ID_Centro = " + idCentro + ";";
        
        ResultSet resultadoOpciones = instanciaOpciones.execQry(myQuery);
        if(resultadoOpciones.next()){
            int horaInicio = 999;
            int horaFinal = 0;
            
            for (int i = 1; i <= 14; i++) {
                if( resultadoOpciones.getInt(i) < horaInicio && i <= 7)
                    horaInicio = resultadoOpciones.getInt(i) / 100;
                
                if(resultadoOpciones.getInt(i) > horaFinal && i > 7)
                    horaFinal = resultadoOpciones.getInt(i) / 100;
            }
            
            respuesta = "\"options\" : { " +
                                    "\"businessHours\": {" +
                                    "        \"start\": " + horaInicio + ", " +
                                    "        \"end\": " + horaFinal + ", " +
                                    "        \"limitDisplay\": true } },";
        } else
            respuesta = "\"options\" : { " +
                                    "\"businessHours\": {" +
                                    "        \"start\": 1, \"end\": 24, " +
                                    "        \"limitDisplay\": true } },";
        
        
        // *** Busca todas sesiones de los planes de rehabilitación activos.
        // Recupera las fechas conforme a la parametrización realizada al
        // calendario para llenarlo de solamente los elementos necesarios.
        LocalDate today = LocalDate.now();
        DayOfWeek thisDay = today.getDayOfWeek();
        int fechaMinima = Integer.parseInt(String.valueOf(
                            today.with(TemporalAdjusters.previousOrSame(thisDay.minus(1))))
                            .replace("-", ""));
        int fechaMaxima = Integer.parseInt(String.valueOf(
                            today.with(TemporalAdjusters.nextOrSame(thisDay.plus(6))))
                            .replace("-", ""));
        myQuery =
                "SELECT PR.ID_PlanRehabilitacion, P.ID_Paciente AS Expediente, " +
                "       P.Nombres AS NombrePaciente, P.Apellidos AS ApellidoPaciente, " +
                "       P.Telefono, E.Nombres AS NombreEmpleado, E.Apellidos AS ApellidoEmpleado, " +
                "       Fecha, HoraInicio, HoraFinal " +
                "FROM Plan_Rehabilitacion PR " +
                "     INNER JOIN Cita_Plan_Rehabilitacion C " +
                "        ON PR.ID_PlanRehabilitacion = C.ID_PlanRehabilitacion " +
                "     INNER JOIN Empleado E " +
                "        ON PR.ID_Empleado = E.ID_Empleado " +
                "     INNER JOIN Paciente P " +
                "        ON PR.ID_Paciente = P.ID_Paciente " +
                "WHERE PR.Activo = 1 " +
                "  AND Fecha BETWEEN " + fechaMinima + " AND " + fechaMaxima + ";";
        
        ResultSet resultadoEventos = instanciaEventos.execQry(myQuery);
        if(resultadoEventos.next()){
            int idEvento = 1;
            respuesta += "\"events\": [";
            
            do{
                int ausencias = obtenerAsistenciaSesiones(
                                        resultadoEventos.getInt("ID_PlanRehabilitacion"));
                
                respuesta +=
                    "{" +
                    "  \"id\": " + idEvento++ + ", " +
                    "  \"paciente\": \"" + ServiciosU.obtenerNombreCompleto(
                                        resultadoEventos.getString("NombrePaciente"),
                                        resultadoEventos.getString("ApellidoPaciente")) + "\", " +
                    "  \"terapeuta\": \"" + ServiciosU.obtenerNombreCompleto(
                                        resultadoEventos.getString("NombreEmpleado"),
                                        resultadoEventos.getString("ApellidoEmpleado")) + "\", " +
                    "  \"expediente\": \"" + resultadoEventos.getString("Expediente") + "\", " +
                    "  \"telefono\": \"" +
                                        resultadoEventos.getString("Telefono") + "\", " +
                    "  \"ausencias\": " + ausencias + ", " +
                    "  \"color\": \"" + (ausencias == 0 ? "#477998":
                                         ausencias == 1 ? "#261C15": "#2D2D2A") + "\", " +
                    "  \"start\": \"" + ServiciosU.obtenerFechaEvento(
                                        resultadoEventos.getInt("Fecha"),
                                        resultadoEventos.getInt("HoraInicio") ) + "\", " +
                    "  \"end\": \"" + ServiciosU.obtenerFechaEvento(
                                        resultadoEventos.getInt("Fecha"),
                                        resultadoEventos.getInt("HoraFinal") ) + "\" " +
                    "},";
            } while(resultadoEventos.next());
            
            respuesta = respuesta.substring(0, respuesta.lastIndexOf(",")) + "]";
        } else
            respuesta = respuesta.substring(0, respuesta.lastIndexOf(","));
            
        return respuesta;
    } // Fin del método obtenerEventosWeb().
    
    
    // Método llamado por:  ServiciosC.java.
    public String obtenerExistenciaMedicamento(String serie) throws ClassNotFoundException, SQLException {
        $ instanciaBD = new $();
        String respuesta = "";
        String myQuery =
                "SELECT C.Serie, Nombre, Lote, FechaVencimiento, " +
                "CantidadExistencial - UnidadesCongeladas AS CantidadDisponibleCatalogo, " +
                "CantidadIngresada - CantidadDevuelta AS CantidadDisponibleLote " +
                "FROM Catalogo_Medicamento C " +
                "     INNER JOIN Inventario_Medicamento I " +
                "        ON C.Serie = I.Serie " +
                "WHERE C.Serie = ? " +
                "  AND (CantidadIngresada - CantidadDevuelta) > 0 " +
                "ORDER BY FechaVencimiento;";
                
        PreparedStatement psDetalleMedicamento = instanciaBD.prepStatement(myQuery);
        psDetalleMedicamento.setString(1, serie);
        ResultSet resultado = psDetalleMedicamento.executeQuery();
        
        if (resultado.next()) {
            String informacionLotes = "";
            String elementoSELECTLotes = "";
            
            respuesta =
                    "{ \"error\": false, " +
                    "  \"mensaje\": \"\", " +
                    "  \"serie\": \"" + resultado.getString("Serie") + "\", " +
                    "  \"nombre\": \"" + resultado.getString("Nombre") + "\", " +
                    "  \"cantidadDisponibleCatalogo\": \"" + resultado.getInt("CantidadDisponibleCatalogo") + "\", " +
                    "  \"selectLotes\": \"$#elementoSelectLotes#$\", " +
                    "  \"lotes\": [ $#informacionLotes#$ ] } ";
            
            do{
                informacionLotes +=
                        "{" +
                        "  \"codigo\": \"" + resultado.getString("Lote") + "\", " +
                        "  \"lote\": \"" + resultado.getString("Lote") + "\", " +
                        "  \"fechaVencimiento\": \"Vencimiento: <span id='sv" + resultado.getString("Lote") + "'>" + ServiciosU.formatearFecha(resultado.getInt("FechaVencimiento")) + "</span>\", " +
                        "  \"cantidadDisponibleLote\": \"" + resultado.getInt("CantidadDisponibleLote") + "\" }, ";
                
                elementoSELECTLotes +=
                        "<option value='" + resultado.getString("Lote") + "'>" +
                            resultado.getString("Lote") + 
                        "</option>";
            } while(resultado.next());
            
            informacionLotes = informacionLotes.substring(0, informacionLotes.lastIndexOf(","));
            return respuesta
                        .replace("$#elementoSelectLotes#$", elementoSELECTLotes)
                        .replace("$#informacionLotes#$", informacionLotes);
        } else {
            return "{ \"error\": true, " +
                   "  \"mensaje\": \"No existen planes activos para el paciente.\" } ";
        }
    } // Fin del método obtenerExistenciaMedicamento().
    
    
    // Método llamado por:  ServiciosC.java.
    public String obtenerPlanRehabilitacionL1(String idPaciente) throws ClassNotFoundException, SQLException {
        // Resultado devuelto:
        //      ID_PlanRehabilitacion, CantidadSesiones, Expediente, NombreCompleto,
        //      Asistencias, Fecha inicial y final del plan de rehabilitación.
        $ instanciaBD1 = new $();
        $ instanciaBD2 = new $();
        int numeroPlanRehabilitacion = 0;
        String myQuery =
                "SELECT TOP(1) ID_PlanRehabilitacion " +
                "FROM Plan_Rehabilitacion " +
                "WHERE ID_Paciente = ? " +
                "  AND Activo = 1;";
                
        PreparedStatement psNumeroPlanRehabilitacion = instanciaBD1.prepStatement(myQuery);
        psNumeroPlanRehabilitacion.setString(1, idPaciente);
        ResultSet resultado = psNumeroPlanRehabilitacion.executeQuery();
        if (resultado.next()) {
            numeroPlanRehabilitacion = resultado.getInt("ID_PlanRehabilitacion");
        } else {
            return "{ \"error\": true, " +
                   "  \"mensaje\": \"No existen planes activos para el paciente.\" } ";
        }
                
                
        myQuery =
                "WITH Plan_Info_CTE( PlanRehabilitacion, SesionesProgramadas, " +
                "                    Expediente, NombreCompleto )  AS( " +
                "         SELECT ID_PlanRehabilitacion, CantidadSesiones, P.ID_Paciente, " +
                "                Nombres + ' ' + Apellidos " +
                "         FROM Plan_Rehabilitacion PR INNER JOIN Paciente P " +
                "              ON PR.ID_Paciente = P.ID_Paciente " +
                "         WHERE P.ID_Paciente = ? ), " +
                "     Asistencia_CTE( Asistencias ) AS ( " +
                "         SELECT COUNT(*) AS Asistencias " +
                "         FROM Cita_Plan_Rehabilitacion " +
                "         WHERE ID_PlanRehabilitacion = " + numeroPlanRehabilitacion + " " +
                "           AND Asistio IN ( '1', '2' ) ), " +
                "     Inicio_Fin_CTE( PlanRehabilitacion, FechaInicio, FechaFinal ) AS( " +
                "         SELECT " + numeroPlanRehabilitacion + ", MIN(C.Fecha), MAX(C.Fecha) " +
                "         FROM Cita_Plan_Rehabilitacion C " +
                "         WHERE ID_PlanRehabilitacion = " + numeroPlanRehabilitacion + " ) " +
                "SELECT P.*, A.Asistencias, I.FechaInicio, I.FechaFinal " +
                "FROM Plan_Info_CTE P INNER JOIN Inicio_Fin_CTE I " +
                "     ON P.PlanRehabilitacion = I.PlanRehabilitacion" +
                "     CROSS JOIN Asistencia_CTE A;";

        PreparedStatement psInfoPlanRehabilitacion = instanciaBD2.prepStatement(myQuery);
        psInfoPlanRehabilitacion.setString(1, idPaciente);
        resultado = psInfoPlanRehabilitacion.executeQuery();
        
        if (resultado.next()) {
            return "{ \"error\": false, " +
                   "  \"mensaje\": \"\", " +
                   "  \"expediente\": \"" + resultado.getString("Expediente") + "\", " +
                   "  \"nombrePaciente\": \"Nombre: " + resultado.getString("NombreCompleto") + "\", " +
                   "  \"numeroPlan\": " + resultado.getInt("PlanRehabilitacion") + ", " +
                   "  \"asistencia\": \"Asistencias/sesiones: " + resultado.getInt("Asistencias") + 
                                      "/" + resultado.getString("SesionesProgramadas") + "\", " +
                   "  \"fechas\": \"Inicio: " +
                            ServiciosU.formatearFecha(resultado.getInt("PlanRehabilitacion")) +
                                      "  -  Finalización: " +
                            ServiciosU.formatearFecha(resultado.getInt("PlanRehabilitacion")) + "\" }";
        } else {
            return "{ \"error\": true, " +
                   "  \"mensaje\": \"No existen ausencias que justifiquen la creación de la cita médica.\" } ";
        }
    } // Fin del método obtenerPlanRehabilitacionL1().
    
    
    // Método llamado por:  ServiciosC.java.
    public String obtenerPlanRehabilitacionL2(int idPlanRehabilitacion) throws ClassNotFoundException, SQLException {
        // Resultado devuelto:
        //      ID_PlanRehabilitacion, CantidadSesiones, Expediente, NombreCompleto
        //      FechaNacimiento (años), Fecha inicial y final del plan de rehabilitación.
        $ instanciaBD = new $();
        String myQuery =
                "WITH Fechas_CTE " +
                "      (ID_PlanRehabilitacion, FechaInicial, FechaFinal) " +
                "AS (  SELECT " + idPlanRehabilitacion + ", MIN(Fecha), MAX(Fecha) " +
                "      FROM Cita_Plan_Rehabilitacion " +
                "      WHERE ID_PlanRehabilitacion = " + idPlanRehabilitacion + " ) " +
                "SELECT PR.ID_PlanRehabilitacion, CantidadSesiones, P.ID_Paciente, " +
                "       Nombres + ' ' + Apellidos AS NombreCompleto, FechaNacimiento, " +
                "       FechaInicial, FechaFinal " +
                "FROM Plan_Rehabilitacion PR " +
                "      INNER JOIN Paciente P " +
                "         ON PR.ID_Paciente = P.ID_Paciente " +
                "      INNER JOIN Fechas_CTE F " +
                "         ON PR.ID_PlanRehabilitacion = F.ID_PlanRehabilitacion;";

        ResultSet resultado = instanciaBD.execQry(myQuery);
        
        int anioActual = Calendar.getInstance().get(Calendar.YEAR);
        if (resultado.next())
            return "{ \"error\": false, " +
                   "  \"mensaje\": \"\", " +
                   "  \"numeroPlan\": " + resultado.getInt("ID_PlanRehabilitacion") + ", " +
                   "  \"expediente\": \"" + resultado.getString("ID_Paciente") + "\", " +
                   "  \"nombrePaciente\": \"Nombre: " + resultado.getString("NombreCompleto") + "\", " +
                   "  \"cantidadSesiones\": " + resultado.getInt("CantidadSesiones") + ", " +
                   "  \"edad\": " + (anioActual - resultado.getInt("FechaNacimiento") / 10000) + ", " +
                   "  \"fechaInicial\": \"" + ServiciosU.formatearFecha(
                                resultado.getInt("FechaInicial")) + "\", " +
                   "  \"fechaFinal\": \"" + ServiciosU.formatearFecha(
                                resultado.getInt("FechaFinal")) + "\" }";
        else
            return "{ \"error\": true, " +
                   "  \"mensaje\": \"Ocurrió un error al intentar cargar la información.\" } ";
    } // Fin del método obtenerPlanRehabilitacionL2().

    
    // Método llamado por:  ServiciosC.java.
    public String obtenerPlanRehabilitacionL3(
            String idPaciente, int fecha, int horaInicio, int horaFinal)
            throws ClassNotFoundException, SQLException {
        // Recupera el plan de rehabilitación al que pertenece la cita indicada.
        $ instanciaBD = new $();
        int idPlanRehabilitacion = 0;
        String myQuery =
                "SELECT P.ID_PlanRehabilitacion " +
                "FROM Plan_Rehabilitacion P " +
                "     INNER JOIN Cita_Plan_Rehabilitacion C " +
                "        ON P.ID_PlanRehabilitacion = C.ID_PlanRehabilitacion " +
                "WHERE ID_Paciente = '" + idPaciente + "' " +
                "  AND Fecha = " + fecha + " " +
                "  AND HoraInicio = " + horaInicio + " " +
                "  AND HoraFinal = " + horaFinal + ";";
                
        ResultSet resultadoCitaMedica = instanciaBD.execQry(myQuery);
        if(resultadoCitaMedica.next())
            idPlanRehabilitacion = resultadoCitaMedica.getInt("ID_PlanRehabilitacion");
        else
            return "{ \"error\": true, " +
               "  \"mensaje\": \"Ocurrió un error al intentar cargar la información.\" } ";
        
        
        // Resultado devuelto:
        //      ID_PlanRehabilitacion, Fecha de la sesión así como hora inicial y final.
        //      Asistecnias, CantidadSesiones, Expediente, NombreCompleto
        //      FechaNacimiento (años), Fecha inicial y final del plan de rehabilitación.
        myQuery =
                "WITH Fechas_CTE (ID_PlanRehabilitacion, FechaInicial, FechaFinal) AS( " +
                "      SELECT " + idPlanRehabilitacion + ", MIN(Fecha), MAX(Fecha) " +
                "      FROM Cita_Plan_Rehabilitacion " +
                "      WHERE ID_PlanRehabilitacion = " + idPlanRehabilitacion + " ), " +
                "Asistencia_CTE( Asistencias ) AS ( " +
                "         SELECT COUNT(*) AS Asistencias " +
                "         FROM Cita_Plan_Rehabilitacion  " +
                "         WHERE ID_PlanRehabilitacion = " + idPlanRehabilitacion + " " +
                "           AND Asistio IN ( '2' ) ) " +
                "SELECT PR.ID_PlanRehabilitacion, A.Asistencias, CantidadSesiones, " +
                "       P.ID_Paciente, Nombres + ' ' + Apellidos AS NombreCompleto, " +
                "       FechaNacimiento, FechaInicial, FechaFinal " +
                "FROM Plan_Rehabilitacion PR " +
                "      INNER JOIN Paciente P " +
                "         ON PR.ID_Paciente = P.ID_Paciente " +
                "      INNER JOIN Fechas_CTE F " +
                "         ON PR.ID_PlanRehabilitacion = F.ID_PlanRehabilitacion " +
                "      CROSS JOIN Asistencia_CTE A;";

        ResultSet resultado = instanciaBD.execQry(myQuery);
        
        int anioActual = Calendar.getInstance().get(Calendar.YEAR);
        if (resultado.next())
            return "{ \"error\": false, " +
                   "  \"mensaje\": \"\", " +
                   "  \"numeroPlan\": " + resultado.getInt("ID_PlanRehabilitacion") + ", " +
                   "  \"expediente\": \"" + resultado.getString("ID_Paciente") + "\", " +
                   "  \"nombrePaciente\": \"" + resultado.getString("NombreCompleto") + "\", " +
                   "  \"asistencias\": " + resultado.getInt("Asistencias") + ", " +
                   "  \"cantidadSesiones\": " + resultado.getInt("CantidadSesiones") + ", " +
                   "  \"edad\": " + (anioActual - resultado.getInt("FechaNacimiento") / 10000) + ", " +
                    "  \"fechaSesion\": \"" + ServiciosU.formatearFecha(fecha) + "\", " +
                    "  \"horaInicio\": \"" + ServiciosU.formatearHora(horaInicio) + "\", " +
                    "  \"horaFinal\": \"" + ServiciosU.formatearHora(horaFinal) + "\", " +
                   "  \"fechaInicial\": \"" + ServiciosU.formatearFecha(
                                resultado.getInt("FechaInicial")) + "\", " +
                   "  \"fechaFinal\": \"" + ServiciosU.formatearFecha(
                                resultado.getInt("FechaFinal")) + "\" }";
        else
            return "{ \"error\": true, " +
                   "  \"mensaje\": \"Ocurrió un error al intentar cargar la información.\" } ";
    } // Fin del método obtenerPlanRehabilitacionL2().

    
    // Método llamado por: Medicamento.java.
    public String obtenerRetroalimentacionOperador(int idUsuario, String serie, String lote) {
        $ instanciaAprobaciones = null;
        $ instanciaGestion = null;
            
        
        try{
            instanciaAprobaciones = new $();
            instanciaGestion = new $();
            long fechaAprobacion1 = 0;
            long fechaAprobacion2 = 0;
            int contador = 1;
            String respuesta = "";
            String myQuery
                    = "SELECT FechaAccion, MensajeMostrado, "
                    + "       NotaAccion, "
                    + "       CASE WHEN CantidadDevuelta > 0 THEN 0 "
                    + "            ELSE 1 END AS RECHAZADO "
                    + "FROM Bitacora_DevolucionMedicamento "
                    + "WHERE Serie = ? "
                    + "  AND Lote = ? "
                    + "  AND CodigoRegistro = 'PO' "
                    + "ORDER BY FechaAccion DESC;";
            
            // Recupera la información generada durante el proceso de aprobación,
            // esto es si se rechazó, el momento y el mensaje de retroalimentación
            // para el usuario operador.
            PreparedStatement psAprobaciones = instanciaAprobaciones.prepStatement(myQuery);
            psAprobaciones.setString(1, serie);
            psAprobaciones.setString(2, lote);
            
            ResultSet resultado = psAprobaciones.executeQuery();
            while (resultado.next()) {
                if(contador == 1 && !resultado.getBoolean("RECHAZADO")){
                    // Se lee el registro más reciente de la bitácora, si fue aprobado
                    // no es necesario indicárselo al usuario operador para no saturarlo de alertas.
                    return respuesta;
                
                } else if(contador == 1 && resultado.getBoolean("MensajeMostrado")){
                    // Si se rechazó y ya se mostró el mensaje al usuario no se
                    // muestra de nuevo de forma automática.
                    return respuesta;
                } else if(contador == 1){
                    // Debido a que se rechazó y no se ha mostrado, se guarda
                    // el mensaje para comprobar si el usuario que está accediendo
                    // a la pantalla es el mismo que solicitó la devolución de medicamento.
                    fechaAprobacion1 = resultado.getLong("FechaAccion");
                    respuesta = resultado.getString("NotaAccion");
                } else {
                    fechaAprobacion2 = resultado.getLong("FechaAccion");
                    break;
                }

                contador++;
            }


            myQuery = 
                    "SELECT CASE WHEN CantidadDevuelta > 0 THEN 0" +
                    "       ELSE 1 END AS RECHAZADO, " +
                    "       MensajeMostrado, UsuarioModifico " +
                    "FROM Bitacora_DevolucionMedicamento " +
                    "WHERE Serie = ? " +
                    "   AND Lote = ? " +
                    "   AND FechaAccion BETWEEN " + fechaAprobacion2 + " AND " + fechaAprobacion1 +
                    "   AND CodigoRegistro = 'PR'" +
                    "ORDER BY FechaAccion DESC;";
            PreparedStatement psGestion = instanciaGestion.prepStatement(myQuery);
            psGestion.setString(1, serie);
            psGestion.setString(2, lote);

            // Determina si el usuario que está accediento a la pantalla de
            // devolución de medicamento es el mismo que realizó la gestión que
            // fue rechazada para mostrarle mensaje de retroalimentación correspondiente.
            resultado = psGestion.executeQuery();
            while (resultado.next()) {
                if(idUsuario == resultado.getInt("UsuarioModifico"))
                    return respuesta;
                break;
            }

            return "";
        } catch(Exception ex){
            ex.printStackTrace();
            return "";
        } finally{
            if(instanciaAprobaciones != null)
                instanciaAprobaciones.desconectar();
            
            if(instanciaGestion != null)
                instanciaGestion.desconectar();
                
            instanciaAprobaciones = null;
            instanciaGestion = null;
            System.gc();
        }
    } // Fin del método obtenerRetroalimentacionOperador().

    
    // Método llamado por: ServiciosM.java.
    public int obtenerAsistenciaSesiones(int idPlanRehabilitacion) throws ClassNotFoundException, SQLException{
        String[] registro = {"", "", "", "", ""};
        return obtenerAsistenciaSesiones(idPlanRehabilitacion, registro);
    } // Fin del método obtenerAsistenciaSesiones();

    
    // Método llamado por: ServiciosM.java.
    public int obtenerAsistenciaSesiones(int idPlanRehabilitacion, String[] registro)
            throws ClassNotFoundException, SQLException {
        $ instanciaAsistencias = new $();
        List<Integer> registroAsistencias = new ArrayList();
        
        String myQuery =
                "SELECT TOP(5) Asistio FROM Cita_Plan_Rehabilitacion " +
                "WHERE ID_PlanRehabilitacion = " + idPlanRehabilitacion + " " +
                "  AND FechaModificacion > 0 " +
                "ORDER BY Fecha DESC, HoraInicio DESC, HoraFinal;";

        // Recupera la información de las últimas cinco sesiones, esto debido
        // a que por políticas de la institución a la tercera ausencia se
        // cancela el plan de rehabilitación.
        ResultSet resultadoAsistencias = instanciaAsistencias.execQry(myQuery);
        while (resultadoAsistencias.next()) {
            registroAsistencias.add(resultadoAsistencias.getInt("Asistio"));
        }
        
        int contadorInasistencias = 0;
        int indiceRegistroAsistencias = 0;
        System.out.println(registro.length);
        int conteoRequerido = registroAsistencias.size() < 3 ? registroAsistencias.size() : 3 ;
        for(int elementosRestantes = registroAsistencias.size();
                elementosRestantes > 0 ; elementosRestantes--){
            int i = elementosRestantes - 1;
            
            if(elementosRestantes <= conteoRequerido){
                if(registroAsistencias.get(i) == 0)
                    contadorInasistencias++;
                else
                    contadorInasistencias = 0;
            }
            
            registro[i] += registroAsistencias.get(indiceRegistroAsistencias++);
            if(indiceRegistroAsistencias == registroAsistencias.size())
                break;
        }
        System.out.println("***************************************************");
        System.out.println(registro);
        
        
        return contadorInasistencias;
    } // Fin del método obtenerUltimasSesiones().
    
    
    // Método llamado por:  ServiciosC.java.
    public String obtenerLogo() throws ClassNotFoundException, SQLException {
        $ instanciaBD = new $();
        int idCentro = 1;
        String myQuery
                = "SELECT Logo "
                + "FROM Centro "
                + "WHERE ID_Centro = " + idCentro + ";";

        ResultSet resultado = instanciaBD.execQry(myQuery);
        if (resultado.next()) {
            return File.separator + Consts.CARPETA_APP +
                   File.separator +  Consts.SUBCARPETA_APP +
                                     String.format("%05d", idCentro) +
                   File.separator + Consts.CARPETA_CENTRO +
                   File.separator + resultado.getString("Logo");
        } else 
            return "";
    } // Fin del método obtenerCodigoCatalogo().
    
    
    // Método llamado por:  ServiciosC.java.
    public void registrarAccion(
            long idSesion, String paginaWeb, String idRegistro, String accion) {
        long fechaActual = fechaATimestamp();
        $ instanciaBD = null;
        PreparedStatement psAccionesUsuario = null;
        String myQuery
                = "INSERT INTO Acciones_Usuario "
                + "VALUES(?, ?, ?, ?, ?);";

        try {
            paginaWeb = paginaWeb.split("\\?")[0];
            instanciaBD = new $();

            String[] codigos;
            if (idRegistro.contains(Consts.SEPARADOR)) {
                codigos = idRegistro.split(Consts.SEPARADOR_REGEX);
            } else {
                codigos = new String[1];
                codigos[0] = idRegistro;
            }

            for (int i = 0; i < codigos.length; i++) {
                psAccionesUsuario = instanciaBD.prepStatement(myQuery);
                psAccionesUsuario.setLong(1, idSesion);
                psAccionesUsuario.setString(2, codigos[i]);
                psAccionesUsuario.setLong(3, fechaActual);
                psAccionesUsuario.setString(4, paginaWeb);
                psAccionesUsuario.setString(5, accion);
                psAccionesUsuario.executeUpdate();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (instanciaBD != null) {
                instanciaBD.desconectar();
            }

            instanciaBD = null;
            System.gc();
        }
    } //  Fin del método registrarAccion().

    // Método llamado por:  ServiciosC.java.
    public static boolean tieneAcceso(int idCentro, int idUsuario, String paginaWeb, boolean[] accesos) {
        $ instanciaBD = null;
        ResultSet resultado = null;
        PreparedStatement ps = null;
        String myQuery
                = "SELECT U.Activo AS PAGINA_PERMITIDA, "
                + "       AU.AccesoVer, AU.AccesoEditar "
                + "FROM Acceso_Usuario AU "
                + "  INNER JOIN Pagina_Web P "
                + "          ON P.ID_Pagina = AU.ID_Pagina "
                + "  INNER JOIN Usuario U "
                + "          ON AU.ID_Usuario = U.ID_Usuario "
                + "WHERE P.Pagina = ? "
                + "  AND AU.ID_Usuario = " + idUsuario + ";"; // XXXX??? Vista
        
        
        // Predefinido accesos de ver y editar por si está en configuraón de app.
        accesos[0] = true;
        accesos[1] = true;

        try {
            instanciaBD = new $();
            paginaWeb = paginaWeb.split("\\?")[0].split("\\#")[0];
            ps = instanciaBD.prepStatement(myQuery);
            ps.setString(1, paginaWeb);
            resultado = ps.executeQuery();

            if (resultado.next()) {
                if (resultado.getBoolean("PAGINA_PERMITIDA")) {
                    // tiene acceso a la página web.
                    accesos[0] = resultado.getBoolean("AccesoVer");
                    accesos[1] = resultado.getBoolean("AccesoEditar");
                    return true;
                }
            } else {
                // sino tiene acceso verifica si está configurando la aplicación
                switch (ParametrosBD.getConfiguracionSistema(idCentro)) {
                    case Consts.SIN_CONFIGURACION:
                        if (paginaWeb.equals(Consts.CENTRO_HTML)) {
                            return true;
                        }
                        break;
                    case Consts.CENTRO_CONFIGURADO:
                        if (paginaWeb.equals(Consts.DEPARTAMENTO_HTML)) {
                            return true;
                        }
                        break;
                    case Consts.DEPARTAMENTO_CONFIGURADO:
                        if (paginaWeb.equals(Consts.PUESTO_HTML)) {
                            return true;
                        }
                        break;
                    case Consts.PUESTO_CONFIGURADO:
                        if (paginaWeb.equals(Consts.EMPLEADO_HTML)) {
                            return true;
                        }
                        break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (instanciaBD != null) {
                instanciaBD.desconectar();
            }

            instanciaBD = null;
            System.gc();
        }

        return false;
    } //  Fin del método tieneAcceso().
} // Fin de la clase ServiciosM.
