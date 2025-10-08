package es.uco.pw.demo.model;

import java.time.LocalDate;

public class Inscripcion {
  
  private Integer id;
  private InscriptionType type;
  private Integer yearFee;
  private Integer socioTitular;
  private LocalDate date;
  private Integer familiaId;

  // Es yearFee un atributo calculado en caso de que type = Familiar, teniendo asi que sumar la cuota de todos los integrantes de la familia, iterando en los socios de la familia de familiaId y sumando sus cuotas?

  public Integer getId() {
      return id;
  }

  public InscriptionType getType() {
      return type;
  }

  public Integer getYearFee() {
      return yearFee;
  }

  public Integer getSocioTitular() {
      return socioTitular;
  }

  public LocalDate getDate() {
      return date;
  }

  public Integer getFamiliaId() {
      return familiaId;
  }

  public void setId(Integer id) {
      this.id = id;
  }

  public void setType(InscriptionType type) {
      this.type = type;
  }

  public void setYearFee(Integer yearFee) {
      this.yearFee = yearFee;
  }

  public void setSocioTitular(Integer socioTitular) {
      this.socioTitular = socioTitular;
  }

  public void setDate(LocalDate date) {
      this.date = date;
  }

  public void setFamiliaId(Integer familiaId) {
      this.familiaId = familiaId;
  }


}
