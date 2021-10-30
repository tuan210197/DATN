package shupship.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shupship.domain.model.Industry;
@AllArgsConstructor
@Data
@NoArgsConstructor
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
