package shupship.service.impl;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shupship.controller.BaseController;
import shupship.domain.model.BasicLogin;
import shupship.domain.model.Lead;
import shupship.domain.model.LeadAssign;
import shupship.domain.model.Users;
import shupship.enums.LeadStatus;
import shupship.enums.LeadType;
import shupship.repo.BasicLoginRepo;
import shupship.repo.ILeadAssignRepository;
import shupship.repo.ILeadRepository;
import shupship.repo.UserRepository;
import shupship.request.LeadAssignRequest;
import shupship.request.LeadAssignRequestV2;
import shupship.service.ILeadAssignService;
import shupship.service.MailSenderService;
import shupship.util.exception.HieuDzException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
public class LeadAssignServiceImpl implements ILeadAssignService {
    @Autowired
    ILeadRepository iLeadRepository;

    @Autowired
    UserRepository userRepo;

    @Autowired
    BasicLoginRepo basicLoginRepo;

    @Autowired
    private MailSenderService mailSenderService;

    @Autowired
    ILeadAssignRepository leadAssignRepo;

    @Override
    public LeadAssign createLeadAssign(Users user, LeadAssignRequest leadAssginReq) {
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
            try {
                LeadAssign data = LeadAssignRequest.leadAssignDtoToModel(leadAssginReq);
                data.setStatus(5L);
                data.setLeads(lead);
                data.setUsers(users);
                data.setCreatedBy(user.getEmpSystemId());
                assign = leadAssignRepo.save(data);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return assign;
    }

    @Override
    public List<LeadAssign> createLeadAssignV2(Users user, LeadAssignRequestV2 leadAssginReq) {

        List<LeadAssign> assigns = new ArrayList<>();
        if (CollectionUtils.isEmpty(leadAssginReq.getLeadIds())) {
            throw new HieuDzException("Illegal Arguments");
        }

        for (Long leadId : leadAssginReq.getLeadIds()) {
            LeadAssign assign = null;
            Lead lead = iLeadRepository.findLeadById(leadId);
            if (lead == null)
                throw new HieuDzException("Khách hàng không tồn tại");

            if (leadAssginReq.getUserRecipientId() == null || StringUtils.isEmpty(String.valueOf(leadAssginReq.getUserRecipientId())))
                throw new HieuDzException("Chưa chọn nhân viên đi tiếp xúc");

            Users employee = userRepo.getUserById(leadAssginReq.getUserRecipientId());
            if (employee == null)
                throw new HieuDzException("Nhân viên không tồn tại");

            if (employee.getIsActive() != null && employee.getIsActive() == 0)
                throw new HieuDzException("Nhân viên đã bị khóa");



            if (lead.getStatus() == 1) {

                LeadAssign leadAssign = leadAssignRepo.findLeadAssignByLeadIdAndEmpId(leadId, leadAssginReq.getUserRecipientId());
                if (leadAssign != null)
                    throw new HieuDzException("Đã giao tiếp xúc cho nhân viên");

                if (lead.getIsFromEVTP() != null && lead.getIsFromEVTP() == 1) {
                    lead.setType(1L);
                    if (lead.getStatus() == 1)
                        lead.setStatus(5L);
                }

                if (CollectionUtils.isNotEmpty(lead.getLeadAssigns())) {
                    Collection<LeadAssign> leadAssigns = lead.getLeadAssigns();

                    List<LeadAssign> listAss = leadAssigns.stream().filter(e -> (e.getStatus().equals(LeadStatus.NEW.getType()) && e.getDeletedStatus() == 0)).collect(Collectors.toList());
                    if (listAss.size() > 0) {
                        assign = listAss.get(0);
                    }

                    if (assign != null) {

                        assign.setDeptCode(leadAssginReq.getDeptCode());
                        assign.setPostCode(leadAssginReq.getPostCode());
                        assign.setUserAssigneeId(leadAssginReq.getUserAssigneeId());
                        assign.setUserRecipientId(leadAssginReq.getUserRecipientId());
                        assign.setStatus(5L);
                        assign.setUsers(employee);
                        assign.setCreatedBy(user.getCreatedBy());
                        assign = leadAssignRepo.save(assign);
                    } else {
                        LeadAssign data = LeadAssignRequestV2.leadAssignDtoToModel(leadAssginReq);
                        data.setStatus(1L);
                        data.setLeads(lead);
                        data.setUsers(employee);
                        data.setCreatedBy(user.getCreatedBy());
                        assign = leadAssignRepo.save(data);
                    }
                } else {
                    assign = leadAssignRepo.findLeadAssignedForPostCode(leadAssginReq.getPostCode(), leadId);
                    if (assign != null) {
                        if (assign.getUsers() == null) {
                            assign.setUserAssigneeId(leadAssginReq.getUserAssigneeId());
                            assign.setUserRecipientId(leadAssginReq.getUserRecipientId());
                            assign.setStatus(5L);
                            assign.setUsers(employee);
                            assign.setCreatedBy(user.getCreatedBy());
                            assign = leadAssignRepo.save(assign);

                        } else {
                            LeadAssign data = LeadAssignRequestV2.leadAssignDtoToModel(leadAssginReq);
                            data.setStatus(5L);
                            data.setLeads(lead);
                            data.setUsers(employee);
                            data.setCreatedBy(user.getCreatedBy());
                            assign = leadAssignRepo.save(data);

                        }
                    } else {
                        LeadAssign data = LeadAssignRequestV2.leadAssignDtoToModel(leadAssginReq);
                        data.setStatus(5L);
                        data.setLeads(lead);
                        data.setUsers(employee);
                        data.setCreatedBy(user.getCreatedBy());
                        assign = leadAssignRepo.save(data);
                    }
                }
                assigns.add(assign);
            } else {
                LeadAssign findLeadAssignById = leadAssignRepo.getLeadAssignById(leadId);
                if (findLeadAssignById != null) {
                    findLeadAssignById.setDeletedStatus(1L);
                    leadAssignRepo.save(findLeadAssignById);
                    sendEmailAssign(findLeadAssignById, leadAssginReq.getUserRecipientId(), leadAssginReq.getNote());
                }

                if (lead.getIsFromEVTP() == null || lead.getIsFromEVTP() != null && lead.getIsFromEVTP() == 1) {
                    lead.setType(1L);
                    lead.setStatus(5L);
                }

                // chuyển tiếp xúc (thêm tiếp xúc mới)
                LeadAssign data = LeadAssignRequestV2.leadAssignDtoToModel(leadAssginReq);
                data.setStatus(5L);
                data.setLeads(lead);
                data.setUsers(employee);
                data.setCreatedBy(user.getCreatedBy());
                assign = leadAssignRepo.save(data);

                assigns.add(assign);
            }
        }
        return assigns;
    }

    public void sendEmailAssign(LeadAssign leadAssign, Long userAssigned, String reason) {

        Users users = userRepo.getUserById(leadAssign.getUserRecipientId());
        Users users1 = userRepo.getUserById(userAssigned);
        if (users == null || users1 == null)
            throw new HieuDzException("Nhân viên không tồn tại");
        BasicLogin basicLogin = basicLoginRepo.findByUserUid(users.getUid().trim());
        BasicLogin basicLogin1 = basicLoginRepo.findByUserUid(users1.getUid().trim());
        Lead lead = iLeadRepository.findLeadById(leadAssign.getLeads().getId());
        String sub = "THÔNG BÁO TIẾP XÚC";
        String content = "Hệ thống đã chuyển tiếp xúc khách hàng " + lead.getFullName() + "cho nhân viên khác. Lý do " + reason +
                "GIAO TIẾP XÚC THÌ ĐÉO ĐẶT LỊCH CÒN OM";
        mailSenderService.sendSimpleMessage(basicLogin.getEmail(), sub, content);
//        log.info("Send email for: " + users.getEmpSystemId());

        String sub1 = "THÔNG BÁO TIẾP XÚC";
        String content1 = "Đồng chí vừa được chuyển tiếp xúc 1 khách hàng. Vui lòng kiểm tra lại. LO MÀ ĐẶT LỊCH KO THÌ BỐ XỬ";
        mailSenderService.sendSimpleMessage(basicLogin1.getEmail(), sub1, content1);
//        log.info("Send email for: " + users1.getEmpSystemId());
    }

}
