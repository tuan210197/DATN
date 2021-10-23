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
import shupship.common.Constants;
import shupship.domain.message.MessageResponse;
import shupship.domain.model.Lead;
import shupship.enums.LeadSource;
import shupship.request.LeadRequest;
import shupship.request.LeadUpdateRequest;
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

    @PostMapping(value = "/createEvtp")
    public ResponseEntity createLeadOnEVTP(HttpServletRequest request, @Valid @RequestBody LeadRequest inputData) throws Exception {
        Lead data = leadService.insertLead(inputData);
        BeanUtils.copyProperties(inputData, data);
        LeadResponse response = LeadResponse.leadModelToDto(data);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PutMapping(value = "evtp/{leadId}")
    public ResponseEntity<LeadResponse> updateLeadOnEVTP(@RequestBody LeadUpdateRequest inputData, @PathVariable(value = "leadId") Long leadId) throws ApplicationException {
        //validateLeadSource
//        if (!validateIndustry(inputData.getLeadSource())) {
//            throw new BusinessException(new ErrorMessage("ERR_002", "Industry code is not defined"));
//        }

        Lead data = leadService.updateLead(leadId, inputData);
        LeadResponse response = LeadResponse.leadModelToDto(data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping(value = "/evtp/{leadId}")
    public ResponseEntity deleteLeadOnEVTP(@PathVariable(value = "leadId") Long leadId) throws Exception {
        Lead data = leadService.deleteLeadOnEVTP(leadId);
        return new ResponseEntity(new MessageResponse((true)), HttpStatus.OK);
    }

    @GetMapping(value = "/evtp/{leadId}")
    public ResponseEntity detailLead(@PathVariable(value = "leadId") Long leadId) throws Exception {
        LeadResponse data = leadService.detailLead(leadId);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
