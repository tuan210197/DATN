package shupship.domain.dto;


import lombok.Data;

@Data
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Location {
    public static final String LAT = "lat";
    public static final String LNG = "lng";

    public static java.util.Collection<String> fields() {
        return java.util.Arrays.asList(
                LAT, LNG
        );
    }

    public Location copy() {
        return copyTo(new Location());
    }


    protected Location copyTo(Location clone) {
        clone.lat = this.lat;
        clone.lng = this.lng;
        return clone;
    }

    private Double lat;

    private Double lng;
}
