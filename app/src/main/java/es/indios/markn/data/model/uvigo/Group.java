package es.indios.markn.data.model.uvigo;

/**
 * Created by CristinaPosada on 12/04/2018.
 */

public class Group {

    private String code;
    private String subject_name;
    private int number;
    private Location classroom;
    private Teacher teacher;


    public Group(String code, String subject_name, int number, Location classroom, Teacher teacher) {
        this.code = code;
        this.subject_name = subject_name;
        this.number = number;
        this.classroom = classroom;
        this.teacher = teacher;
    }


    public Teacher getTeacher() {
        return teacher;
    }

    public Location getClassroom() {
        return classroom;
    }

    public int getNumber() {
        return number;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public String getCode() {
        return code;
    }
}
