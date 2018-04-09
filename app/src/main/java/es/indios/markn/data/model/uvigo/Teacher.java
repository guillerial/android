package es.indios.markn.data.model.uvigo;

/**
 * Created by guille on 9/04/18.
 */

public class Teacher {

    private String name;
    private String email;
    private String office;

    public Teacher(String name, String email, String office) {
        this.name = name;
        this.email = email;
        this.office = office;
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
}
