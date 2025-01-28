package model;

import java.io.Serializable;

public class Horarios implements Serializable {

    private static final long serialVersionUID = 1258367426131934365L;
    private HorariosId id;
    private Users users;
    private Modulos modulos;

    public Horarios() {
    }

    public Horarios(HorariosId id, Users users, Modulos modulos) {
        this.id = id;
        this.users = users;
        this.modulos = modulos;
    }

    public HorariosId getId() {
        return this.id;
    }

    public void setId(HorariosId id) {
        this.id = id;
    }

    public Users getUsers() {
        return this.users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Modulos getModulos() {
        return this.modulos;
    }

    public void setModulos(Modulos modulos) {
        this.modulos = modulos;
    }

    // Sobrescribir el método toString para que la impresión de este objeto sea más legible
    @Override
    public String toString() {
        return "Dia: " + this.id.getDia() +
                ", Hora: " + this.id.getHora() +
                ", Módulo ID: " + this.id.getModuloId() +
                ", Profesor ID: " + this.id.getProfeId();
    }
}
