package shupship.service;

import org.springframework.web.multipart.MultipartFile;
import shupship.domain.model.LeadAssign;
import shupship.domain.model.Users;
import shupship.request.LeadAssignRequest;
import shupship.request.LeadAssignRequestV2;
import shupship.response.LeadAssignHisResponse;

import java.io.IOException;
import java.util.List;

public interface ILeadAssignService {

    LeadAssign createLeadAssign(Users user, LeadAssignRequest leadAssginReq);

    List<LeadAssign> createLeadAssignV2(Users user, LeadAssignRequestV2 leadAssignReques);

    String checkFileInput(MultipartFile multipartFile);

    LeadAssignHisResponse importFileLeadAssign(Users user, MultipartFile reapExcelDataFile) throws Exception;

    LeadAssign assignLeadForPostCode(Users users, String postCode, String deptCode, Long leadId) throws IOException;
}
