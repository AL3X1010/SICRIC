/*  Nombre.....:  Paciente.java
 *  Descripción:  Carga el registro de la tabla Paciente.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Octubre, 2019.
 */
package Modelo;

import Utilitarios.Consts;
import Utilitarios.ServiciosU;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Paciente {
    // Constantes de la clase Paciente.
    private final String PAGINA_WEB = Consts.PACIENTE_HTML;
    private final String TITULO = Consts.TITULO_LISTA_PACIENTES;
    private final String ETIQUETA_EXISTENTE = Consts.ETIQUETA_EXISTENTE_PACIENTE;
    private final String ETIQUETA_NUEVO = Consts.ETIQUETA_NUEVO_PACIENTE;
    
    // Variables de la clase Paciente.
    private String idPaciente;
    private int idCentro;
    private int estadoCivil;
    private int Genero;
    private int hospitalRemitio;
    private String medicoRemitio;
    private String nombres;
    private String apellidos;
    private String nombreCompleto;
    private String identidad;
    private String direccion;
    private int telefono;
    private int telefonoEmergencia;
    private String correoElectronico;
    private int fechaNacimiento;
    private int fechaIngreso;
    private int usuarioModifico;
    private long fechaModificacion;
    private String selectHospital;
    private String selectEstadoCivil;
    private String selectGenero;
    private boolean modoVisualizacion;
    private boolean modoEdicion;

    // Método constructor de la clase Paciente.
    public Paciente() {
        this.idPaciente = "-";
        this.idCentro = -1;
        this.hospitalRemitio = 1;
        this.medicoRemitio = "";
        this.estadoCivil = -1;
        this.Genero = -1;
        this.nombres = "";
        this.apellidos = "";
        this.nombreCompleto = "";
        this.identidad = "";
        this.direccion = "";
        this.telefono = -1;
        this.telefonoEmergencia = -1;
        this.correoElectronico = "";
        this.fechaNacimiento = -1;
        this.fechaIngreso = -1;
        this.usuarioModifico = -1;
        this.fechaModificacion = -1;
        this.selectHospital = "";
        this.selectEstadoCivil = "";
        this.selectGenero = "";
    } // Fin del método constructor.

    // Metodos get y set de la clase Paciente.
    public String getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(String idPaciente) {
        this.idPaciente = idPaciente;
    }

    public int getIdCentro() {
        return idCentro;
    }

    public void setIdCentro(int idCentro) {
        this.idCentro = idCentro;
    }

    public int getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(int estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public int getGenero() {
        return Genero;
    }

    public void setGenero(int Genero) {
        this.Genero = Genero;
    }

    public int getHospitalRemitio() {
        return hospitalRemitio;
    }

    public void setHospitalRemitio(int hospitalRemitio) {
        this.hospitalRemitio = hospitalRemitio;
    }

    public String getMedicoRemitio() {
        return medicoRemitio;
    }

    public void setMedicoRemitio(String medicoRemitio) {
        this.medicoRemitio = medicoRemitio;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getIdentidad() {
        return identidad;
    }

    public void setIdentidad(String identidad) {
        this.identidad = identidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public int getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(int fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(int fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
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

    public String getSelectEstadoCivil() {
        return selectEstadoCivil;
    }

    public void setSelectEstadoCivil(String selectEstadoCivil) {
        this.selectEstadoCivil = selectEstadoCivil;
    }

    public String getSelectGenero() {
        return selectGenero;
    }

    public void setSelectGenero(String selectGenero) {
        this.selectGenero = selectGenero;
    }

    public int getTelefonoEmergencia() {
        return telefonoEmergencia;
    }

    public void setTelefonoEmergencia(int telefonoEmergencia) {
        this.telefonoEmergencia = telefonoEmergencia;
    }

    public String getSelectHospital() {
        return selectHospital;
    }

    public void setSelectHospital(String selectHospital) {
        this.selectHospital = selectHospital;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombres, String apellidos) {
        this.nombreCompleto = nombres.split(" ")[0] + " " + apellidos.split(" ")[0];
    }

    public boolean getModoVisualizacion() {
        return modoVisualizacion;
    }

    public void setModoVisualizacion(boolean modoVisualizacion) {
        this.modoVisualizacion = modoVisualizacion;
    }

    public boolean getModoEdicion() {
        return modoEdicion;
    }

    public void setModoEdicion(boolean modoEdicion) {
        this.modoEdicion = modoEdicion;
    }

    // Metodos utilitarios de la clase Paciente.
    public String cargarLista(int idCentro, boolean modoVisualizacion, boolean modoEdicion) {
        $ instanciaListaPacientes = null;
        String respuesta = "";
        setModoVisualizacion(modoVisualizacion);
        setModoEdicion(modoEdicion);
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
            // Realiza consulta de pacientes a la base de datos.
            instanciaListaPacientes = new $();
            ResultSet resultado = instanciaListaPacientes.execQry(
                    "SELECT ID_Paciente, Nombres, Apellidos, Telefono "
                    + "FROM Paciente "
                    + "WHERE ID_Centro = " + idCentro + " "
                    + "ORDER BY ID_Paciente;");

            if (resultado.next()) {
                respuesta
                        = // Retroalimentación al usuario del proceso.
                        "{ \"error\" : false, "
                        + "  \"mensaje\" : \"\", "
                        + // Información recuperada de la tabla Departamento.
                        "  \"elementosHTML\": \""
                        + "            <h5 class='center'>" + TITULO
                        + "              <a href='" + PAGINA_WEB + "' class='" + ETIQUETA_NUEVO + "'>"
                        + "                <i class='material-icons right'>add_circle_outline</i>"
                        + "              </a>"
                        + "            </h5><hr/>"
                        + "            <table id='myTable2'>"
                        + "              <thead>"
                        + "                <tr>"
                        + "                  <th onclick='sortTable(0)' style='cursor:pointer' id='nombreCol'>Código<i></th>"
                        + "                  <th onclick='sortTable(0)' style='cursor:pointer' id='nombreCol'>Nombre<i></th>"
                        + "                  <th>Teléfono</th>"
                        + (getModoVisualizacion() ? "<th class='center'>Ver</th>" : "")
                        + (getModoEdicion() ? "<th class='center'>Editar</th>" : "")
                        + "                </tr>"
                        + "              </thead>"
                        + "            <tbody>";

                do {
                    // Si existen resultados crea arreglo JSON para mostrar la lista de pacientes.
                    this.setIdPaciente(resultado.getString("ID_Paciente"));
                    this.setNombreCompleto(resultado.getString("Nombres"),
                            resultado.getString("Apellidos"));
                    this.setTelefono(resultado.getInt("Telefono"));

                    respuesta += this.toHTML();
                } while (resultado.next());

                // Completa la estructura del objeto JSON de respuesta.
                respuesta = respuesta + "</tbody></table>\"}";
            } else {
                respuesta = "{ \"error\" : false, " +
                            "  \"mensaje\" : \"No existen registros para mostrar.\" ," +
                               ESTRUCTURA_ERROR
                                      .replace("$#titulo#$", TITULO)
                                      .replace("$#paginaWeb#$", PAGINA_WEB)
                                      .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                            "}";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            respuesta = "{ \"error\" : true, " +
                        "  \"mensaje\" : \"Ocurrió un error al intentar cargar los registros.\", " +
                           ESTRUCTURA_ERROR
                                    .replace("$#titulo#$", TITULO)
                                    .replace("$#paginaWeb#$", PAGINA_WEB)
                                    .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                        "}";
        } finally {
            if (instanciaListaPacientes != null) {
                instanciaListaPacientes.desconectar();
            }

            instanciaListaPacientes = null;
            System.gc();
        }

        return respuesta;
    } // Fin del método cargarLista().

    public void cargarRegistro(int idCentro, String idPaciente) {
        setIdPaciente("-1");
        setIdCentro(idCentro);
        ServiciosM modelo = new ServiciosM();
        $ instanciaBD = null;

        try {
            if (!idPaciente.equals("0") && idPaciente.length() > 0) {
                instanciaBD = new $();
                String myQuery
                        = "SELECT ID_Paciente, ID_Centro, ID_EstadoCivil, ID_Genero, "
                        + "       RemitidoPor, MedicoRemitio, Nombres, Apellidos, "
                        + "       Identidad, Direccion, Telefono, TelefonoEmergencia, "
                        + "       CorreoElectronico, FechaNacimiento, FechaIngreso, "
                        + "       UsuarioModifico, FechaModificacion "
                        + "FROM Paciente "
                        + "WHERE ID_Paciente = ?;";

                PreparedStatement psPaciente = instanciaBD.prepStatement(myQuery);
                psPaciente.setString(1, idPaciente);
                ResultSet resultado = psPaciente.executeQuery();

                if (resultado.next()) {
                    if (idCentro == resultado.getInt("ID_Centro")) {
                        setIdPaciente(idPaciente);
                        setEstadoCivil(resultado.getInt("ID_EstadoCivil"));
                        setGenero(resultado.getInt("ID_Genero"));
                        setHospitalRemitio(resultado.getInt("RemitidoPor"));
                        setMedicoRemitio(resultado.getString("MedicoRemitio"));
                        setNombres(resultado.getString("Nombres"));
                        setApellidos(resultado.getString("Apellidos"));
                        setIdentidad(resultado.getString("Identidad"));
                        setDireccion(resultado.getString("Direccion"));
                        setTelefono(resultado.getInt("Telefono"));
                        setTelefonoEmergencia(resultado.getInt("TelefonoEmergencia"));
                        setCorreoElectronico(resultado.getString("CorreoElectronico"));
                        setFechaNacimiento(resultado.getInt("FechaNacimiento"));
                        setFechaIngreso(resultado.getInt("FechaIngreso"));
                        setUsuarioModifico(resultado.getInt("UsuarioModifico"));
                        setFechaModificacion(resultado.getInt("FechaModificacion"));
                    } else {
                        setIdPaciente("");
                        setEstadoCivil(0);
                        setGenero(0);
                        setHospitalRemitio(1);
                        setMedicoRemitio("");
                        setNombres("");
                        setApellidos("");
                        setIdentidad("");
                        setDireccion("");
                        setTelefono(0);
                        setTelefonoEmergencia(0);
                        setCorreoElectronico("");
                        setFechaNacimiento(0);
                        setFechaIngreso(0);
                        setUsuarioModifico(0);
                        setFechaModificacion(0);
                    }
                }
            } else {
                setIdPaciente("");
                setEstadoCivil(0);
                setGenero(0);
                setHospitalRemitio(1);
                setMedicoRemitio("");
                setNombres("");
                setApellidos("");
                setIdentidad("");
                setDireccion("");
                setTelefono(0);
                setTelefonoEmergencia(0);
                setCorreoElectronico("");
                setFechaNacimiento(0);
                setFechaIngreso(0);
                setUsuarioModifico(0);
                setFechaModificacion(0);
            }

            setSelectHospital(modelo.elementoSELECTHospital(getHospitalRemitio()));
            setSelectEstadoCivil(modelo.elementoSELECTEstadoCivil(getEstadoCivil()));
            setSelectGenero(modelo.elementoSELECTGenero(getGenero()));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (instanciaBD != null)
                instanciaBD.desconectar();

            instanciaBD = null;
            modelo = null;
            System.gc();
        }
    } // Fin del método cargarRegistro().
    
    
    // Parsea la clase Paciente a JSON.
    public String toJSON() {
        String mensajeError = "";
        if(getIdPaciente().equals("-1"))
            mensajeError = "Ocurrió un error al cargar el registro del departamento.";

        return
            "{" +
            "   \"error\": " + (getIdPaciente().equals("-1")) + ", " +
            "   \"mensaje\": \"" + mensajeError + "\", " +
            "   \"codigo\": \"" + getIdPaciente() + "\", "
            + "   \"medicoRemitio\": \"" + getMedicoRemitio() + "\","
            + "   \"nombres\": \"" + getNombres() + "\","
            + "   \"apellidos\": \"" + getApellidos() + "\","
            + "   \"nombreCompleto\": \"" + getNombreCompleto() + "\","
            + "   \"identidad\": \"" + getIdentidad() + "\","
            + "   \"direccion\": \"" + getDireccion() + "\","
            + "   \"telefono\": " + getTelefono() + ","
            + "   \"telefonoEmergencia\": " + getTelefonoEmergencia() + ","
            + "   \"correoElectronico\": \"" + getCorreoElectronico() + "\","
            + "   \"fechaNacimiento\": " + getFechaNacimiento() + ","
            + "   \"fechaIngreso\": " + getFechaIngreso() + ","
            + "   \"usuarioModifico\": " + getUsuarioModifico() + ","
            + "   \"selectHospital\": \"" + getSelectHospital() + "\","
            + "   \"selectEstadoCivil\": \"" + getSelectEstadoCivil() + "\","
            + "   \"selectGenero\": \"" + getSelectGenero() + "\","
            + "   \"error\": false"
            + "}";
    } // Fin del metodo toJSON().

    
    // Devuelve el registro con formato de tabla HTML.
    private String toHTML() {
        return "<tr style='border: none;' class='resaltar'>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + getIdPaciente() + "</td>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + getNombreCompleto() + "</td>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + getTelefono() + "</td>" +
               ( getModoVisualizacion() ? 
               "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'><a href='" + PAGINA_WEB + "' class='" + ETIQUETA_EXISTENTE + "' " +
               "      data-codigo='" + getIdPaciente() + "' " +
               "      data-modo='mod-v'>" +
               "    <i class='material-icons'>visibility</i></a>" +
               "  </td>" : "" ) +
               ( getModoEdicion() ? 
               "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'><a href='" + PAGINA_WEB + "' class='" + ETIQUETA_EXISTENTE + "' " +
               "      data-codigo='" + getIdPaciente() + "' " +
               "      data-modo='mod-e'>" +
               "    <i class='material-icons'>edit</i></a>" +
               "  </td>" : "" ) +
               "</tr>";
    } // Fin del método toHTML.
} // Fin de la clase Paciente.