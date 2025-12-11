package es.uco.pw.demo.model;

import java.time.LocalDate;

public class Reserva {

    private int id;
    private String matriculaEmbarcacion;
    private int plazasReserva;
    private LocalDate fechaReserva;
    private double precioReserva;

    public Reserva() {
    }

    public Reserva(int id,String matriculaEmbarcacion,int plazasReserva,LocalDate fechaReserva,double precioReserva){
        this.id = id;
        this.matriculaEmbarcacion = matriculaEmbarcacion;
        this.plazasReserva = plazasReserva;
        this.fechaReserva = fechaReserva;
        this.precioReserva = precioReserva;
    }

    public int getId() { return id; }
    public String getMatriculaEmbarcacion() { return matriculaEmbarcacion; }
    public int getPlazasReserva() { return plazasReserva; }
    public LocalDate getFechaReserva() { return fechaReserva; }
    public double getPrecioReserva() { return precioReserva; }

    public void setId(int id) { this.id = id; }
    public void setMatriculaEmbarcacion(String matriculaEmbarcacion) { this.matriculaEmbarcacion = matriculaEmbarcacion; }
    public void setPlazasReserva(int plazasReserva) { this.plazasReserva = plazasReserva; }
    public void setFechaReserva(LocalDate fechaReserva) { this.fechaReserva = fechaReserva; }
    public void setPrecioReserva(double precioReserva) { this.precioReserva = precioReserva; }

}
