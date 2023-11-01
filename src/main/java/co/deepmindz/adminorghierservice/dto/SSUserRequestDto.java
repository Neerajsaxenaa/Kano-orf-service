package co.deepmindz.adminorghierservice.dto;

import co.deepmindz.adminorghierservice.utils.CustomDataTypes.SupervisorOfSSUSer;
import co.deepmindz.adminorghierservice.utils.CustomDataTypes.ZoneForSSUSer;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SSUserRequestDto {

	@NotEmpty
	@NotNull
	private String name;

	@NotEmpty
	@NotNull
	private String role;

	private String phoneNumber;

	private String user;

	private String password;

	@NotEmpty
	@NotNull
	private ZoneForSSUSer[] linked_zones;

	private SupervisorOfSSUSer[] linked_supervisors;

}
