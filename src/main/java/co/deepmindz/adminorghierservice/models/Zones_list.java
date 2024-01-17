package co.deepmindz.adminorghierservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "Zones_list")
public class Zones_list {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String _id;
	@NotNull
	
	@Column(unique = true)
	private String name;
	private String linked_zone_list;
	private String code;
	private String belongs_to_zone;
	private String cordinator;

}
