package co.deepmindz.adminorghierservice.dto;

import java.util.List;

import co.deepmindz.adminorghierservice.dto.MessageApiJson.Account;
import co.deepmindz.adminorghierservice.dto.MessageApiJson.Messages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VasudevMessageApiJson {
	private Account account;
	private List<Messages> messages;
}
