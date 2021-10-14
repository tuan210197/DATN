package shupship.domain.dto;

import lombok.Data;

import java.util.List;

@Data
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class AddressMapDto {
    public static final String FORMATTEDADDRESS = "formattedAddress";
    public static final String COMPONENTS = "components";
    public static final String GEOMETRY = "geometry";
    public static final String ID = "id";
    public static final String VERSION = "version";
    public static final String CODE = "code";

//    private

    private String formattedAddress;

    private List<AddressComponent> components;

    private Geometry geometry;

    private String id;

    private Long version;

    private String code;

}
