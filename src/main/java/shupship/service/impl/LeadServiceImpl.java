package shupship.service.impl;


import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shupship.domain.model.Address;
import shupship.domain.model.Lead;
import shupship.enums.CommonEnums;
import shupship.enums.LeadSource;
import shupship.enums.LeadStatus;
import shupship.enums.LeadType;
import shupship.repo.ILeadRepository;
import shupship.request.AddressRequest;
import shupship.request.LeadRequest;
import shupship.response.LeadResponse;
import shupship.response.PagingRs;
import shupship.service.ILeadService;
import shupship.util.CommonUtils;


@Log4j2
@Service
public class LeadServiceImpl implements ILeadService {
    @Autowired
    ILeadRepository iLeadRepository;

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
        Lead lead = iLeadRepository.save(data);
        lead.setCustomerCode("KH".concat(String.valueOf(lead.getId())));

//        activityLogService.createActivityLog(user, data, lead, actionType);
        // Logobject
        return lead;
    }

}

