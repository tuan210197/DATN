package shupship.domain.dto;

import lombok.Data;

import java.util.List;

@Data
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Geometry {
    public static final String LOCATION = "location";
    public static final String BOUNDS = "bounds";
    public static final String COORDS = "coords";

    public static java.util.Collection<String> fields() {
        return java.util.Arrays.asList(
                LOCATION, BOUNDS, COORDS
        );
    }

    public Geometry copy() {
        return copyTo(new Geometry());
    }


    protected Geometry copyTo(Geometry clone) {
        clone.location = this.location;
        clone.bounds = this.bounds;
        clone.coords = this.coords;
        return clone;
    }

    private Location location;

    private Bounds bounds;

    private List<Location> coords;
}
