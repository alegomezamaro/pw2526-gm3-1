package es.uco.pw.demo.model;

public class Embarcacion {

    private String matricula;
    private String nombre;
    private EmbarcacionType tipo;
    private int plazas;
    private String dimensiones;
    private Patron patronAsignado;

    public Embarcacion(String matricula, String nombre, EmbarcacionType tipo, int plazas, String dimensiones, Patron patronAsignado) {
        this.matricula = matricula;
        this.nombre = nombre;
        this.tipo = tipo;
        this.plazas = plazas;
        this.dimensiones = dimensiones;
        this.patronAsignado = patronAsignado;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getNombre() {
        return nombre; 
    }

    public EmbarcacionType getTipo() {
        return tipo;
    }

    public int getPlazas() {
        return plazas;
    }

    public String getDimensiones() {
        return dimensiones;
    }

    public Patron getPatronAsignado() {
        return patronAsignado;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(EmbarcacionType tipo) {
        this.tipo = tipo;
    }

    public void setPlazas(int plazas) {
        this.plazas = plazas;
    }

    public void setDimensiones(String dimensiones) {
        this.dimensiones = dimensiones;
    }

    public void setPatronAsignado(Patron patronAsignado) {
        this.patronAsignado = patronAsignado;
    }


}
