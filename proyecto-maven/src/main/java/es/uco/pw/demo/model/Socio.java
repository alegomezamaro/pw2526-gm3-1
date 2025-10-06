package es.uco.pw.demo.model;

import java.time.LocalDate;

public class Socio {
    
    private int dni;
    private String name;
    private String surname;
    private String direction;
    private LocalDate birthDate;
    private LocalDate inscriptionDate;
    private boolean patronEmbarcacion;
    private int cuota;
    
    public Socio(int dni, String name, String surname, LocalDate birthDate, LocalDate inscriptionDate, String direction, boolean patronEmbarcacion, int cuota) {
        this.dni = dni;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.cuota = cuota;
        this.inscriptionDate = inscriptionDate;
        this.patronEmbarcacion = patronEmbarcacion;
        this.direction = direction;
    }

    public int getDni() {
        return dni;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getDirection() {
        return direction;
    }

    public LocalDate getInscriptionDate() {
        return inscriptionDate;
    }

    public boolean getPatronEmbarcacion() {
        return patronEmbarcacion;
    }
    public int getCuota() {
        return cuota;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    
    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setCuota(int cuota) {
        this.cuota = cuota;
    }

    public void setInscriptionDate(LocalDate date) {
        this.inscriptionDate = date;
    }

    public void setPatronEmbarcacion(boolean patron) {
        this.patronEmbarcacion = patron;
    }
}