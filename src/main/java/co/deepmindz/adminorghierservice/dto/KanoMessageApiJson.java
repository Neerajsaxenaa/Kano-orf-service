package co.deepmindz.adminorghierservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KanoMessageApiJson {

	public String api_key;
	public String to;
	public String from;
	public String sms;
	public String type;
	public String channel;

}
