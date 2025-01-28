package model;

import java.io.Serializable;

public class HorariosId implements Serializable {

    private static final long serialVersionUID = -8043428246151946896L;

    private String dia;
    private char hora;
    private int moduloId;
    private int profeId;

    public HorariosId() {
    }

    public HorariosId(String dia, char hora, int moduloId, int profeId) {
        this.dia = dia;
        this.hora = hora;
        this.moduloId = moduloId;
        this.profeId = profeId;
    }

    public String getDia() {
        return this.dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public char getHora() {
        return this.hora;
    }

    public void setHora(char hora) {
        this.hora = hora;
    }

    public int getModuloId() {
        return this.moduloId;
    }

    public void setModuloId(int moduloId) {
        this.moduloId = moduloId;
    }

    public int getProfeId() {
        return this.profeId;
    }

    public void setProfeId(int profeId) {
        this.profeId = profeId;
    }

    // Sobrescribir el método toString para que la impresión de este objeto sea más legible
    @Override
    public String toString() {
        return "Día: " + this.dia +
                ", Hora: " + this.hora +
                ", Módulo ID: " + this.moduloId +
                ", Profesor ID: " + this.profeId;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof HorariosId))
            return false;
        HorariosId castOther = (HorariosId) other;

        return ((this.getDia() == castOther.getDia())
                || (this.getDia() != null && castOther.getDia() != null && this.getDia().equals(castOther.getDia())))
                && (this.getHora() == castOther.getHora()) && (this.getModuloId() == castOther.getModuloId())
                && (this.getProfeId() == castOther.getProfeId());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + (getDia() == null ? 0 : this.getDia().hashCode());
        result = 37 * result + this.getHora();
        result = 37 * result + this.getModuloId();
        result = 37 * result + this.getProfeId();
        return result;
    }
}
