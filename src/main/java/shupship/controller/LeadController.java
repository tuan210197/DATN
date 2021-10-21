package shupship.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shupship.common.Const;
import shupship.common.Constants;
import shupship.domain.model.Lead;
import shupship.enums.LeadSource;
import shupship.request.LeadRequest;
import shupship.response.LeadResponse;
import shupship.response.PagingRs;
import shupship.service.ILeadService;
import shupship.util.exception.ApplicationException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/lead")
public class LeadController extends BaseController {
    @Autowired
    ILeadService leadService;

    public static boolean validateIndustry(String ls) throws Exception {
        for (LeadSource leadSource : LeadSource.values())
            if (leadSource.name().equals(ls))
                return true;
        return false;
    }

    @GetMapping(value = "/filter")
    public ResponseEntity getListLead(@PageableDefault(page = 1)
                                      @SortDefault.SortDefaults({@SortDefault(sort = "lastModifiedDate", direction = Sort.Direction.DESC)}) Pageable pageable) {
        Pageable pageablerequest = PageRequest.of(pageable.getPageNumber() - 1, Constants.PAGE_SIZE, pageable.getSort());
        PagingRs pagingRs = leadService.getListLead(pageablerequest);
        return new ResponseEntity<>(pagingRs, HttpStatus.OK);
    }

    @PostMapping(value = "/evtp")
    public ResponseEntity createLeadOnEVTP(HttpServletRequest request, @Valid @RequestBody LeadRequest inputData) throws Exception {
        ///Users user = getCurrentUser(request, inputData);
        //validateLeadSource
//        if (!validateIndustry(inputData.getLeadSource())) {
//            throw new ApplicationException(Constants.ERR_002);
//        }

        Lead data = leadService.insertLead(inputData);
        BeanUtils.copyProperties(inputData, data);
        LeadResponse response = LeadResponse.leadModelToDto(data);
//        HashMap phoneEvtp = getPhoneEVTP(inputData.getPhone());
//
//        if (phoneEvtp != null && phoneEvtp.size() != 0) {
//            LeadHadPhoneResponseDto responseDto = new LeadHadPhoneResponseDto();
//            responseDto.setErrorCode(HAD_PHONE_EVTP);
//            responseDto.setMessage("Khách hàng đã sử dụng dịch vụ của Viettel Post.");
//            responseDto.setCustomerCode((String) phoneEvtp.get("MA_KH"));
//            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
//        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
