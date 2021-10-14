package shupship.domain.dto;

import lombok.Data;

@Data
public class WardDto {

    private String wardId;
    private String wardCode;
    private String wardName;
    private String formattedAddress;
    private String districtCode;
    private String provinceCode;
    private Double lat;
    private Double lng;

}
