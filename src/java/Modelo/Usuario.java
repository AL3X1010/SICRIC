/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Utilitarios.Consts;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Moises Snow
 */
public class Usuario {// Constantes de la clase Usuario.

    private final String PAGINA_WEB = Consts.USUARIO_HTML;
    private final String TITULO = Consts.TITULO_LISTA_USUARIOS;
    private final String ETIQUETA_EXISTENTE = Consts.ETIQUETA_EXISTENTE_USUARIO;
    private final String ETIQUETA_NUEVO = Consts.ETIQUETA_NUEVO_USUARIO;

    // Variables de la clase Usuario.
    private int idUsuario;
    private int idRol;
    private String rol;
    private String usuario;
    private boolean activo;
    private int usuarioModifico;
    private long fechaModificacion;
    private long fechaUltimoIngreso;
    private String selectRol;
    private String mapaAccesos;

    // Método constructor de la clase Usuario.
    public Usuario() {
        this.idUsuario = 0;
        this.idRol = 0;
        this.rol = "";
        this.usuario = "";
        this.activo = true;
        this.usuarioModifico = 0;
        this.fechaModificacion = 0;
        this.fechaUltimoIngreso = 0;
        this.selectRol = "";
        this.mapaAccesos = "";
    } // Fin del método constructor.

    // Metodos GET y SET de la clase.
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getUsuarioModifico() {
        return usuarioModifico;
    }

    public void setUsuarioModifico(int usuarioModifico) {
        this.usuarioModifico = usuarioModifico;
    }

    public long getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(long fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public long getFechaUltimoIngreso() {
        return fechaUltimoIngreso;
    }

    public void setFechaUltimoIngreso(long fechaUltimoIngreso) {
        this.fechaUltimoIngreso = fechaUltimoIngreso;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getSelectRol() {
        return selectRol;
    }

    public void setSelectRol(String selectRol) {
        this.selectRol = selectRol;
    }

    public String getMapaAccesos() {
        return mapaAccesos;
    }

    public void setMapaAccesos(String mapaAccesos) {
        this.mapaAccesos = mapaAccesos;
    }

    // Métodos utilitarios de la clase.
    // Carga la lista de todos los usuarios del sistema, activos e inactivos.
    public String cargarLista(int idCentro, int idRol) {
        $ instanciaListaUsuarios = null;
        String respuesta = "";
        // Define la estructura HTML de respuesta.
        final String ESTRUCTURA_ERROR
                = "  \"elementosHTML\": \""
                + "            <h5 class='center'>$#titulo#$"
                + "              <a href='$#paginaWeb#$' class='$#etiquetaNuevo#$'>"
                + "                <i class='material-icons right'>add_circle_outline</i>"
                + "              </a>"
                + "            </h5><hr/>"
                + "            <div class='col s12 center'>"
                + "              <p> *** No se puede mostrar la información solicitada. ***</p>"
                + "            </div>\"";

        try {
            // Realiza consulta de usuarios a la base de datos.
            instanciaListaUsuarios = new $();
            ResultSet resultado = instanciaListaUsuarios.execQry(
                    "WITH LogBD_CTE AS ( "
                    + "   SELECT ID_Usuario, FechaIngreso, "
                    + "          ROW_NUMBER() OVER ( "
                    + "              PARTITION BY ID_Usuario "
                    + "              ORDER BY FechaIngreso DESC ) AS NumeroFila "
                    + "   FROM LogBD ) "
                    + "SELECT U.ID_Usuario, U.Usuario, R.Rol, L.FechaIngreso, U.Activo, "
                    + "       U.UsuarioModifico, U.FechaModificacion "
                    + "FROM LogBD_CTE L "
                    + "     INNER JOIN Usuario U "
                    + "        ON L.ID_Usuario = U.ID_Usuario "
                    + "     INNER JOIN Rol R "
                    + "        ON U.ID_Rol = R.ID_Rol "
                    + "WHERE L.NumeroFila = 1 "
                    + "  AND U.ID_Centro = " + idCentro + ";");

            if (resultado.next()) {
                respuesta
                        = // Retroalimentación al usuario del proceso.
                        "{ \"error\" : false, "
                        + "  \"mensaje\" : \"\", "
                        + // Información recuperada de la tabla Usuario.
                        "  \"elementosHTML\": \""
                        + "            <h5 class='center'>" + TITULO
                        + "              <a href='" + PAGINA_WEB + "' class='" + ETIQUETA_NUEVO + "'>"
                        + "                <i class='material-icons right'>add_circle_outline</i>"
                        + "              </a>"
                        + "            </h5><hr/>"
                        + "            <table id='myTable2'>"
                        + "              <thead>"
                        + "                <tr>"
                        + "                  <th onclick='sortTable(0)' style='cursor:pointer' id='usuarioCol'>Usuario<i></th>"
                        + "                  <th onclick='sortTable(1)' style='cursor:pointer' id='rolCol'>Rol</th>"
                        + "                  <th>ÚltimoIngreso</th>"
                        + "                  <th onclick='sortTable(3)' style='cursor:pointer' id='activoCol' class='center'>Activo</th>"
                        + "                  <th class='center'>Ver</th>"
                        + "                  <th class='center'>Editar</th>"
                        + "                </tr>"
                        + "              </thead>"
                        + "            <tbody>";

                do {
                    // Si existen resultados crea arreglo JSON para mostrar la lista de usuarios.
                    this.setIdUsuario(resultado.getInt("ID_Usuario"));
                    this.setUsuario(resultado.getString("Usuario"));
                    this.setRol(resultado.getString("Rol"));
                    this.setFechaUltimoIngreso(resultado.getLong("FechaIngreso"));
                    this.setActivo(resultado.getBoolean("Activo"));
                    this.setUsuarioModifico(resultado.getInt("UsuarioModifico"));
                    this.setFechaModificacion(resultado.getLong("FechaModificacion"));

                    respuesta += this.toHTML();
                } while (resultado.next());

                // Completa la estructura del objeto JSON de respuesta.
                respuesta = respuesta + "</tbody></table>\"}";
            } else {
                respuesta = "[ { \"error\" : false, "
                        + "    \"mensaje\" : \"No existen registros para mostrar.\" } ]";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            respuesta = "[ { \"error\" : true, "
                    + "    \"mensaje\" : \"Ocurrió un error al intentar cargar los registros.\" } ]";
        } finally {
            if (instanciaListaUsuarios != null) {
                instanciaListaUsuarios.desconectar();
            }

            instanciaListaUsuarios = null;
            System.gc();
        }

        return respuesta;
    } // Fin del método cargarLista().

    public void cargarRegistro(int idUsuario) {
        setIdUsuario(-1);
        $ instanciaBD = null;
        ServiciosM modelo = new ServiciosM();

        try {
            if (idUsuario > 0) {
                instanciaBD = new $();
                String myQuery
                        = "SELECT ID_Usuario, ID_Rol, Usuario, Activo, "
                        + "       UsuarioModifico, FechaModificacion "
                        + "FROM Usuario "
                        + "WHERE ID_Usuario = ?;";

                PreparedStatement psUsuario = instanciaBD.prepStatement(myQuery);
                psUsuario.setInt(1, idUsuario);
                ResultSet resultado = psUsuario.executeQuery();

                if (resultado.next()) {
                    setIdUsuario(idUsuario);
                    setIdRol(resultado.getInt("ID_Rol"));
                    setUsuario(resultado.getString("Usuario"));
                    setActivo(resultado.getInt("Activo") == 1);
                    setUsuarioModifico(resultado.getInt("UsuarioModifico"));
                }
            } else {
                setIdUsuario(idUsuario);
                setIdRol(0);
                setUsuario("");
                setActivo(true);
                setUsuarioModifico(0);
            }

            // Carga la lista de departamento en un elemento SELECT-html para que el
            // usuario pueda seleccionar a cual departamento pertenecerá el puesto de
            // trabajo mostrado en pantalla.
            int[] idRol = {getIdRol()};
            setSelectRol(modelo.elementoSELECTRol(idRol));
            setMapaAccesos(modelo.cargarMapaAccesos(idUsuario, idRol[0]));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (instanciaBD != null) {
                instanciaBD.desconectar();
            }

            instanciaBD = null;
            System.gc();
        }
    } // Fin del metodo cargarRegistro().

    public String toJSON() {
        // Parsea la clase Usuario a JSON.
        String mensajeError = "";
        if (getIdUsuario() < 0) {
            mensajeError = "Ocurrió un error al cargar el registro del usuario.";
        }

        if (getIdUsuario() >= 0) {
            mensajeError
                    = "   \"error\": " + (getIdUsuario() < 0) + ", "
                    + "   \"mensaje\": \"" + mensajeError + "\", "
                    + "   \"codigo\": " + getIdUsuario() + ", "
                    + "   \"usuario\": \"" + getUsuario() + "\", "
                    + "   \"rol\": \"" + getRol() + "\", "
                    + "   \"activo\": " + getActivo() + ", "
                    + "   \"usuarioModifico\": \"" + getUsuarioModifico() + "\", "
                    + "   \"fechaUltimoIngreso\": \"" + getFechaUltimoIngreso() + "\", "
                    + "   \"selectRol\": \"" + getSelectRol() + "\", "
                    + "   \"mapaAccesos\": \"" + getMapaAccesos() + "\"";
        }

        return mensajeError;
    } // Fin del metodo toJSON().

    // Devuelve el registro con formato de tabla HTML.
    private String toHTML() {
        return "<tr style='border: none;' class='resaltar'>"
                + "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + getUsuario() + "</td>"
                + "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + getRol() + "</td>"
                + "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + getFechaUltimoIngreso() + "</td>"
                + "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'>"
                + "    <i class='material-icons'>"
                + (getActivo() ? "check_box" : "check_box_outline_blank")
                + "    </i></a>"
                + "  </td>"
                + "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'><a href='" + PAGINA_WEB + "' class='" + ETIQUETA_EXISTENTE + "' "
                + "      data-codigo='" + getIdUsuario() + "' "
                + "      data-modo='mod-v'>"
                + "    <i class='material-icons'>visibility</i></a>"
                + "  </td>"
                + "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'><a href='" + PAGINA_WEB + "' class='" + ETIQUETA_EXISTENTE + "' "
                + "      data-codigo='" + getIdUsuario() + "' "
                + "      data-modo='mod-e'>"
                + "    <i class='material-icons'>edit</i></a>"
                + "  </td>"
                + "</tr>";
    } // Fin del método toHTML.
} // Fin de la clase Usuario.
