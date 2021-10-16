package shupship.domain.dto;

import lombok.Data;

@Data
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Bounds {
    public static final String NORTHEAST = "northeast";
    public static final String SOUTHWEST = "southwest";

    public static java.util.Collection<String> fields() {
        return java.util.Arrays.asList(
                NORTHEAST, SOUTHWEST
        );
    }

    public Bounds copy() {
        return copyTo(new Bounds());
    }


    protected Bounds copyTo(Bounds clone) {
        clone.northeast = this.northeast;
        clone.southwest = this.southwest;
        return clone;
    }

    private Location northeast;

    private Location southwest;
}
