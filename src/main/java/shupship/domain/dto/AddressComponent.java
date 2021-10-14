package shupship.domain.dto;

import lombok.Data;

@Data
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class AddressComponent {
    public static final String TYPE = "type";
    public static final String NAME = "name";
    public static final String ID = "id";
    public static final String VERSION = "version";
    public static final String CODE = "code";

    public enum Type {
        COUNTRY, STATE, PROVINCE, DISTRICT, WARD, AREA, STREET, BUILDING, OTHER, HOUSE_NUMBER;
    }

    private Type type;

    private String name;

    private String id;

    private Long version;

    private String code;
}
