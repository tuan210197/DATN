
package shupship.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import shupship.service.ILeadService;
import shupship.util.exception.ApplicationException;

@RestController
@RequestMapping(value = "api/lead")
public class LeadController extends BaseController {
    @Autowired
    ILeadService leadService;

    @PostMapping(value = "")
    @ResponseBody
    public ResponseEntity<?> getListLead() throws ApplicationException {

        return null;
    }
}

