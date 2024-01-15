package co.deepmindz.adminorghierservice.utils;

import java.util.List;

import co.deepmindz.adminorghierservice.dto.Zones_list_ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class CustomDataTypes {

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	static public class relation {
		public String zone;
		public String zone_id;
		public String assignedRole;
		public String role_id;
		public String reportsTo;
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@Data
	static public class keyValuePair {
		public String key;
		public String value;
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	static public class subZoneData {
		public String zone;
		public List<Zones_list_ResponseDto> subZones;
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@ToString
	static public class ZoneForSSUSer {
		public String zone;
		public String zone_id;
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@ToString
	static public class SupervisorOfSSUSer {
		public String supervisor;
		public String supervisor_id;
	}

}
