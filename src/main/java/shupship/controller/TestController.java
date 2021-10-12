package shupship.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shupship.service.MailSenderService;

/**
 * @author tuandv
 */
@RestController
public class TestController extends BaseController {

	@Autowired
	private MailSenderService mailSenderService;

	@RequestMapping({ "/hello" })
	public String firstPage() {
		return "hello";
	}

	@RequestMapping("/sendEmail")
	public void sendEmail() {
		mailSenderService.sendSimpleMessage("skyfortrucker@gmail.com", "test", "test");
	}

}