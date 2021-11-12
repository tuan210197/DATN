package shupship.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shupship.domain.model.CommonCode;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonCodeResponseDto {

    private Long id;

    private String classCd;

    private String code;

    private String name;

    private Long priority;

    private Long type;

    private String description;

    public static CommonCodeResponseDto commonCodeResponseDto(CommonCode data) {
        CommonCodeResponseDto response = new CommonCodeResponseDto();
        response.setId(data.getId());
        response.setCode(data.getCode());
        response.setDescription(data.getDescription());
        response.setName(data.getName());
        response.setClassCd(data.getClassCd());
        response.setPriority(data.getPriority());
        response.setType(Long.valueOf(data.getExtValue()));
        return response;
    }
}
