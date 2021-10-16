package shupship.domain.dto;

import lombok.Data;

@Data
public class ProvinceDto {
    private Long provinceId;
    private String provinceCode;
    private String provinceName;
    private String formattedAddress;
    private Double lat;
    private Double lng;
}
