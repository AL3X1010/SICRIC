/*  Nombre.....:  Consts.java
 *  Descripción:  Contiene los contrusctores y utilitarios requeridos del sistema.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Julio, 2019.
 */
package Utilitarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Consts {
    // Constructor de la clase Consts.
    private Consts(){
        throw new AssertionError();
    } // Fin del métodoConstructor().
    
    // Constantes de configuración de la empresa.
    public static final String SIN_CONFIGURACION = "&";
    public static final String CENTRO_CONFIGURADO = "$";
    public static final String DEPARTAMENTO_CONFIGURADO = "#";
    public static final String PUESTO_CONFIGURADO = "%";
    public static final String APP_CONFIGURADA = "@";
    
    
    // Variables constantes tipo entero.
    public static final int ROL_SA = 1;
    public static final int ROL_ADMINISTRADOR = 2;
    public static final int ROL_FISIOTERAPEUTA = 3;
    public static final int ROL_COLABORADOR = 4;
    public static final int ROL_FARMACIA_APROBADOR = 5;
    public static final int ROL_FARMACIA_OPERADOR = 6;
    public static final int ROL_FARMACIA_MIXTO = 7;
    
    public static final int NO_ENCONTRADO = -1;
    public static final int FECHA_PREDEFINIDA = 19400101;
    public static final int HORA_INICIO_PREDEFINIDA = 400;
    public static final int HORA_FINAL_PREDEFINIDA = 2200;
    public static final int LONGITUD_TIMESTAMP = 17;
    public static final long MENOS_UNA_SEMANA = -7000000000L;
    
    // Variables constantes tipo texto.
    public static final String BLOQUE_A = "a";
    public static final String BLOQUE_B = "b";
    public static final String BLOQUE_C = "c";
    public static final String BLOQUE_D = "d";
    public static final String BLOQUE_E = "e";
    public static final String CODIGO_ERRONEO = "-1";
    public static final String CONSULTA = "C";
    public static final String CONTRASENIA_SIN_USUARIO = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String DAR_DE_BAJA = "-1";
    public static final String DESCRIPCION_SALARIO_BASE = "Salario base";
    public static final String DESCRIPCION_SALARIO_NETO = "Salario neto";
    public static final String error = "error";
    public static final String EDICION = "mod-e";
    public static final String ESTADO_CIVIL_CASADO = "C";
    public static final String ESTADO_CIVIL_DIVORCIADO = "D";
    public static final String ESTADO_CIVIL_INDISTINTO = "I";
    public static final String ESTADO_CIVIL_SOLTERO = "S";
    public static final String ESTADO_CIVIL_UNION_LIBRE = "U";
    public static final String ESTADO_CIVIL_VIUDO = "V";
    public static final String FRECUENCIA_MES = "M";
    public static final String FRECUENCIA_QUINCENA = "Q";
    public static final String FRECUENCIA_DIA = "D";
    public static final String FRECUENCIA_HORA = "H";
    public static final int GENERO_FEMENINO = 1;
    public static final int GENERO_MASCULINO = 2;
    public static final int ID_ANTICIPO = 3;
    public static final String LISTA_ANTIGUA = "A";
    public static final String LISTA_RECIENTE = "R";
    public static final String MODIFICACION = "M";
    public static final String ORIGEN_TEMPORAL = "T";
    public static final String ORIGEN_MAESTRO = "M";
    
    public static final String CODIGO_PROVEE_EQUIPOS_MEDICOS = "E";
    public static final String PROVEE_EQUIPOS_MEDICOS = "Únicamente equipos médicos";
    public static final String CODIGO_PROVEE_MEDICAMENTOS = "M";
    public static final String PROVEE_MEDICAMENTOS = "Únicamente medicamentos";
    public static final String CODIGO_PROVEE_SUMINISTROS_MEDICOS = "S";
    public static final String PROVEE_SUMINISTROS_MEDICOS = "Únicamente suministros médicos";
    public static final String CODIGO_PROVEE_MEDICAMENTOS_EQUIPOS = "1";
    public static final String PROVEE_MEDICAMENTOS_EQUIPOS = "Provee medicamentos y equipos médicos";
    public static final String CODIGO_PROVEE_EQUIPOS_SUMINISTROS = "2";
    public static final String PROVEE_EQUIPOS_SUMINISTROS = "Provee equipos y suministros médicos";
    public static final String CODIGO_PROVEE_MEDICAMENTOS_SUMINISTROS = "3";
    public static final String PROVEE_MEDICAMENTOS_SUMINISTROS = "Provee medicamentos y suministros médicos";
    public static final String CODIGO_PROVEE_MEDICAMENTOS_EQUIPOS_SUMINISTROS = "4";
    public static final String PROVEE_MEDICAMENTOS_EQUIPOS_SUMINISTROS = "Provee medicamentos, equipos y suministros médicos";
    
    
    public static final String SALARIO_TIEMPO = "T";
    public static final String SALARIO_OBRA = "O";
    public static final String SALARIO_PARTICIPACION = "P";
    public static final int SEGUNDA_QUINCENA = 2;
    public static final String SIN_USUARIO = "SIN USUARIO";
    public static final String SUBTIPO_FIJO = "F";
    public static final String SUBTIPO_VARIABLE = "V";
    public static final String TEXTO_SEPARADOR_REGISTROS = "{\"codigoEmpleado\":";
    public static final String TIPO_SALARIO_TIEMPO = "T";
    public static final String TIPO_SALARIO_OBRA = "O";
    public static final String TIPO_SALARIO_PARTICIPACION = "P";
    public static final String VISUALIZACION = "mod-v";
    public static final String YYYYMMDD = "YYYYMMDD";
    
    public static final String INICIO_ADMIRACION = "¡";
    public static final String A_MAYUSCULA = "A";
    public static final String E_MAYUSCULA = "E";
    public static final String I_MAYUSCULA = "I";
    public static final String O_MAYUSCULA = "O";
    public static final String U_MAYUSCULA = "U";
    public static final String N_MAYUSCULA = "N";
    public static final String A_MINUSCULA = "a";
    public static final String E_MINUSCULA = "e";
    public static final String I_MINUSCULA = "i";
    public static final String O_MINUSCULA = "o";
    public static final String U_MINUSCULA = "u";
    public static final String N_MINUSCULA = "n";
    public static final String A_MAYUSCULA_TILDADA = "Á";
    public static final String E_MAYUSCULA_TILDADA = "É";
    public static final String I_MAYUSCULA_TILDADA = "Í";
    public static final String O_MAYUSCULA_TILDADA = "Ó";
    public static final String U_MAYUSCULA_TILDADA = "Ú";
    public static final String N_MAYUSCULA_TILDADA = "Ñ";
    public static final String A_MINUSCULA_TILDADA = "á";
    public static final String E_MINUSCULA_TILDADA = "é";
    public static final String I_MINUSCULA_TILDADA = "í";
    public static final String O_MINUSCULA_TILDADA = "ó";
    public static final String U_MINUSCULA_TILDADA = "ú";
    public static final String N_MINUSCULA_TILDADA = "ñ";
    public static final String INICIO_ADMIRACION_UNICODE = "\\u00A1";
    public static final String A_MAYUSCULA_TILDADA_UNICODE = "\\u00C1";
    public static final String E_MAYUSCULA_TILDADA_UNICODE = "\\u00C9";
    public static final String I_MAYUSCULA_TILDADA_UNICODE = "\\u00CD";
    public static final String O_MAYUSCULA_TILDADA_UNICODE = "\\u00D3";
    public static final String U_MAYUSCULA_TILDADA_UNICODE = "\\u00DA";
    public static final String N_MAYUSCULA_TILDADA_UNICODE = "\\u00D1";
    public static final String A_MINUSCULA_TILDADA_UNICODE = "\\u00E1";
    public static final String E_MINUSCULA_TILDADA_UNICODE = "\\u00E9";
    public static final String I_MINUSCULA_TILDADA_UNICODE = "\\u00ED";
    public static final String O_MINUSCULA_TILDADA_UNICODE = "\\u00F3";
    public static final String U_MINUSCULA_TILDADA_UNICODE = "\\u00FA";
    public static final String N_MINUSCULA_TILDADA_UNICODE = "\\u00F1";
    
    public static final List<String> ARREGLO_MESES_3 =
            new ArrayList<>(Arrays.asList("",
                  "Ene", "Feb", "Mar", "Abr", "May", "Jun",
                  "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"));
    
    public static final String SPI_CENTRO = "SPI_Centro";
    public static final int CANT_PARMS_SPI_CENTRO = 6;
    
    public static final String SPU_CENTRO = "SPU_Centro";
    public static final int CANT_PARMS_SPU_CENTRO = 23;
    
    
    public static final String ASIGNAR_MODALIDAD_HTML = "asignarmodalidad.html";
    
    
    
    
    
    
    
    
    
    
    
    
    public static final int SQL_OK = 1;
    public static final String SEPARADOR = "$#@||@#$";
    public static final String SEPARADOR_REGEX = "\\$#@\\|\\|@#\\$";
    
    
    // Constantes necesarias para registrar acciones en bitácora.
    public static final String INDEX_HTML = "index.html";
    public static final String LOBBY_HTML = "lobby.html";
    public static final String LOBBY_ADMIN_HTML = "lobbyad.html";
    public static final String LOBBY_SA_HTML = "lobbysa.html";
    
    // *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
    public static final int MODULO_ORGANIZACION = 1;
    // *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
    public static final String CENTRO_HTML = "centro.html";
    
    //. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
    public static final String SP_DEPARTAMENTO = "SP_Departamento";
    public static final int CANT_PARMS_SP_DEPARTAMENTO = 8;
    
    public static final String LOBBY_DEPARTAMENTOS_HTML = "lobbydepartamentos.html";
    public static final String ETIQUETA_LOBBY_DEPARTAMENTOS = "LobbyDepartamentos";
    public static final String TITULO_LISTA_DEPARTAMENTOS = "Departamentos en la Empresa";
    public static final String ETIQUETA_EXISTENTE_DEPARTAMENTO = "departamentoExistente";
    public static final String ETIQUETA_NUEVO_DEPARTAMENTO = "departamentoNuevo";
    
    public static final String DEPARTAMENTO_HTML = "departamento.html";
    public static final String ETIQUETA_DEPARTAMENTO = "Departamento";
    
    
    //. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
    
    public static final String SP_PUESTO = "SP_Puesto";
    public static final int CANT_PARMS_SP_PUESTO = 29;
    
    public static final String LOBBY_PUESTOS_HTML = "lobbypuestos.html";
    public static final String ETIQUETA_LOBBY_PUESTOS = "LobbyPuestos";
    public static final String TITULO_LISTA_PUESTOS = "Puestos de Trabajo de la Empresa";
    public static final String ETIQUETA_EXISTENTE_PUESTO = "puestoExistente";
    public static final String ETIQUETA_NUEVO_PUESTO = "puestoNuevo";
    
    public static final String PUESTO_HTML = "puesto.html";
    public static final String ETIQUETA_PUESTO = "Puesto";
    
    //. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
    
    public static final String SP_EMPLEADO = "SP_Empleado";
    public static final int CANT_PARMS_SP_EMPLEADO = 26;
    
    public static final String LOBBY_EMPLEADOS_HTML = "lobbyempleados.html";
    public static final String ETIQUETA_LOBBY_EMPLEADOS = "LobbyEmpleados";
    public static final String TITULO_LISTA_EMPLEADOS = "Empleados de la Empresa";
    public static final String ETIQUETA_EXISTENTE_EMPLEADO = "empleadoExistente";
    public static final String ETIQUETA_NUEVO_EMPLEADO = "empleadoNuevo";
    
    public static final String EMPLEADO_HTML = "empleado.html";
    public static final String ETIQUETA_EMPLEADO = "Empleado";
    
    //. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
    
    public static final String SP_PROVEEDOR = "SP_Proveedor";
    public static final int CANT_PARMS_SP_PROVEEDOR = 13;
    
    public static final String LOBBY_PROVEEDORES_HTML = "lobbyproveedores.html";
    public static final String ETIQUETA_LOBBY_PROVEEDORES = "LobbyProveedores";
    public static final String TITULO_LISTA_PROVEEDORES = "Proveedores de la Empresa";
    public static final String ETIQUETA_EXISTENTE_PROVEEDOR = "proveedorExistente";
    public static final String ETIQUETA_NUEVO_PROVEEDOR = "proveedorNuevo";
    
    public static final String PROVEEDOR_HTML = "proveedor.html";
    public static final String ETIQUETA_PROVEEDOR = "Proveedor";
    
    
    
    // *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
    public static final int MODULO_ADMON_REHABILITACION = 2;
    // *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
    public static final String SP_MODALIDAD = "SP_Modalidad";
    public static final int CANT_PARMS_SP_MODALIDAD = 7;
    
    public static final String LOBBY_MODALIDADES_HTML = "lobbymodalidades.html";
    public static final String ETIQUETA_LOBBY_MODALIDADES = "LobbyModalidades";
    public static final String TITULO_LISTA_MODALIDADES = "Modalidades de la Empresa";
    public static final String ETIQUETA_EXISTENTE_MODALIDAD = "modalidadExistente";
    public static final String ETIQUETA_NUEVO_MODALIDAD = "modalidadNuevo";
    
    public static final String MODALIDAD_HTML = "modalidad.html";
    public static final String ETIQUETA_MODALIDAD = "Modalidad";
    
    //. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
    public static final String SP_CENTRO_REMISION = "SP_CentroRemision";
    public static final int CANT_PARMS_SP_CENTRO_REMISION = 8;
    
    public static final String LOBBY_CENTROS_REM_HTML = "lobbycentrosremision.html";
    public static final String ETIQUETA_LOBBY_CENTROS_REM = "LobbyCentrosRemision";
    public static final String TITULO_LISTA_CENTROS_REM = "Centros de Remisión";
    public static final String ETIQUETA_EXISTENTE_CENTRO_REM = "centroRemisionExistente";
    public static final String ETIQUETA_NUEVO_CENTRO_REM = "centroRemisionNuevo";
    
    public static final String CENTRO_REM_HTML = "centroremision.html";
    public static final String ETIQUETA_CENTRO_REM = "CentroRemision";
    
    //. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
    
    public static final String SP_PACIENTE = "SP_Paciente";
    public static final int CANT_PARMS_SP_PACIENTE = 19;
    
    public static final String LOBBY_PACIENTES_HTML = "lobbypacientes.html";
    public static final String ETIQUETA_LOBBY_PACIENTES = "LobbyPacientes";
    public static final String TITULO_LISTA_PACIENTES = "Pacientes de la Empresa";
    public static final String ETIQUETA_EXISTENTE_PACIENTE = "pacienteExistente";
    public static final String ETIQUETA_NUEVO_PACIENTE = "pacienteNuevo";
    
    public static final String PACIENTE_HTML = "paciente.html";
    public static final String ETIQUETA_PACIENTE = "Paciente";
    
    
    // *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
    public static final int MODULO_REHABILITACION = 3;
    // *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
    public static final String AGENDA_HTML = "agenda.html";
    
    
    public static final String SP_CITA_PLAN_REHABILITACION = "SP_CitaPlanRehabilitacion";
    public static final int CANT_PARMS_SP_CITA_PLAN_REHABILITACION = 9;
    public static final String CITA_MEDICA_HTML = "citamedica.html";
    
    public static final String SP_PLAN_REHABILITACION = "SP_PlanRehabilitacion";
    public static final int CANT_PARMS_SP_PLAN_REHABILITACION = 8;
    public static final String SP_DET_PLAN_REHABILITACION = "SP_DetallePlanRehabilitacion";
    public static final int CANT_PARMS_SP_DET_PLAN_REHABILITACION = 10;
    public static final String PLAN_REHABILITACION_HTML = "planrehabilitacion.html";
    
    public static final String SP_CHECK_IN_PLAN_REHABILITACION = "SP_CheckInPlanRehabilitacion";
    public static final int CANT_PARMS_SP_CHECK_IN_PLAN_REHABILITACION = 15;
    public static final String CHECKIN_HTML = "checkin.html";
    
    
    // *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
    public static final int MODULO_INVENTARIO_FARMACIA = 4;
    // *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
    public static final String SP_CATALOGO_MEDICAMENTO = "SP_CatalogoMedicamento";
    public static final int CANT_PARMS_SP_CATALOGO_MEDICAMENTO = 10;
    
    public static final String LOBBY_CATALOGO_MEDICAMENTOS_HTML = "lobbycatalogomedicamentos.html";
    public static final String ETIQUETA_LOBBY_CATALOGO_MEDICAMENTOS = "LobbyCatalogoMedicamentos";
    public static final String TITULO_LISTA_CATALOGO_MEDICAMENTOS = "Catálogo de Medicamentos";
    public static final String ETIQUETA_EXISTENTE_CATALOGO_MEDICAMENTO = "catalogoMedicamentoExistente";
    public static final String ETIQUETA_NUEVO_CATALOGO_MEDICAMENTO = "catalogoMedicamentoNuevo";
    
    public static final String CATALOGO_MEDICAMENTO_HTML = "catalogomedicamento.html";
    public static final String ETIQUETA_CATALOGO_MEDICAMENTO = "CatalogoMedicamento";
    
    //. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
    
    public static final String SP_INV_MEDICAMENTO = "SP_InventarioMedicamento";
    public static final int CANT_PARMS_SP_INV_MEDICAMENTO = 9;
    
    public static final String LOBBY_MEDICAMENTOS_HTML = "lobbymedicamentos.html";
    public static final String ETIQUETA_LOBBY_MEDICAMENTOS = "LobbyMedicamento";
    public static final String TITULO_LISTA_MEDICAMENTOS = "Inventario de Medicamentos";
    public static final String ETIQUETA_EXISTENTE_MEDICAMENTO = "medicamentoExistente";
    public static final String ETIQUETA_NUEVO_MEDICAMENTO = "medicamentoNuevo";
    
    public static final String MEDICAMENTO_HTML = "medicamento.html";
    public static final String ETIQUETA_MEDICAMENTO = "Medicamento";
    
    //. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
    
    public static final String SP_EXPEDIR_MEDICAMENTO = "SP_ExpedirMedicamento";
    public static final int CANT_PARMS_SP_EXPEDIR_MEDICAMENTO = 7;
    public static final String EXPEDIR_MEDICAMENTO_HTML = "expedirmedicamento.html";
    
    public static final String SP_AGREGAR_MEDICAMENTO = "SP_AgregarMedicamento";
    public static final int CANT_PARMS_SP_AGREGAR_MEDICAMENTO = 11;
    public static final String AGREGAR_MEDICAMENTO_HTML = "agregarmedicamento.html";
    public static final String ETIQUETA_AGREGAR_MEDICAMENTO = "AgregarMedicamento";
    
    public static final String SP_APR_DEVOLUCION_MEDICAMENTO = "SP_AprobarDevolucionMedicamento";
    public static final int CANT_PARMS_SP_APR_DEVOLUCION_MEDICAMENTO = 11;
    public static final String APROBAR_DEVOLUCION_MEDICAMENTO_HTML = "aprobardevolucion.html";
    public static final String ETIQUETA_DEVOLUCION_MEDICAMENTO = "DevolucionMedicamento";
    
    public static final String SP_DEVOLUCION_MEDICAMENTO = "SP_DevolucionMedicamento";
    public static final int CANT_PARMS_SP_DEVOLUCION_MEDICAMENTO = 9;
    public static final String DEVOLUCION_MEDICAMENTO_HTML = "devolucionmedicamento.html";
    
    
    
    
    
    // *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
    public static final int MODULO_INVENTARIO_EQUIPO = 5;
    // *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
    public static final String SP_CATALOGO_EQUIPO_MEDICO = "SP_CatalogoEquipo";
    public static final int CANT_PARMS_SP_CATALOGO_EQUIPO_MEDICO = 11;
    
    public static final String LOBBY_CATALOGO_EQUIPOS_HTML = "lobbycatalogoequipos.html";
    public static final String ETIQUETA_LOBBY_CATALOGO_EQUIPOS = "LobbyCatalogoEquipos";
    public static final String TITULO_LISTA_CATALOGO_EQUIPOS = "Catálogo de Equipos Médicos";
    public static final String ETIQUETA_EXISTENTE_CATALOGO_EQUIPO = "catalogoEquipoExistente";
    public static final String ETIQUETA_NUEVO_CATALOGO_EQUIPO = "catalogoEquipoNuevo";
    
    public static final String CATALOGO_EQUIPO_HTML = "catalogoequipo.html";
    public static final String ETIQUETA_CATALOGO_EQUIPO = "CatalogoEquipo";
    
    //. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
    
    public static final String SP_INV_EQUIPO_MEDICO = "SP_InventarioEquipoMedico";
    public static final int CANT_PARMS_SP_INV_EQUIPO_MEDICO = 11;
    
    public static final String LOBBY_EQUIPOS_HTML = "lobbyequipos.html";
    public static final String ETIQUETA_LOBBY_EQUIPOS = "LobbyEquipos";
    public static final String TITULO_LISTA_EQUIPOS = "Inventario de Equipos Médicos";
    public static final String ETIQUETA_EXISTENTE_EQUIPO = "equipoExistente";
    public static final String ETIQUETA_NUEVO_EQUIPO = "equipoNuevo";
    
    public static final String EQUIPO_HTML = "equipo.html";
    public static final String ETIQUETA_EQUIPO = "EquipoMedico";
    
    //. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
    
    public static final String SP_CATALOGO_SUMINISTRO_MEDICO = "SP_CatalogoSuministro";
    public static final int CANT_PARMS_SP_CATALOGO_SUMINISTRO_MEDICO = 10;
    
    public static final String LOBBY_CATALOGO_SUMINISTROS_HTML = "lobbycatalogosuministros.html";
    public static final String ETIQUETA_LOBBY_CATALOGO_SUMINISTROS = "LobbyCatalogoSuministros";
    public static final String TITULO_LISTA_CATALOGO_SUMINISTROS = "Catálogo de Suministros Médicos";
    public static final String ETIQUETA_EXISTENTE_CATALOGO_SUMINISTRO = "catalogoSuministroExistente";
    public static final String ETIQUETA_NUEVO_CATALOGO_SUMINISTRO = "catalogoSuministroNuevo";
    
    public static final String CATALOGO_SUMINISTRO_HTML = "catalogosuministro.html";
    public static final String ETIQUETA_CATALOGO_SUMINISTRO = "CatalogoSuministro";
    
    //. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
    
    public static final String SP_INV_SUMINISTRO_MEDICO = "SP_InventarioSuministroMedico";
    public static final int CANT_PARMS_SP_INV_SUMINISTRO_MEDICO = 9;
    
    public static final String LOBBY_SUMINISTROS_HTML = "lobbysuministros.html";
    public static final String ETIQUETA_LOBBY_SUMINISTROS = "LobbySuministros";
    public static final String TITULO_LISTA_SUMINISTROS = "Inventario de Suministros Médicos";
    public static final String ETIQUETA_EXISTENTE_SUMINISTRO = "suministroExistente";
    public static final String ETIQUETA_NUEVO_SUMINISTRO = "suministroNuevo";
    
    public static final String SUMINISTRO_HTML = "suministro.html";
    public static final String ETIQUETA_SUMINISTRO = "SuministroMedico";
    
    public static final String SP_ASIG_SUMINISTRO_MEDICO = "SP_AsignarSuministroMedico";
    public static final int CANT_PARMS_SP_ASIG_SUMINISTRO_MEDICO = 9;
    public static final String ASIGNAR_SUMINISTRO_HTML = "asignarsuministro.html";
    
    //. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
    
    public static final String SP_ADIC_SUMINISTRO_MEDICO = "SP_AgregarSuministroMedico";
    public static final int CANT_PARMS_SP_ADIC_SUMINISTRO_MEDICO = 11;
    
    public static final String AGREGAR_SUMINISTRO_HTML = "agregarsuministro.html";
    public static final String ETIQUETA_AGREGAR_SUMINISTRO = "AgregarSuministroMedico";
    
    
    
    
    
    // *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
    public static final int MODULO_SEGURIDAD = 101;
    // *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
    public static final String SP_USUARIO = "SP_Departamento";
    public static final int CANT_PARMS_SP_USUARIO = 8;
    
    public static final String LOBBY_USUARIOS_HTML = "lobbyusuarios.html";
    public static final String ETIQUETA_LOBBY_USUARIOS = "LobbyUsuarios";
    public static final String TITULO_LISTA_USUARIOS = "Usuarios del Sistema";
    public static final String ETIQUETA_EXISTENTE_USUARIO = "usuarioExistente";
    public static final String ETIQUETA_NUEVO_USUARIO = "usuarioNuevo";
    
    public static final String USUARIO_HTML = "usuario.html";
    public static final String ETIQUETA_USUARIO = "Usuario";
    
    public static final String SP_RESPALDAR_BD = "SP_RespaldarBD";
    public static final String SP_RESTAURAR_BD = "SP_RestaurarBD";
    public static final String RESPALDO_HTML = "respaldo.html";
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public static final int MODULO_MANTENIMIENTO = 6;
    public static final int MODULO_REPORTERIA = 7;
    public static final String REPORTE_HTML = "reporte.html";
    public static final String LOBBY_SESIONES_HTML = "lobbysesiones.html";
    public static final String ETIQUETA_SESION = "SesionExistente";
    public static final String DETALLE_SESION_HTML = "detallesesion.html";
    public static final String ETIQUETA_LOBBY_SESIONES = "LobbySesiones";
    public static final String REPORTE_INV_EQUIPO_MEDICO = "equipo";
    public static final String REPORTE_INV_SUMINISTRO_MEDICO = "insumo";
    public static final String REPORTE_INV_MEDICAMENTO = "medicamento";
    public static final String REPORTE_COSTOS = "costos";
    public static final String REPORTE_TERAPEUTA = "terapeuta";
    public static final String REPORTE_ASISTENCIA = "asistencia";
    public static final String REPORTE_BITACORA_X_SESION = "sesion";
    public static final String REPORTE_BITACORA_X_EVENTO = "evento";
    
    public static final String ACCION_ADICION = "A";
    public static final String ACCION_ELIMINACION = "E";
    public static final String ACCION_INGRESO = "I";
    public static final String ACCION_MODIFICACION = "M";
    public static final String ACCION_VISUALIZACION = "V";
    
    public static final String ETIQUETA_INGRESO = "INGRESO";
    public static final String ETIQUETA_NUEVO = "NUEVO";
    public static final String ETIQUETA_LOBBY = "---";
    
    public static final String CARPETA_APP = "SICRIC_App";
    public static final String SUBCARPETA_APP = "Centro";
    public static final String CARPETA_CENTRO = "arc_centro";
    public static final String PREFIJO_CENTRO = "cnt_";
    public static final String REMITE_CENTRO = "C";
    public static final int NUMERO_MAXIMO_ARCHIVOS_CENTRO = 1;
    public static final String EXTENSION_PERMITIDA_CENTRO = ".png";
    public static final String CARPETA_EMPLEADOS = "arc_empleados";
    public static final String PREFIJO_EMPLEADOS = "emp_";
    public static final String REMITE_EMPLEADO = "E";
    public static final int NUMERO_MAXIMO_ARCHIVOS_EMPLEADO = 1;
    public static final String EXTENSION_PERMITIDA_EMPLEADO = ".pdf";
    public static final String CARPETA_PACIENTES = "arc_pacientes";
    public static final String PREFIJO_PACIENTES = "pcn_";
    public static final String REMITE_PACIENTE = "P";
    public static final int NUMERO_MAXIMO_ARCHIVOS_PACIENTE = 255;
    public static final String[] EXTENSIONES_PERMITIDAS_PACIENTE = {".pdf", ".png"};
    public static final String CAMPO_ARCHIVO = "nombreArchivo";
    public static final int GRAVEDAD_ERROR = 9;
    public static final int GRAVEDAD_ADVERTENCIA = 1;
    public static final int GRAVEDAD_CERO = 0;
} // Fin de la clase Consts.