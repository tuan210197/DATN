package shupship.service.impl;


import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shupship.common.Constants;
import shupship.domain.model.Address;
import shupship.domain.model.Industry;
import shupship.domain.model.Lead;
import shupship.domain.model.Schedule;
import shupship.enums.LeadSource;
import shupship.enums.LeadStatus;
import shupship.enums.LeadType;
import shupship.repo.IIndustryRepository;
import shupship.repo.ILeadRepository;
import shupship.repo.IScheduleRepository;
import shupship.request.AddressRequest;
import shupship.request.LeadRequest;
import shupship.request.LeadUpdateRequest;
import shupship.response.LeadResponse;
import shupship.response.PagingRs;
import shupship.service.ILeadService;
import shupship.util.CommonUtils;
import shupship.util.exception.ApplicationException;
import shupship.util.exception.HieuDzException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Log4j2
@Service
public class LeadServiceImpl implements ILeadService {
    @Autowired
    ILeadRepository iLeadRepository;

    @Autowired
    IScheduleRepository scheduleRepository;

    @Autowired
    IIndustryRepository industryRepository;

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
        if (StringUtils.isNotEmpty(leadRequest.getTitle()))
            data.setTitle(leadRequest.getTitle());
        else throw new HieuDzException("Không được để trống tille");

        if (StringUtils.isNotEmpty(leadRequest.getFullName())) {
            data.setFullName(leadRequest.getFullName());
            data.setCompanyName(leadRequest.getFullName());
        }
        if (StringUtils.isNotEmpty(leadRequest.getCompanyName())) {
            data.setFullName(leadRequest.getCompanyName());
            data.setCompanyName(leadRequest.getCompanyName());
        }
        data.setSalutation(leadRequest.getSalutation());
        data.setStatus(LeadStatus.NEW.getType());
        data.setLeadSource(LeadSource.valueOf(leadRequest.getLeadSource()).name());
        data.setEmail(leadRequest.getEmail());
        data.setPhone(CommonUtils.convertPhone(leadRequest.getPhone()));
        data.setType(LeadType.TU_NHAP.getType());
        // Neu duoc tao tu EVTP -> isFromEVTP = 1
        data.setIsFromEVTP(1L);
        data.setDescription(leadRequest.getDescription());
        data.setQuantityMonth(leadRequest.getQuantityMonth());
        data.setWeight(leadRequest.getWeight());
        data.setQuality(leadRequest.getQuality());
        data.setCompensation(leadRequest.getCompensation());
        data.setPayment(leadRequest.getPayment());
        data.setOther(leadRequest.getOther());
        data.setExpectedRevenue(leadRequest.getExpectedRevenue());
        data.setRepresentation(leadRequest.getRepresentation());
        data.setStatus(LeadStatus.NEW.getType());
        Address address = AddressRequest.addressDtoToModel(leadRequest.getAddress());
        data.setAddress(address);

        if (CollectionUtils.isNotEmpty(leadRequest.getIndustry())) {
            List<Industry> industries = industryRepository.findIndustriesByCodeIn(leadRequest.getIndustry());
            if (CollectionUtils.isNotEmpty(industries)) {
                data.setIndustries(industries);
            }
        }
        Lead lead = iLeadRepository.save(data);
        lead.setCustomerCode("KH".concat(String.valueOf(lead.getId())));
        return lead;
    }

    @Override
//    @Transactional
    public Lead updateLead(Long id, LeadUpdateRequest leadRequest) throws ApplicationException {
        PagingRs pagingRs = new PagingRs();
        Lead existData = iLeadRepository.findLeadById(id);
        try {
            if (existData == null) {
                throw new HieuDzException("Khách hàng không tồn tại");
            }
            if (StringUtils.isNotBlank(leadRequest.getPhone())) {
                if (!existData.getPhone().equals(leadRequest.getPhone())) {
                    List<Schedule> schedules = scheduleRepository.getSchedulesByLeadId(id);
                    if (CollectionUtils.isNotEmpty(schedules)) {
                        throw new HieuDzException("LeadService dòng 116");
                    } else {
                        if (existData.getPhone().equals(CommonUtils.convertPhone(leadRequest.getPhone()))) {
                            existData.setPhone(CommonUtils.convertPhone(leadRequest.getPhone()));
                        } else {
                            if (CollectionUtils.isNotEmpty(iLeadRepository.findLeadWithPhoneOnEVTP(CommonUtils.convertPhone(leadRequest.getPhone()))))
                                throw new HieuDzException("LeadService dong 122");
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
        } catch (Exception e) {
            e.getLocalizedMessage();
            throw e;
        }
        return iLeadRepository.save(existData);
    }

    @Override
    public Lead deleteLeadOnEVTP(Long leadId) throws ApplicationException {
        Lead existData = iLeadRepository.findLeadById(leadId);

        if (existData == null) {
            throw new HieuDzException("dòng 162");
        }
        List<Schedule> schedules = scheduleRepository.getSchedulesByLeadId(leadId);
        if (CollectionUtils.isNotEmpty(schedules)) {
            throw new HieuDzException("dòng 166");
        }
        existData.setDeletedStatus(Constants.DELETE_LEAD);
        Lead lead = iLeadRepository.save(existData);
        return lead;
    }

    @Override
    public LeadResponse detailLead(Long id) throws ApplicationException {
        LeadResponse leadResponse = new LeadResponse();
        Lead existData = iLeadRepository.findLeadById(id);
        BeanUtils.copyProperties(existData, leadResponse);
        return leadResponse;
    }

}

