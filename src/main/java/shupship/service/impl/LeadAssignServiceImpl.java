package shupship.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shupship.domain.model.Lead;
import shupship.domain.model.LeadAssign;
import shupship.domain.model.Users;
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

    @Override
    @Transactional
    public LeadAssign createLeadAssign(Users user, LeadAssignRequest leadAssginReq) throws Exception {
        Lead lead = iLeadRepository.findLeadById(leadAssginReq.getLeadId());
        if (lead == null)
            throw new HieuDzException("Khách hàng không tồn tại.");
        Users employee = userRepo.getUserById(leadAssginReq.getUserRecipientId());
        if (employee == null)
            throw new HieuDzException("Bưu tá không tồn tại.");

        return null;
    }
}
