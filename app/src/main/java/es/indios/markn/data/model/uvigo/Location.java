package es.indios.markn.data.model.uvigo;

public class Location {

    private String name;
    private String nearby_beacon;
    private String last_indication;
    private String last_image;

    public Location(String name, String nearby_beacon, String last_indication, String last_image) {
        this.name = name;
        this.nearby_beacon = nearby_beacon;
        this.last_indication = last_indication;
        this.last_image = last_image;
    }

    public String getName() {
        return name;
    }

    public String getNearby_beacon() {
        return nearby_beacon;
    }

    public String getLast_indication() {
        return last_indication;
    }

    public String getLast_image() {
        return last_image;
    }
}
