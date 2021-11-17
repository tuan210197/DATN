package shupship.controller;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shupship.domain.model.LeadAssign;
import shupship.domain.model.Users;
import shupship.request.LeadAssignRequestV2;
import shupship.response.LeadAssignHisResponse;
import shupship.response.LeadAssignResponse;
import shupship.response.RestResponse;
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

    @PostMapping(value = "/import")
    public RestResponse importLeadAssign(HttpServletRequest request, @RequestParam("file") MultipartFile reapExcelDataFile) throws Exception {

        RestResponse rest = new RestResponse();
        String message = leadAssignService.checkFileInput(reapExcelDataFile);
        if (StringUtils.isEmpty(message)) {
            Users user = getCurrentUser();
            LeadAssignHisResponse listRes = leadAssignService.importFileLeadAssign(user, reapExcelDataFile);
            rest.setData(listRes);
            rest.setStatus(listRes != null ? "Thành công ❤❤❤" : "Thất bại ❤❤❤");
        } else {
            rest.setData(message);
            rest.setStatus("Thất bại");
        }
        return rest;
    }

}
