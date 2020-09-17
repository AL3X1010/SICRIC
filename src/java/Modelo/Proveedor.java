/*  Nombre.....:  Proveedor.java
 *  Descripción:  Carga el registro de la tabla Proveedor.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Julio, 2019.
 */
package Modelo;

import Utilitarios.Consts;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Proveedor {
    // Constantes de la clase Proveedor.
    private final String PAGINA_WEB = Consts.PROVEEDOR_HTML;
    private final String TITULO = Consts.TITULO_LISTA_PROVEEDORES;
    private final String ETIQUETA_EXISTENTE = Consts.ETIQUETA_EXISTENTE_PROVEEDOR;
    private final String ETIQUETA_NUEVO = Consts.ETIQUETA_NUEVO_PROVEEDOR;
    
    // Variables de la clase Proveedor.
    private int idProveedor;
    private String nombreProveedor;
    private int telefonoProveedor;
    private String correoElectronico;
    private String direccion;
    private String nombreContacto;
    private int telefonoContacto;
    private String tipo;
    private boolean activo;
    private int usuarioModifico;
    private boolean modoVisualizacion;
    private boolean modoEdicion;
    
    
    // Método constructor de la clase Proveedor.
    public Proveedor() {
        this.idProveedor = -1;
        this.nombreProveedor = "";
        this.telefonoProveedor = 0;
        this.correoElectronico = "";
        this.direccion = "";
        this.nombreContacto = "";
        this.telefonoContacto = 0;
        this.tipo = "";
        this.activo = true;
        this.usuarioModifico = 0;
    } // Fin del método constructor.
    
    
    // Metodos GET y SET de la clase Proveedor.
    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int Proveedor) {
        this.idProveedor = Proveedor;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public int getTelefonoProveedor() {
        return telefonoProveedor;
    }

    public void setTelefonoProveedor(int telefonoProveedor) {
        this.telefonoProveedor = telefonoProveedor;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public int getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(int telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public String getSelectTipo(String tipo) {
        return
            "<option value='" + Consts.CODIGO_PROVEE_MEDICAMENTOS + (
                    tipo.equals(Consts.CODIGO_PROVEE_MEDICAMENTOS) ? "' selected>" : "'>" ) +
                Consts.PROVEE_MEDICAMENTOS + "</option>" +
            "<option value='" + Consts.CODIGO_PROVEE_EQUIPOS_MEDICOS + (
                    tipo.equals(Consts.CODIGO_PROVEE_MEDICAMENTOS) ? "' selected>" : "'>" ) +
                Consts.PROVEE_EQUIPOS_MEDICOS + "</option>" +
            "<option value='" + Consts.CODIGO_PROVEE_SUMINISTROS_MEDICOS + (
                    tipo.equals(Consts.CODIGO_PROVEE_MEDICAMENTOS) ? "' selected>" : "'>" ) +
                Consts.PROVEE_SUMINISTROS_MEDICOS + "</option>" +
            "<option value='" + Consts.CODIGO_PROVEE_MEDICAMENTOS_EQUIPOS + (
                    tipo.equals(Consts.CODIGO_PROVEE_MEDICAMENTOS) ? "' selected>" : "'>" ) +
                Consts.PROVEE_MEDICAMENTOS_EQUIPOS+ "</option>" +
            "<option value='" + Consts.CODIGO_PROVEE_EQUIPOS_SUMINISTROS + (
                    tipo.equals(Consts.CODIGO_PROVEE_MEDICAMENTOS) ? "' selected>" : "'>" ) +
                Consts.PROVEE_EQUIPOS_SUMINISTROS + "</option>" +
            "<option value='" + Consts.CODIGO_PROVEE_MEDICAMENTOS_SUMINISTROS + (
                    tipo.equals(Consts.CODIGO_PROVEE_MEDICAMENTOS) ? "' selected>" : "'>" ) +
                Consts.PROVEE_MEDICAMENTOS_SUMINISTROS + "</option>" +
            "<option value='" + Consts.CODIGO_PROVEE_MEDICAMENTOS_EQUIPOS_SUMINISTROS + (
                    tipo.equals(Consts.CODIGO_PROVEE_MEDICAMENTOS) ? "' selected>" : "'>" ) +
                Consts.PROVEE_MEDICAMENTOS_EQUIPOS_SUMINISTROS + "</option>";
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
    
    
    // Métodos utilitarios de la clase Proveedor.
    // Carga la lista de todos los proveedores de la empresa, activos e inactivos.
    public String cargarLista(int idCentro, boolean modoVisualizacion, boolean modoEdicion){
      $ instanciaListaProveedores = null;
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
        // Realiza consulta de proveedores a la base de datos.
        instanciaListaProveedores = new $();
        ResultSet resultado = instanciaListaProveedores.execQry(
            "SELECT ID_Proveedor, NombreProveedor, TelefonoProveedor," +
            "        Tipo, Activo " +
            "FROM Proveedor " +
            "WHERE ID_Centro = " + idCentro + " " +
            "ORDER BY NombreProveedor;");
        
        if( resultado.next() ){
          respuesta =
                // Retroalimentación al usuario del proceso.
                "{ \"error\" : false, " +
                "  \"mensaje\" : \"\", " +
                // Información recuperada de la tabla Proveedor.
                "  \"elementosHTML\": \"" +
                "            <h5 class='center'>" + TITULO +
                "              <a href='" + PAGINA_WEB + "' class='" + ETIQUETA_NUEVO + "'>" +
                "                <i class='material-icons right'>add_circle_outline</i>" +
                "              </a>" +
                "            </h5><hr/>" +
                "            <table id='myTable2'>" +
                "              <thead>" +
                "                <tr style='border-bottom: none;'>" +
                "                  <th></th>" +
                "                  <th colspan='3' class='center' style='border-bottom: 1px solid black;'>Provee</th>" +
                "                  <th></th>" +
                "                  <th></th>" +
                ( getModoVisualizacion() ? "<th></th>" : "" ) +
                ( getModoEdicion() ? "<th></th>" : "" ) +
                "                </tr>" +
                "                <tr>" +
                "                  <th onclick='sortTable(0, 2)' style='cursor:pointer;' id='nombreCol'>Nombre<i></th>" +
                "                  <th onclick='sortTable(1, 2)' style='cursor:pointer;' id='medicamentoCol' class='center'>Medicamentos</th>" +
                "                  <th onclick='sortTable(2, 2)' style='cursor:pointer;' id='equipoCol' class='center'>Equipos</th>" +
                "                  <th onclick='sortTable(3, 2)' style='cursor:pointer;' id='suministroCol' class='center'>Suministros</th>" +
                "                  <th>Telefono</th>" +
                "                  <th onclick='sortTable(5, 2)' style='cursor:pointer' id='activoCol' class='center'>Activo</th>" +
                ( getModoVisualizacion() ? "<th class='center'>Ver</th>" : "" ) +
                ( getModoEdicion() ? "<th class='center'>Editar</th>" : "" ) +
                "                </tr>" +
                "              </thead>" +
                "            <tbody>";
                  
          do{
            // Si existen resultados crea arreglo JSON para mostrar la lista de proveedores.
            this.setIdProveedor(resultado.getInt("ID_Proveedor"));
            this.setNombreProveedor(resultado.getString("NombreProveedor"));
            this.setTelefonoProveedor(resultado.getInt("TelefonoProveedor"));
            this.setTipo(resultado.getString("Tipo"));
            this.setActivo(resultado.getBoolean("Activo"));
            
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
        if(instanciaListaProveedores != null)
          instanciaListaProveedores.desconectar();
          
        instanciaListaProveedores = null;
        System.gc();
      }

      return respuesta;
    } // Fin del método cargarLista().
    
    
    // Carga el registro de proveedor con el código proporcionado.
    public void cargarRegistro(int idProveedor) {
        ServiciosM modelo = new ServiciosM();
        setIdProveedor(-1);  
        $ instanciaBD = null;
        
        try{
            if(idProveedor > 0){
                instanciaBD = new $();
                String myQuery =
                    "SELECT NombreProveedor, TelefonoProveedor, CorreoElectronico, " +
                    "       Direccion, NombreContacto, TelefonoContacto, Tipo, Activo " +
                    "FROM Proveedor " +
                    "WHERE ID_Proveedor = ?;";  

                PreparedStatement psProveedor = instanciaBD.prepStatement(myQuery);      
                psProveedor.setInt(1, idProveedor);
                ResultSet resultado = psProveedor.executeQuery(); 

                if(resultado.next()){
                    setIdProveedor(idProveedor); 
                    setNombreProveedor(resultado.getString("NombreProveedor"));
                    setTelefonoProveedor(resultado.getInt("TelefonoProveedor"));
                    setCorreoElectronico(resultado.getString("CorreoElectronico"));
                    setDireccion(resultado.getString("Direccion"));
                    setNombreContacto(resultado.getString("NombreContacto"));
                    setTelefonoContacto(resultado.getInt("TelefonoContacto"));
                    setTipo(resultado.getString("Tipo"));
                    setActivo(resultado.getInt("Activo") == 1);
                }
            } else {
                setIdProveedor(idProveedor);
                setNombreProveedor("");
                setTelefonoProveedor(0);
                setCorreoElectronico("");
                setDireccion("");
                setNombreContacto("");
                setTelefonoContacto(0);
                setTipo("");
                setActivo(true);
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
    
    
    // Devuelve el registro con formato de objeto JSON.
    public String toJSON(){
        String mensajeError = "";
        if(getIdProveedor() < 0)
            mensajeError = "Ocurrió un error al cargar el registro del proveedor.";

        return
            "   \"error\": " + (getIdProveedor() < 0) + ", " +
            "   \"mensaje\": \"" + mensajeError + "\", " +
            "   \"codigo\": " + getIdProveedor() + ", " +
            "   \"nombreProveedor\": \"" + getNombreProveedor() + "\", " +
            "   \"telefonoProveedor\": " + getTelefonoProveedor() + ", " +
            "   \"correoElectronico\": \"" + getCorreoElectronico() + "\", " +
            "   \"direccion\": \"" + getDireccion() + "\", " +
            "   \"nombreContacto\": \"" + getNombreContacto() + "\", " +
            "   \"telefonoContacto\": " + getTelefonoContacto() + ", " +
            "   \"activo\": " + getActivo() + ", " +
            "   \"selectTipo\": \"" + getSelectTipo(getTipo()) + "\"";
    } // Fin del metodo toJSON().

    // Devuelve el registro con formato de tabla HTML.
    private String toHTML() {
        final String columnaMarcada =
                "<td class='center' style='padding-top: 1%; padding-bottom: 1%;'>" +
                "  <i class='material-icons'>check_box</i></a>" +
                "</td>";
        final String columnaDesmarcada =
                "<td class='center' style='padding-top: 1%; padding-bottom: 1%;'>" +
                "  <i class='material-icons'>check_box_outline_blank</i></a>" +
                "</td>";
        
        return "<tr style='border: none;' class='resaltar'>" +
               "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + getNombreProveedor() + "</td>" +
               ( getTipo().equals(Consts.CODIGO_PROVEE_MEDICAMENTOS) ?
                    columnaMarcada + columnaDesmarcada + columnaDesmarcada :
                 getTipo().equals(Consts.CODIGO_PROVEE_EQUIPOS_MEDICOS) ?
                    columnaDesmarcada + columnaMarcada + columnaDesmarcada :
                 getTipo().equals(Consts.CODIGO_PROVEE_SUMINISTROS_MEDICOS) ?
                    columnaDesmarcada + columnaDesmarcada + columnaMarcada :
                 getTipo().equals(Consts.CODIGO_PROVEE_MEDICAMENTOS_EQUIPOS) ?
                    columnaMarcada + columnaMarcada + columnaDesmarcada :
                 getTipo().equals(Consts.CODIGO_PROVEE_MEDICAMENTOS_SUMINISTROS) ?
                    columnaMarcada + columnaDesmarcada + columnaMarcada :
                 getTipo().equals(Consts.CODIGO_PROVEE_EQUIPOS_SUMINISTROS) ?
                    columnaDesmarcada + columnaMarcada + columnaMarcada :
                 getTipo().equals(Consts.CODIGO_PROVEE_MEDICAMENTOS_EQUIPOS_SUMINISTROS) ?
                    columnaMarcada + columnaMarcada + columnaMarcada :
                    columnaDesmarcada + columnaDesmarcada + columnaDesmarcada ) +
               "  <td style='padding-top: 1%; padding-bottom: 1%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + getTelefonoProveedor() + "</td>" +
               "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'>" +
               "    <i class='material-icons'>" + 
                      (getActivo() ? "check_box": "check_box_outline_blank") + 
               "    </i></a>" +
               "  </td>" +
               ( getModoVisualizacion() ? 
               "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'><a href='" + PAGINA_WEB + "' class='" + ETIQUETA_EXISTENTE + "' " +
               "      data-codigo='" + getIdProveedor() + "' " +
               "      data-modo='mod-v'>" +
               "    <i class='material-icons'>visibility</i></a>" +
               "  </td>" : "" ) +
               ( getModoEdicion() ? 
               "  <td class='center' style='padding-top: 1%; padding-bottom: 1%;'><a href='" + PAGINA_WEB + "' class='" + ETIQUETA_EXISTENTE + "' " +
               "      data-codigo='" + getIdProveedor() + "' " +
               "      data-modo='mod-e'>" +
               "    <i class='material-icons'>edit</i></a>" +
               "  </td>" : "" ) +
               "</tr>";
    } // Fin del método toHTML.
} // Fin de la clase Proveedor.