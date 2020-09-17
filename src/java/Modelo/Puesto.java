/*  Nombre.....:  Puesto.java
 *  Descripción:  Carga el registro de la tabla Puesto.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Agosto, 2019.
 */
package Modelo;

import Utilitarios.Consts;
import Utilitarios.ServiciosU;
import java.sql.ResultSet;

public class Puesto {
    // Constantes de la clase Puesto.
    private final String PAGINA_WEB = Consts.PUESTO_HTML;
    private final String TITULO = Consts.TITULO_LISTA_PUESTOS;
    private final String ETIQUETA_EXISTENTE = Consts.ETIQUETA_EXISTENTE_PUESTO;
    private final String ETIQUETA_NUEVO = Consts.ETIQUETA_NUEVO_PUESTO;
    
    // Variables de la clase Puesto.
    private int idPuesto;
    private int idDepartamento;
    private int idPuestoJefe;
    private int idPuestoRespaldo;
    private String idSalario;
    private String nombre;
    private String nombreDepartamento;
    private String descripcion;
    private int edadMinima;
    private int edadMaxima;
    private int genero;
    private int estadoCivil;
    private int experienciaRequerida;
    private String notaExperiencia;
    private String objetivo;
    private String autoridad;
    private String tipoSalario;
    private boolean activo;
    private String usuarioModifico;
    private long fechaModificacion;
    private String selectDepartamento;
    private String selectPuestoJefe;
    private String selectPuestoRespaldo;
    private String selectEstadoCivil;
    private String selectExperiencia;
    private String selectGenero;
    private boolean flagSelectJefe;
    private boolean flagSelectRespaldo;
    private float salarioBase;
    private float salarioObra;
    private float participacion;
    private float suelo;
    private float techo;
    private String frecuencia;
    private boolean modoVisualizacion;
    private boolean modoEdicion;

    // Método constructor de la clase Puesto.
    public Puesto() {
        idPuesto = -1;
        idDepartamento = -1;
        idPuestoJefe = -1;
        idPuestoRespaldo = -1;
        idSalario = "";
        nombre = "";
        nombreDepartamento = "";
        descripcion = "";
        edadMinima = -1;
        edadMaxima = -1;
        genero = -1;
        estadoCivil = -1;
        experienciaRequerida = -1;
        notaExperiencia = "";
        objetivo = "";
        autoridad = "";
        activo = false;
        usuarioModifico = "";
        selectDepartamento = "";
        selectPuestoJefe = "";
        selectPuestoRespaldo = "";
        selectEstadoCivil = "";
        selectExperiencia = "";
        selectGenero = "";
        flagSelectJefe = false;
        flagSelectRespaldo = false;
        tipoSalario = "";
        salarioBase = -1;
        salarioObra = -1;
        participacion = -1;
        suelo = -1;
        techo = -1;
        frecuencia = "";
    } // Fin del método constructor.

    // Metodos get y set de la clase Puesto.
    public int getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(int idPuesto) {
        this.idPuesto = idPuesto;
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public int getIdPuestoJefe() {
        return idPuestoJefe;
    }

    public void setIdPuestoJefe(int idPuestoJefe) {
        this.idPuestoJefe = idPuestoJefe;
    }

    public int getIdPuestoRespaldo() {
        return idPuestoRespaldo;
    }

    public void setIdPuestoRespaldo(int idPuestoRespaldo) {
        this.idPuestoRespaldo = idPuestoRespaldo;
    }

    public String getIdSalario() {
        return idSalario;
    }

    public void setIdSalario(String idSalario) {
        this.idSalario = idSalario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getUsuarioModifico() {
        return usuarioModifico;
    }

    public void setUsuarioModifico(String usuarioModifico) {
        this.usuarioModifico = usuarioModifico;
    }

    public String getSelectDepartamento() {
        return selectDepartamento;
    }

    public void setSelectDepartamento(String selectDepartamento) {
        this.selectDepartamento += selectDepartamento;
    }

    public int getEdadMinima() {
        return edadMinima;
    }

    public void setEdadMinima(int edadMinima) {
        this.edadMinima = edadMinima;
    }

    public int getEdadMaxima() {
        return edadMaxima;
    }

    public void setEdadMaxima(int edadMaxima) {
        this.edadMaxima = edadMaxima;
    }

    public int getGenero() {
        return genero;
    }

    public void setGenero(int genero) {
        this.genero = genero;
    }

    public int getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(int estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public int getExperienciaRequerida() {
        return experienciaRequerida;
    }

    public void setExperienciaRequerida(int experienciaRequerida) {
        this.experienciaRequerida = experienciaRequerida;
    }

    public String getNotaExperiencia() {
        return notaExperiencia;
    }

    public void setNotaExperiencia(String notaExperiencia) {
        this.notaExperiencia = notaExperiencia;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getAutoridad() {
        return autoridad;
    }

    public void setAutoridad(String autoridad) {
        this.autoridad = autoridad;
    }

    public String getSelectPuestoJefe() {
        return selectPuestoJefe;
    }

    public void setSelectPuestoJefe(String selectPuestoJefe) {
        this.selectPuestoJefe += selectPuestoJefe;
    }

    public String getSelectPuestoRespaldo() {
        return selectPuestoRespaldo;
    }

    public void setSelectPuestoRespaldo(String selectPuestoRespaldo) {
        this.selectPuestoRespaldo += selectPuestoRespaldo;
    }

    public String getSelectEstadoCivil() {
        return selectEstadoCivil;
    }

    public void setSelectEstadoCivil(String selectEstadoCivil) {
        this.selectEstadoCivil += selectEstadoCivil;
    }

    public String getSelectExperiencia() {
        return selectExperiencia;
    }

    public void setSelectExperiencia(String selectExperiencia) {
        this.selectExperiencia += selectExperiencia;
    }

    public String getSelectGenero() {
        return selectGenero;
    }

    public void setSelectGenero(String selectGenero) {
        this.selectGenero += selectGenero;
    }

    public boolean getFlagSelectJefe() {
        return flagSelectJefe;
    }

    public void setFlagSelectJefe(boolean flagSelectJefe) {
        this.flagSelectJefe = flagSelectJefe;
    }

    public boolean getFlagSelectRespaldo() {
        return flagSelectRespaldo;
    }

    public void setFlagSelectRespaldo(boolean flagSelectRespaldo) {
        this.flagSelectRespaldo = flagSelectRespaldo;
    }

    public float getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(float salarioBase) {
        this.salarioBase = salarioBase;
    }

    public float getSalarioObra() {
        return salarioObra;
    }

    public void setSalarioObra(float salarioObra) {
        this.salarioObra = salarioObra;
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

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    public String getTipoSalario() {
        return tipoSalario;
    }

    public void setTipoSalario(String tipoSalario) {
        this.tipoSalario = tipoSalario;
    }

    public long getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(long fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
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
    
    

    // Metodos utilitarios de la clase Puesto.
    // Carga la lista de todos los puestos de la empresa, activos e inactivos.
    public String cargarLista(int idCentro, boolean modoVisualizacion, boolean modoEdicion){
        $ instanciaListaPuestos = null;
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
            // Realiza consulta de puestos de trabajo a la base de datos.
            instanciaListaPuestos = new $();
            ResultSet resultado = instanciaListaPuestos.execQry(
                    "SELECT ID_Puesto, P.Nombre NombrePuesto, D.Nombre NombreDepartamento, "
                    + "       CASE ID_Salario "
                    + "            WHEN 'T' THEN 'Tiempo' "
                    + "            WHEN 'O' THEN 'Obra' "
                    + "            when 'P' THEN 'Participacion' "
                    + "       END TipoSalario, "
                    + "       P.Activo Activo "
                    + "FROM Puesto P "
                    + "     INNER JOIN Departamento D "
                    + "             ON P.ID_Departamento = D.ID_Departamento "
                    + "WHERE ID_Centro = " + idCentro + " "
                    + "ORDER BY NombrePuesto;");

            if (resultado.next()) {
                respuesta =
                // Retroalimentación al usuario del proceso.
                "{ \"error\" : false, " +
                "  \"mensaje\" : \"\", " +
                // Información recuperada de la tabla Departamento.
                "  \"elementosHTML\": \"" +
                "            <h5 class='center'>" + TITULO +
                "              <a href='" + PAGINA_WEB + "' class='" + ETIQUETA_NUEVO + "'>" +
                "                <i class='material-icons right'>add_circle_outline</i>" +
                "              </a>" +
                "            </h5><hr/>" +
                "            <table id='myTable2'>" +
                "              <thead>" +
                "                <tr>" +
                "                  <th onclick='sortTable(0)' style='cursor:pointer' id='nombreCol'>Puesto de trabajo<i></th>" +
                "                  <th onclick='sortTable(1)' style='cursor:pointer' id='departamentoCol'>Departamento</th>" +
                "                  <th onclick='sortTable(2)' style='cursor:pointer' id='activoCol' class='center'>Activo</th>" +
                ( getModoVisualizacion() ? "<th class='center'>Ver</th>" : "" ) +
                ( getModoEdicion() ? "<th class='center'>Editar</th>" : "" ) +
                "                </tr>" +
                "              </thead>" +
                "            <tbody>";

                do {
                    // Si existen resultados crea arreglo JSON para mostrar la lista de puestos.
                    this.setIdPuesto(resultado.getInt("ID_Puesto"));
                    this.setNombre(resultado.getString("NombrePuesto"));
                    this.setNombreDepartamento(resultado.getString("NombreDepartamento"));
                    this.setTipoSalario(resultado.getString("TipoSalario"));
                    this.setActivo(resultado.getBoolean("Activo"));

                    respuesta += this.toHTML();
                } while (resultado.next());

                // Completa la estructura del objeto JSON de respuesta.
                respuesta = respuesta + "</tbody></table>\"}";
            } else
                respuesta = "{ \"error\" : false, " +
                      "  \"mensaje\" : \"No existen registros para mostrar.\" ," +
                         ESTRUCTURA_ERROR
                                .replace("$#titulo#$", TITULO)
                                .replace("$#paginaWeb#$", PAGINA_WEB)
                                .replace("$#etiquetaNuevo#$", ETIQUETA_NUEVO) +
                      "}";
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
            if (instanciaListaPuestos != null)
                instanciaListaPuestos.desconectar();

            instanciaListaPuestos = null;
            System.gc();
        }

        return respuesta;
    } // Fin del método cargarLista().

    public void cargarRegistro(int idPuesto) {
        setIdPuesto(-1);
        $ instanciaBD = null;
        $ instanciaBD2 = null;
        $ instanciaBD3 = null;
        ServiciosM modelo = new ServiciosM();

        try {
            if (idPuesto > 0) {
                instanciaBD = new $();
                String myQuery
                        = "SELECT ID_Departamento, ID_PuestoJefe, ID_PuestoRespaldo, "
                        + "       ID_Salario, Nombre, Descripcion, EdadMinima, "
                        + "       EdadMaxima, Genero, EstadoCivil, ExperienciaRequerida, "
                        + "       NotaExperiencia, Objetivo, Autoridad, SalarioBase,"
                        + "       SalarioObra, Participacion, Suelo, Techo, "
                        + "       Frecuencia, Activo, UsuarioModifico "
                        + "FROM Puesto "
                        + "WHERE ID_Puesto = " + idPuesto + ";";

                ResultSet resultado = instanciaBD.execQry(myQuery);
                if (resultado.next()) {
                    setIdPuesto(idPuesto);
                    setIdDepartamento(resultado.getInt("ID_Departamento"));
                    setIdPuestoJefe(resultado.getInt("ID_PuestoJefe"));
                    setIdPuestoRespaldo(resultado.getInt("ID_PuestoRespaldo"));
                    setIdSalario(resultado.getString("ID_Salario"));
                    setNombre(resultado.getString("Nombre"));
                    setDescripcion(resultado.getString("Descripcion"));
                    setEdadMinima(resultado.getInt("EdadMinima"));
                    setEdadMaxima(resultado.getInt("EdadMaxima"));
                    setGenero(resultado.getInt("Genero"));
                    setEstadoCivil(resultado.getInt("EstadoCivil"));
                    setExperienciaRequerida(resultado.getInt("ExperienciaRequerida"));
                    setNotaExperiencia(resultado.getString("NotaExperiencia"));
                    setObjetivo(resultado.getString("Objetivo"));
                    setAutoridad(resultado.getString("Autoridad"));
                    setSalarioBase(resultado.getFloat("SalarioBase"));
                    setSalarioObra(resultado.getFloat("SalarioObra"));
                    setParticipacion(resultado.getFloat("Participacion"));
                    setSuelo(resultado.getFloat("Suelo"));
                    setTecho(resultado.getFloat("Techo"));
                    setFrecuencia(resultado.getString("Frecuencia"));
                    setActivo(resultado.getInt("Activo") == 1);
                    setUsuarioModifico(resultado.getString("UsuarioModifico"));
                }
            } else {
                setIdPuesto(idPuesto);
                setIdDepartamento(0);
                setIdPuestoJefe(0);
                setIdPuestoRespaldo(0);
                setIdSalario(Consts.SALARIO_TIEMPO);
                setNombre("");
                setDescripcion("");
                setEdadMinima(18);
                setEdadMaxima(60);
                setGenero(0);
                setEstadoCivil(0);
                setExperienciaRequerida(0);
                setNotaExperiencia("");
                setObjetivo("");
                setAutoridad("");
                setSalarioBase(0);
                setSalarioObra(0);
                setParticipacion(0);
                setSuelo(0);
                setTecho(0);
                setFrecuencia(Consts.FRECUENCIA_MES);
                setActivo(true);
                setUsuarioModifico("");
            }

            // Carga la lista de departamento en un elemento SELECT-html para que el
            // usuario pueda seleccionar a cual departamento pertenecerá el puesto de
            // trabajo mostrado en pantalla.
            setSelectDepartamento(modelo.elementoSELECTDepartamento(getIdDepartamento()));

            // Carga la lista de puestos de trabajo en un elemento SELECT-html para
            // que el usuario pueda seleccionar el puesto del jefe y el puesto de
            // respaldo.
            instanciaBD2 = new $();
            setFlagSelectJefe(true);
            String myQuery
                    = "SELECT ID_Puesto, Nombre "
                    + "FROM Puesto "
                    + "WHERE Activo = 1 "
                    + "  AND ID_Departamento = " + getIdDepartamento() + " "
                    + "ORDER BY Nombre;";

            ResultSet listaDePuestos = instanciaBD2.execQry(myQuery);
            if (listaDePuestos.next()) {
                // Llena los elementos SELECT-html para el puesto del jefe y del respaldo.
                do {
                    // Crea elemento SELECT-html para el puesto del jefe.
                    if (getIdPuestoJefe() == listaDePuestos.getInt("ID_Puesto")) {
                        setSelectPuestoJefe(
                                "<option value='" + listaDePuestos.getInt("ID_Puesto") + "' selected>"
                                + listaDePuestos.getString("Nombre")
                                + "</option>"
                        );
                    } else {
                        setSelectPuestoJefe(
                                "<option value='" + listaDePuestos.getInt("ID_Puesto") + "'>"
                                + listaDePuestos.getString("Nombre")
                                + "</option>"
                        );
                    }

                    // Crea elemento SELECT-html para el puesto del respaldo.
                    if (getIdPuestoRespaldo() == listaDePuestos.getInt("ID_Puesto")) {
                        setSelectPuestoRespaldo(
                                "<option value='" + listaDePuestos.getInt("ID_Puesto") + "' selected>"
                                + listaDePuestos.getString("Nombre")
                                + "</option>"
                        );
                    } else {
                        setSelectPuestoRespaldo(
                                "<option value='" + listaDePuestos.getInt("ID_Puesto") + "'>"
                                + listaDePuestos.getString("Nombre")
                                + "</option>"
                        );
                    }
                } while (listaDePuestos.next());

                // Una vez que ha llenado los elementos define si estos se han de mostrar
                // o se ocultarán.
                myQuery
                        = // Determina si existen puestos con ID menor, para mostrar el elemento
                        // de HTML que permitirá seleccionar el puesto del jefe.
                        "SELECT CASE COUNT(*) "
                        + "       WHEN 0 THEN 1 "
                        + "       ELSE 0 END AS RESPUESTA_CONSULTA "
                        + "FROM Puesto "
                        + "WHERE ID_Puesto < " + getIdPuesto() + " "
                        + "  AND ID_Departamento = " + getIdDepartamento() + " "
                        + "UNION ALL "
                        + // Determina si existen puestos con ID mayor, para mostrar el elemento
                        // de HTML que permitirá seleccionar el puesto del respaldo.
                        "SELECT CASE COUNT(*) "
                        + "       WHEN 0 THEN 0 "
                        + "       ELSE 1 END AS RESPUESTA_CONSULTA "
                        + "FROM Puesto "
                        + "WHERE ID_Puesto > " + getIdPuesto() + " "
                        + "  AND ID_Departamento = " + getIdDepartamento() + ";";

                instanciaBD3 = new $();
                ResultSet flagPuestos = instanciaBD3.execQry(myQuery);
                flagPuestos.next();
                boolean esJefe = flagPuestos.getBoolean("RESPUESTA_CONSULTA");
                flagPuestos.next();
                boolean tieneRespaldo = flagPuestos.getBoolean("RESPUESTA_CONSULTA");

                if (esJefe) {
                    // No muestra el elemento SELECT-html para elegir el jefe debido a que
                    // este registro que se está modificando corresponde al jefe.
                    setFlagSelectJefe(false);
                    setSelectPuestoJefe("");
                }

                if (tieneRespaldo) // Debido a que existen más puestos para el mismo departamento y no es
                // el jefe se muestran los elementos SELECT-html para elegir el jefe y
                // el respaldo.
                {
                    setFlagSelectRespaldo(true);
                } else {
                    setSelectPuestoRespaldo("");
                    setFlagSelectRespaldo(false);
                }
            } else {
                // Como no existen más puestos para el departamento se ocultan los
                // elementos SELECT-html para elegir el jefe y el respaldo.
                setFlagSelectJefe(false);
                setFlagSelectRespaldo(false);
                setSelectPuestoJefe("");
                setSelectPuestoRespaldo("");
            }

            // Carga la lista de estados civiles en un elemento SELECT-html para que
            // el usuario pueda seleccionar el estado civil apropiado al puesto de
            // trabajo.
            setSelectEstadoCivil(modelo.elementoSELECTEstadoCivil(getEstadoCivil()));

            // Carga la lista de tiempos de experiencia requerida en un elemento
            // SELECT-html para que el usuario pueda seleccionar el período de
            // experiencia que el puesto de trabajo requiera.
            setSelectExperiencia(modelo.elementoSELECTExperiencia(getExperienciaRequerida()));

            // Carga la lista de géneros en un elemento SELECT-html para seleccionar
            // el género que requiere el puesto de trabajo.
            setSelectGenero(modelo.elementoSELECTGenero(getGenero()));
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
    } // Fin del metodo cargarRegistro().

    
    public String toJSON() {
        // Parsea la clase Puesto a JSON.
        String respuesta
                = "{"
                + "   \"error\": true, "
                + "   \"mensaje\": \" Ocurrió un error al cargar el registro Puesto.\" "
                + "}";

        if (getIdPuesto() >= 0) {
            respuesta
                    = "{"
                    + "   \"codigo\": " + getIdPuesto() + ","
                    + "   \"codigo2\": \"" + getIdSalario() + "\","
                    + "   \"nombre\": \"" + getNombre() + "\","
                    + "   \"nombreDepartamento\": \"" + getNombreDepartamento() + "\","
                    + "   \"descripcion\": \"" + getDescripcion() + "\","
                    + "   \"edadMinima\": " + getEdadMinima() + ","
                    + "   \"edadMaxima\": " + getEdadMaxima() + ","
                    + "   \"notaExperiencia\": \"" + getNotaExperiencia() + "\","
                    + "   \"objetivo\": \"" + getObjetivo() + "\","
                    + "   \"autoridad\": \"" + getAutoridad() + "\","
                    + "   \"tipoSalario\": \"" + getTipoSalario() + "\","
                    + "   \"salarioBase\": " + getSalarioBase() + ","
                    + "   \"salarioObra\": " + getSalarioObra() + ","
                    + "   \"participacion\": " + getParticipacion() + ","
                    + "   \"suelo\": " + getSuelo() + ","
                    + "   \"techo\": " + getTecho() + ","
                    + "   \"frecuencia\": \"" + getFrecuencia() + "\","
                    + "   \"activo\": " + getActivo() + ","
                    + "   \"usuarioModifico\": \"" + getUsuarioModifico() + "\","
                    + "   \"flagSelectJefe\": " + getFlagSelectJefe() + ","
                    + "   \"flagSelectRespaldo\": " + getFlagSelectRespaldo() + ","
                    + "   \"selectDepartamento\": \"" + getSelectDepartamento() + "\","
                    + "   \"selectPuestoJefe\": \"" + getSelectPuestoJefe() + "\","
                    + "   \"selectPuestoRespaldo\": \"" + getSelectPuestoRespaldo() + "\","
                    + "   \"selectEstadoCivil\": \"" + getSelectEstadoCivil() + "\","
                    + "   \"selectExperiencia\": \"" + getSelectExperiencia() + "\","
                    + "   \"selectGenero\": \"" + getSelectGenero() + "\","
                    + "   \"error\": false"
                    + "}";
        }

        return ServiciosU.sustituirCaracteres(respuesta);
    } // Fin del metodo toJSON().
    
    
    // Devuelve el registro con formato de tabla HTML.
    private String toHTML() {
        return "<tr style='border: none;' class='resaltar'>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + getNombre() + "</td>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + getNombreDepartamento() + "</td>" +
               "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'>" +
               "    <i class='material-icons'>" + 
                      (getActivo() ? "check_box": "check_box_outline_blank") + 
               "    </i></a>" +
               "  </td>" +
               ( getModoVisualizacion() ? 
               "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'><a href='" + PAGINA_WEB + "' class='" + ETIQUETA_EXISTENTE + "' " +
               "      data-codigo='" + getIdPuesto() + "' " +
               "      data-modo='mod-v'>" +
               "    <i class='material-icons'>visibility</i></a>" +
               "  </td>" : "" ) +
               ( getModoEdicion() ? 
               "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'><a href='" + PAGINA_WEB + "' class='" + ETIQUETA_EXISTENTE + "' " +
               "      data-codigo='" + getIdPuesto() + "' " +
               "      data-modo='mod-e'>" +
               "    <i class='material-icons'>edit</i></a>" +
               "  </td>" : "" ) +
               "</tr>";
    }
} // Fin de la clase Puesto.
