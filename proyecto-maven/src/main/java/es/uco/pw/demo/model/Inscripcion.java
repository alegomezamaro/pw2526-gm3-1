package es.uco.pw.demo.model;

import java.time.LocalDate;

import org.springframework.cglib.core.Local;

public class Inscripcion {
  
  private Integer id;
  private InscripcionType tipoCuota;
  private Integer cuotaAnual;
  private LocalDate fechaInscripcion;
  private String dniTitular;
  private Integer familiaId;

  // Es cuotaAnual un atributo calculado en caso de que tipoCuota = Familiar, teniendo asi que sumar la cuota de todos los integrantes de la familia, iterando en los socios de la familia de familiaId y sumando sus cuotas?

  public Inscripcion(Integer id, InscripcionType tipoCuota, Integer cuotaAnual,
                  LocalDate fechaInscripcion, String dniTitular, Integer familiaId) {
      this.id = id;
      this.tipoCuota = tipoCuota;
      this.cuotaAnual = cuotaAnual;
      this.fechaInscripcion = fechaInscripcion;
      this.dniTitular = dniTitular;
      this.familiaId = familiaId;
  }

  
  public Inscripcion(){}

  public Integer getId() {
      return id;
  }

  public InscripcionType getTipoCuota() {
      return tipoCuota;
  }

  public Integer getCuotaAnual() {
      return cuotaAnual;
  }

  public String getDniTitular() {
      return dniTitular;
  }

  public LocalDate getFechaInscripcion() {
      return fechaInscripcion;
  }

  public Integer getFamiliaId() {
      return familiaId;
  }

  public void setId(Integer id) {
      this.id = id;
  }

  public void setTipoCuota(InscripcionType tipoCuota) {
      this.tipoCuota = tipoCuota;
  }

  public void setCuotaAnual(Integer cuotaAnual) {
      this.cuotaAnual = cuotaAnual;
  }

  public void setDniTitular(String dniTitular) {
      this.dniTitular = dniTitular;
  }

  public void setFechaInscripcion(LocalDate fechaInscripcion) {
      this.fechaInscripcion = fechaInscripcion;
  }

  public void setFamiliaId(Integer familiaId) {
      this.familiaId = familiaId;
  }


}
