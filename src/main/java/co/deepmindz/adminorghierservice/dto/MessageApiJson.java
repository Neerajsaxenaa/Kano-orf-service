package co.deepmindz.adminorghierservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MessageApiJson {

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Account {

		public String apiKey;
		public String senderId;
		public int channel;
		public int dcs;
		public int route;

	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Messages {
		public String number;
		public String text;

	}

}
