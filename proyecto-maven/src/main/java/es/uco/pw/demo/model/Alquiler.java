package es.uco.pw.demo.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Alquiler {

    private int id;
    private int embarcacion;
    private int socioTitular;
    private List<String> participantes;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int plazasSolicitadas;
    private double precioTotal;

    public Alquiler(int id, int embarcacion, int socioTitular, List<String> participantes, LocalDate fechaInicio, LocalDate fechaFin, int plazasSolicitadas, double precioTotal) {
        this.id = id;
        this.embarcacion = embarcacion;
        this.socioTitular = socioTitular;
        this.participantes = (participantes != null) ? participantes : new ArrayList<>();
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.plazasSolicitadas = plazasSolicitadas;
        this.precioTotal = precioTotal;
    }

    public Alquiler(){}

    public int getId() {
        return id;
    }

    public int getEmbarcacion() {
        return embarcacion;
    }

    public int getSocioTitular() {
        return socioTitular;
    }

    public List<String> getParticipantes() {
        return participantes;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public int getPlazasSolicitadas() {
        return plazasSolicitadas;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmbarcacion(int embarcacion) {
        this.embarcacion = embarcacion;
    }

    public void setSocioTitular(int socioTitular) {
        this.socioTitular = socioTitular;
    }

    public void setParticipantes(List<String> participantes) {
        this.participantes = participantes;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setPlazasSolicitadas(int plazasSolicitadas) {
        this.plazasSolicitadas = plazasSolicitadas;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }


}
