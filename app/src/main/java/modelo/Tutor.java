package modelo;

public class Tutor {

    private String nombreTutor;
    private String apellidoTutor;
    private String nombreAlumno;
    private String apellidoAlumno;

    public Tutor(String nombreTutor, String apellidoTutor, String nombreAlumno, String apellidoAlumno) {
        this.nombreTutor = nombreTutor;
        this.apellidoTutor = apellidoTutor;
        this.nombreAlumno = nombreAlumno;
        this.apellidoAlumno = apellidoAlumno;
    }

    public String getNombreTutor() {
        return nombreTutor;
    }

    public void setNombreTutor(String nombreTutor) {
        this.nombreTutor = nombreTutor;
    }

    public String getApellidoTutor() {
        return apellidoTutor;
    }

    public void setApellidoTutor(String apellidoTutor) {
        this.apellidoTutor = apellidoTutor;
    }

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }

    public String getApellidoAlumno() {
        return apellidoAlumno;
    }

    public void setApellidoAlumno(String apellidoAlumno) {
        this.apellidoAlumno = apellidoAlumno;
    }
}
