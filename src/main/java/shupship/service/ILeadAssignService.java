package shupship.service;

import shupship.domain.model.LeadAssign;
import shupship.domain.model.Users;
import shupship.request.LeadAssignRequest;

public interface ILeadAssignService {
    LeadAssign createLeadAssign(Users user, LeadAssignRequest leadAssginReq) throws Exception;
}
