package shupship.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import shupship.domain.model.LeadAssign;

import javax.validation.constraints.Size;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class LeadAssignRequestV2 extends BaseRequest {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userAssigneeId;

    private Long userRecipientId;

    private List<Long> leadIds;

    // Ma chi nhanh
    @Size(max = 20)
    private String deptCode;

    // Ma buu cuc
    @Size(max = 20)
    private String postCode;

    @Size(max = 100)
    private String note;

    public static LeadAssign leadAssignDtoToModel(LeadAssignRequestV2 leadAssignRequest) {
        LeadAssign leadAssign = new LeadAssign();
        leadAssign.setUserAssigneeId(leadAssignRequest.getUserAssigneeId());
        leadAssign.setUserRecipientId(leadAssignRequest.getUserRecipientId());
        leadAssign.setDeptCode(leadAssignRequest.getDeptCode());
        leadAssign.setPostCode(leadAssignRequest.getPostCode());
        leadAssign.setNote(leadAssignRequest.getNote());

        return leadAssign;
    }
}
