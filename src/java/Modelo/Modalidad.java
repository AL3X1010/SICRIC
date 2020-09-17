/*  Nombre.....:  Modalidad.java
 *  Descripción:  Carga el registro de la tabla Modalidad.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Diciembre, 2019.
 */
package Modelo;

import Utilitarios.Consts;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Modalidad {
    // Constantes de la clase Modalidad.
    private final String PAGINA_WEB = Consts.MODALIDAD_HTML;
    private final String TITULO = Consts.TITULO_LISTA_MODALIDADES;
    private final String ETIQUETA_EXISTENTE = Consts.ETIQUETA_EXISTENTE_MODALIDAD;
    private final String ETIQUETA_NUEVO = Consts.ETIQUETA_NUEVO_MODALIDAD;
    
    // Variables de la clase Modalidad.
    private int idModalidad;
    private String nombre;
    private String jqueryOpciones;
    private boolean activo;
    private int cantidadElementos;
    private int cantidadPlanes;
    private int usuarioModifico;
    private long fechaModificacion;
    private boolean modoVisualizacion;
    private boolean modoEdicion;

    
    // Método constructor de la clase Modalidad.
    public Modalidad() {
        this.idModalidad = -1;
        this.nombre = "";
        this.jqueryOpciones = "";
        this.cantidadElementos = 0;
        this.cantidadPlanes = 0;
        this.activo = true;
        this.usuarioModifico = 0;
    } // Fin del método constructor.
    
    
    // Metodos GET y SET de la clase Modalidad.
    public int getIdModalidad(){
        return idModalidad;
    }

    public void setIdModalidad(int idModalidad) {
        this.idModalidad = idModalidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getElementos_Y_Opciones() throws ClassNotFoundException, SQLException {
        if(getIdModalidad() == 0 )
            return "";
        
        $ instanciaBD = new $();
        String respuesta = "";
        String jQueryOpciones = "";   // lleva todas las opciones como un JSON para ejecutarlos.
        int consecutivoIds = 1; // id consecutivo para etiquetas de inputs y chips
        
        // Recupera los elementos que componen la modalidad actual.
        String myQuery = 
                    "SELECT Correlativo, TipoElemento, Etiqueta1, Etiqueta2 " +
                    "FROM Elementos_Modalidad " +
                    "WHERE ID_Modalidad = " + getIdModalidad() + " " +
                    "ORDER BY Correlativo;";
        
        // Recupera los elementos que componen la modalidad actual.
        ResultSet resultadoElementos = instanciaBD.execQry(myQuery);
        while(resultadoElementos.next()){
            int correlativoElemento = resultadoElementos.getInt("Correlativo");
            String tipoElemento = resultadoElementos.getString("TipoElemento");
            String etiqueta1 = resultadoElementos.getString("Etiqueta1");
            String etiqueta2 = resultadoElementos.getString("Etiqueta2");

            // Crea el elemento HTML para los elementos que integran la
            // modalidad actual según su tipo.
            if(tipoElemento.equals("AYT")){
                respuesta +=
                "<div class='row elementoSeleccionado' data-tipo='areaYtiempo'>" +
                "   <div class='col s6'>" +
                "     Área:" +
                "     <div class='input-field inline'>" +
                "       <input type='text' class='validate campoLetrasEspacios center' disabled value='M u e s t r a'>"+
                "     </div>" +
                "   </div>" +
                "   <div class='col s5'>" +
                "     Tiempo:" +
                "     <div class='input-field inline'>" +
                "       <input type='text' class='validate campoEntero center' disabled value='M u e s t r a'>" +
                "     </div>" +
                "   </div>" +
                "   <div class='col s1 center'>" +
                "     <i class='material-icons btnEliminar' style='padding-top: 100%; cursor: pointer; color: red;'>delete</i>" +
                "   </div>" +
                "</div>";
                
                continue;
            } else if(tipoElemento.equals("CAB")) {
                respuesta +=
                "<div class='row elementoSeleccionado' data-tipo='comentarioAbierto'>" +
                "   <div class='input-field col s6'>" +
                "       <input type='text' class='validate campoLetrasEspacios'required=''" +
                "           aria-required='true' maxlength='50' id='inp" + consecutivoIds + "'" +
                "           value='" + etiqueta1 + "'>" +
                "       <label for='inp" + consecutivoIds++ + "' class='active'>Etiqueta</label>" +
                "   </div>" +
                "   <div class='input-field col s4 offset-s1'>" +
                "     <div class='input-field' style='font-weight: bold;'>" +
                "        <label>Tipo: Abierto</label>" +
                "     </div>" +
                "   </div>" +
                "   <div class='input-field col s1 center'>" +
                "     <i class='material-icons btnEliminar' style='padding-top: 100%; cursor: pointer; color: red;'>delete</i>" +
                "   </div>" +
                "   <div class='col s12 input-field' style='margin-top: -5%;'>" +
                "      <textarea class='materialize-textarea' disabled></textarea>" +
                "      <label>Este campo será llenado por el fisioterapeuta cuando defina el plan de rehabilitación.</label>" +
                "   </div>" +
                "</div>";
                
                continue;
            } else if(tipoElemento.equals("SUN"))
                respuesta +=
                "<div class='row elementoSeleccionado' data-tipo='seleccionUnica'>" +
                "   <div class='input-field col s6'>" +
                "       <input type='text' class='validate campoLetrasEspacios'required=''" +
                "           aria-required='true' maxlength='50' id='inp" + consecutivoIds + "'" +
                "           value='" + etiqueta1 + "'>" +
                "       <label for='inp" + consecutivoIds + "' class='active'>Etiqueta</label>" +
                "   </div>" +
                "   <div class='col s4 offset-s1'>" +
                "     <div class='input-field' style='font-weight: bold;'>" +
                "        <label>Tipo: Selección Única</label>" +
                "     </div>" +
                "   </div>" +
                "   <div class='col s1 center'>" +
                "     <i class='material-icons btnEliminar' style='padding-top: 100%; cursor: pointer; color: red;'>delete</i>" +
                "   </div>" +
                "   <div class='col s12' style='margin-top: -5%;'>" +
                "      <div class='chips' id='div" + consecutivoIds + "'></div>" +
                "   </div>" +
                "</div>";
            else if(tipoElemento.equals("SMU"))
                respuesta +=
                "<div class='row elementoSeleccionado' data-tipo='seleccionMultiple'>" +
                    "   <div class='input-field col s6'>" +
                    "       <input type='text' class='validate campoLetrasEspacios'required='' " +
                    "           aria-required='true' maxlength='50' id='inp" + consecutivoIds + "'" +
                    "           value='" + etiqueta1 + "'>" +
                    "       <label for='inp" + consecutivoIds + "' class='active'>Etiqueta</label>" +
                    "   </div>" +
                    "   <div class='col s4 offset-s1'>" +
                    "     <div class='input-field' style='font-weight: bold;'>" +
                    "        <label>Tipo: Selección Múltiple</label>" +
                    "     </div>" +
                    "   </div>" +
                    "   <div class='col s1 center'>" +
                    "     <i class='material-icons btnEliminar' style='padding-top: 100%; cursor: pointer; color: red;'>delete</i>" +
                    "   </div>" +
                    "   <div class='col s12' style='margin-top: -5%;'>" +
                    "      <div class='chips' id='div" + consecutivoIds + "'></div>" +
                    "   </div>" +
                    "</div>";

            // Busca las opciones que que pertenecen al elemento actual ya sea
            // este de selección única o múltiple.
            myQuery =
                    "SELECT CorrelativoOpcion, Opcion " +
                    "FROM Opciones_Elementos_Modalidad " +
                    "WHERE ID_Modalidad = " + getIdModalidad() + " " +
                    "  AND CorrelativoElemento = " + correlativoElemento + " " +
                    "ORDER BY CorrelativoOpcion;";
            
            ResultSet resultadoOpciones = instanciaBD.execQry(myQuery);
            if(resultadoOpciones.next()){
                int correlativoOpcion;
                String opcion;
                jQueryOpciones +=
                        "$('#div" + consecutivoIds + "').chips({" +
                        "   data: [";
                
                do{
                    jQueryOpciones += "{ tag: '"  + 
                            resultadoOpciones.getString("Opcion") + "', }, ";
                } while (resultadoOpciones.next());
                
                jQueryOpciones = jQueryOpciones.substring(0, jQueryOpciones.lastIndexOf(",")) + "], });";
            }
            
            setJQueryOpciones(jQueryOpciones);
            consecutivoIds++;
        }
        
        return respuesta;
    }

    public String getJQueryOpciones() {
        return jqueryOpciones;
    }

    public void setJQueryOpciones(String jqueryOpciones) {
        this.jqueryOpciones += jqueryOpciones;
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
    
    public long getFechaModificacion(){
      return this.fechaModificacion;
    }
    
    public void setFechaModificacion(long fechaModificacion){
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

    public String getJqueryOpciones() {
        return jqueryOpciones;
    }

    public void setJqueryOpciones(String jqueryOpciones) {
        this.jqueryOpciones = jqueryOpciones;
    }

    public int getCantidadElementos() {
        return cantidadElementos;
    }

    public void setCantidadElementos(int cantidadElementos) {
        this.cantidadElementos = cantidadElementos;
    }

    public int getCantidadPlanes() {
        return cantidadPlanes;
    }

    public void setCantidadPlanes(int cantidadPlanes) {
        this.cantidadPlanes = cantidadPlanes;
    }
    
    
    // Métodos utilitarios de la clase Modalidad.
    // Carga la lista de todos las modalidades de la empresa.
    public String cargarLista(int idCentro, boolean modoVisualizacion, boolean modoEdicion){
        $ instanciaListaModalidades = null;
        String respuesta = "";
        setModoVisualizacion(modoVisualizacion);
        setModoEdicion(modoEdicion);
        // Define la estructura HTML de respuesta.
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

        try {
          // Realiza consulta de departamentos a la base de datos.
          instanciaListaModalidades = new $();
          ResultSet resultado = instanciaListaModalidades.execQry(
              "SELECT ID_Modalidad, Nombre " +
              "FROM Modalidad " +
              "WHERE ID_Centro = " + idCentro + " " +
              "ORDER BY Nombre;");

          if( resultado.next() ){
            respuesta =
                  // Retroalimentación al usuario del proceso.
                  "{ \"error\" : false, " +
                  "  \"mensaje\" : \"\", " +
                  // Información recuperada de la tabla Modalidad.
                  "  \"elementosHTML\": \"" +
                  "            <h5 class='center'>" + TITULO +
                  "              <a href='" + PAGINA_WEB + "' class='" + ETIQUETA_NUEVO + "'>" +
                  "                <i class='material-icons right'>add_circle_outline</i>" +
                  "              </a>" +
                  "            </h5><hr/>" +
                  "            <table id='myTable2'>" +
                  "              <thead>" +
                  "                <tr>" +
                  "                  <th onclick='sortTable(0)' style='cursor:pointer' id='nombreCol'><br/>Nombre<i></th>" +
                  "                  <th onclick='sortTable(1)' style='cursor:pointer' id='nombreCol' class='center'>Cantidad<br/>Elementos</th>" +
                  "                  <th onclick='sortTable(2)' style='cursor:pointer' id='nombreCol' class='center'>Planes Activos<br/>Relacionados</th>" +
                  ( getModoVisualizacion() ? "<th class='center'><br/>Ver</th>" : "" ) +
                  ( getModoEdicion() ? "<th class='center'><br/>Editar</th>" : "" ) +
                  "                </tr>" +
                  "              </thead>" +
                  "            <tbody>";

            do{
                // Si existen resultados crea arreglo JSON para mostrar la lista de departamentos.
                this.setIdModalidad(resultado.getInt("ID_Modalidad"));
                this.setNombre(resultado.getString("Nombre"));

                // Recupera el número de elementos de esta modalidad.
                ResultSet cantidadElementos = instanciaListaModalidades.execQry(
                        "SELECT COUNT(*) AS CantidadElementos " +
                        "FROM Elementos_Modalidad " +
                        "WHERE ID_Modalidad = "  + getIdModalidad() + ";");
                if(cantidadElementos.next())
                    setCantidadElementos(cantidadElementos.getInt("CantidadElementos"));
                
                
                // Recupera el número de planes activos relacionados a esta modalidad.
                ResultSet cantidadPlanesRehabilitacion = instanciaListaModalidades.execQry(
                        "SELECT COUNT(*) AS CantidadPlanes " +
                        "FROM Modalidades_Indicadas M " +
                        "     INNER JOIN Plan_Rehabilitacion PR " +
                        "        ON M.ID_PlanRehabilitacion = PR.ID_PlanRehabilitacion " +
                        "WHERE M.ID_Modalidad = "  + getIdModalidad() + " " +
                        "  AND PR.Activo = 1;");
                if(cantidadPlanesRehabilitacion.next())
                    setCantidadPlanes(cantidadPlanesRehabilitacion.getInt("CantidadPlanes"));
                
                
                respuesta += this.toHTML();
            } while( resultado.next() );

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
          if(instanciaListaModalidades != null)
            instanciaListaModalidades.desconectar();

          instanciaListaModalidades = null;
          System.gc();
        }

        return respuesta;
    } // Fin del método cargarLista().
    
    
    // Carga el registro de la modalidad con el código proporcionado.
    public void cargarRegistro(int idModalidad) {
        setIdModalidad(-1);  
        $ instanciaBD = null;
        
        try{
            if(idModalidad > 0){
                instanciaBD = new $();
                String myQuery =
                    "SELECT Nombre, Activo, UsuarioModifico " +
                    "FROM Modalidad " +
                    "WHERE ID_Modalidad = ?;";  

                PreparedStatement psModalidad = instanciaBD.prepStatement(myQuery);      
                psModalidad.setInt(1, idModalidad);
                ResultSet resultado = psModalidad.executeQuery(); 

                if(resultado.next()){
                    setIdModalidad(idModalidad);
                    setNombre(resultado.getString("Nombre"));
                    setActivo(resultado.getInt("Activo") == 1);
                    setUsuarioModifico(resultado.getInt("UsuarioModifico"));
                }
            } else {
                setIdModalidad(idModalidad);
                setNombre("");
                setJQueryOpciones("");
                setActivo(true);
                setUsuarioModifico(0);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(instanciaBD != null)
                instanciaBD.desconectar();
            
            instanciaBD = null;
            System.gc();
        }
    } // Fin del metodo cargarRegistro().
    
    
    // Recupera los elementos y las opciones de la modalidad indicada.
    // Devuelve el registro con formato de objeto JSON.
    public String toJSON() throws ClassNotFoundException, SQLException{
        String mensajeError = "";
        if(getIdModalidad() < 0)
            mensajeError = "Ocurrió un error al cargar el registro del departamento.";

        return
            "   \"error\": " + (getIdModalidad() < 0) + ", " +
            "   \"mensaje\": \"" + mensajeError + "\", " +
            "   \"codigo\": " + getIdModalidad() + ", " +
            "   \"nombre\": \"" + getNombre() + "\", " +
            "   \"elementos\": \"" + getElementos_Y_Opciones() + "\", " +
            "   \"jquery\": \"" + getJQueryOpciones() + "\", " +
            "   \"activo\": " + getActivo() + ", " +
            "   \"usuarioModifico\": \"" + getUsuarioModifico() + "\"";
    } // Fin del metodo toJSON().

    
    // Devuelve el registro con formato de tabla HTML.
    private String toHTML() {
        return "<tr style='border: none;' class='resaltar'>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + getNombre() + "</td>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;' class='center'>" + getCantidadElementos() + "</td>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;' class='center'>" + getCantidadPlanes() + "</td>" +
               ( getModoVisualizacion() ? 
               "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'><a href='" + PAGINA_WEB + "' class='" + ETIQUETA_EXISTENTE + "' " +
               "      data-codigo='" + getIdModalidad() + "' " +
               "      data-modo='mod-v'>" +
               "    <i class='material-icons'>visibility</i></a>" +
               "  </td>" : "" ) +
               ( getModoEdicion() ? 
               "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'><a href='" + PAGINA_WEB + "' class='" + ETIQUETA_EXISTENTE + "' " +
               "      data-codigo='" + getIdModalidad() + "' " +
               "      data-modo='mod-e'>" +
               "    <i class='material-icons'>edit</i></a>" +
               "  </td>" : "" ) +
               "</tr>";
    } // Fin del método toHTML.
    
    
    public String listaDetalleModalidades(int idCentro, int idPlan){
        String myQuery;
        String nombreModalidad;
        String tipoElemento;
        String etiqueta1;
        String etiqueta2;
        String opcion;
        String respuesta = "<ul class='collapsible popout'>";
        int longitudRespuestaInicial = respuesta.length();
        int indiceModalidad;
        int correlativoElemento;
        int correlativoOpcion;
        $ instanciaEncabezadoModalidades = null;
        $ instanciaElementosModalidades = null;
        $ instanciaOpcionesModalidades = null;
        $ instanciaIndicacionModalidades = null;
        
        try {
            instanciaEncabezadoModalidades = new $();
            instanciaElementosModalidades = new $();
            instanciaOpcionesModalidades = new $();
            instanciaIndicacionModalidades = new $();
            
            // Recupera el encabezado de la modalidad, solo muestra las activas.
            if(idPlan == 0){
                myQuery =
                        "SELECT ID_Modalidad, Nombre " +
                        "FROM Modalidad " +
                        "WHERE ID_Centro = " + idCentro + " " +
                        "  AND Activo = 1 " +
                        "ORDER BY ID_Modalidad;";
            } else {
                myQuery =
                        "SELECT M.ID_Modalidad, M.Nombre " +
                        "FROM Modalidad M " +
                        "   INNER JOIN Modalidades_Indicadas MI " +
                        "      ON M.ID_Modalidad = MI.ID_Modalidad " +
                        "WHERE ID_PlanRehabilitacion = " + idPlan + " " +
                        "ORDER BY ID_Modalidad;";
            }
            
            ResultSet resultadoEncabezado = instanciaEncabezadoModalidades.execQry(myQuery);
            while(resultadoEncabezado.next()){
                int correlativoSUN = 1;
                indiceModalidad = resultadoEncabezado.getInt("ID_Modalidad");
                nombreModalidad = resultadoEncabezado.getString("Nombre");
                
                // Crea el elemento de HTML correpondiente al encabezado.
                respuesta +=
                "<li><div class='collapsible-header' data-codigo='" + indiceModalidad + "'>" +
                   nombreModalidad + "</div>" +
                "  <div class='collapsible-body'>" +
                "    <div class='row' style='margin-top: -3%;'>";
                
                // Recupera los elementos que componen la modalidad actual.
                myQuery = 
                        "SELECT Correlativo, TipoElemento, Etiqueta1, Etiqueta2 " +
                        "FROM Elementos_Modalidad " +
                        "WHERE ID_Modalidad = " + indiceModalidad + " " +
                        "ORDER BY Correlativo;";
                
                ResultSet resultadoElementos = instanciaElementosModalidades.execQry(myQuery);
                while(resultadoElementos.next()){
                    correlativoElemento = resultadoElementos.getInt("Correlativo");
                    tipoElemento = resultadoElementos.getString("TipoElemento");
                    etiqueta1 = resultadoElementos.getString("Etiqueta1");
                    etiqueta2 = resultadoElementos.getString("Etiqueta2");
                    
                    // Crea el elemento HTML para los elementos que integran la
                    // modalidad actual según su tipo.
                    if(tipoElemento.equals("AYT")){
                        respuesta +=
                        "<div data-correlativo='" + correlativoElemento + "' data-tipo='divAYT'>" +
                        "  <div class='input-field col s6' data-tipo='divArea'>" +
                        "    <input type='text' class='campoLetrasEspacios' id='area" + nombreModalidad + "'" +
                        "           value='$#valorArea#$' $#readonlyArea#$/>" +
                        "    <label for='area" + nombreModalidad + "' class='$#claseActivaArea#$'>Área</label>" +
                        "  </div>" +

                        "  <div class='input-field col s6' data-tipo='divTiempo'>" +
                        "    <input type='text' class='campoEntero' id='tiempo" + nombreModalidad + "'" +
                        "           $#maxlength#$ value='$#valorTiempo#$' $#readonlyTiempo#$/>" +
                        "    <label for='tiempo" + nombreModalidad + "' class='$#claseActivaTiempo#$'>Tiempo (en minutos)</label>" +
                        "  </div>" +
                        "</div>";
                        
                        // Debido a que este tipo de elmento se compone de una
                        // plantilla fija pasa al siguiente en caso de no buscar
                        // las modalidades de un plan específico.
                        if(idPlan == 0){
                            respuesta = respuesta.replace("$#valorArea#$", "")
                                                 .replace("$#valorTiempo#$", "")
                                                 .replace("$#maxlength#$", "maxlength='2'")
                                                 .replace("$#$#readonly#$Tiempo#$", "")
                                                 .replace("$#$#readonlyArea#$#$", "")
                                                 .replace("$#claseActivaArea#$", "")
                                                 .replace("$#claseActivaTiempo#$", "");
                                                
                            continue;
                        } else {
                            // Dado que la solicitud corresponde a buscar las modalidades
                            // que se aplicarán para un plan de rehabilitación definido.
                            // se buscan los valores establecidos.
                            myQuery =
                                    "SELECT Tipo, Instruccion " +
                                    "FROM Instrucciones_Modalidad_Indicada " +
                                    "WHERE ID_PlanRehabilitacion = " + idPlan + " " +
                                    "  AND ID_Modalidad = " + indiceModalidad + " " +
                                    "  AND CorrelativoElemento = " + correlativoElemento + " " +
                                    "  AND Tipo IN('AREA', 'TIEMPO');";
                            
                            ResultSet resultadoInstruccionesIndicadas = instanciaIndicacionModalidades.execQry(myQuery);
                            while(resultadoInstruccionesIndicadas.next()){
                                if(resultadoInstruccionesIndicadas.getString("Tipo").equals("AREA"))
                                    respuesta = respuesta.replace("$#valorArea#$",
                                                    resultadoInstruccionesIndicadas.getString("Instruccion"))
                                                         .replace("$#claseActivaArea#$", "active");
                                else if(resultadoInstruccionesIndicadas.getString("Tipo").equals("TIEMPO"))
                                    respuesta = respuesta.replace("$#valorTiempo#$",
                                                    resultadoInstruccionesIndicadas.getString("Instruccion"))
                                                         .replace("$#claseActivaTiempo#$", "active");
                            }
                            
                            respuesta = respuesta.replace("$#valorArea#$", "")
                                                 .replace("$#valorTiempo#$", "")
                                                 .replace("$#readonlyArea#$", "readonly")
                                                 .replace("$#readonlyTiempo#$", "readonly")
                                                 .replace("$#claseActivaArea#$", "")
                                                 .replace("$#claseActivaTiempo#$", "");
                        }
                    } else if(tipoElemento.equals("SUN"))
                        respuesta +=
                        "<div class='col s12' data-correlativo='" + correlativoElemento + "' " +
                        "      data-tipo='divUnico'>" +
                        "  <h6 style='font-weight: bold;'>" + etiqueta1 + "</h6>" +
                        "</div>";
                    else if(tipoElemento.equals("SMU"))
                        respuesta +=
                        "<div class='col s12' data-correlativo='" + correlativoElemento + "' " +
                        "      data-tipo='divMultiple'>" +
                        "  <h6 style='font-weight: bold;'>" + etiqueta1 + "</h6>" +
                        "</div>";
                    else if(tipoElemento.equals("CAB")) {
                        respuesta +=
                        "<div class='input-field col s12' data-correlativo='" + correlativoElemento + "' " +
                        "      data-tipo='divComentario'>" +
                        "  <textarea name='" + etiqueta1.toLowerCase() + nombreModalidad + "'" +
                        "      id='" + etiqueta1.toLowerCase() + nombreModalidad + "' maxlength='300'" +
                        "      class='materialize-textarea campoLetrasEspacios' " +
                        "      $#readonlyAbierto#$>$#valorAbierto#$</textarea>" +
                        "  <label id='lbl" + etiqueta1.toLowerCase() + nombreModalidad + "'" +
                        "      for='" + etiqueta1.toLowerCase() + nombreModalidad + "'" +
                        "      class='$#claseActivaAbierto#$'>" +
                             etiqueta1 + "</label>" +
                        "</div>";
                        
                        // Debido a que este tipo de elmento se compone de una
                        // plantilla fija pasa al siguiente en caso de no buscar
                        // las modalidades de un plan específico.
                        if(idPlan == 0){
                            respuesta = respuesta.replace("$#valorAbierto#$", "")
                                                 .replace("$#claseActivaAbierto#$", "")
                                                 .replace("$#readonlyAbierto#$", "");
                            continue;
                        } else {
                            // Dado que la solicitud corresponde a buscar las modalidades
                            // que se aplicarán para un plan de rehabilitación definido.
                            // se buscan los valores establecidos.
                            myQuery =
                                    "SELECT Tipo, Instruccion " +
                                    "FROM Instrucciones_Modalidad_Indicada " +
                                    "WHERE ID_PlanRehabilitacion = " + idPlan + " " +
                                    "  AND ID_Modalidad = " + indiceModalidad + " " +
                                    "  AND CorrelativoElemento = " + correlativoElemento + " " +
                                    "  AND Tipo = 'ABIERTO';";
                            
                            ResultSet resultadoInstruccionesIndicadas = instanciaIndicacionModalidades.execQry(myQuery);
                            if(resultadoInstruccionesIndicadas.next()
                                    && resultadoInstruccionesIndicadas.getString("Tipo").equals("ABIERTO"))
                                respuesta = respuesta.replace("$#valorAbierto#$",
                                                resultadoInstruccionesIndicadas.getString("Instruccion"))
                                                     .replace("$#claseActivaAbierto#$", "active");
                            else
                                respuesta = respuesta.replace("$#valorAbierto#$", "")
                                                     .replace("$#claseActivaAbierto#$", "");
                            
                            respuesta = respuesta.replace("$#readonlyAbierto#$", "readonly");
                        }
                    }
                    
                    // Busca las opciones que aparecen en los elementos donde el usuario
                    // podra seleccionar los valores que estime convenientes.
                    myQuery =
                            "SELECT CorrelativoOpcion, Opcion " +
                            "FROM Opciones_Elementos_Modalidad " +
                            "WHERE ID_Modalidad = " + indiceModalidad + " " +
                            "  AND CorrelativoElemento = " + correlativoElemento + " " +
                            "ORDER BY CorrelativoOpcion;";
                    
                    ResultSet resultadoOpciones = instanciaOpcionesModalidades.execQry(myQuery);
                    while(resultadoOpciones.next()){
                        correlativoOpcion = resultadoOpciones.getInt("CorrelativoOpcion");
                        opcion = resultadoOpciones.getString("Opcion");
                        
                        // Crea las opciones de cada uno de los elementos de HTML
                        // creados a partir del nombre brindado.
                        if(tipoElemento.equals("SUN")){
                            respuesta +=
                            "<div class='col s6 m4 l3' data-correlativo='" + correlativoOpcion + "'>" +
                            "  <label>" +
                            "    <input name='_" + nombreModalidad + correlativoSUN + "' type='radio' $#disabled#$/>" +
                            "    <span $#style#$>" + opcion + "</span>" +
                            "  </label>" +
                            "</div>";
                        } else if( tipoElemento.equals("SMU"))
                            respuesta +=
                            "<div class='col s6 m4 l3' data-correlativo='" + correlativoOpcion + "'>" +
                            "  <label>" +
                            "    <input type='checkbox' $#disabled#$/>" +
                            "    <span $#style#$>" + opcion + "</span>" +
                            "  </label>" +
                            "</div>";
                            
                        
                        // Recupera las opciones indicadas de cada elemento.
                        if(idPlan == 0){
                            respuesta = respuesta.replace("$#disabled#$", "");
                        } else {
                            myQuery =
                                    "SELECT COUNT(*) AS Marcado " +
                                    "FROM Opciones_Modalidad_Indicadas " +
                                    "WHERE ID_PlanRehabilitacion = " + idPlan + " " +
                                    "  AND ID_Modalidad = " + indiceModalidad + " " +
                                    "  AND CorrelativoElemento = " + correlativoElemento + " " +
                                    "  AND CorrelativoOpcion = " + correlativoOpcion + ";";
                            
                            ResultSet resultadoOpcionesIndicadas = instanciaIndicacionModalidades.execQry(myQuery);
                            if(resultadoOpcionesIndicadas.next() && resultadoOpcionesIndicadas.getBoolean("Marcado")){
                                respuesta = respuesta.replace("$#disabled#$", "checked")
                                                     .replace("$#style#$", "style='font-weight: bold; color: lightseagreen;'");
                            } else
                                respuesta = respuesta.replace("$#disabled#$", "disabled")
                                                     .replace("$#style#$", "");
                        }
                    }
                    
                    correlativoSUN++;
                }
            }
            
            if(respuesta.length() > longitudRespuestaInicial)
                respuesta += 
                "    </div>" +
                "  </div>" +
                "</li>";
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if(instanciaEncabezadoModalidades != null)
                instanciaEncabezadoModalidades.desconectar();
            
            if(instanciaElementosModalidades != null)
                instanciaElementosModalidades.desconectar();
                
            if(instanciaOpcionesModalidades != null)
                instanciaOpcionesModalidades.desconectar();
            
            if(instanciaIndicacionModalidades != null)
                instanciaIndicacionModalidades.desconectar();
            
            instanciaEncabezadoModalidades = null;
            instanciaElementosModalidades = null;
            instanciaOpcionesModalidades = null;
            instanciaIndicacionModalidades = null;
            System.gc();
        }
        
        return respuesta;
    }
} // Fin de la clase Modalidad.