package shupship.response;

import lombok.Data;
import shupship.domain.model.Province;

@Data
public class ProvinceResponse {
    private Long provinceId;
    private String provinceCode;
    private String provinceName;

    public static ProvinceResponse leadModelToDto(Province province){
        ProvinceResponse model = new ProvinceResponse();
        model.setProvinceId(province.getProvinceId());
        model.setProvinceCode(province.getProvinceCode());
        model.setProvinceName(province.getProvinceName());
        return model;
    }
}
