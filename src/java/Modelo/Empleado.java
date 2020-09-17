/*  Nombre.....:  Empleado.java
 *  Descripción:  Carga el registro de la tabla Empleado.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Octubre, 2019.
 */
package Modelo;

import Utilitarios.ServiciosU;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Empleado {
    // Variables de la clase Empleado.
    private String idEmpleado;
    private int idPuesto;
    private String idSalario;
    private int estadoCivil;
    private int Genero;
    private String nombres;
    private String apellidos;
    private String identidad;
    private String nombreCompleto;
    private String nombrePuesto;
    private String direccion;
    private int telefono;
    private String correoElectronico;
    private int fechaNacimiento;
    private int fechaIngreso;
    private String hojaDeVida;
    private boolean activo;
    private int usuarioModifico;
    private long fechaModificacion;
    private float salarioBase;
    private float salarioPorObra;
    private float participacion;
    private float suelo;
    private float techo;
    private String frecuencia;
    private String selectDepartamento;
    private String selectPuesto;
    private String selectEstadoCivil;
    private String selectGenero;
    private boolean modoVisualizacion;
    private boolean modoEdicion;

    // Método constructor de la clase Empleado.
    public Empleado() {
        this.idEmpleado = "";
        this.idPuesto = -1;
        this.idSalario = "";
        this.estadoCivil = -1;
        this.Genero = -1;
        this.nombres = "";
        this.apellidos = "";
        this.nombreCompleto = "";
        this.nombrePuesto = "";
        this.identidad = "";
        this.direccion = "";
        this.telefono = -1;
        this.correoElectronico = "";
        this.fechaNacimiento = -1;
        this.fechaIngreso = -1;
        this.hojaDeVida = "";
        this.activo = false;
        this.usuarioModifico = -1;
        this.fechaModificacion = -1;
        this.salarioBase = 0;
        this.salarioPorObra = 0;
        this.participacion = 0;
        this.suelo = 0;
        this.techo = 0;
        this.frecuencia = "";
        this.selectPuesto = "";
        this.selectEstadoCivil = "";
        this.selectGenero = "";
    } // Fin del método constructor.

    // Métodos GET y SET de la clase Empleado.
    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public int getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(int idPuesto) {
        this.idPuesto = idPuesto;
    }

    public String getIdSalario() {
        return idSalario;
    }

    public void setIdSalario(String idSalario) {
        this.idSalario = idSalario;
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

    public String getTelefono() {
        return telefono == 0 ? "" : String.valueOf(telefono);
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

    public String getHojaDeVida() {
        return hojaDeVida;
    }

    public void setHojaDeVida(String hojaDeVida) {
        this.hojaDeVida = hojaDeVida;
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

    public float getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(float salarioBase) {
        this.salarioBase = salarioBase;
    }

    public float getSalarioPorObra() {
        return salarioPorObra;
    }

    public void setSalarioPorObra(float salarioPorObra) {
        this.salarioPorObra = salarioPorObra;
    }

    public float getParticipacion() {
        return participacion;
    }

    public void setParticipacion(float participacion) {
        this.participacion = participacion;
    }

    public float getSuelo() {
        return suelo;
    }

    public void setSuelo(float suelo) {
        this.suelo = suelo;
    }

    public float getTecho() {
        return techo;
    }

    public void setTecho(float techo) {
        this.techo = techo;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public String getSelectPuesto() {
        return selectPuesto;
    }

    public void setSelectPuesto(String selectPuesto) {
        this.selectPuesto = selectPuesto;
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

    public String getSelectDepartamento() {
        return selectDepartamento;
    }

    public void setSelectDepartamento(String selectDepartamento) {
        this.selectDepartamento = selectDepartamento;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombres, String apellidos) {
        this.nombreCompleto = nombres.split(" ")[0] + " " + apellidos.split(" ")[0];
    }

    public String getNombrePuesto() {
        return nombrePuesto;
    }

    public void setNombrePuesto(String nombrePuesto) {
        this.nombrePuesto = nombrePuesto;
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

    // Métodos utilitarios de la clase Empleado.
    // Carga la lista de todos los empleados de la empresa, activos e inactivos.
    public String cargarLista(int idCentro, boolean modoVisualizacion, boolean modoEdicion) {
        $ instanciaListaEmpleados = null;
        String respuesta = "";
        setModoVisualizacion(modoVisualizacion);
        setModoEdicion(modoEdicion);

        try {
            // Realiza consulta de empleados a la base de datos.
            instanciaListaEmpleados = new $();
            ResultSet resultado = instanciaListaEmpleados.execQry(
                    "SELECT ID_Empleado, Nombres, Apellidos, NombrePuesto, " +
                    "       Activo, UsuarioModifico, FechaModificacion " +
                    "FROM V_Lista_Empleados;");

            if (resultado.next()) {
                // Retroalimentación al usuario del proceso.
                respuesta =
                    "{" +
                    "  \"error\" : false, " +
                    "  \"mensaje\" : \"\", " +
                    // Información recuperada de la tabla Empleado.
                    "  \"elementosHTML\": \"" +
                    "            <h5 class='center'>Lobby Empleados" +
                    "              <a href='empleado.html' class='empleadoNuevo'>" +
                    "                <i class='material-icons right'>add_circle_outline</i>" +
                    "              </a>" +
                    "            </h5><hr/>" +
                    "            <table id='myTable2'>" +
                    "              <thead>" +
                    "                <tr>" +
                    "                  <th onclick='sortTable(0)' style='cursor:pointer' id='nombreCol'>Código<i></th>" +
                    "                  <th>Nombre</th>" +
                    "                  <th onclick='sortTable(2)' style='cursor:pointer' id='puestoCol' class='center'>Puesto</th>" +
                    "                  <th onclick='sortTable(3)' style='cursor:pointer' id='activoCol' class='center'>Activo</th>" +
                    ( getModoVisualizacion() ? "<th class='center'>Ver</th>" : "" ) +
                    ( getModoEdicion() ? "<th class='center'>Editar</th>" : "" ) +
                    "                </tr>" +
                    "              </thead>" +
                    "            <tbody>";

                do {
                    // Si existen resultados crea arreglo JSON para mostrar la lista de empleados.
                    this.setIdEmpleado(resultado.getString("ID_Empleado"));
                    this.setNombreCompleto(
                            resultado.getString("Nombres"),
                            resultado.getString("Apellidos"));
                    this.setNombrePuesto(resultado.getString("NombrePuesto"));
                    this.setActivo(resultado.getBoolean("Activo"));
                    this.setUsuarioModifico(resultado.getInt("UsuarioModifico"));
                    this.setFechaModificacion(resultado.getLong("FechaModificacion"));

                    respuesta += this.toHTML();
                } while (resultado.next());

                // Completa la estructura del objeto JSON de respuesta.
                respuesta = respuesta + "</tbody></table>\"}";
            } else {
                respuesta = "{ \"error\" : false, " +
                      "  \"mensaje\" : \"No existen registros para mostrar.\" ," +
                      "  \"elementosHTML\": \"" +
                      "            <h5 class='center'>Empleados en la empresa" +
                      "              <a href='empleado.html' class='empleadoNuevo'>" +
                      "                <i class='material-icons right'>add_circle_outline</i>" +
                      "              </a>" +
                      "            </h5><hr/>" +
                      "            <div class='col s12 center'>" +
                      "              <p> *** Antes debe crear un empleado ***</p>" +
                      "            </div>\"" +
                      "}";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            respuesta = "{ \"error\" : true, " +
                    "  \"mensaje\" : \"Ocurrió un error al intentar cargar los registros.\", " +
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
            if (instanciaListaEmpleados != null) {
                instanciaListaEmpleados.desconectar();
            }

            instanciaListaEmpleados = null;
            System.gc();
        }

        return respuesta;
    } // Fin del método cargarLista().

    
    // Carga el registro de empleado con el código proporcionado.
    public void cargarRegistro(int idCentro, String idEmpleado) {
        ServiciosM modelo = new ServiciosM();
        $ instanciaBD = null;
        setIdEmpleado("-1");
        int[] idDepartamento = {0};

        try {
            instanciaBD = new $();
            if (idEmpleado.length() > 0) {
                // Recupera el registro del empleado y el salario que se le asignó.
                String myQuery =
                    "SELECT ID_Empleado, ID_Puesto, ID_Salario, ID_EstadoCivil, " +
                    "       ID_Genero, Nombres, Apellidos, Identidad, Direccion, " +
                    "       Telefono, CorreoElectronico, FechaNacimiento, FechaIngreso, " +
                    "       HojaDeVida, SalarioBase, SalarioObra, Participacion, " +
                    "       Suelo, Techo, Frecuencia, Activo, UsuarioModifico, " +
                    "       FechaModificacion " +
                    "FROM V_Empleado_Salario " +
                    "WHERE ID_Empleado = ?;";

                PreparedStatement psEmpleado = instanciaBD.prepStatement(myQuery);
                psEmpleado.setString(1, idEmpleado);
                ResultSet resultado = psEmpleado.executeQuery();
                if (resultado.next()) {
                    setIdEmpleado(idEmpleado);
                    setIdPuesto(resultado.getInt("ID_Puesto"));
                    setIdSalario(resultado.getString("ID_Salario"));
                    setEstadoCivil(resultado.getInt("ID_EstadoCivil"));
                    setGenero(resultado.getInt("ID_Genero"));
                    setNombres(resultado.getString("Nombres"));
                    setApellidos(resultado.getString("Apellidos"));
                    setIdentidad(resultado.getString("Identidad"));
                    setDireccion(resultado.getString("Direccion"));
                    setTelefono(resultado.getInt("Telefono"));
                    setCorreoElectronico(resultado.getString("CorreoElectronico"));
                    setFechaNacimiento(resultado.getInt("FechaNacimiento"));
                    setFechaIngreso(resultado.getInt("FechaIngreso"));
                    setHojaDeVida(resultado.getString("HojaDeVida"));
                    setSalarioBase(resultado.getFloat("SalarioBase"));
                    setSalarioPorObra(resultado.getFloat("SalarioObra"));
                    setParticipacion(resultado.getFloat("Participacion"));
                    setSuelo(resultado.getFloat("Suelo"));
                    setTecho(resultado.getFloat("Techo"));
                    setFrecuencia(resultado.getString("Frecuencia"));
                    setActivo(resultado.getInt("Activo") == 1);
                    setUsuarioModifico(resultado.getInt("UsuarioModifico"));
                    setFechaModificacion(resultado.getInt("FechaModificacion"));
                }
            } else {
                // Al ser empleado nuevo, recupera la información base del salario
                // a cargar en los campos de acuerdo al puesto seleccionado.
                String myQuery =
                    "SELECT TOP(1) ID_Salario, SalarioBase, SalarioObra, " +
                    "              Participacion, Suelo, Techo, Frecuencia, " +
                    "              P.ID_Departamento " +
                    "FROM Puesto P INNER JOIN Departamento D " +
                    "         ON D.ID_Departamento = P.ID_Departamento " +
                    "WHERE D.Activo = 1 " +
                    "  AND P.Activo = 1 " +
                    "ORDER BY D.Nombre, P.Nombre"; // XXX???

                PreparedStatement psSalario = instanciaBD.prepStatement(myQuery);
                ResultSet resultado = psSalario.executeQuery();
                if (resultado.next()) {
                    setIdSalario(resultado.getString("ID_Salario"));
                    setSalarioBase(resultado.getFloat("SalarioBase"));
                    setSalarioPorObra(resultado.getFloat("SalarioObra"));
                    setParticipacion(resultado.getFloat("Participacion"));
                    setSuelo(resultado.getFloat("Suelo"));
                    setTecho(resultado.getFloat("Techo"));
                    setFrecuencia(resultado.getString("Frecuencia"));
                } else {
                    setIdSalario("T");
                    setSalarioBase(0);
                    setSalarioPorObra(0);
                    setParticipacion(0);
                    setSuelo(0);
                    setTecho(0);
                    setFrecuencia("M");
                }

                setIdEmpleado(idEmpleado);
                setIdPuesto(0);
                setEstadoCivil(0);
                setGenero(0);
                setNombres("");
                setApellidos("");
                setIdentidad("");
                setDireccion("");
                setTelefono(0);
                setCorreoElectronico("");
                setFechaNacimiento(0);
                setFechaIngreso(0);
                setHojaDeVida("");
                setActivo(true);
                setUsuarioModifico(0);
                setFechaModificacion(0);
            }

            setSelectEstadoCivil(modelo.elementoSELECTEstadoCivil(getEstadoCivil()));
            setSelectGenero(modelo.elementoSELECTGenero(getGenero()));
            setSelectPuesto(modelo.elementoSELECTPuesto(idCentro, idDepartamento, getIdPuesto()));
            setSelectDepartamento(modelo.elementoSELECTDepartamento(idDepartamento[0]));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (instanciaBD != null)
                instanciaBD.desconectar();

            instanciaBD = null;
            System.gc();
        }
    } // Fin del método cargarRegistro().
    
    
    // Devuelve el registro con formato de objeto JSON.
    public String toJSON() {
        String mensajeError = "";
        if (getIdEmpleado().equals("-1"))
            mensajeError = "Ocurrió un error al cargar el registro del empleado.";
        
        return
            "   \"error\": " + getIdEmpleado().equals("-1") + ", " +
            "   \"mensaje\": \"" + mensajeError + "\", " +
            "   \"codigo\": \"" + getIdEmpleado() + "\", " +
            "   \"codigo2\": " + getIdPuesto() + ", " +
            "   \"codigo3\": \"" + getIdSalario() + "\", " +
            "   \"nombres\": \"" + getNombres() + "\", " +
            "   \"apellidos\": \"" + getApellidos() + "\", " +
            "   \"nombreCompleto\": \"" + getNombreCompleto() + "\", " +
            "   \"identidad\": \"" + getIdentidad() + "\", " +
            "   \"direccion\": \"" + getDireccion() + "\", " +
            "   \"telefono\": \"" + getTelefono() + "\", " +
            "   \"correoElectronico\": \"" + getCorreoElectronico() + "\", " +
            "   \"fechaNacimiento\": \"" + ServiciosU.formatearFecha(getFechaNacimiento()) + "\", " +
            "   \"fechaIngreso\": \"" + ServiciosU.formatearFecha(getFechaIngreso()) + "\", " +
            "   \"hojaDeVida\": \"" + getHojaDeVida() + "\", " +
            "   \"activo\": " + getActivo() + ", " +
            "   \"salarioBase\": " + getSalarioBase() + ", " +
            "   \"salarioObra\": " + getSalarioPorObra() + ", " +
            "   \"participacion\": " + getParticipacion() + ", " +
            "   \"suelo\": " + getSuelo() + ", " +
            "   \"techo\": " + getTecho() + ", " +
            "   \"frecuencia\": \"" + getFrecuencia() + "\", " +
            "   \"usuarioModifico\": " + getUsuarioModifico() + ", " +
            "   \"nombrePuesto\": \"" + getNombrePuesto() + "\", " +
            "   \"selectDepartamento\": \"" + getSelectDepartamento() + "\", " +
            "   \"selectEstadoCivil\": \"" + getSelectEstadoCivil() + "\", " +
            "   \"selectGenero\": \"" + getSelectGenero() + "\", " +
            "   \"selectPuesto\": \"" + getSelectPuesto() + "\"";
    } // Fin del metodo toJSON().
    
    // Devuelve el registro con formato de tabla HTML.
    private String toHTML() {
        return "<tr style='border: none;' class='resaltar'>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + getIdEmpleado() + "</td>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + getNombreCompleto() + "</td>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + getNombrePuesto() + "</td>" +
               "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'>" +
               "    <i class='material-icons'>" + 
                      (getActivo() ? "check_box": "check_box_outline_blank") + 
               "    </i></a>" +
               "  </td>" +
               ( getModoVisualizacion() ? 
               "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'><a href='empleado.html' class='empleadoExistente' " +
               "      data-codigo='" + getIdEmpleado() + "' " +
               "      data-modo='mod-v'>" +
               "    <i class='material-icons'>visibility</i></a>" +
               "  </td>" : "" ) +
               ( getModoEdicion() ? 
               "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'><a href='empleado.html' class='empleadoExistente' " +
               "      data-codigo='" + getIdEmpleado() + "' " +
               "      data-modo='mod-e'>" +
               "    <i class='material-icons'>edit</i></a>" +
               "  </td>" : "" ) +
               "</tr>";
    } // Fin del método toHTML.
} // Fin de la clase Empleado.