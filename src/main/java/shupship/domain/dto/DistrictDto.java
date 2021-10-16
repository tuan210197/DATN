package shupship.domain.dto;


import lombok.Data;

@Data
public class DistrictDto {

    private Long districtId;
    private String districtCode;
    private String districtName;
    private String formattedAddress;
    private Double lat;
    private Double lng;
    private String provinceCode;
}
