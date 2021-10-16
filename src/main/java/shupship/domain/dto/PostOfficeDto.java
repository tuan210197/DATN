package shupship.domain.dto;


import lombok.Data;
import shupship.domain.model.AuditEntity;

@Data
public class PostOfficeDto extends AuditEntity {
    private String postCode;
    private String postName;
    private String postPhone;
    private Double lat;
    private Double lng;
    private String provinceCode;
    private String districtCode;
    private String wardCode;

}
