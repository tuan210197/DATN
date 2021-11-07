package shupship.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shupship.domain.model.LeadAssign;
import shupship.domain.model.Users;
import shupship.request.LeadAssignRequestV2;
import shupship.response.LeadAssignResponse;
import shupship.service.ILeadAssignService;
import shupship.service.ILeadService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lead-assigns")
public class LeadAssignController extends BaseController {

    @Autowired
    private ILeadAssignService leadAssignService;

    @PostMapping
    public ResponseEntity createLeadAssign(HttpServletRequest request, @Valid @RequestBody LeadAssignRequestV2 inputData) {
        Users users = getCurrentUser();
        List<LeadAssign> leadAssignList = leadAssignService.createLeadAssignV2(users, inputData);
        List<LeadAssignResponse> response = leadAssignList.stream().map(LeadAssignResponse::leadAssignModelToDto).collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
