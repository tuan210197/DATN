package shupship.service.impl;


import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shupship.common.Const;
import shupship.common.Constants;
import shupship.domain.model.Lead;
import shupship.domain.model.Schedule;
import shupship.domain.model.Users;
import shupship.enums.CommonEnums;
import shupship.enums.LeadSource;
import shupship.enums.LeadStatus;
import shupship.enums.LeadType;
import shupship.repo.ILeadRepository;
import shupship.repo.IScheduleRepository;
import shupship.request.LeadRequest;
import shupship.request.LeadUpdateRequest;
import shupship.response.LeadResponse;
import shupship.response.PagingRs;
import shupship.service.ILeadService;
import shupship.util.CommonUtils;
import shupship.util.exception.ApplicationException;

import java.time.Instant;
import java.util.List;


@Log4j2
@Service
public class LeadServiceImpl implements ILeadService {
    @Autowired
    ILeadRepository iLeadRepository;
    @Autowired
    IScheduleRepository scheduleRepository;

    @Override
    public PagingRs getListLead(Pageable pageable) throws ApplicationContextException {

        Page<Lead> leadList = iLeadRepository.getListLead(pageable);
//        List<LeadResponse> leadResponses = new ArrayList<>();
//        for (Lead model : leadList){
//            LeadResponse laLeadResponse = LeadResponse.leadModelToDto(model);
//            leadResponses.add(laLeadResponse);
//        }
        Page<LeadResponse> leadResponses = leadList.map(LeadResponse::leadModelToDto);
        PagingRs listLeadResponse = new PagingRs();
        listLeadResponse.setTotalItem(leadList.getTotalElements());
        listLeadResponse.setData(leadResponses.getContent());
        return listLeadResponse;
    }

    @Override
    public Lead insertLead(LeadRequest leadRequest) throws ApplicationContextException {
        Lead data = new Lead();
        data.setTitle(leadRequest.getTitle());
        data.setRepresentation(leadRequest.getRepresentation());
        if (StringUtils.isNotEmpty(leadRequest.getFullName())) {
            data.setFullName(leadRequest.getFullName());
            data.setCompanyName(leadRequest.getFullName());
        }
        if (StringUtils.isNotEmpty(leadRequest.getCompanyName())) {
            data.setFullName(leadRequest.getCompanyName());
            data.setCompanyName(leadRequest.getCompanyName());
        }
        data.setConvertStatus(CommonEnums.LeadConvertStatus.NORMAL);
        data.setStatus(LeadStatus.NEW.getType());
        data.setLeadSource(LeadSource.valueOf(leadRequest.getLeadSource()).name());

        data.setPhone(CommonUtils.convertPhone(leadRequest.getPhone()));
        data.setType(LeadType.TU_NHAP.getType());
        // Neu duoc tao tu EVTP -> isFromEVTP = 1
        data.setIsFromEVTP(1L);
        data.setCreatedDate(Instant.now());
        Lead lead = iLeadRepository.save(data);
        BeanUtils.copyProperties(leadRequest, data);
        lead.setCustomerCode("KH".concat(String.valueOf(lead.getId())));

//        activityLogService.createActivityLog(user, data, lead, actionType);
        // Logobject
        return lead;
    }

    @Override
    @Transactional
    public Lead updateLead(Long id, LeadUpdateRequest leadRequest) throws ApplicationException {
     PagingRs pagingRs = new PagingRs();
        Lead existData = iLeadRepository.findLeadById(id);
        try{
            if (existData == null) {
                throw new ApplicationException(Const.LEAD_NOT_EXIT);
            }
            if (StringUtils.isNotBlank(leadRequest.getPhone())) {
                if (!existData.getPhone().equals(leadRequest.getPhone())) {
                    List<Schedule> schedules = scheduleRepository.getSchedulesByLeadId(id);
                    if (CollectionUtils.isNotEmpty(schedules)) {
                        throw new ApplicationException(Const.SCHEDULE_PHONE_ONLY);
                    } else {
                        if (existData.getPhone().equals(CommonUtils.convertPhone(leadRequest.getPhone()))) {
                            existData.setPhone(CommonUtils.convertPhone(leadRequest.getPhone()));
                        } else {
                            if (CollectionUtils.isNotEmpty(iLeadRepository.findLeadWithPhoneOnEVTP(CommonUtils.convertPhone(leadRequest.getPhone()))))
                                throw new ApplicationException(Const.PHONE_EXIT_DATA);
                            else existData.setPhone(CommonUtils.convertPhone(leadRequest.getPhone()));
                        }
                    }
                } else existData.setPhone(CommonUtils.convertPhone(leadRequest.getPhone()));
            }

            if (StringUtils.isNotEmpty(leadRequest.getFullName())) {
                existData.setFullName(leadRequest.getFullName());
                existData.setCompanyName(leadRequest.getFullName());
            }
            if (StringUtils.isNotEmpty(leadRequest.getCompanyName())) {
                existData.setFullName(leadRequest.getCompanyName());
                existData.setCompanyName(leadRequest.getCompanyName());
            }
            existData.setRepresentation(leadRequest.getRepresentation());
            existData.setTitle(leadRequest.getTitle());
//        existData.setAnnualQuantity(inputData.getAnnualQuantity());
//        existData.setWeight(inputData.getWeight());
//        existData.setExpectedRevenue(inputData.getExpectedRevenue());
//        existData.setLeadSource(inputData.getLeadSource());
//        existData.setInProvincePrice(inputData.getInProvincePrice());
//        existData.setOutProvincePrice(inputData.getOutProvincePrice());
//        existData.setQuality(inputData.getQuality());
//        existData.setCompensation(inputData.getCompensation());
//        existData.setPayment(inputData.getPayment());
//        existData.setOther(inputData.getOther());
//        existData.setQuantityMonth(inputData.getQuantityMonth());
        }catch (Exception e){
            e.getLocalizedMessage();
            throw e;
        }
       return iLeadRepository.save(existData);
    }

    @Override
    public Lead deleteLeadOnEVTP(Long leadId) throws ApplicationException {
        Lead existData = iLeadRepository.findLeadById(leadId);

        if (existData == null) {
            throw new ApplicationException(Const.LEAD_NOT_EXIT);
        }
        List<Schedule> schedules = scheduleRepository.getSchedulesByLeadId(leadId);
        if (CollectionUtils.isNotEmpty(schedules)) {
           throw  new ApplicationException(Const.SCHEDULE_PHONE_ONLY);
        }
        existData.setDeletedStatus(Constants.DELETE_LEAD);
        Lead lead = iLeadRepository.save(existData);
        return lead;
    }

}

