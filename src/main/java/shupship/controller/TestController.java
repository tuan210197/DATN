package shupship.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

//	@Autowired
//	TwilioSmsSender twilioSmsSender;

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


//	@PostMapping(value = "/send-sms")
//	public void sendSms(@Valid @RequestBody SmsRequest smsRequest) {
//		twilioSmsSender.sendSms(smsRequest);
//	}

}