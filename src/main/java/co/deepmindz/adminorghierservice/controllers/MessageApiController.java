package co.deepmindz.adminorghierservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import co.deepmindz.adminorghierservice.dto.KanoMessageApiJson;
import co.deepmindz.adminorghierservice.dto.VasudevMessageApiJson;

@RestController
@RequestMapping("/organization/message-api")
public class MessageApiController {

	@Autowired
	RestTemplate restTemplate;

	@PostMapping("/vasudev-api")
	public Object messageApiVasudev(@RequestBody VasudevMessageApiJson message) {
		try {
			String smsApi = "https://portal.vasudevsms.in/api/mt/SendSMS";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<VasudevMessageApiJson> entity = new HttpEntity<VasudevMessageApiJson>(message, headers);
			ResponseEntity<Object> exchange = restTemplate.exchange(smsApi, HttpMethod.POST, entity, Object.class);
			return exchange;
		} catch (Exception e) {
			System.out.println("smsApi Api not working");
			e.printStackTrace();
			return " ";
		}

	}

	@PostMapping("/kano-api")
	public Object messageApiKanoTermi(@RequestBody KanoMessageApiJson message) {
		try {
			String smsApi = "https://api.ng.termii.com/api/sms/send";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<KanoMessageApiJson> entity = new HttpEntity<KanoMessageApiJson>(message,headers);
			ResponseEntity<Object> exchange = restTemplate.exchange(smsApi, HttpMethod.POST, entity, Object.class);
			return exchange.getBody();
		} catch (Exception e) {
			System.out.println("smsApi Api not working");
			e.printStackTrace();
			return " ";
		}

	}

}
