package es.uco.pw.demo.model;

import java.time.LocalDate;

public class Socio {

    private Integer dni;
    private String name;
    private String surname;
    private String address;
    private LocalDate birthDate;
    private LocalDate inscriptionDate;
    private boolean patronEmbarcacion;
    private Integer inscriptionId;
    private Integer familiaId;
    private FamiliaType relacionFamiliar;

    public Socio() {
    }

    public Socio(Integer dni, String name, String surname, LocalDate birthDate, LocalDate inscriptionDate, String address, boolean patronEmbarcacion, Integer inscriptionId, Integer familiaId, FamiliaType relacionFamiliar) {
        this.dni = dni;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.inscriptionDate = inscriptionDate;
        this.address = address;
        this.patronEmbarcacion = patronEmbarcacion;
        this.inscriptionId = inscriptionId;
        this.familiaId = familiaId;
        this.relacionFamiliar = relacionFamiliar;
    }

    // Familia debe ser opcional asi que tendre que tener dos constructores no?
    public Socio(Integer dni, String name, String surname, LocalDate birthDate, LocalDate inscriptionDate, String address, boolean patronEmbarcacion, Integer inscriptionId) {
        this(dni, name, surname, birthDate, inscriptionDate, address,
             patronEmbarcacion, inscriptionId, null, null);
    }

    public Integer getDni() { return dni; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getAddress() { return address; }
    public LocalDate getBirthDate() { return birthDate; }
    public LocalDate getInscriptionDate() { return inscriptionDate; }
    public boolean isPatronEmbarcacion() { return patronEmbarcacion; }
    public Integer getInscriptionId() { return inscriptionId; }
    public Integer getFamiliaId() { return familiaId; }
    public FamiliaType getRelacionFamiliar() { return relacionFamiliar; }

    public void setDni(Integer dni) { this.dni = dni; }
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setAddress(String address) { this.address = address; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public void setInscriptionDate(LocalDate inscriptionDate) { this.inscriptionDate = inscriptionDate; }
    public void setPatronEmbarcacion(boolean patronEmbarcacion) { this.patronEmbarcacion = patronEmbarcacion; }
    public void setInscriptionId(Integer inscriptionId) { this.inscriptionId = inscriptionId; }
    public void setFamiliaId(Integer familiaId) { this.familiaId = familiaId; }
    public void setRelacionFamiliar(FamiliaType relacionFamiliar) { this.relacionFamiliar = relacionFamiliar; }
}
