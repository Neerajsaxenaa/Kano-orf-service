package co.deepmindz.adminorghierservice.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SSUserResponseDto {

	private String id;

//	private String name;

	private String role;

	private String username;

	private String phoneNumber;

//	private String password;

	private String[] linked_zones;
	
	private boolean active;

	private String[] linked_supervisors;

	private Timestamp created_at;
}
