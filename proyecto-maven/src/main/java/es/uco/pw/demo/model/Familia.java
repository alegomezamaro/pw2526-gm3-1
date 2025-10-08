package es.uco.pw.demo.model;

import java.util.List;

public class Familia {
  
  private Integer id;
  private Integer mainDni;
  private List<Integer> familiaDnis;

  public Integer getId() {
      return id;
  }

  public Integer getMainDni() {
      return mainDni;
  }

  public List<Integer> getFamiliaDnis() {
      return familiaDnis;
  }
  
  public void setId(Integer id) {
      this.id = id;
  }

  public void setMainDni(Integer mainDni) {
      this.mainDni = mainDni;
  }

  public void setFamiliaDnis(List<Integer> familiaDnis) {
      this.familiaDnis = familiaDnis;
  }


}
