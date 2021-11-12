package shupship.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shupship.domain.model.LeadAssign;
import shupship.util.DateTimeUtils;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeadAssignResponseDto {

    private Long id;

    private LocalDateTime createdDate;

    private String deptCode;

    private String postCode;

    private Long userAssigneeId;

    private Long userRecipientId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long fileId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String note;

    private Long status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorMsg;

    private String assigneeName;

    public static LeadAssignResponseDto leadAssignModelToDto(LeadAssign data) {
        LeadAssignResponseDto response = new LeadAssignResponseDto();
        response.setId(data.getId());
        response.setCreatedDate(DateTimeUtils.instantToLocalDateTime(data.getCreatedDate()));
        response.setUserAssigneeId(data.getUserAssigneeId());
        if (data.getUserRecipientId() != null)
            response.setUserRecipientId(data.getUserRecipientId());
        response.setDeptCode(data.getDeptCode());
        response.setPostCode(data.getPostCode());
        response.setStatus(data.getStatus());
        response.setErrorMsg(data.getErrorMsg());
        response.setFileId(data.getFileId());
        if (data.getUsers() != null)
            response.setAssigneeName(data.getUsers().getFullName());
        else response.setAssigneeName("");
        return response;
    }
}
