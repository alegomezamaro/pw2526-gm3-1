package es.uco.pw.demo.model;

import java.time.LocalDate;

public class Reserva {

    private int id;
    private Embarcacion embarcacion;
    private String descripcion;
    private Socio socioSolicitante;
    private int plazasReserva;
    private LocalDate fechaReserva;
    private double precioReserva;

    public Reserva() {
    }


    public Reserva(int id,Embarcacion embarcacion,String descripcion,Socio socioSolicitante,int plazasReserva,LocalDate fechaReserva,double precioReserva){
        this.id = id;
        this.embarcacion = embarcacion;
        this.descripcion = descripcion;
        this.socioSolicitante = socioSolicitante;
        this.plazasReserva = plazasReserva;
        this.fechaReserva = fechaReserva;
        this.precioReserva = precioReserva;
    }

    public int getId() { return id; }
    public Embarcacion getEmbarcacion() { return embarcacion; }
    public String getDescripcion() { return descripcion; }
    public Socio getSocioSolicitante() { return socioSolicitante; }
    public int getPlazasReserva() { return plazasReserva; }
    public LocalDate getFechaReserva() { return fechaReserva; }
    public double getPrecioReserva() { return precioReserva; }

    public void setId(int id) { this.id = id; }
    public void setEmbarcacion(Embarcacion embarcacion) { this.embarcacion = embarcacion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setSocioSolicitante(Socio socioSolicitante) { this.socioSolicitante = socioSolicitante; }
    public void setPlazasReserva(int plazasReserva) { this.plazasReserva = plazasReserva; }
    public void setFechaReserva(LocalDate fechaReserva) { this.fechaReserva = fechaReserva; }
    public void setPrecioReserva(double precioReserva) { this.precioReserva = precioReserva; }

}
