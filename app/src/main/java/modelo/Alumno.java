package modelo;

public class Alumno {
    private String nombre;
    private String apellido;
    private String Carrera;
    private String semestre;

    public Alumno(String nombre, String apellido, String carrera, String semestre) {
        this.nombre = nombre;
        this.apellido = apellido;
        Carrera = carrera;
        this.semestre = semestre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCarrera() {
        return Carrera;
    }

    public void setCarrera(String carrera) {
        Carrera = carrera;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }
}
