package co.deepmindz.adminorghierservice.models;

import java.sql.Timestamp;

import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "Zones_list")
public class Zones_list {

	@Id
	@UuidGenerator
	private String _id;

	@NotNull
	@Column(unique = true)
	private String name;

	private String linked_zone_list;

	private String code;

	private String belongs_to_zone;

	private String cordinator;

	private Integer totalvisits;

	@Column(updatable = true)
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Timestamp lastvisited;
}
