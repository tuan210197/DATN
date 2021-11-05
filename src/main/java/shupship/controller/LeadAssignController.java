package shupship.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shupship.request.LeadAssignRequestV2;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/lead-assigns")
public class LeadAssignController {

    public ResponseEntity createLeadAssign(HttpServletRequest request, @Valid @RequestBody LeadAssignRequestV2 inputData){


        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

}
