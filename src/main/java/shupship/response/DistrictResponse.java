package shupship.response;

import lombok.Data;
import shupship.domain.model.District;

@Data
public class DistrictResponse {
    private Long districtId;
    private String districtCode;
    private String districtName;

    public static DistrictResponse leadModelToDto(District district){
        DistrictResponse model = new DistrictResponse();
        model.setDistrictId(district.getDistrictId());
        model.setDistrictCode(district.getDistrictCode());
        model.setDistrictName(district.getDistrictName());
        return model;
    }
}
