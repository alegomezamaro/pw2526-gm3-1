package es.uco.pw.demo.model;

import java.time.LocalDate;

public class Socio {

    private String dni;
    private String nombre;
    private String apellidos;
    private String direccion;
    private LocalDate fechaNacimiento;
    private boolean patronEmbarcacion;

    public Socio() {
    }

    public Socio(String dni, String nombre, String apellidos, LocalDate fechaNacimiento, String direccion, boolean patronEmbarcacion) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.patronEmbarcacion = patronEmbarcacion;
    }

    public String getDni() { return dni; }
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public String getDireccion() { return direccion; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public boolean esPatronEmbarcacion() { return patronEmbarcacion; }

    public void setDni(String dni) { this.dni = dni; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setPatronEmbarcacion(boolean patronEmbarcacion) { this.patronEmbarcacion = patronEmbarcacion; }
}
