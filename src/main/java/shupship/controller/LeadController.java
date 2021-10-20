package shupship.controller;

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
import shupship.request.LeadRequest;
import shupship.response.PagingRs;
import shupship.service.ILeadService;
import shupship.util.exception.ApplicationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/lead")
public class LeadController extends BaseController {
    @Autowired
    ILeadService leadService;

    @GetMapping(value = "/filter")
    public ResponseEntity getListLead(@PageableDefault(page = 1)
                                          @SortDefault.SortDefaults({@SortDefault(sort = "lastModifiedDate", direction = Sort.Direction.DESC)}) Pageable pageable) {
        Pageable pageablerequest = PageRequest.of(pageable.getPageNumber() - 1,Constants.PAGE_SIZE, pageable.getSort());
        PagingRs pagingRs = leadService.getListLead(pageablerequest);
        return new ResponseEntity<>(pagingRs,HttpStatus.OK);
    }
}
