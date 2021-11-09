package shupship.controller;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shupship.common.Constants;
import shupship.domain.message.MessageResponse;
import shupship.domain.model.Lead;
import shupship.domain.model.Users;
import shupship.dto.LeadHadPhoneResponseDto;
import shupship.dto.LeadResponseWithDescriptionDto;
import shupship.enums.LeadSource;
import shupship.enums.LeadStatus;
import shupship.helper.ResponseUtil;
import shupship.request.LeadRequest;
import shupship.request.LeadUpdateRequest;
import shupship.response.LeadResponse;
import shupship.response.PagingRs;
import shupship.service.ILeadService;
import shupship.util.CommonUtils;
import shupship.util.DateTimeUtils;
import shupship.util.exception.ApplicationException;
import shupship.util.exception.HieuDzException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import static java.util.Comparator.naturalOrder;

@RestController
@RequestMapping(value = "/api/lead")
public class LeadController extends BaseController {
    @Autowired
    ILeadService leadService;

    @GetMapping(value = "/filter")
    public ResponseEntity getListLead(@PageableDefault(page = 1)
                                      @SortDefault.SortDefaults({@SortDefault(sort = "lastModifiedDate", direction = Sort.Direction.DESC)}) Pageable pageable,
                                      @RequestParam(required = false) String status,
                                      @RequestParam(required = false) String from, @RequestParam(required = false) String to) throws Exception {
        Users users = getCurrentUser();
        Pageable pageablerequest = PageRequest.of(pageable.getPageNumber() - 1, Constants.PAGE_SIZE, pageable.getSort());
        Long leadStatus = null;
        if (StringUtils.isNotEmpty(status)){
            leadStatus = LeadStatus.valueOf(status).getType();
        }
        LocalDateTime startDate;
        LocalDateTime endDate;

        Date startDate1 = null;
        Date endDate1 = null;

        Timestamp startTimestamp = Timestamp.valueOf(LocalDateTime.now().withDayOfMonth(1).toLocalDate().atStartOfDay());
        Timestamp endTimestamp = Timestamp.valueOf(LocalDateTime.now());

        if (StringUtils.isNotBlank(from) || StringUtils.isNotBlank(to)) {
            if ((StringUtils.isBlank(from) && !DateTimeUtils.isValidDate(from)) || (StringUtils.isBlank(to) && !DateTimeUtils.isValidDate(to)))
                throw new HieuDzException("Ngày nhập vào không đúng định dạng!");

            startDate = DateTimeUtils.StringToLocalDate(from).atStartOfDay();
            endDate = DateTimeUtils.StringToLocalDate(to).plusDays(1).atStartOfDay();

            if (startDate.isAfter(endDate))
                throw new HieuDzException("Ngày bắt đầu phải nhỏ hơn ngày kết thúc!");

            //check quá 31 ngày
            startDate1 = DateTimeUtils.StringToDate(from);
            endDate1 = DateTimeUtils.StringToDate(to);

            long diff = endDate1.getTime() - startDate1.getTime();
            long getDaysDiff = diff / (24 * 60 * 60 * 1000);

            if (getDaysDiff < 0 || getDaysDiff >= 31)
                throw new HieuDzException("Không được tìm kiếm quá 31 ngày");

            startTimestamp = Timestamp.valueOf(startDate);
            endTimestamp = Timestamp.valueOf(endDate);

        }
        PagingRs pagingRs = leadService.getListLead(pageablerequest, startTimestamp, endTimestamp, leadStatus, users);
        return new ResponseEntity<>(pagingRs, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity getListLeadOnEmp(@PageableDefault(page = 1)
                                      @SortDefault.SortDefaults({@SortDefault(sort = "lastModifiedDate", direction = Sort.Direction.DESC)}) Pageable pageable,
                                      @RequestParam(required = false) String status, @RequestParam(required = false) String key,
                                      @RequestParam(required = false) String from, @RequestParam(required = false) String to) throws Exception {
        Users users = getCurrentUser();
        Pageable pageablerequest = PageRequest.of(pageable.getPageNumber() - 1, Constants.PAGE_SIZE, pageable.getSort());
        Long leadStatus = null;
        if (StringUtils.isNotEmpty(status)){
            leadStatus = LeadStatus.valueOf(status).getType();
        }
        LocalDateTime startDate;
        LocalDateTime endDate;

        Date startDate1 = null;
        Date endDate1 = null;

        Timestamp startTimestamp = Timestamp.valueOf(LocalDateTime.now().withDayOfMonth(1).toLocalDate().atStartOfDay());
        Timestamp endTimestamp = Timestamp.valueOf(LocalDateTime.now());

        if (StringUtils.isNotBlank(from) || StringUtils.isNotBlank(to)) {
            if ((StringUtils.isBlank(from) && !DateTimeUtils.isValidDate(from)) || (StringUtils.isBlank(to) && !DateTimeUtils.isValidDate(to)))
                throw new HieuDzException("Ngày nhập vào không đúng định dạng!");

            startDate = DateTimeUtils.StringToLocalDate(from).atStartOfDay();
            endDate = DateTimeUtils.StringToLocalDate(to).plusDays(1).atStartOfDay();

            if (startDate.isAfter(endDate))
                throw new HieuDzException("Ngày bắt đầu phải nhỏ hơn ngày kết thúc!");

            //check quá 31 ngày
            startDate1 = DateTimeUtils.StringToDate(from);
            endDate1 = DateTimeUtils.StringToDate(to);

            long diff = endDate1.getTime() - startDate1.getTime();
            long getDaysDiff = diff / (24 * 60 * 60 * 1000);

            if (getDaysDiff < 0 || getDaysDiff >= 31)
                throw new HieuDzException("Không được tìm kiếm quá 31 ngày");

            startTimestamp = Timestamp.valueOf(startDate);
            endTimestamp = Timestamp.valueOf(endDate);

        }
        PagingRs pagingRs = leadService.getListLeadOnEmp(pageablerequest, startTimestamp, endTimestamp, leadStatus, users, key);
        return new ResponseEntity<>(pagingRs, HttpStatus.OK);
    }

    @PostMapping(value = "/createWeb")
    public ResponseEntity createLeadOnWEB(HttpServletRequest request, @Valid @RequestBody LeadRequest inputData) throws Exception {
        Users users = getCurrentUser();
        Lead data = leadService.insertLead(inputData, users);
        LeadResponse response = LeadResponse.leadModelToDto(data);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "evtp/{leadId}")
    public ResponseEntity<LeadResponse> updateLeadOnWEB(@RequestBody LeadUpdateRequest inputData, @PathVariable(value = "leadId") Long leadId)
            throws ApplicationException {
        //validateLeadSource
//        if (!validateIndustry(inputData.getLeadSource())) {
//            throw new BusinessException(new ErrorMessage("ERR_002", "Industry code is not defined"));
//        }

        Lead data = leadService.updateLead(leadId, inputData);
        LeadResponse response = LeadResponse.leadModelToDto(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping(value = "/evtp/{leadId}")
    public ResponseEntity deleteLeadOnWEB(@PathVariable(value = "leadId") Long leadId) throws Exception {
        Lead data = leadService.deleteLeadOnWEB(leadId);
        return new ResponseEntity(new MessageResponse((true)), HttpStatus.OK);
    }

    @GetMapping(value = "/evtp/{leadId}")
    public ResponseEntity detailLead(@PathVariable(value = "leadId") Long leadId) throws Exception {
        LeadResponse data = leadService.detailLead(leadId);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    public static final String HAD_PHONE_CRM = "ERR_301";
    public static final String HAD_PHONE_EVTP = "ERR_303";
    @PostMapping
    public ResponseEntity createLead(HttpServletRequest request, @Valid @RequestBody LeadRequest inputData) throws Exception {
        Users users = getCurrentUser();
        //validateLeadSource
        if (!validateIndustry(inputData.getLeadSource())) {
            throw new HieuDzException ("Industry code is not defined");
        }
        if (StringUtils.isNotBlank(inputData.getPhone())) {
            LeadHadPhoneResponseDto responseHadPhoneUser = leadService.findLeadHadPhoneByUser(inputData, users.getEmpSystemId());
//          LeadHadPhoneResponseDto responseHadPhone = leadService.findLeadHadPhone(inputData, user.getId());
            if (responseHadPhoneUser != null) {
                responseHadPhoneUser.setErrorCode(HAD_PHONE_CRM);
                responseHadPhoneUser.setMessage("Khách hàng đã được thêm vào hệ thống.");
                return new ResponseEntity<>(responseHadPhoneUser, HttpStatus.BAD_REQUEST);
            }

        }
        Lead data = leadService.createLeadWMO(inputData, users);
        LeadResponse response = LeadResponse.leadModelToDto(data);
//        HashMap phoneEvtp = getPhoneEVTP(inputData.getPhone());
//
//        if (phoneEvtp != null && phoneEvtp.size() != 0) {
//            LeadHadPhoneResponseDto responseDto = new LeadHadPhoneResponseDto();
//            responseDto.setErrorCode(HAD_PHONE_EVTP);
//            responseDto.setMessage("Khách hàng đã sử dụng dịch vụ của ShupShip.");
//            responseDto.setCustomerCode((String) phoneEvtp.get("MA_KH"));
//            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
//        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @DeleteMapping(value = "/{leadId}")
    public ResponseEntity deleteLead(@PathVariable(value = "leadId") Long leadId) throws Exception {
        Lead data = leadService.deleteLeadWMO(leadId);
        return new ResponseEntity(new MessageResponse((true)), HttpStatus.OK);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity findLeadById(@PathVariable Long id) throws Exception {
        LeadResponseWithDescriptionDto data = leadService.findLeadDetail(id);
        if (CollectionUtils.isNotEmpty(data.getSchedules())) {
            data.getSchedules().sort(Comparator.nullsLast(naturalOrder()));
        }
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(data));
    }

    private HashMap getPhoneEVTP(String phone) {
        if (StringUtils.isNotBlank(phone)) {
            HashMap resp = leadService.getCustomerByPhone(CommonUtils.convertPhone(phone));
            return resp;
        }
        return null;
    }

    public static boolean validateIndustry(String ls) throws Exception {
        for (LeadSource leadSource : LeadSource.values())
            if (leadSource.name().equals(ls))
                return true;
        return false;
    }
}
