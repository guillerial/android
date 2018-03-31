package es.indios.markn.data.model.uvigo;

public class Location {

    private String name;
    private String nearby_beacon;

    public Location(String name, String nearby_beacon) {
        this.name = name;
        this.nearby_beacon = nearby_beacon;
    }

    public String getName() {
        return name;
    }

    public String getNearby_beacon() {
        return nearby_beacon;
    }
}
