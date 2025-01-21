package model;

import java.io.Serializable;
import java.util.Set;

public class Users implements Serializable {
    private static final long serialVersionUID = 7324357341566270684L;

    private int id;
    private String email;
    private String username;
    private String password;
    private String nombre;
    private String apellidos;
    private String dni;
    private String direccion;
    private Integer telefono1;
    private Integer telefono2;
    private Set matriculacioneses;
    private Set reunionesesForProfesorId;
    private Set reunionesesForAlumnoId;
    private Set horarioses;

    public Users(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(Integer telefono1) {
        this.telefono1 = telefono1;
    }

    public Integer getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(Integer telefono2) {
        this.telefono2 = telefono2;
    }

    public Set getMatriculacioneses() {
        return matriculacioneses;
    }

    public void setMatriculacioneses(Set matriculacioneses) {
        this.matriculacioneses = matriculacioneses;
    }

    public Set getReunionesesForProfesorId() {
        return reunionesesForProfesorId;
    }

    public void setReunionesesForProfesorId(Set reunionesesForProfesorId) {
        this.reunionesesForProfesorId = reunionesesForProfesorId;
    }

    public Set getReunionesesForAlumnoId() {
        return reunionesesForAlumnoId;
    }

    public void setReunionesesForAlumnoId(Set reunionesesForAlumnoId) {
        this.reunionesesForAlumnoId = reunionesesForAlumnoId;
    }

    public Set getHorarioses() {
        return horarioses;
    }

    public void setHorarioses(Set horarioses) {
        this.horarioses = horarioses;
    }
}
