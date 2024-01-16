package co.deepmindz.adminorghierservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ZonesRequestDto {

	@NotEmpty(message = "Provide valid name")
	private String name;

	private String parentZone_id;

	@NotEmpty(message = "Provide valid zone_code")
	private String zone_code;

	@NotNull
	private String role;

	public ZonesRequestDto() {
		super();
	}

	public ZonesRequestDto(@NotEmpty(message = "Provide valid name") String name, String parentZone_id,
			@NotEmpty(message = "Provide valid zone_code") String zone_code, @NotNull String role) {
		super();
		this.name = name;
		this.parentZone_id = parentZone_id;
		this.zone_code = zone_code;
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentZone_id() {
		return parentZone_id;
	}

	public void setParentZone_id(String parentZone_id) {
		this.parentZone_id = parentZone_id;
	}

	public String getZone_code() {
		return zone_code;
	}

	public void setZone_code(String zone_code) {
		this.zone_code = zone_code;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "ZonesRequestDto [name=" + name + ", parentZone_id=" + parentZone_id + ", zone_code=" + zone_code
				+ ", role=" + role + "]";
	}

}
