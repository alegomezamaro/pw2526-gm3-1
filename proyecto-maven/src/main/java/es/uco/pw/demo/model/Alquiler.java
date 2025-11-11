package es.uco.pw.demo.model;

import java.time.LocalDate;

public class Alquiler {

    private Integer id;
    private String matriculaEmbarcacion;
    private String dniTitular;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer plazasSolicitadas;
    private double precioTotal;

    public Alquiler(Integer id, String matriculaEmbarcacion, String dniTitular, LocalDate fechaInicio, LocalDate fechaFin, Integer plazasSolicitadas, double precioTotal) {
        this.id = id;
        this.matriculaEmbarcacion = matriculaEmbarcacion;
        this.dniTitular = dniTitular;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.plazasSolicitadas = plazasSolicitadas;
        this.precioTotal = precioTotal;
    }

    public Alquiler(){}

    public Integer getId() {
        return id;
    }

    public String getMatriculaEmbarcacion() {
        return matriculaEmbarcacion;
    }

    public String getDniTitular() {
        return dniTitular;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public Integer getPlazasSolicitadas() {
        return plazasSolicitadas;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setMatriculaEmbarcacion(String matriculaEmbarcacion) {
        this.matriculaEmbarcacion = matriculaEmbarcacion;
    }

    public void setDniTitular(String dniTitular) {
        this.dniTitular = dniTitular;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setPlazasSolicitadas(Integer plazasSolicitadas) {
        this.plazasSolicitadas = plazasSolicitadas;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }


}
