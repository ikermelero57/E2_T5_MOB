package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class Ciclos implements Serializable {


    private static final long serialVersionUID = 6448776979065012089L;
    private int id;
    private String nombre;


    public Ciclos() {
    }

    public Ciclos(int id) {
        this.id = id;
    }

    public Ciclos(int id, String nombre, Set matriculacioneses, Set moduloses) {
        this.id = id;
        this.nombre = nombre;

    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }



}
