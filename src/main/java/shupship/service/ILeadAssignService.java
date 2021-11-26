package shupship.service;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import shupship.domain.model.LeadAssign;
import shupship.domain.model.Users;
import shupship.request.LeadAssignRequest;
import shupship.request.LeadAssignRequestV2;
import shupship.response.LeadAssignExcelResponse;
import shupship.response.LeadAssignHisResponse;
import shupship.response.PagingRs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface ILeadAssignService {

    LeadAssign createLeadAssign(Users user, LeadAssignRequest leadAssginReq);

    List<LeadAssign> createLeadAssignV2(Users user, LeadAssignRequestV2 leadAssignReques);

    String checkFileInput(MultipartFile multipartFile);

    LeadAssignHisResponse importFileLeadAssign(Users user, MultipartFile reapExcelDataFile) throws Exception;

    LeadAssign assignLeadForPostCode(Users users, LeadAssignRequest leadAssignRequest) throws IOException;

    PagingRs getLeadAssignHis(Users users, Pageable pageable);

    LeadAssignHisResponse getDetailFile(Long fileId);

    ByteArrayInputStream exportExcel(Collection<LeadAssignExcelResponse> list) throws IOException, InvalidFormatException;

}
