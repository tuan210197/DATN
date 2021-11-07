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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shupship.common.Constants;
import shupship.domain.model.*;
import shupship.dto.LeadHadPhoneResponseDto;
import shupship.dto.LeadResponseWithDescriptionDto;
import shupship.dto.ScheduleResponseLeadDto;
import shupship.enums.LeadSource;
import shupship.enums.LeadStatus;
import shupship.enums.LeadType;
import shupship.repo.*;
import shupship.request.AddressRequest;
import shupship.request.LeadAssignRequest;
import shupship.request.LeadRequest;
import shupship.request.LeadUpdateRequest;
import shupship.response.LeadResponse;
import shupship.response.PagingRs;
import shupship.service.ILeadAssignService;
import shupship.service.ILeadService;
import shupship.util.CommonUtils;
import shupship.util.exception.ApplicationException;
import shupship.util.exception.HieuDzException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;


@Log4j2
@Service
public class LeadServiceImpl implements ILeadService {
    @Autowired
    ILeadRepository iLeadRepository;

    @Autowired
    ILeadAssignRepository iLeadAssignRepository;
    @Autowired
    IndustryDetailRepository industryDetailRepository;

    @Autowired
    IScheduleRepository scheduleRepository;

    @Autowired
    IIndustryRepository industryRepository;

    @Autowired
    UserRepo userRepo;

    @Autowired
    BasicLoginRepo basicLoginRepo;

    @Autowired
    ILeadAssignService leadAssignService;

    @Override
    public PagingRs getListLead(Pageable pageable, Timestamp from, Timestamp to, Long status, Users users) throws ApplicationContextException {

        Instant startDate = from.toInstant();
        Instant endDate = to.toInstant();

        Page<Lead> leadList = null;

        if (users.getRoles().equals("TCT")) {
            leadList = iLeadRepository.findAllLeadbyCriteria(status, null, startDate, endDate, null, pageable);
        } else if (users.getRoles().equals("CN")) {
            leadList = iLeadRepository.findAllLeadbyCriteriaByCNBC(status, null, startDate, endDate, users.getEmpSystemId(), users.getDeptCode(), pageable);
        } else if (users.getRoles().equals("BC")) {
            leadList = iLeadRepository.findAllLeadbyCriteriaByCNBC(status, null, startDate, endDate, users.getEmpSystemId(), users.getPostCode(), pageable);
        }

        Page<LeadResponse> leadResponses = leadList.map(LeadResponse::leadModelToDto);
        PagingRs listLeadResponse = new PagingRs();
        listLeadResponse.setTotalItem(leadList.getTotalElements());
        listLeadResponse.setData(leadResponses.getContent());
        return listLeadResponse;
    }

    @Override
    public Lead insertLead(LeadRequest leadRequest, Users users) throws ApplicationContextException {
        Lead data = new Lead();
        if (StringUtils.isNotEmpty(leadRequest.getTitle())) {
            data.setTitle(leadRequest.getTitle());
        } else throw new HieuDzException("Không được để trống tille");

//        if (StringUtils.isNotEmpty(leadRequest.getFullName())) {
//            data.setFullName(leadRequest.getFullName());
//            data.setCompanyName(leadRequest.getFullName());
//        } else throw new HieuDzException("Không được để trống tên");

        if (StringUtils.isNotEmpty(leadRequest.getCompanyName())) {
            data.setFullName(leadRequest.getCompanyName());
            data.setCompanyName(leadRequest.getCompanyName());
        } else throw new HieuDzException("Không được để trống tên công ty");

//        data.setSalutation(leadRequest.getSalutation());
        data.setStatus(LeadStatus.NEW.getType());
        if (StringUtils.isNotEmpty(LeadSource.valueOf(leadRequest.getLeadSource()).name())) {
            data.setLeadSource(LeadSource.valueOf(leadRequest.getLeadSource()).name());
        } else throw new HieuDzException("Chưa chọn phân loại khách hàng");

        if (StringUtils.isNotEmpty(leadRequest.getPhone())) {
            data.setPhone(CommonUtils.convertPhone(leadRequest.getPhone()));
        } else throw new HieuDzException("Không được để trống số điện thoại");

        if (StringUtils.isEmpty(leadRequest.getPhone())) {
            throw new HieuDzException("Không được để trống số điện thoại");
        }
        if (CollectionUtils.isNotEmpty(iLeadRepository.findLeadWithPhoneOnEVTP(CommonUtils.convertPhone(leadRequest.getPhone()))))
            throw new HieuDzException("Số điện thoại đã tồn tại trên hệ thống!");
        else data.setPhone(CommonUtils.convertPhone(leadRequest.getPhone()));

        data.setType(LeadType.TU_NHAP.getType());
        data.setIsFromEVTP(1L);
        data.setRepresentation(leadRequest.getRepresentation());

        data.setStatus(LeadStatus.NEW.getType());
        Address address = AddressRequest.addressDtoToModel(leadRequest.getAddress());
        data.setAddress(address);

        if (CollectionUtils.isNotEmpty(leadRequest.getIndustry())) {
            List<Industry> industries = industryRepository.findIndustriesByCodeIn(leadRequest.getIndustry());
            if (CollectionUtils.isNotEmpty(industries)) {
                data.setIndustries(industries);
            }
        } else {
            throw new HieuDzException("Lỗi bỏ trống sp");
        }
        Lead lead = iLeadRepository.save(data);
        lead.setCustomerCode("KH".concat(String.valueOf(lead.getId())));
        BeanUtils.copyProperties(data, lead);
        return lead;
    }

    @Override
    @Transactional
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
                        throw new HieuDzException("Chỉ cho phép sửa SĐT của khách hàng khi khách hàng chưa có lịch tiếp xúc và chưa có kết quả tiếp xúc");
                    } else {
                        if (existData.getPhone().equals(CommonUtils.convertPhone(leadRequest.getPhone()))) {
                            existData.setPhone(CommonUtils.convertPhone(leadRequest.getPhone()));
                        } else {
                            if (CollectionUtils.isNotEmpty(iLeadRepository.findLeadWithPhoneOnEVTP(CommonUtils.convertPhone(leadRequest.getPhone()))))
                                throw new HieuDzException("Số điện thoại đã tồn tại trên hệ thống!");
                            else existData.setPhone(CommonUtils.convertPhone(leadRequest.getPhone()));
                        }
                    }
                } else existData.setPhone(CommonUtils.convertPhone(leadRequest.getPhone()));
            }

//            if (StringUtils.isNotEmpty(leadRequest.getFullName())) {
//                existData.setFullName(leadRequest.getFullName());
//                existData.setCompanyName(leadRequest.getFullName());
//            } else throw new HieuDzException("Không được để trống tên");

            if (StringUtils.isNotEmpty(leadRequest.getCompanyName())) {
                existData.setFullName(leadRequest.getCompanyName());
                existData.setCompanyName(leadRequest.getCompanyName());
            } else throw new HieuDzException("Không được để trống tên công ty");
            existData.setRepresentation(leadRequest.getRepresentation());

            if (StringUtils.isNotEmpty(leadRequest.getTitle())) {
                existData.setTitle(leadRequest.getTitle());
            } else throw new HieuDzException("Không được để trống title");
            existData.setLeadSource(leadRequest.getLeadSource());

            if (leadRequest.getAddress() != null) {
                Address address = AddressRequest.addressDtoToModel(leadRequest.getAddress());
                existData.setAddress(address);
            }

            if (CollectionUtils.isNotEmpty(leadRequest.getIndustry())) {
                List<Industry> industries = industryRepository.findIndustriesByCodeIn(leadRequest.getIndustry());
                if (CollectionUtils.isNotEmpty(industries)) {
                    existData.setIndustries(industries);
                }
            } else {
                throw new HieuDzException("Chưa chọn sản phẩm kinh doanh");
            }
        } catch (Exception e) {
            e.getLocalizedMessage();
            throw e;
        }
        return iLeadRepository.save(existData);
    }

    @Override
    public Lead deleteLeadOnWEB(Long leadId) throws ApplicationException {
        Lead existData = iLeadRepository.findLeadById(leadId);

        if (existData == null) {
            throw new HieuDzException("Khách hàng không tồn tại");
        }
        List<Schedule> schedules = scheduleRepository.getSchedulesByLeadId(leadId);
        if (CollectionUtils.isNotEmpty(schedules)) {
            throw new HieuDzException("Chỉ được xóa khách hàng khi không có lịch tiếp xúc và chưa cập nhật kết quả");
        }
        existData.setDeletedStatus(Constants.DELETE_LEAD);
        Lead lead = iLeadRepository.save(existData);
        return lead;
    }

    @Override
    public LeadResponse detailLead(Long leadId) throws ApplicationException {
        Lead existData = iLeadRepository.findLeadById(leadId);
        if (existData == null) {
            throw new HieuDzException("Khách hàng không tồn tại");
        }
        LeadResponse leadResponse = LeadResponse.leadModelToDto(existData);
        return leadResponse;
    }

    @Override
    public Lead createLeadWMO(LeadRequest inputData, Users users) throws Exception {

        Lead data = new Lead();

        if (StringUtils.isNotEmpty(inputData.getCompanyName())) {
            data.setFullName(inputData.getCompanyName());
            data.setCompanyName(inputData.getCompanyName());
        }
        data.setStatus(LeadStatus.NOT_CONTACTED.getType());
        data.setLeadSource(LeadSource.valueOf(inputData.getLeadSource()).name());
        data.setPhone(CommonUtils.convertPhone(inputData.getPhone()));
        data.setType(LeadType.TU_NHAP.getType());
        data.setRepresentation(inputData.getRepresentation());

        Address address = AddressRequest.addressDtoToModel(inputData.getAddress());
        data.setAddress(address);

        if (CollectionUtils.isNotEmpty(inputData.getIndustry())) {
            List<Industry> industries = industryRepository.findIndustriesByCodeIn(inputData.getIndustry());
            if (CollectionUtils.isNotEmpty(industries)) {
                data.setIndustries(industries);
            }
        } else {
            throw new HieuDzException("Lỗi bỏ trống sp");
        }
        data.setCreatedBy(users.getEmpSystemId());
        Lead lead = iLeadRepository.save(data);
        lead.setCustomerCode("KH".concat(String.valueOf(lead.getId())));

        LeadAssignRequest leadAssignRequest = new LeadAssignRequest();
        leadAssignRequest.setLeadId(lead.getId());
        leadAssignRequest.setUserAssigneeId(users.getEmpSystemId());
        leadAssignRequest.setUserRecipientId(users.getEmpSystemId());
        leadAssignRequest.setDeptCode(users.getDeptCode());
        leadAssignRequest.setPostCode(users.getPostCode());
        leadAssignRequest.setStatus(5L);
        leadAssignService.createLeadAssign(users, leadAssignRequest);
        return lead;
    }

    @Override
    public Lead deleteLeadWMO(Long leadId) throws Exception {
        Users user = getCurrentUser();
        Lead existData = iLeadRepository.findLeadById(leadId);

        if (existData == null) {
            throw new HieuDzException("Khách hàng không tồn tại");
        }
        List<Schedule> schedules = scheduleRepository.getSchedulesByLeadId(leadId);
        if (CollectionUtils.isNotEmpty(schedules)) {
            throw new HieuDzException("Chỉ được xóa khách hàng khi không có lịch tiếp xúc và chưa cập nhật kết quả.");
        }
        existData.setDeletedStatus(1L);
        Lead lead = iLeadRepository.save(existData);

        lead.getIndustries().forEach(e ->
                industryDetailRepository.deleteByRelatedToIdAndIndustryId(existData.getId(), e.getId()));

        lead.getSchedules().forEach(e ->
                scheduleRepository.deleteSchedule(e.getId(), user.getEmpSystemId()));

        lead.getLeadAssigns().forEach(e ->
                iLeadAssignRepository.deleteAssign(existData.getId()));

//        lead.getLeadAssigns().forEach(e ->
//                leadAssignExcelRepository.deleteLeadAssignExcel(e.getId()));
        return lead;
    }

    @Override
    public LeadResponseWithDescriptionDto findLeadDetail(Long id) throws Exception {
        Users user = getCurrentUser();
        Lead lead = iLeadRepository.findLeadById(id);
        if (lead == null) {
            throw new HieuDzException("Khách hàng không tồn tại");
        }
        LeadResponseWithDescriptionDto leadResponseWithDescriptionDto = LeadResponseWithDescriptionDto.leadModelToDto(lead);
        List<ScheduleResponseLeadDto> scheduleResponseLeadDto = leadResponseWithDescriptionDto.getSchedules();

        if (scheduleResponseLeadDto != null) {
            leadResponseWithDescriptionDto.setSchedules(scheduleResponseLeadDto);
        }
        return leadResponseWithDescriptionDto;
    }

    @Override
    public LeadHadPhoneResponseDto findLeadHadPhoneByUser(LeadRequest inputData, Long userId) {
        List<Lead> leadWithPhone = iLeadRepository.findLeadWithPhoneOnEVTP(CommonUtils.convertPhone(inputData.getPhone()));
        if (CollectionUtils.isEmpty(leadWithPhone)) {
            return null;
        }
        return LeadHadPhoneResponseDto.leadModelToDto(leadWithPhone.get(0), null);
    }

    @Override
    public HashMap getCustomerByPhone(String phone) {
        HashMap customer = new HashMap();
        return customer;
    }

    protected Users getCurrentUser() throws Exception {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername();
        BasicLogin basicLogin = basicLoginRepo.findByEmail(email);
        Users users = userRepo.findByUid(basicLogin.getUserUid());
        if (users == null) {
            throw new ApplicationException("Users is null");
        }
        return users;
    }
}

