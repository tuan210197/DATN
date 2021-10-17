package shupship.controller;

import org.hibernate.collection.internal.PersistentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shupship.domain.dto.response.lead.PagingRs;
import shupship.domain.model.Lead;
import shupship.service.ILeadService;
import shupship.util.exception.ApplicationException;

import java.util.ArrayList;
import java.util.List;

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
