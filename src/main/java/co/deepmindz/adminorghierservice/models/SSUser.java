package co.deepmindz.adminorghierservice.models;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SSUser {

	@Id
	@UuidGenerator
	private String user_id;

//	@NotEmpty
//	@NotNull
	private String status;
	
	private String name;

	@NotEmpty
	@NotNull
	private String role_id;

	@NotEmpty
	@NotNull
	@Column(unique = true)
	private String username;
	
	private String phoneNumber;
	
	private String password;

	@NotNull
	private String[] linkedParentZones; // Include the whole hierarchy

	@NotEmpty
	@NotNull
	private String linkedZone; // Include the immediate linked zone

	@Column(name = "is_team_lead")
	private boolean isTeamLead;

	private String[] linkedSupervisors;

	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Timestamp created_at;

}
