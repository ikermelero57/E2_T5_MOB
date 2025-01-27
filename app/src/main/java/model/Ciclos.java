package model;

import java.util.HashSet;
import java.util.Set;


public class Ciclos implements java.io.Serializable {


    private static final long serialVersionUID = 6448776979065012089L;
    private int id;
    private String nombre;
    private Set matriculacioneses = new HashSet(0);
    private Set moduloses = new HashSet(0);

    public Ciclos() {
    }

    public Ciclos(int id) {
        this.id = id;
    }

    public Ciclos(int id, String nombre, Set matriculacioneses, Set moduloses) {
        this.id = id;
        this.nombre = nombre;
        this.matriculacioneses = matriculacioneses;
        this.moduloses = moduloses;
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

    public Set getMatriculacioneses() {
        return this.matriculacioneses;
    }

    public void setMatriculacioneses(Set matriculacioneses) {
        this.matriculacioneses = matriculacioneses;
    }

    public Set getModuloses() {
        return this.moduloses;
    }

    public void setModuloses(Set moduloses) {
        this.moduloses = moduloses;
    }

}
