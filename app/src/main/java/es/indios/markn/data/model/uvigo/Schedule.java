package es.indios.markn.data.model.uvigo;

/**
 * Created by CristinaPosada on 12/04/2018.
 */

public class Schedule {

    private int id;
    private int day;
    private int start_hour;
    private int finish_hour;
    private Group group;

    public Schedule(int id, int day, int start_hour, int finish_hour, Group group) {
        this.id = id;
        this.day = day;
        this.start_hour = start_hour;
        this.finish_hour = finish_hour;
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    public int getFinish_hour() {
        return finish_hour;
    }

    public int getStart_hour() {
        return start_hour;
    }

    public int getDay() {
        return day;
    }

    public int getId() {
        return id;
    }

}
