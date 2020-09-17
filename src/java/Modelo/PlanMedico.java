/*  Nombre.....:  Cita_Medica.java
 *  Descripción:  Carga el registro de la tabla Cita_Medica.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Agosto, 2019.
 */
package Modelo;

import Utilitarios.ServiciosU;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PlanMedico {
  // Variables de la clase PlanMedico.
  private Long idPlanRehabilitacion;
  private String consecutivo;
  private String medicoAtendio;
  private int fecha;
  private int horaInicio;
  private int horaFinal;
  private String diagnosticoMedico;
  private String tratamientoRecetado;
  private String examenesIndicados;
  private String observaciones;
  private boolean asistio;
  private int usuarioModifico;
  private Long fechaModificacion;

  // Método constructor de la clase PlanMedico.
  public PlanMedico() {
    this.idPlanRehabilitacion = (long) -1;
    this.consecutivo = "";
    this.medicoAtendio = "";
    this.fecha = -1;
    this.horaInicio = -1;
    this.horaFinal = -1;
    this.diagnosticoMedico = "";
    this.tratamientoRecetado = "";
    this.examenesIndicados = "";
    this.observaciones = "";
    this.asistio = false;
    this.usuarioModifico = -1;
    this.fechaModificacion = (long) -1;
  } // Fin del método constructor.

  
  // Metodos get y set de la clase PlanMedico.
  public Long getIdPlanRehabilitacion() {
    return idPlanRehabilitacion;
  }

  public void setIdPlanRehabilitacion(Long codigoPlanRehabilitacion) {
    this.idPlanRehabilitacion = codigoPlanRehabilitacion;
  }

  public String getConsecutivo() {
    return consecutivo;
  }

  public void setConsecutivo(String consecutivo) {
    this.consecutivo = consecutivo;
  }

  public String getMedicoAtendio() {
    return medicoAtendio;
  }

  public void setMedicoAtendio(String medicoAtendio) {
    this.medicoAtendio = medicoAtendio;
  }

  public int getFecha() {
    return fecha;
  }

  public void setFecha(int fecha) {
    this.fecha = fecha;
  }

  public int getHoraInicio() {
    return horaInicio;
  }

  public void setHoraInicio(int horaInicio) {
    this.horaInicio = horaInicio;
  }

  public int getHoraFinal() {
    return horaFinal;
  }

  public void setHoraFinal(int horaFinal) {
    this.horaFinal = horaFinal;
  }

  public String getDiagnosticoMedico() {
    return diagnosticoMedico;
  }

  public void setDiagnosticoMedico(String diagnosticoMedico) {
    this.diagnosticoMedico = diagnosticoMedico;
  }

  public String getTratamientoRecetado() {
    return tratamientoRecetado;
  }

  public void setTratamientoRecetado(String tratamientoRecetado) {
    this.tratamientoRecetado = tratamientoRecetado;
  }

  public String getExamenesIndicados() {
    return examenesIndicados;
  }

  public void setExamenesIndicados(String examenesIndicados) {
    this.examenesIndicados = examenesIndicados;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public boolean getAsistio() {
    return asistio;
  }

  public void setAsistio(boolean asistio) {
    this.asistio = asistio;
  }

  public int getUsuarioModifico() {
    return usuarioModifico;
  }

  public void setUsuarioModifico(int usuarioModifico) {
    this.usuarioModifico = usuarioModifico;
  }

  public Long getFechaModificacion() {
    return fechaModificacion;
  }

  public void setFechaModificacion(Long fechaModificacion) {
    this.fechaModificacion = fechaModificacion;
  }
  
  
  // Metodos utilitarios de la clase PlanMedico.
  public void cargarRegistro(long idPlanRehabilitacion, int fecha, int horaInicio,
                             int horaFinal) {
    setIdPlanRehabilitacion((long) -1);
    $ instanciaBD = null;
    $ instanciaBD2 = null;
    $ instanciaBD3 = null;
    $ instanciaBD4 = null;
    $ instanciaBD5 = null;
    $ instanciaBD6 = null;
    $ instanciaBD7 = null;

    try {
      if(idPlanRehabilitacion > 0 && fecha > 0 && horaInicio > 0 && horaFinal > 0){
        instanciaBD = new $();
        String myQuery =
            "SELECT Consecutivo, MedicoAtendio, DiagnosticoMedico, " +
            "       TratamientoRecetado, ExamenesIndicados, " +
            "       Observaciones, Asistio, UsuarioModifico, FechaModificacion " +
            "FROM Cita_Medica " +
            "WHERE ID_PlanRehabilitacion = ? " +
            "  AND Fecha = ? " +
            "  AND HoraInicio = ? " +
            "  AND HoraFinal = ?;";  

        PreparedStatement psCitaMedica = instanciaBD.prepStatement(myQuery);      
        psCitaMedica.setLong(1, idPlanRehabilitacion);
        psCitaMedica.setInt(2, fecha);
        psCitaMedica.setInt(3, horaInicio);
        psCitaMedica.setInt(4, horaFinal);
        ResultSet resultadoCitaMedica = psCitaMedica.executeQuery(); 
        
        if(resultadoCitaMedica.next()){
            setIdPlanRehabilitacion(idPlanRehabilitacion);
            setConsecutivo(resultadoCitaMedica.getString("Consecutivo"));
            setFecha(fecha);
            setHoraInicio(horaInicio);
            setHoraFinal(horaFinal);
            setMedicoAtendio(resultadoCitaMedica.getString("MedicoAtendio"));
            setDiagnosticoMedico(resultadoCitaMedica.getString("DiagosticoMedico"));
            setTratamientoRecetado(resultadoCitaMedica.getString("TratamientoRecetado"));
            setExamenesIndicados(resultadoCitaMedica.getString("ExamenesIndicados"));
            setObservaciones(resultadoCitaMedica.getString("Observaciones"));
            setAsistio(resultadoCitaMedica.getBoolean("Asistio"));
            setUsuarioModifico(resultadoCitaMedica.getInt("UsuarioModifico"));
            setFechaModificacion(resultadoCitaMedica.getLong("FechaModificacion"));
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      if(instanciaBD != null)
        instanciaBD.desconectar();
      
      instanciaBD = null;
      System.gc();
    }
  } // Fin del método cargarRegistro().
  
  
  public String toJSON(){
    // Parsea la clase PlanMedico a JSON.
        String respuesta =
            "{" +
            "   \"error\": true, " +
            "   \"mensaje\": \"Ocurrió un error al cargar el registro seleccionado.\" " +
            "}";
        
        if(getIdPlanRehabilitacion() > 0 )
            respuesta = 
                "{" +
                "   \"codigo\": " + getIdPlanRehabilitacion() + "," +
                "   \"correlativo\": \"" + getConsecutivo() + "\"," +
                "   \"fecha\": \"" + getFecha() + "\"," +
                "   \"horaInicio\": \"" + getHoraInicio() + "\"," +
                "   \"horaFinal\": \"" + getHoraFinal() + "\"," +
                "   \"diagnosticoMedico\": \"" + getDiagnosticoMedico() + "\"," +
                "   \"tratamientoRecetado\": \"" + getTratamientoRecetado() + "\"," +
                "   \"examenesIndicados\": \"" + getExamenesIndicados() + "\"," +
                "   \"observaciones\": \"" + getObservaciones() + "\"," +
                "   \"asistio\": " + getAsistio() + ", " +
                "   \"usuarioModifico\": \"" + getUsuarioModifico() + "\"," +
                "   \"error\": false" +
                "}";
                
        return ServiciosU.sustituirCaracteres(respuesta);
  } // Fin del método toJSON().
} // Fin de la clase PlanMédico.