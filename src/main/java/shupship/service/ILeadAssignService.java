package shupship.service;

import shupship.domain.model.LeadAssign;
import shupship.domain.model.Users;
import shupship.request.LeadAssignRequest;
import shupship.request.LeadAssignRequestV2;

import java.util.List;

public interface ILeadAssignService {

    LeadAssign createLeadAssign(Users user, LeadAssignRequest leadAssginReq);

    List<LeadAssign> createLeadAssignV2(Users user, LeadAssignRequestV2 leadAssignReques);
}
