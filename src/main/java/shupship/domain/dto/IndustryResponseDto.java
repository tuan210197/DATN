package shupship.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shupship.domain.model.Industry;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IndustryResponseDto {
    private Long id;

    private String code;

    private String name;

    private String description;

    public static IndustryResponseDto industryDtoToModel(Industry data) {
        IndustryResponseDto response = new IndustryResponseDto();
        response.setId(data.getId());
        response.setCode(data.getCode());
        response.setDescription(data.getDescription());
        response.setName(data.getName());
        return response;
    }
}
