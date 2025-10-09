package es.uco.pw.demo.model;

import java.time.LocalDate;

public class Patron {

    private int id;
    private String dni;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private LocalDate fechaTituloPatron;

    public Patron(int id, String dni, String nombre, String apellidos, LocalDate fechaNacimiento, LocalDate fechaTituloPatron) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaTituloPatron = fechaTituloPatron;
    }

    public int getId() {
        return id;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public LocalDate getFechaTituloPatron() {
        return fechaTituloPatron;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setFechaTituloPatron(LocalDate fechaTituloPatron) {
        this.fechaTituloPatron = fechaTituloPatron;
    }


}
