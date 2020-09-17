/*  Nombre.....:  Centro.java
 *  Descripción:  Carga el registro de la tabla Centro.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Julio, 2019.
 */
package Modelo;

import Utilitarios.Consts;
import Utilitarios.ServiciosU;
import java.sql.ResultSet;

public class Centro {
  // Variables de la clase Centro.
  private int idCentro;
  private String nombre;
  private String direccion;
  private String logo;
  private int telefono;
  private String correo;
  private int horaInicioLunes;
  private int horaFinalLunes;
  private int horaInicioMartes;
  private int horaFinalMartes;
  private int horaInicioMiercoles;
  private int horaFinalMiercoles;
  private int horaInicioJueves;
  private int horaFinalJueves;
  private int horaInicioViernes;
  private int horaFinalViernes;
  private int horaInicioSabado;
  private int horaFinalSabado;
  private int horaInicioDomingo;
  private int horaFinalDomingo;
  private String usuarioModifico;

  // Método constructor de la clase Centro.
  public Centro() {
      this.idCentro = -1;
      this.nombre = "";
      this.direccion = "";
      this.logo = "";
      this.telefono = -1;
      this.correo = "";
      this.horaInicioLunes = 0;
      this.horaFinalLunes = 0;
      this.horaInicioMartes = 0;
      this.horaFinalMartes = 0;
      this.horaInicioMiercoles = 0;
      this.horaFinalMiercoles = 0;
      this.horaInicioJueves = 0;
      this.horaFinalJueves = 0;
      this.horaInicioViernes = 0;
      this.horaFinalViernes = 0;
      this.horaInicioSabado = 0;
      this.horaFinalSabado = 0;
      this.horaInicioDomingo = 0;
      this.horaFinalDomingo = 0;
      this.usuarioModifico = "";
  } // Fin del método constructor.


  // Métodos GET y SET de la clase Centro.
  public int getIdCentro() {
    return idCentro;
  }

  public void setIdCentro(int idCentro) {
    this.idCentro = idCentro;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public String getLogo() {
    return logo;
  }

  public void setLogo(String logo) {
    this.logo = logo;
  }

  public int getTelefono() {
    return telefono;
  }

  public void setTelefono(int telefono) {
    this.telefono = telefono;
  }

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

  public String getUsuarioModifico() {
    return usuarioModifico;
  }

  public void setUsuarioModifico(String usuarioModifico) {
    this.usuarioModifico = usuarioModifico;
  }

  public String getHoraInicioLunes() {
    return ServiciosU.formatearHora24(horaInicioLunes);
  }

  public void setHoraInicioLunes(int horaInicioLunes) {
    this.horaInicioLunes = horaInicioLunes;
  }

  public String getHoraFinalLunes() {
    return ServiciosU.formatearHora24(horaFinalLunes);
  }

  public void setHoraFinalLunes(int horaFinalLunes) {
    this.horaFinalLunes = horaFinalLunes;
  }

  public String getHoraInicioMartes() {
    return ServiciosU.formatearHora24(horaInicioMartes);
  }

  public void setHoraInicioMartes(int horaInicioMartes) {
    this.horaInicioMartes = horaInicioMartes;
  }

  public String getHoraFinalMartes() {
    return ServiciosU.formatearHora24(horaFinalMartes);
  }

  public void setHoraFinalMartes(int horaFinalMartes) {
    this.horaFinalMartes = horaFinalMartes;
  }

  public String getHoraInicioMiercoles() {
    return ServiciosU.formatearHora24(horaInicioMiercoles);
  }

  public void setHoraInicioMiercoles(int horaInicioMiercoles) {
    this.horaInicioMiercoles = horaInicioMiercoles;
  }

  public String getHoraFinalMiercoles() {
    return ServiciosU.formatearHora24(horaFinalMiercoles);
  }

  public void setHoraFinalMiercoles(int horaFinalMiercoles) {
    this.horaFinalMiercoles = horaFinalMiercoles;
  }

  public String getHoraInicioJueves() {
    return ServiciosU.formatearHora24(horaInicioJueves);
  }

  public void setHoraInicioJueves(int horaInicioJueves) {
    this.horaInicioJueves = horaInicioJueves;
  }

  public String getHoraFinalJueves() {
    return ServiciosU.formatearHora24(horaFinalJueves);
  }

  public void setHoraFinalJueves(int horaFinalJueves) {
    this.horaFinalJueves = horaFinalJueves;
  }

  public String getHoraInicioViernes() {
    return ServiciosU.formatearHora24(horaInicioViernes);
  }

  public void setHoraInicioViernes(int horaInicioViernes) {
    this.horaInicioViernes = horaInicioViernes;
  }

  public String getHoraFinalViernes() {
    return ServiciosU.formatearHora24(horaFinalViernes);
  }

  public void setHoraFinalViernes(int horaFinalViernes) {
    this.horaFinalViernes = horaFinalViernes;
  }

  public String getHoraInicioSabado() {
    return ServiciosU.formatearHora24(horaInicioSabado);
  }

  public void setHoraInicioSabado(int horaInicioSabado) {
    this.horaInicioSabado = horaInicioSabado;
  }

  public String getHoraFinalSabado() {
    return ServiciosU.formatearHora24(horaFinalSabado);
  }

  public void setHoraFinalSabado(int horaFinalSabado) {
    this.horaFinalSabado = horaFinalSabado;
  }

  public String getHoraInicioDomingo() {
    return ServiciosU.formatearHora24(horaInicioDomingo);
  }

  public void setHoraInicioDomingo(int horaInicioDomingo) {
    this.horaInicioDomingo = horaInicioDomingo;
  }

  public String getHoraFinalDomingo() {
    return ServiciosU.formatearHora24(horaFinalDomingo);
  }

  public void setHoraFinalDomingo(int horaFinalDomingo) {
    this.horaFinalDomingo = horaFinalDomingo;
  }
  
    
  // Metodos utilitarios de la clase Centro.
  public void cargarRegistro(int idCentro) {
      setIdCentro(-1);
      $ instanciaBD = null;

      try{
          if(idCentro > 0){
              instanciaBD = new $();
              String myQuery =
                  "SELECT C.Nombre, C.Direccion, C.Logo, C.Telefono, C.Correo, " +
                  "       P.HoraInicioLunes, P.HoraFinalLunes, " +
                  "       P.HoraInicioMartes, P.HoraFinalMartes, " +
                  "       P.HoraInicioMiercoles, P.HoraFinalMiercoles, " +
                  "       P.HoraInicioJueves, P.HoraFinalJueves, " +
                  "       P.HoraInicioViernes, P.HoraFinalViernes, " +
                  "       P.HoraInicioSabado, P.HoraFinalSabado, " +
                  "       P.HoraInicioDomingo, P.HoraFinalDomingo, " +
                  "       C.UsuarioModifico " +
                  "FROM Centro C INNER JOIN ParametrosBD P " +
                  "  ON C.ID_Centro = P.ID_Centro " +
                  "WHERE C.ID_Centro = " + idCentro + ";";

              ResultSet resultado = instanciaBD.execQry(myQuery);
              if(resultado.next()){
                  setIdCentro(idCentro);
                  setNombre(resultado.getString("Nombre"));
                  setDireccion(resultado.getString("Direccion"));
                  setLogo(resultado.getString("Logo"));
                  setTelefono(resultado.getInt("Telefono"));
                  setCorreo(resultado.getString("Correo"));
                  setHoraInicioLunes(resultado.getInt("HoraInicioLunes"));
                  setHoraFinalLunes(resultado.getInt("HoraFinalLunes"));
                  setHoraInicioMartes(resultado.getInt("HoraInicioMartes"));
                  setHoraFinalMartes(resultado.getInt("HoraFinalMartes"));
                  setHoraInicioMiercoles(resultado.getInt("HoraInicioMiercoles"));
                  setHoraFinalMiercoles(resultado.getInt("HoraFinalMiercoles"));
                  setHoraInicioJueves(resultado.getInt("HoraInicioJueves"));
                  setHoraFinalJueves(resultado.getInt("HoraFinalJueves"));
                  setHoraInicioViernes(resultado.getInt("HoraInicioViernes"));
                  setHoraFinalViernes(resultado.getInt("HoraFinalViernes"));
                  setHoraInicioSabado(resultado.getInt("HoraInicioSabado"));
                  setHoraFinalSabado(resultado.getInt("HoraFinalSabado"));
                  setHoraInicioDomingo(resultado.getInt("HoraInicioDomingo"));
                  setHoraFinalDomingo(resultado.getInt("HoraFinalDomingo"));
                  setUsuarioModifico(resultado.getString("UsuarioModifico"));
              } else {
                  setIdCentro(idCentro);
                  setNombre("");
                  setDireccion("");
                  setLogo("");
                  setTelefono(0);
                  setCorreo("");
                  setHoraInicioLunes(Consts.HORA_INICIO_PREDEFINIDA);
                  setHoraFinalLunes(Consts.HORA_FINAL_PREDEFINIDA);
                  setHoraInicioMartes(Consts.HORA_INICIO_PREDEFINIDA);
                  setHoraFinalMartes(Consts.HORA_FINAL_PREDEFINIDA);
                  setHoraInicioMiercoles(Consts.HORA_INICIO_PREDEFINIDA);
                  setHoraFinalMiercoles(Consts.HORA_FINAL_PREDEFINIDA);
                  setHoraInicioJueves(Consts.HORA_INICIO_PREDEFINIDA);
                  setHoraFinalJueves(Consts.HORA_FINAL_PREDEFINIDA);
                  setHoraInicioViernes(Consts.HORA_INICIO_PREDEFINIDA);
                  setHoraFinalViernes(Consts.HORA_FINAL_PREDEFINIDA);
                  setHoraInicioSabado(Consts.HORA_INICIO_PREDEFINIDA);
                  setHoraFinalSabado(Consts.HORA_FINAL_PREDEFINIDA);
                  setHoraInicioDomingo(Consts.HORA_INICIO_PREDEFINIDA);
                  setHoraFinalDomingo(Consts.HORA_FINAL_PREDEFINIDA);
                  setUsuarioModifico("");
              }
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

  public String toJSON(){
      // Parsea la clase Centro a JSON.
      String respuesta =
          "{" +
          "   \"error\": true, " +
          "   \"mensaje\": \"Ocurrió un error al cargar el registro del centro.\" " +
          "}";

      if(getIdCentro() > 0 )
          respuesta = 
              "{" +
              "   \"codigo\": " + getIdCentro() + "," +
              "   \"nombre\": \"" + getNombre() + "\"," +
              "   \"direccion\": \"" + getDireccion() + "\"," +
              "   \"logo\": \"" + getLogo() + "\"," +
              "   \"telefono\": " + getTelefono() + "," +
              "   \"correo\": \"" + getCorreo() + "\"," +
              "   \"horaInicioLunes\": \"" + getHoraInicioLunes() + "\"," +
              "   \"horaFinalLunes\": \"" + getHoraFinalLunes() + "\"," +
              "   \"horaInicioMartes\": \"" + getHoraInicioMartes() + "\"," +
              "   \"horaFinalMartes\": \"" + getHoraFinalMartes() + "\"," +
              "   \"horaInicioMiercoles\": \"" + getHoraInicioMiercoles() + "\"," +
              "   \"horaFinalMiercoles\": \"" + getHoraFinalMiercoles() + "\"," +
              "   \"horaInicioJueves\": \"" + getHoraInicioJueves() + "\"," +
              "   \"horaFinalJueves\": \"" + getHoraFinalJueves() + "\"," +
              "   \"horaInicioViernes\": \"" + getHoraInicioViernes() + "\"," +
              "   \"horaFinalViernes\": \"" + getHoraFinalViernes() + "\"," +
              "   \"horaInicioSabado\": \"" + getHoraInicioSabado() + "\"," +
              "   \"horaFinalSabado\": \"" + getHoraFinalSabado() + "\"," +
              "   \"horaInicioDomingo\": \"" + getHoraInicioDomingo() + "\"," +
              "   \"horaFinalDomingo\": \"" + getHoraFinalDomingo() + "\"," +
              "   \"usuarioModifico\": \"" + getUsuarioModifico() + "\"," +
              "   \"error\": false" +
              "}";

      return ServiciosU.sustituirCaracteres(respuesta);
  } // Fin del metodo toJSON().
} // Fin de la clase Centro.