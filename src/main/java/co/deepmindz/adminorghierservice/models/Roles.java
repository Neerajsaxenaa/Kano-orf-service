package co.deepmindz.adminorghierservice.models;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

//@Data
@Entity
//@AllArgsConstructor
//@NoArgsConstructor
public class Roles {

	@Override
	public String toString() {
		return "Roles [role_id=" + role_id + ", title=" + title + ", role_code=" + role_code + ", zone_id=" + zone_id
				+ ", supervisorRole_id=" + supervisorRole_id + ", created_at=" + created_at + "]";
	}

	@Id
	@UuidGenerator
	private String role_id;

	private String title;

	private String role_code;

	private String zone_id;

	private String supervisorRole_id;

	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Timestamp created_at;

	public Roles() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Roles(String role_id, String title, String role_code, String zone_id, String supervisorRole_id,
			Timestamp created_at) {
		super();
		this.role_id = role_id;
		this.title = title;
		this.role_code = role_code;
		this.zone_id = zone_id;
		this.supervisorRole_id = supervisorRole_id;
		this.created_at = created_at;
	}

	public String getRole_id() {
		return role_id;
	}

	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRole_code() {
		return role_code;
	}

	public void setRole_code(String role_code) {
		this.role_code = role_code;
	}

	public String getZone_id() {
		return zone_id;
	}

	public void setZone_id(String zone_id) {
		this.zone_id = zone_id;
	}

	public String getSupervisorRole_id() {
		return supervisorRole_id;
	}

	public void setSupervisorRole_id(String supervisorRole_id) {
		this.supervisorRole_id = supervisorRole_id;
	}

	public Timestamp getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}
	
	
	
}
