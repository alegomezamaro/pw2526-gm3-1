package es.uco.pw.demo.model;

import java.util.List;

public class Familia {
  
  private Integer id;
  private String dniTitular;
  private Integer numAdultos;
  private Integer numNiños;

  public Integer getId() {
      return id;
  }

  public String getDniTitular() {
      return dniTitular;
  }

  public Integer getNumAdultos() {
      return numAdultos;
  }

  public Integer getNumNiños() {
      return numNiños;
  }

  
  public void setId(Integer id) {
      this.id = id;
  }

  public void setDniTitular(String dniTitular) {
      this.dniTitular = dniTitular;
  }

  public void setNumAdultos(Integer numAdultos) {
      this.numAdultos = numAdultos;
  }

  public void setNumNiños(Integer numNiños) {
      this.numNiños = numNiños;
  }


}
