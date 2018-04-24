package es.indios.markn.data.model.uvigo;

import java.util.List;

/**
 * Created by guille on 9/04/18.
 */

public class Teacher {

    private String id;
    private String name;
    private String email;
    private String office;
    private List<Schedule> schedules;

    public Teacher(String id, String name, String email, String office, List<Schedule> schedules) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.office = office;
        this.schedules = schedules;
    }

    public String getOffice() {
        return office;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

}
