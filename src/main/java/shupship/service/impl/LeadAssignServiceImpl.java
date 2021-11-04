package shupship.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shupship.domain.model.Lead;
import shupship.domain.model.LeadAssign;
import shupship.domain.model.Users;
import shupship.enums.LeadType;
import shupship.repo.ILeadAssignRepository;
import shupship.repo.ILeadRepository;
import shupship.repo.UserRepo;
import shupship.request.LeadAssignRequest;
import shupship.service.ILeadAssignService;
import shupship.util.exception.HieuDzException;

@Log4j2
@Service
public class LeadAssignServiceImpl implements ILeadAssignService {
    @Autowired
    ILeadRepository iLeadRepository;
    @Autowired
    UserRepo userRepo;

    @Autowired
    ILeadAssignRepository leadAssignRepo;

    @Override
    @Transactional
    public LeadAssign createLeadAssign(Users user, LeadAssignRequest leadAssginReq) throws Exception {
        Lead lead = iLeadRepository.findLeadById(leadAssginReq.getLeadId());
        if (lead == null)
            throw new HieuDzException("Khách hàng không tồn tại.");
        Users users = userRepo.getUserById(leadAssginReq.getUserRecipientId());
        if (users == null)
            throw new HieuDzException("Bưu tá không tồn tại.");

        LeadAssign findLeadAssignById = leadAssignRepo.findLeadAssignByLeadIdAndEmpId(leadAssginReq.getLeadId(), leadAssginReq.getUserRecipientId());
        if (findLeadAssignById != null && !findLeadAssignById.getUserAssigneeId().equals(findLeadAssignById.getUserRecipientId()))
            throw new HieuDzException("Đã giao tiếp xúc cho bưu tá.");

        if (lead.getIsFromEVTP() != null && lead.getIsFromEVTP().equals(1L)) {
            lead.setType(LeadType.DUOC_GIAO.getType());
            if (lead.getStatus() == 1)
                lead.setStatus(5L);
        }

        LeadAssign assign = leadAssignRepo.findLeadAssignedForPostCode(leadAssginReq.getPostCode(), leadAssginReq.getLeadId());
        if (assign != null) {
            if (assign.getUsers() == null) {
                assign.setUserAssigneeId(leadAssginReq.getUserAssigneeId());
                assign.setUserRecipientId(leadAssginReq.getUserRecipientId());
                assign.setStatus(5L);
                assign.setUsers(users);
                assign = leadAssignRepo.save(assign);
            } else {
                LeadAssign data = LeadAssignRequest.leadAssignDtoToModel(leadAssginReq);
                data.setStatus(5L);
                data.setLeads(lead);
                data.setUsers(users);
                assign = leadAssignRepo.save(data);
            }
        } else {
            LeadAssign data = LeadAssignRequest.leadAssignDtoToModel(leadAssginReq);
            data.setStatus(5L);
            data.setLeads(lead);
            data.setUsers(users);
            assign = leadAssignRepo.save(data);
        }
        return assign;
    }
}
