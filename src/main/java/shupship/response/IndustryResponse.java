package shupship.response;

import lombok.Data;
import shupship.domain.model.District;
import shupship.domain.model.Industry;

@Data
public class IndustryResponse {
    private String code;
    private String name;

    public static IndustryResponse leadModelToDto(Industry industry){
        IndustryResponse model = new IndustryResponse();
        model.setCode(industry.getCode());
        model.setName(industry.getName());
        return model;
    }
}
