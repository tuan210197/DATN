package shupship.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shupship.domain.dto.response.lead.PagingRs;
import shupship.service.ILeadService;
import shupship.util.exception.ApplicationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/lead")
public class LeadController extends BaseController {
    @Autowired
    ILeadService leadService;

    @GetMapping(value = "")
    public ResponseEntity getListLead() throws ApplicationException {
        PagingRs pagingRs = leadService.getListLead();
        return new ResponseEntity<>(pagingRs,HttpStatus.OK);
    }
}
