package shupship.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import shupship.domain.model.LeadAssign;
import shupship.domain.model.LeadAssignExcel;

import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
public class LeadAssignRequest extends BaseRequest {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userAssigneeId;

    private Long userRecipientId;

    private Long leadId;

    // Ma chi nhanh
    @Size(max = 20)
    private String deptCode;

    // Ma buu cuc
    @Size(max = 20)
    private String postCode;

    @Size(max = 100)
    private String note;

    private Long status;

    private LeadAssignExcel leadAssignExcel;

    public static LeadAssign leadAssignDtoToModel(LeadAssignRequest leadAssignRequest) {
        LeadAssign leadAssign = new LeadAssign();
        leadAssign.setUserAssigneeId(leadAssignRequest.getUserAssigneeId());
        leadAssign.setUserRecipientId(leadAssignRequest.getUserRecipientId());
        leadAssign.setDeptCode(leadAssignRequest.getDeptCode());
        leadAssign.setPostCode(leadAssignRequest.getPostCode());
        leadAssign.setNote(leadAssignRequest.getNote());
        ////leadAssign.setLeadAssignExcel(leadAssignRequest.getLeadAssignExcel());
        leadAssign.setStatus(leadAssignRequest.getStatus());

        return leadAssign;
    }
}
