package shupship.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shupship.cronjob.JobDistrict;
import shupship.cronjob.JobProvince;
import shupship.cronjob.JobWard;
import shupship.service.MailSenderService;

/**
 * @author tuandv
 */
@RestController
public class TestController extends BaseController {

	@Autowired
	private MailSenderService mailSenderService;

	@Autowired
	JobProvince jobProvince;

	@Autowired
	JobDistrict jobDistrict;

	@Autowired
	JobWard jobWard;

	@RequestMapping({ "/hello" })
	public String firstPage() {
		return "hello";
	}

	@RequestMapping("/sendEmail")
	public void sendEmail() {
		mailSenderService.sendSimpleMessage("skyfortrucker@gmail.com", "test", "test");
	}

	@GetMapping(value = "/sync")
	public ResponseEntity syncProDisWarPost(){
		jobProvince.syncProvince();
		jobDistrict.syncDistrict();
		jobWard.syncWard();
		return new ResponseEntity("OK", HttpStatus.OK);
	}

}